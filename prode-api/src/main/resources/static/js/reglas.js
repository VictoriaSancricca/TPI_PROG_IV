const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

document.getElementById("nombreUsuario").innerText =
    usuario.nombre;

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