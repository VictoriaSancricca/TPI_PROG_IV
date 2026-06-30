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

    if(resultado.isConfirmed){

        localStorage.removeItem("usuario");

        window.location.href="/login";

    }

}

cargarDashboard();

async function cargarDashboard(){

    try{

        const usuarios = await fetch("http://localhost:8081/usuarios")
            .then(r => r.json());

        const equipos = await fetch("http://localhost:8081/equipos")
            .then(r => r.json());

        const partidos = await fetch("http://localhost:8081/partidos")
            .then(r => r.json());

        document.getElementById("cantUsuarios").innerText = usuarios.length;

        document.getElementById("cantEquipos").innerText = equipos.length;

        document.getElementById("cantPartidos").innerText = partidos.length;

        document.getElementById("cantGrupos").innerText = 8;

    }catch(e){

        console.error(e);

    }

}