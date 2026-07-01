const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {

    window.location.href = "/login";

}

cargarPronosticos();

async function cargarPronosticos() {

    const response = await fetch(
        `http://localhost:8081/pronosticos/usuario/${usuario.id}`
    );

    const pronosticos = await response.json();

    const contenedor = document.getElementById("partidosContainer");

    contenedor.innerHTML = "";

    if (pronosticos.length === 0) {

        contenedor.innerHTML = `
            <div class="match-card">
                <h3>No realizaste ningún pronóstico todavía.</h3>
            </div>
        `;

        return;
    }

    pronosticos.forEach(pronostico => {

        const fechaHora = new Date(pronostico.partido.fechaHora);

        const fecha = fechaHora.toLocaleDateString("es-AR", {
            day: "2-digit",
            month: "short"
        }).toUpperCase();

        contenedor.innerHTML += `

        <div class="match-card">

            <div class="match-date">

                <span class="group">

                    ${
                        pronostico.partido.grupo
                            ? "Grupo " + pronostico.partido.grupo.nombre
                            : pronostico.partido.fase.replaceAll("_", " ")
                    }

                </span>

                <small>

                    <i class="bi bi-calendar3"></i>

                    ${fecha}

                </small>

            </div>

            <div class="team">

                ${pronostico.partido.local.nombre}

            </div>

            <div class="score">

                ${pronostico.golesLocal} - ${pronostico.golesVisitante}

            </div>

            <div class="team">

                ${pronostico.partido.visitante.nombre}

            </div>

            <button disabled>

                ${pronostico.puntos} pts

            </button>

        </div>

        `;

    });

}