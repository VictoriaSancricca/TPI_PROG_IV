const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

document.getElementById("nombreUsuario").innerText = usuario.nombre;

cargarPerfil();

async function cargarPerfil() {

    const response = await fetch(
        `/usuarios/${usuario.id}`
    );

    const perfil = await response.json();

    document.getElementById("perfilNombre").innerText =
        perfil.nombre;

    document.getElementById("perfilEmail").innerText =
        perfil.email;

    document.getElementById("perfilPuntos").innerText =
        perfil.puntos;

    document.getElementById("perfilPlenos").innerText =
        perfil.plenos;

}