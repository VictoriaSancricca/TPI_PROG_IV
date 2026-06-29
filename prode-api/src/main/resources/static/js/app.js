const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

const nombreUsuario = document.getElementById("nombreUsuario");

if (nombreUsuario) {
    nombreUsuario.innerText = usuario.nombre;
}

const logout = document.getElementById("logout");

if (logout) {

    logout.addEventListener("click", async function (e) {

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

}

const sidebar =
    document.querySelector(".sidebar");

const contenido =
    document.querySelector(".content");

const boton =
    document.getElementById("toggleSidebar");

if (boton) {

    boton.addEventListener("click", () => {

        sidebar.classList.toggle("collapsed");

        contenido.classList.toggle("expanded");

    });

}