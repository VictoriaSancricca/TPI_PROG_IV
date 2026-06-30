const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

document.getElementById("nombreUsuario").innerText = usuario.nombre;

cargarPartidos();

async function cargarPartidos() {

    const response = await fetch("http://localhost:8081/partidos");

    const partidos = await response.json();

    const contenedor = document.getElementById("partidosContainer");

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

        <div class="match-card">

            <div class="match-date">

                <span class="group">
                    ${partido.fecha.nombre}
                </span>

                <small>

                    <i class="bi bi-calendar3"></i>

                    ${fecha}

                </small>

                <small>

                    <i class="bi bi-clock"></i>

                    ${hora}

                </small>

            </div>

            <div class="team">

                <div class="team-icon">

                    <i class="bi bi-circle-fill"></i>

                </div>

                <span>${partido.local.nombre}</span>

            </div>

            <div class="score">

                ${
                    partido.estado === "FINALIZADO"
                    ? `${partido.golesLocal} - ${partido.golesVisitante}`
                    : "VS"
                }

            </div>

            <div class="team">

                <div class="team-icon">

                    <i class="bi bi-circle-fill"></i>

                </div>

                <span>${partido.visitante.nombre}</span>

            </div>

            <div class="estado ${partido.estado.toLowerCase()}">

                ${partido.estado.replace("_"," ")}

            </div>

        </div>

        `;

    });

}