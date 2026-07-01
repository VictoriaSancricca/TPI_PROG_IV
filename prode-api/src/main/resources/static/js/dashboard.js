const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {

    window.location.href = "/login";

} else {

    document.getElementById("saludo").innerHTML =
        `¡Hola, ${usuario.nombre}! 👋`;

    const nombre = document.getElementById("nombreUsuario");

    if (nombre) {
        nombre.innerText = usuario.nombre;
    }

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
        "/usuarios/ranking"
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

        ranking.slice(0, 3).forEach((usuario, index) => {

        let medalla = "";

        if (index === 0) {
            medalla = "🥇";
        } else if (index === 1) {
            medalla = "🥈";
        } else {
            medalla = "🥉";
        }

        contenedor.innerHTML += `

            <div class="ranking-card">

                <span class="puesto">

                    ${medalla} #${index + 1}

                </span>

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
        "/partidos"
    );

    let partidos = await response.json();

    partidos = partidos
        .filter(p => p.estado === "POR_JUGARSE")
        .sort((a, b) =>
            new Date(a.fechaHora) - new Date(b.fechaHora)
        )
        .slice(0, 5);

    const contenedor =
        document.getElementById("partidosContainer");

    contenedor.innerHTML = "";

    partidos.forEach(partido => {

        const fechaHora = new Date(partido.fechaHora);

        const fecha = fechaHora.toLocaleDateString("es-AR", {
            day: "2-digit",
            month: "short"
        }).toUpperCase();

        const hora = fechaHora.toLocaleTimeString("es-AR", {
            hour: "2-digit",
            minute: "2-digit"
        });

        contenedor.innerHTML += `

        <div class="ranking-card">

            <div>

                <span class="group">

                    ${
                        partido.grupo
                            ? "Grupo " + partido.grupo.nombre
                            : partido.fase.replaceAll("_"," ")
                    }

                </span>

            </div>

            <strong>

                ${partido.local.nombre}

                VS

                ${partido.visitante.nombre}

            </strong>

            <span>${fecha}</span>

            <span>${hora}</span>

        </div>

        `;

    });

}