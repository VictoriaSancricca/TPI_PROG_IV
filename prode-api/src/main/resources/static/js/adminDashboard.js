const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

if (usuario.rol !== "ROLE_ADMIN") {

    Swal.fire({
        icon: "error",
        title: "Acceso denegado",
        text: "No tenés permisos para ingresar al panel administrador."
    }).then(() => {
        window.location.href = "/dashboard";
    });

}

document.getElementById("logout")
    .addEventListener("click", cerrarSesion);

async function cerrarSesion(e) {

    e.preventDefault();

    const resultado = await Swal.fire({

        title: "¿Cerrar sesión?",

        text: "Se cerrará la sesión del administrador.",

        icon: "question",

        showCancelButton: true,

        confirmButtonText: "Sí",

        cancelButtonText: "Cancelar"

    });

    if (resultado.isConfirmed) {

        localStorage.removeItem("usuario");

        window.location.href = "/login";

    }

}

cargarDashboard();

async function cargarDashboard() {

    try {

        const usuarios = await fetch("/usuarios")
            .then(r => r.json());

        const equipos = await fetch("/equipos")
            .then(r => r.json());

        const grupos = await fetch("/grupos")
            .then(r => r.json());

        const partidos = await fetch("/partidos")
            .then(r => r.json());

        // Tarjetas superiores

        document.getElementById("cantUsuarios").innerText = usuarios.length;
        document.getElementById("cantEquipos").innerText = equipos.length;
        document.getElementById("cantGrupos").innerText = grupos.length;
        document.getElementById("cantPartidos").innerText = partidos.length;

        // Resumen

        const jugados = partidos.filter(
            p => p.estado === "FINALIZADO"
        ).length;

        const pendientes = partidos.filter(
            p => p.estado === "POR_JUGARSE"
        ).length;

        document.getElementById("partidosJugados").innerText = jugados;
        document.getElementById("partidosPendientes").innerText = pendientes;
        document.getElementById("totalPartidos").innerText = partidos.length;

        // Fase actual

        const fases = [
            "GRUPOS",
            "PRELIMINAR",
            "OCTAVOS",
            "CUARTOS",
            "SEMIFINAL",
            "TERCER_PUESTO",
            "FINAL"
        ];

        let faseActual = "FINALIZADO";

        for (const fase of fases) {

            if (partidos.some(
                p => p.fase === fase && p.estado === "POR_JUGARSE"
            )) {

                faseActual = fase.replaceAll("_", " ");
                break;

            }

        }

        document.getElementById("faseActual").innerText = faseActual;

        // Próximos partidos

        const contenedor = document.getElementById("proximosPartidos");

        contenedor.innerHTML = "";

        partidos
            .filter(p => p.estado === "POR_JUGARSE")
            .sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora))
            .slice(0, 5)
            .forEach(p => {

                const fecha = new Date(p.fechaHora);

                contenedor.innerHTML += `

                    <div class="proximo">

                        <div>

                            <strong>${p.local.nombre} vs ${p.visitante.nombre}</strong><br>

                            <small>
                                ${fecha.toLocaleDateString("es-AR")} -
                                ${fecha.toLocaleTimeString("es-AR", {
                                    hour: "2-digit",
                                    minute: "2-digit"
                                })}
                            </small>

                        </div>

                    </div>

                `;

            });

    } catch (e) {

        console.error(e);

    }

}