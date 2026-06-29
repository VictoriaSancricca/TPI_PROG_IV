const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

document.getElementById("nombreUsuario").innerText =
    usuario.nombre;

cargarRanking();

async function cargarRanking() {

    const response = await fetch(
        "http://localhost:8081/usuarios/ranking"
    );

    const ranking = await response.json();

    const contenedor =
        document.getElementById("rankingContainer");

    contenedor.innerHTML = "";

    ranking.forEach((usuario, index) => {

        let medalla = "";

        if (index === 0)
            medalla = "🥇";

        else if (index === 1)
            medalla = "🥈";

        else if (index === 2)
            medalla = "🥉";

        contenedor.innerHTML += `

        <div class="ranking-card">

            <span>${medalla} #${index + 1}</span>

            <strong>${usuario.nombre}</strong>

            <span>${usuario.puntos} pts</span>

            <span>${usuario.plenos} plenos</span>

        </div>

        `;

    });

}