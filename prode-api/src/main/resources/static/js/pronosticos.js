const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

document.getElementById("nombreUsuario").innerText = usuario.nombre;

cargarPronosticos();

async function cargarPronosticos() {

    const response = await fetch(
        "http://localhost:8081/pronosticos/usuario/1"
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

        contenedor.innerHTML += `

        <div class="match-card">

            <span class="group">
                ${pronostico.partido.fecha.nombre}
            </span>

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