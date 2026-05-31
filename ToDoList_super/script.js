let input = document.querySelector('input');
let btn = document.querySelector('button');
let list = document.querySelector('.list');
let btnClear = document.getElementById('button-addon2');
let asc = document.getElementById('button-addon3');
let desc = document.getElementById('button-addon4');

function loadItems() {
    const savedItems = JSON.parse(localStorage.getItem('todoItems')) || [];
    savedItems.forEach(item => {
        addItemToDOM(item.text, item.checked);
    });
}

function saveItems() {
    const items = [];
    list.querySelectorAll('.list-group-item').forEach(item => {
        items.push({
            text: item.querySelector('.item-text').textContent,
            checked: item.classList.contains('text-success') // Проверяем, зеленый ли текст
        });
    });
    localStorage.setItem('todoItems', JSON.stringify(items));
}

function addItemToDOM(text, isChecked = false) {
    const activeClass = isChecked ? 'text-success' : '';
    
    const html = `
    <div class="list-group-item list-group-item-action d-flex justify-content-between align-items-center ${activeClass}">
        <span class="item-text">${text}</span>
        <div>
            <button class="btn btn-sm btn-outline-success me-1 btn-check-item">✓</button>
            <button class="btn btn-sm btn-outline-danger btn-delete-item">×</button>
        </div>
    </div>`;
    
    list.insertAdjacentHTML('beforeend', html);
}

btn.addEventListener('click', () => {
    let str = input.value.trim();
    if (str === '') return;

    addItemToDOM(str);
    saveItems(); 

    input.value = '';
    input.focus();
});

list.addEventListener('click', (e) => {
    const item = e.target.closest('.list-group-item');
    if (!item) return;

    if (e.target.classList.contains('btn-check-item')) {
        item.classList.toggle('text-success');
        saveItems();
    }

    if (e.target.classList.contains('btn-delete-item')) {
        item.remove();
        saveItems();
    }
});

input.addEventListener('keydown', (e) => {
    if (e.key === "Enter") btn.click();
});

asc.addEventListener('click', () => {
    let items = Array.from(list.querySelectorAll('.list-group-item'));
    items.sort((a, b) => {
        let textA = a.querySelector('.item-text').textContent;
        let textB = b.querySelector('.item-text').textContent;
        return textA.localeCompare(textB);
    });
    items.forEach(item => list.appendChild(item));
    saveItems();
});

desc.addEventListener('click', () => {
    let items = Array.from(list.querySelectorAll('.list-group-item'));
    items.sort((a, b) => {
        let textA = a.querySelector('.item-text').textContent;
        let textB = b.querySelector('.item-text').textContent;
        return textB.localeCompare(textA);
    });
    items.forEach(item => list.appendChild(item));
    saveItems();
});

btnClear.addEventListener('click', () => {
    list.innerHTML = '';
    localStorage.removeItem('todoItems');
});

loadItems();
