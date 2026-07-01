document.addEventListener("DOMContentLoaded", () => {

    cargarDashboard();

});

async function cargarDashboard() {

    try {

        const response = await fetch(`${API}/admin/dashboard`);

        if (!response.ok) {

            throw new Error();

        }

        const data = await response.json();

        document.getElementById("cantUsuarios").textContent = data.usuarios;
        document.getElementById("cantEquipos").textContent = data.equipos;
        document.getElementById("cantGrupos").textContent = data.grupos;
        document.getElementById("cantPartidos").textContent = data.totalPartidos;

        document.getElementById("faseActual").textContent = data.faseActual;
        document.getElementById("partidosJugados").textContent = data.partidosJugados;
        document.getElementById("partidosPendientes").textContent = data.partidosPendientes;
        document.getElementById("totalPartidos").textContent = data.totalPartidos;

        const contenedor = document.getElementById("proximosPartidos");

        contenedor.innerHTML = "";

        if (data.proximosPartidos.length === 0) {

            contenedor.innerHTML = `
                <p style="color:#64748B;">
                    No hay partidos programados.
                </p>
            `;

            return;

        }

        data.proximosPartidos.forEach(partido => {

            contenedor.innerHTML += `

                <div class="proximo">

                    <div>

                        <strong>${partido.local}</strong>

                        <span> vs </span>

                        <strong>${partido.visitante}</strong>

                    </div>

                    <small>${partido.fecha}</small>

                </div>

            `;

        });

    } catch (error) {

        console.error(error);

    }

}