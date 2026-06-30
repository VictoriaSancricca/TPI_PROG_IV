const API_GRUPOS = "http://localhost:8081/grupos";

const grupoSelect = document.getElementById("grupoSeleccionado");

const tabla = document.getElementById("tablaPosiciones");

const btnGenerarFase = document.getElementById("btnGenerarFase");

let grupos = [];

document.addEventListener("DOMContentLoaded", () => {

    cargarGrupos();
    btnGenerarFase.addEventListener("click", generarFase);

});

grupoSelect.addEventListener("change", () => {

    cargarTabla(grupoSelect.value);

});

async function cargarGrupos() {

    const response = await fetch(API_GRUPOS);

    grupos = await response.json();

    grupoSelect.innerHTML = "";

    grupos.forEach(grupo => {

        grupoSelect.innerHTML += `

            <option value="${grupo.id}">

                Grupo ${grupo.nombre}

            </option>

        `;

    });

    if(grupos.length > 0){

        cargarTabla(grupos[0].id);

    }

}

async function cargarTabla(grupoId){

    const response = await fetch(

        API_GRUPOS + "/" + grupoId + "/tabla"

    );

    const tablaPosiciones = await response.json();

    renderTabla(tablaPosiciones);

}

function renderTabla(lista){

    tabla.innerHTML = "";

    lista.forEach((equipo,index)=>{

        let posicion = index + 1;

        let icono = "";

        if(posicion === 1){

            icono = "🥇";

        }else if(posicion === 2){

            icono = "🥈";

        }else if(posicion === 3){

            icono = "🥉";

        }

        tabla.innerHTML += `

        <div class="equipo-row ${posicion <= 2 ? "clasificado" : ""}">

            <div class="posicion">

                ${icono}

                <span>${posicion}</span>

            </div>

            <div class="partido-equipo">

                <img
                    src="/img/banderas/${equipo.bandera}"
                    class="bandera"
                    onerror="this.src='/img/no-image.png'">

                <span>

                    ${equipo.nombre}

                </span>

            </div>

            <div>${equipo.pj}</div>

            <div>${equipo.pg}</div>

            <div>${equipo.pe}</div>

            <div>${equipo.pp}</div>

            <div>${equipo.gf}</div>

            <div>${equipo.gc}</div>

            <div>

                ${equipo.dg > 0 ? "+" + equipo.dg : equipo.dg}

            </div>

            <div class="puntos">

                ${equipo.puntos}

            </div>

        </div>

        `;

    });

}

async function generarFase() {

    const confirmar = await Swal.fire({

        title: "¿Generar siguiente fase?",

        text: "Se crearán automáticamente los partidos de la siguiente fase del Mundial.",

        icon: "question",

        showCancelButton: true,

        confirmButtonText: "Generar",

        cancelButtonText: "Cancelar",

        confirmButtonColor: "#1565C0"

    });

    if (!confirmar.isConfirmed) return;

    try {

        const response = await fetch(

            "http://localhost:8081/fases/generar",

            {

                method: "POST"

            }

        );

        if (!response.ok) {

            throw new Error();

        }

        await Swal.fire({

            icon: "success",

            title: "¡Fase generada!",

            text: "La siguiente fase fue generada correctamente.",

            confirmButtonColor: "#1565C0"

        });

        location.reload();

    } catch (e) {

        Swal.fire({

            icon: "error",

            title: "Error",

            text: "No fue posible generar la siguiente fase."

        });

    }

}

