/* --- DATA STRUCTURE --- */
let products = [];
let cart = JSON.parse(localStorage.getItem('techstore_cart')) || [];

/* --- TASK 7: SECURE FETCH WRAPPER --- */
// Isang custom fetch wrapper para sa lahat ng API requests mo para i-intercept ang auth errors
async function secureFetch(url, options = {}) {
    // Siguraduhing kasama ang credentials para laging ipadala ang JSESSIONID cookie
    options.credentials = 'include';

    try {
        const response = await fetch(url, options);

        // 1. Intercept 401 Unauthorized (Hindi naka-login o expired ang session)
        if (response.status === 401) {
            alert("Session expired or not logged in. Redirecting to login page...");
            window.location.href = "/login.html"; // Redirect logic kung nasaan ang login UI mo
            return null;
        }

        // 2. Intercept 403 Forbidden (Naka-login pero maling role ang sumusubok mag-access)
        if (response.status === 403) {
            alert("Access Denied: You do not have permission to perform this action.");
            // Pwedeng mag-stay sa page o i-redirect sa safe page tulad ng index.html
            return null;
        }

        return response;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

/* --- FETCH PRODUCTS FROM API --- */
async function fetchProducts() {
    try {
        // Task 7 Update: Ginamit natin ang secureFetch sa halip na regular fetch
        const response = await secureFetch('http://localhost:8080/api/products');

        // Kung na-intercept ng 401 o 403 at nag-return ng null, hinto na dito
        if (!response) return;

        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        products = await response.json();

        // Ito yung kailangan mo para makita ang listahan sa F12 Console
        console.log("Task 8 - Fetched Products from Database:", products);

        renderProducts();
    } catch (error) {
        console.error("Fetch API Error:", error.message);
        const grid = document.querySelector('.product-grid');
        if (grid) {
            grid.innerHTML = `<p style="color:red; text-align:center;">Failed to load products: ${error.message}</p>`;
        }
    }
}

/* --- RENDER PRODUCTS --- */
function renderProducts() {
    const grid = document.querySelector('.product-grid');
    if (!grid) return;

    grid.innerHTML = products.length === 0 ? "<p>No products available right now.</p>" : "";

    products.forEach(p => {
        const article = document.createElement('article');
        article.className = "product-card"; // Added class for easier styling/animation
        article.innerHTML = `
            <img src="${p.imageUrl || 'https://via.placeholder.com/400'}" alt="${p.name}">
            <div class="product-info">
                <h3>${p.name}</h3>
                <p class="price">₱${p.price.toLocaleString()}</p>
            </div>
            <button class="shimmer-btn add-to-cart" data-id="${p.id}">Add to Cart</button>
        `;
        grid.appendChild(article);
    });
}

/* --- CART & EVENT LISTENERS --- */
document.body.addEventListener('click', (e) => {
    // Add to Cart Logic
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

            localStorage.setItem('techstore_cart', JSON.stringify(cart));

            // Interaction feedback
            alert(`${product.name} added to cart!`);
            renderCart();
        }
    }
});

/* --- RENDER CART & UPDATE TOTAL --- */
function renderCart() {
    const container = document.getElementById('cart-items-container');
    const totalEl = document.getElementById('cart-total');

    const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);
    if (totalEl) {
        totalEl.textContent = `Total: ₱${total.toLocaleString()}`;
    }

    if (!container) return;

    container.innerHTML = cart.length === 0 ? "<p>Your bag is empty.</p>" : "";

    cart.forEach((item, index) => {
        const div = document.createElement('div');
        div.className = "cart-item";
        div.innerHTML = `
            <div style="flex:1">
                <h4>${item.name}</h4>
                <p>₱${item.price.toLocaleString()} x ${item.quantity}</p>
            </div>
            <input type="number" value="${item.quantity}" min="0" 
                   class="qty-change" data-index="${index}" 
                   style="width: 50px; text-align: center;">
        `;
        container.appendChild(div);
    });
}

/* --- CHECKOUT FORM HANDLING --- */
document.addEventListener('submit', (e) => {
    if (e.target.id === 'checkout-form') {
        e.preventDefault();

        const nameInput = document.getElementById('fullname');
        const addressInput = document.getElementById('address');

        const name = nameInput ? nameInput.value.trim() : "Customer";
        const address = addressInput ? addressInput.value.trim() : "Not specified";

        if (cart.length === 0) {
            alert("Your cart is empty! Add some items before checking out.");
            return;
        }

        // Show processing state
        alert(`Hi ${name}! We are now processing your order for: ${address}.`);

        // Clear local storage and cart array
        cart = [];
        localStorage.removeItem('techstore_cart');

        // Redirect after short delay (Task 8 Flow Test)
        setTimeout(() => {
            window.location.href = "landing.html";
        }, 1500);
    }
});

/* --- INITIALIZATION --- */
window.onload = () => {
    fetchProducts();
    renderCart();

    const greeting = document.getElementById('user-greeting');
    if (greeting) {
        greeting.textContent = "Welcome back, Stephanie!";
    }
};