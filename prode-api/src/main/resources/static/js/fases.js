const API_GRUPOS = "http://localhost:8081/grupos";

const grupoSelect = document.getElementById("grupoSeleccionado");

const tabla = document.getElementById("tablaPosiciones");

let grupos = [];

document.addEventListener("DOMContentLoaded", () => {

    cargarGrupos();

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

