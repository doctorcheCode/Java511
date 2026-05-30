const API_KEY = '80d0c319';
const BASE_URL = 'https://omdbapi.com';

const searchForm = document.getElementById('search-form');
const searchTitle = document.getElementById('search-title');
const searchType = document.getElementById('search-type');
const resultsContainer = document.getElementById('results-container');
const paginationContainer = document.getElementById('pagination-container');
const detailsContainer = document.getElementById('details-container');

let currentQuery = '';
let currentType = '';
let currentPage = 1;

searchForm.addEventListener('submit', (e) => {
    e.preventDefault();
    currentQuery = searchTitle.value.trim();
    currentType = searchType.value;
    currentPage = 1;
    
    if (currentQuery) {
        fetchMovies(currentQuery, currentType, currentPage);
        detailsContainer.classList.add('hidden'); 
    }
});

async function fetchMovies(title, type, page) {
    resultsContainer.innerHTML = 'Загрузка...';
    paginationContainer.innerHTML = '';
    
    let url = `${BASE_URL}/?apikey=${API_KEY}&s=${encodeURIComponent(title)}&page=${page}`;
    if (type) {
        url += `&type=${type}`;
    }

    try {
        const response = await fetch(url);
        const data = await response.json();

        if (data.Response === 'True') {
            displayMovies(data.Search);
            setupPagination(data.totalResults, page);
        } else {
            resultsContainer.innerHTML = `<p class="error">Movie not found!</p>`;
        }
    } catch (error) {
        resultsContainer.innerHTML = `<p class="error">Ошибка соединения с сервером.</p>`;
    }
}

function displayMovies(movies) {
    resultsContainer.innerHTML = '';
    
    movies.forEach(movie => {
        const movieEl = document.createElement('div');
        movieEl.classList.add('movie-item');
        
        const poster = movie.Poster !== 'N/A' ? movie.Poster : 'https://placeholder.com';
        
        movieEl.innerHTML = `
            <div class="movie-info">
                <img src="${poster}" alt="${movie.Title}" class="movie-poster">
                <div>
                    <strong>${movie.Title}</strong> (${movie.Year}) <br>
                    <small>Type: ${movie.Type}</small>
                </div>
            </div>
            <button class="btn-details" data-id="${movie.imdbID}">Details</button>
        `;
        
        resultsContainer.appendChild(movieEl);
    });

    document.querySelectorAll('.btn-details').forEach(button => {
        button.addEventListener('click', (e) => {
            const imdbID = e.target.getAttribute('data-id');
            fetchMovieDetails(imdbID);
        });
    });
}

function setupPagination(totalResults, activePage) {
    paginationContainer.innerHTML = '';
    const totalPages = Math.ceil(totalResults / 10);
    
    const maxButtons = 5;
    let startPage = Math.max(1, activePage - Math.floor(maxButtons / 2));
    let endPage = Math.min(totalPages, startPage + maxButtons - 1);
    
    if (endPage - startPage + 1 < maxButtons) {
        startPage = Math.max(1, endPage - maxButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        const btn = document.createElement('button');
        btn.innerText = i;
        if (i === activePage) btn.classList.add('active');
        
        btn.addEventListener('click', () => {
            currentPage = i;
            fetchMovies(currentQuery, currentType, currentPage);
            resultsContainer.scrollIntoView({ behavior: 'smooth' });
        });
        
        paginationContainer.appendChild(btn);
    }
}

async function fetchMovieDetails(id) {
    detailsContainer.innerHTML = 'Загрузка деталей...';
    detailsContainer.classList.remove('hidden');
    detailsContainer.scrollIntoView({ behavior: 'smooth' });

    const url = `${BASE_URL}/?apikey=${API_KEY}&i=${id}`;

    try {
        const response = await fetch(url);
        const movie = await response.json();

        if (movie.Response === 'True') {
            const poster = movie.Poster !== 'N/A' ? movie.Poster : 'https://placeholder.com';
            
            detailsContainer.innerHTML = `
                <img src="${poster}" alt="${movie.Title}">
                <div>
                    <h2>${movie.Title} (${movie.Year})</h2>
                    <p><strong>Released:</strong> ${movie.Released}</p>
                    <p><strong>Genre:</strong> ${movie.Genre}</p>
                    <p><strong>Director:</strong> ${movie.Director}</p>
                    <p><strong>Actors:</strong> ${movie.Actors}</p>
                    <p><strong>Plot:</strong> ${movie.Plot}</p>
                    <p><strong>IMDb Rating:</strong> ⭐ ${movie.imdbRating}</p>
                </div>
            `;
        } else {
            detailsContainer.innerHTML = `<p class="error">Не удалось загрузить детали фильма.</p>`;
        }
    } catch (error) {
        detailsContainer.innerHTML = `<p class="error">Ошибка загрузки данных.</p>`;
    }
}
