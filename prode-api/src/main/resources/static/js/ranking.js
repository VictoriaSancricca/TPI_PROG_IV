const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}


cargarRanking();

const USUARIOS_POR_PAGINA = 10;

let ranking = [];

let paginaActual = 1;

async function cargarRanking() {

    const response = await fetch(`${API}/usuarios/ranking`);

    ranking = await response.json();

    renderRanking();

}

function renderRanking() {

    const contenedor =
        document.getElementById("rankingContainer");

    contenedor.innerHTML = "";

    const inicio = (paginaActual - 1) * USUARIOS_POR_PAGINA;

    const fin = inicio + USUARIOS_POR_PAGINA;

    const pagina = ranking.slice(inicio, fin);

    pagina.forEach((usuario, index) => {

        const posicion = inicio + index + 1;

        let medalla = "";

        if (posicion === 1) medalla = "🥇";
        else if (posicion === 2) medalla = "🥈";
        else if (posicion === 3) medalla = "🥉";

        contenedor.innerHTML += `

        <div class="ranking-card">

            <span>${medalla} #${posicion}</span>

            <strong>${usuario.nombre}</strong>

            <span>${usuario.puntos} pts</span>

            <span>${usuario.plenos} plenos</span>

        </div>

        `;

    });

    renderPaginacion();

}

function renderPaginacion() {

    const paginacion =
        document.getElementById("paginacion");

    paginacion.innerHTML = "";

    const total =
        Math.ceil(ranking.length / USUARIOS_POR_PAGINA);

    for(let i = 1; i <= total; i++){

        paginacion.innerHTML += `

        <button
            class="pagina ${i == paginaActual ? "activa" : ""}"
            onclick="irPagina(${i})">

            ${i}

        </button>

        `;

    }

}

function irPagina(numero){

    paginaActual = numero;

    renderRanking();

}

window.irPagina = irPagina;