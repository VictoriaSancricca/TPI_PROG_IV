const PARTIDOS_POR_PAGINA = 10;

let partidos = [];
let paginaActual = 1;
let partidoSeleccionado = null;

cargarPartidos();

async function cargarPartidos() {

    const response = await fetch(`${API}/partidos`);;

    partidos = await response.json();

    partidos.sort((a, b) =>
        new Date(a.fechaHora) - new Date(b.fechaHora)
    );

    renderPartidos();

}

function renderPartidos() {

    const contenedor = document.getElementById("partidosContainer");

    contenedor.innerHTML = "";

    const inicio = (paginaActual - 1) * PARTIDOS_POR_PAGINA;

    const fin = inicio + PARTIDOS_POR_PAGINA;

    const pagina = partidos.slice(inicio, fin);

    pagina.forEach(partido => {

        const fechaHora = new Date(partido.fechaHora);

        const fecha = fechaHora.toLocaleDateString("es-AR", {
            day: "2-digit",
            month: "short"
        }).toUpperCase();

        const hora = fechaHora.toLocaleTimeString("es-AR", {
            hour: "2-digit",
            minute: "2-digit"
        });

        contenedor.innerHTML += `

        <div class="match-card">

            <div class="match-date">

                <span class="group">

                    ${
                        partido.grupo
                            ? "Grupo " + partido.grupo.nombre
                            : partido.fase.replaceAll("_", " ")
                    }

                </span>

                <small>

                    <i class="bi bi-calendar3"></i>

                    ${fecha}

                </small>

                <small>

                    <i class="bi bi-clock"></i>

                    ${hora}

                </small>

            </div>

            <div class="team">

                <div class="team-icon">

                    <i class="bi bi-circle-fill"></i>

                </div>

                <span>${partido.local.nombre}</span>

            </div>

            <div class="score">

                ${
                    partido.estado === "FINALIZADO"
                        ? `${partido.golesLocal} - ${partido.golesVisitante}`
                        : "VS"
                }

            </div>

            <div class="team">

                <div class="team-icon">

                    <i class="bi bi-circle-fill"></i>

                </div>

                <span>${partido.visitante.nombre}</span>

            </div>

            <div>

                <div class="estado ${partido.estado.toLowerCase()}">

                    ${partido.estado.replace("_", " ")}

                </div>

                ${
                    partido.estado === "POR_JUGARSE"
                        ? `
                        <button
                            class="btn-pronosticar"
                            onclick="abrirPronostico(${partido.id}, '${partido.local.nombre}', '${partido.visitante.nombre}')">

                            <i class="bi bi-bullseye"></i>

                            Pronosticar

                        </button>
                        `
                        : ""
                }

            </div>

        </div>

        `;

    });

    renderPaginacion();

}

function renderPaginacion() {

    const paginacion = document.getElementById("paginacion");

    paginacion.innerHTML = "";

    const totalPaginas = Math.ceil(
        partidos.length / PARTIDOS_POR_PAGINA
    );

    for (let i = 1; i <= totalPaginas; i++) {

        paginacion.innerHTML += `

            <button
                class="pagina ${i === paginaActual ? "activa" : ""}"
                onclick="irPagina(${i})">

                ${i}

            </button>

        `;

    }

}

function irPagina(numero) {

    paginaActual = numero;

    renderPartidos();

}

window.irPagina = irPagina;


function abrirPronostico(id, local, visitante) {
    alert("Entró");
    partidoSeleccionado = id;

    document.getElementById("tituloPartido").innerText =
        local + " vs " + visitante;

    document.getElementById("golesLocal").value = "";

    document.getElementById("golesVisitante").value = "";

    document
        .getElementById("modalPronostico")
        .classList.add("show");

}

document
    .getElementById("btnCancelarPronostico")
    .addEventListener("click", () => {

        document
            .getElementById("modalPronostico")
            .classList.remove("show");

    });

window.abrirPronostico = abrirPronostico;

document
    .getElementById("btnGuardarPronostico")
    .addEventListener("click", guardarPronostico);

async function guardarPronostico() {

    const usuario = JSON.parse(localStorage.getItem("usuario"));

    const golesLocal = document.getElementById("golesLocal").value;

    const golesVisitante = document.getElementById("golesVisitante").value;

    if (golesLocal === "" || golesVisitante === "") {

        Swal.fire({

            icon: "warning",

            title: "Ingresá ambos resultados"

        });

        return;

    }

    try {

        const response = await fetch(

            "/pronosticos",

            {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    usuarioId: usuario.id,

                    partidoId: partidoSeleccionado,

                    golesLocal: Number(golesLocal),

                    golesVisitante: Number(golesVisitante)

                })

            }

        );

        if (!response.ok) {

            const mensaje = await response.text();

            Swal.fire({

                icon: "warning",

                title: "No se pudo guardar",

                text: mensaje.includes("expiró")
                    ? "El plazo para realizar este pronóstico ya finalizó."
                    : "No fue posible guardar el pronóstico."

            });

        }

        document
            .getElementById("modalPronostico")
            .classList.remove("show");

        Swal.fire({

            icon: "success",

            title: "Pronóstico guardado",

            timer: 1500,

            showConfirmButton: false

        });

        cargarPartidos();

    } catch (e) {

        Swal.fire({

            icon: "error",

            title: "No se pudo guardar",

            text: e.message

        });

    }

}