/* --- DATA STRUCTURE --- */
let products = [];
let cart = JSON.parse(localStorage.getItem('techstore_cart')) || [];

/* --- FETCH PRODUCTS FROM API --- */
async function fetchProducts() {
    try {
        const response = await fetch('http://localhost:8080/api/products');
        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);
        products = await response.json();
        renderProducts();
    } catch (error) {
        console.error("Fetch API Error:", error.message);
        const grid = document.querySelector('.product-grid');
        if (grid) grid.innerHTML = `<p style="color:red; text-align:center;">Error: ${error.message}</p>`;
    }
}

/* --- RENDER PRODUCTS --- */
function renderProducts() {
    const grid = document.querySelector('.product-grid');
    if (!grid) return;
    grid.innerHTML = products.length === 0 ? "<p>No products available.</p>" : "";

    products.forEach(p => {
        const article = document.createElement('article');
        article.innerHTML = `
            <img src="${p.imageUrl || 'https://via.placeholder.com/400'}" alt="${p.name}">
            <div class="product-info">
                <h3>${p.name}</h3>
                <p class="price">₱${p.price}</p>
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
            if (existing) existing.quantity += 1;
            else cart.push({ ...product, quantity: 1 });

            localStorage.setItem('techstore_cart', JSON.stringify(cart));
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
    if (totalEl) totalEl.textContent = `Total: ₱${total.toLocaleString()}`;

    if (!container) return;
    container.innerHTML = cart.length === 0 ? "<p>Your bag is empty.</p>" : "";
    cart.forEach((item, index) => {
        const div = document.createElement('div');
        div.className = "cart-item";
        div.innerHTML = `
            <div style="flex:1"><h4>${item.name}</h4><p>₱${item.price}</p></div>
            <input type="number" value="${item.quantity}" min="0" class="qty-change" data-index="${index}">
        `;
        container.appendChild(div);
    });
}

/* --- CHECKOUT FORM HANDLING --- */
document.addEventListener('submit', (e) => {
    if (e.target.id === 'checkout-form') {
        e.preventDefault();

        const name = document.getElementById('fullname').value.trim();
        const address = document.getElementById('address').value.trim();

        if (cart.length === 0) {
            alert("Your cart is empty!");
            return;
        }

        // Custom alert message
        alert(`Hi ${name}! We are now processing your checkout for address: ${address}. Please wait...`);

        // Clear everything
        cart = [];
        localStorage.removeItem('techstore_cart');

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
    if (greeting) greeting.textContent = "Welcome back, Stephanie!";
};