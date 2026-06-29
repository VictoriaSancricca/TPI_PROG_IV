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

        contenedor.innerHTML += `

        <div class="match-card">

            <span class="group">
                ${partido.fecha.nombre}
            </span>

            <div class="team">
                ${partido.local.nombre}
            </div>

            <div class="score">

                ${
                    partido.estado === "FINALIZADO"
                    ? `${partido.golesLocal} - ${partido.golesVisitante}`
                    : "VS"
                }

            </div>

            <div class="team">
                ${partido.visitante.nombre}
            </div>

            <button disabled>
                ${partido.estado}
            </button>

        </div>

        `;

    });

}