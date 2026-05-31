
async function searchByTitle(title){
    let result = await fetch(`https://api.openweathermap.org/data/2.5/weather?q=${title}&appid=d9ebd6e3de366196a8e96eefe87eb7ed&units=metric`);

    if(result.status === 404) return null;
    
    let data = await result.json();
    return data;
}

let myForm = document.forms.myForm;
let list = document.querySelector('.list');


myForm.addEventListener('submit', async function(e) {
    e.preventDefault(); 
    
    let cityName = myForm.cityName.value;

    if(cityName.trim()) {
        let result = await searchByTitle(cityName);
        
        list.insertAdjacentHTML('beforeend', `
            <div class="card">
                <img src="https://openweathermap.org/payload/api/media/file/${result.weather[0].icon}.png" class="card-img-top" alt="...">
                <div class="card-body">
                    <h5 class="card-title">${result.name} ${Math.round(result.main.temp)} ℃</h5>
                    <p class="card-text">${result.weather[0].description}</p>
            </div>`);
        
    }

    myForm.reset();
});
