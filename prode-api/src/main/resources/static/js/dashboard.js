const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {

    window.location.href = "/login";

} else {

    document.getElementById("saludo").innerHTML =
        `¡Hola, ${usuario.nombre}! 👋`;

    document.getElementById("nombreUsuario").innerText =
        usuario.nombre;

}

document
    .getElementById("logout")
    .addEventListener("click", async function (e) {

        e.preventDefault();

        const resultado = await Swal.fire({
            title: "¿Cerrar sesión?",
            text: "Tendrás que volver a iniciar sesión.",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Sí",
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33"
        });

        if (resultado.isConfirmed) {

            localStorage.removeItem("usuario");

            window.location.href = "/login";
        }

    });

cargarRanking();
cargarPartidos();

async function cargarRanking() {

    const response = await fetch(
        "http://localhost:8081/usuarios/ranking"
    );

    const ranking = await response.json();

    document.getElementById("misPuntos").innerText = usuario.puntos;
    document.getElementById("misPlenos").innerText = usuario.plenos;

    const miPosicion = ranking.findIndex(u => u.id === usuario.id);

    if (miPosicion !== -1) {
        document.getElementById("miPosicion").innerText = "#" + (miPosicion + 1);
    }

    const contenedor =
        document.getElementById("rankingContainer");

    contenedor.innerHTML = "";

    ranking.forEach((usuario, index) => {

        contenedor.innerHTML += `

            <div class="ranking-card">

                <span>#${index + 1}</span>

                <strong>${usuario.nombre}</strong>

                <span>${usuario.puntos} pts</span>

                <span>${usuario.plenos} plenos</span>

            </div>

        `;

    });

}


const sidebar =
    document.querySelector(".sidebar");

const contenido =
    document.querySelector(".content");

document
    .getElementById("toggleSidebar")
    .addEventListener("click", () => {

        sidebar.classList.toggle("collapsed");

        contenido.classList.toggle("expanded");

    });

async function cargarPartidos() {

    const response = await fetch(
        "http://localhost:8081/partidos"
    );

    const partidos = await response.json();

    const proximos = partidos.filter(
        p => p.estado === "POR_JUGARSE"
    );

    const contenedor =
        document.getElementById("partidosContainer");

    contenedor.innerHTML = "";

    proximos.forEach(partido => {

        contenedor.innerHTML += `

        <div class="match-card">

            <span class="group">
                ${partido.fecha.nombre}
            </span>

            <div class="team">
                ${partido.local.nombre}
            </div>

            <div class="score">
                <input type="number" min="0" id="local-${partido.id}">
                <span>-</span>
                <input type="number" min="0" id="visitante-${partido.id}">
            </div>

            <div class="team">
                ${partido.visitante.nombre}
            </div>

            <button onclick="pronosticar(${partido.id})">
                Pronosticar
            </button>

        </div>

        `;

    });

}