const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {

    window.location.href = "/login";

}

cargarPronosticos();

async function cargarPronosticos() {

    const response = await fetch(`${API}/pronosticos/usuario/${usuario.id}`);

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

    // ==========================
    // BANDERAS
    // ==========================

    const banderaLocal = `/img/banderas/${pronostico.partido.local.nombre
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/\s+/g, "")
        .toLowerCase()}.png`;

    const banderaVisitante = `/img/banderas/${pronostico.partido.visitante.nombre
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/\s+/g, "")
        .toLowerCase()}.png`;

    // ==========================
    // TARJETA
    // ==========================

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
                <i class="bi bi-calendar-event"></i>
                ${fecha}
            </small>

        </div>

        <div class="team">

            <img src="${banderaLocal}" class="flag">

            <span>${pronostico.partido.local.nombre}</span>

        </div>

        <div class="score">

            ${pronostico.golesLocal} - ${pronostico.golesVisitante}

        </div>

        <div class="team">

            <img src="${banderaVisitante}" class="flag">

            <span>${pronostico.partido.visitante.nombre}</span>

        </div>

        <button disabled class="points">

            ${pronostico.puntos} pts

        </button>

    </div>

    `;

});

}