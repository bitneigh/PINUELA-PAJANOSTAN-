/* --- STATE MANAGER VARIABLES --- */
let products = [];
let cart = [];

// Helper function to dynamically calculate cart storage context keys per individual user identity
function getCartStorageKey() {
    const activeToken = localStorage.getItem('jwt_token');
    if (!activeToken) return 'techstore_cart_anonymous';
    try {
        // Safe base64 tracking extraction to parse out username from token payload layers
        const payloadBase64 = activeToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        return `techstore_cart_${decodedPayload.sub}`;
    } catch (e) {
        return 'techstore_cart_anonymous';
    }
}

// Dynamically sync user specific tracking allocations
function loadUserSpecificCart() {
    const key = getCartStorageKey();
    cart = JSON.parse(localStorage.getItem(key)) || [];
}

/* --- TASK 10: STATELESS JWT SECURE FETCH WRAPPER --- */
async function secureFetch(url, options = {}) {
    options.headers = options.headers || {};
    const token = localStorage.getItem('jwt_token');

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            alert("Session expired or unauthorized. Redirecting to login page...");
            localStorage.removeItem('jwt_token');
            window.location.href = "/login.html";
            return null;
        }

        if (response.status === 403) {
            alert("Access Denied: You do not have permission to perform this action.");
            return null;
        }

        return response;
    } catch (error) {
        console.error("Secure fetch tracking error:", error);
        throw error;
    }
}

/* --- FETCH PRODUCTS FROM API --- */
async function fetchProducts() {
    try {
        const response = await secureFetch('http://localhost:8080/api/v1/products');
        if (!response) return;
        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

        products = await response.json();
        renderProducts();
    } catch (error) {
        console.error("Fetch API Error:", error.message);
        const grid = document.getElementById('product-grid-target') || document.querySelector('.product-grid');
        if (grid) {
            grid.innerHTML = `<p style="color:red; text-align:center;">Failed to load products: ${error.message}</p>`;
        }
    }
}

/* --- RENDER PRODUCTS --- */
function renderProducts() {
    const grid = document.getElementById('product-grid-target') || document.querySelector('.product-grid');
    if (!grid) return;

    grid.innerHTML = products.length === 0 ? "<p>No products available right now.</p>" : "";

    products.forEach(p => {
        const article = document.createElement('article');
        article.className = "product-card";
        article.innerHTML = `
            <img src="${p.imageUrl || 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500'}" alt="${p.name}">
            <div class="product-info">
                <h3>${p.name}</h3>
                <p class="price">₱${p.price.toLocaleString()}</p>
            </div>
            <button class="shimmer-btn add-to-cart" data-id="${p.id}">Add to Cart</button>
        `;
        grid.appendChild(article);
    });
}

/* --- CART EVENTS & PERSISTENCE HANDLING --- */
document.body.addEventListener('click', (e) => {
    if (e.target.classList.contains('add-to-cart')) {
        const id = parseInt(e.target.getAttribute('data-id'));
        const product = products.find(prod => prod.id === id);

        if (product) {
            const existing = cart.find(item => item.id === id);
            if (existing) {
                existing.quantity += 1;
            } else {
                cart.push({ ...product, quantity: 1 });
            }

            localStorage.setItem(getCartStorageKey(), JSON.stringify(cart));
            alert(`${product.name} added to cart!`);
            renderCart();
        }
    }
});

function renderCart() {
    const container = document.getElementById('cart-items-container');
    const totalEl = document.getElementById('cart-total');

    const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);
    if (totalEl) totalEl.textContent = `Total: ₱${total.toLocaleString()}`;

    if (!container) return;
    container.innerHTML = cart.length === 0 ? "<p style='color:#a0a5b5;'>Your bag is empty.</p>" : "";

    cart.forEach((item) => {
        const div = document.createElement('div');
        div.className = "cart-item";
        div.innerHTML = `
            <div style="flex:1">
                <h4>${item.name}</h4>
                <p>₱${item.price.toLocaleString()} x ${item.quantity}</p>
            </div>
        `;
        container.appendChild(div);
    });
}

/* --- GLOBAL FORM SUBMISSION HANDLERS --- */
document.addEventListener('submit', async (e) => {

    // Auth Case 1: Processing Sign In Actions
    if (e.target.id === 'login-form') {
        e.preventDefault();
        const usernameInput = document.getElementById('username').value;
        const passwordInput = document.getElementById('password').value;

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: usernameInput, password: passwordInput })
            });

            if (!response.ok) throw new Error('Invalid credentials');
            const data = await response.json();

            if (data.token) {
                localStorage.setItem('jwt_token', data.token);
                alert('Authentication Verified! Transferring access controls.');
                window.location.href = "/landing.html";
            }
        } catch (err) {
            alert('Sign in failed. Verify parameters match accurately.');
            console.error(err);
        }
    }

    // Auth Case 2: Processing Sign Up Actions
    if (e.target.id === 'register-form') {
        e.preventDefault();
        const usernameInput = document.getElementById('reg-username').value;
        const passwordInput = document.getElementById('reg-password').value;
        const roleInput = document.getElementById('reg-role').value;

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: usernameInput, password: passwordInput, role: roleInput })
            });

            if (!response.ok) {
                const errMsg = await response.text();
                throw new Error(errMsg || 'Registration failed');
            }

            alert('Account created successfully! Redirecting to login page...');
            window.location.href = "/login.html";
        } catch (err) {
            alert(`Registration Error: ${err.message}`);
            console.error(err);
        }
    }

    // Order Checkout Case: Processing Cart Order Submissions
    if (e.target.id === 'checkout-form') {
        e.preventDefault();

        if (cart.length === 0) {
            alert("Your cart is empty! Add some items before checking out.");
            return;
        }

        // Sends order details payload to the secure backend REST endpoint using secureFetch
        const response = await secureFetch('http://localhost:8080/api/v1/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ items: cart })
        });

        // Clear local storage cart state and redirect to store catalog view upon successful persist
        if (response && response.ok) {
            alert(`Order processed successfully! Returning to home catalog.`);
            localStorage.removeItem(getCartStorageKey());
            cart = [];
            setTimeout(() => {
                window.location.href = "/landing.html";
            }, 1000);
        }
    }
});

/* --- SYSTEM GLOBAL LOGOUT UTILITY --- */
function handleLogout() {
    localStorage.removeItem('jwt_token');
    alert("Logged out securely from active workstation state.");
    window.location.href = "/login.html";
}

/* --- BOOTSTRAP INITIALIZATION PIPELINE --- */
window.onload = () => {
    loadUserSpecificCart();

    // Safe view container check: Only fire product fetch logic if target DOM element wrapper exists
    const productCatalogTarget = document.getElementById('product-grid-target') || document.querySelector('.product-grid');
    if (productCatalogTarget) {
        fetchProducts();
    }

    renderCart();

    // User Profile Greeting: Extrapolate user subject metadata fields from valid active JWT payload
    const greeting = document.getElementById('user-greeting');
    if (greeting) {
        try {
            const activeToken = localStorage.getItem('jwt_token');
            if (activeToken) {
                const payloadBase64 = activeToken.split('.')[1];
                const decodedPayload = JSON.parse(atob(payloadBase64));
                greeting.textContent = `Welcome back, ${decodedPayload.sub}!`;
            } else {
                greeting.textContent = "Welcome back!";
            }
        } catch (e) {
            greeting.textContent = "Welcome back!";
        }
    }
};