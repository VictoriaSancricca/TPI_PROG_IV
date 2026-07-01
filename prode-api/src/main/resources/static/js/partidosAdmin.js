const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

if (usuario.rol !== "ROLE_ADMIN") {
    window.location.href = "/dashboard";
}

const API = "/partidos";
const API_GRUPOS = "/grupos";
const API_EQUIPOS = "/equipos";

const tabla = document.getElementById("tablaPartidos");

const modal = document.getElementById("modalPartido");

const form = document.getElementById("formPartido");

const tituloModal = document.getElementById("tituloModal");

const idPartido = document.getElementById("idPartido");

const grupo = document.getElementById("grupo");

const local = document.getElementById("local");

const visitante = document.getElementById("visitante");

const fechaHora = document.getElementById("fechaHora");

const buscar = document.getElementById("buscarPartido");

let partidos = [];
let grupos = [];

document.getElementById("btnNuevoPartido")
    .addEventListener("click", nuevoPartido);

document.getElementById("cerrarModal")
    .addEventListener("click", cerrarModal);

document.getElementById("btnCancelar")
    .addEventListener("click", cerrarModal);

form.addEventListener("submit", guardarPartido);

buscar.addEventListener("keyup", filtrarPartidos);

grupo.addEventListener("change", () => {

    cargarEquipos(grupo.value);

});

inicializar();

async function inicializar() {

    await cargarGrupos();

    await cargarPartidos();

}

async function cargarGrupos() {

    const response = await fetch(API_GRUPOS);

    grupos = await response.json();

    grupo.innerHTML =
        "<option value=''>Seleccione un grupo</option>";

    grupos.forEach(g => {

        grupo.innerHTML += `

            <option value="${g.id}">

                Grupo ${g.nombre}

            </option>

        `;

    });

}

async function cargarEquipos(grupoId) {

    local.innerHTML =
        "<option value=''>Seleccione...</option>";

    visitante.innerHTML =
        "<option value=''>Seleccione...</option>";

    if (!grupoId) return;

    const response = await fetch(

        API_EQUIPOS + "/grupo/" + grupoId

    );

    const equipos = await response.json();

    equipos.forEach(e => {

        local.innerHTML += `

            <option value="${e.id}">

                ${e.nombre}

            </option>

        `;

        visitante.innerHTML += `

            <option value="${e.id}">

                ${e.nombre}

            </option>

        `;

    });

}

async function cargarPartidos() {

    const response = await fetch(API);

    partidos = await response.json();

    renderTabla(partidos);

}

function renderTabla(lista) {

    tabla.innerHTML = "";

    document.getElementById("contadorPartidos").innerText =
        `Mostrando ${lista.length} partido${lista.length != 1 ? "s" : ""}`;

    lista.forEach(partido => {

        tabla.innerHTML += `

        <div class="equipo-row">

            <div>

                <span class="grupo-pill">

                    ${partido.grupo?.nombre ?? "-"}

                </span>

            </div>

            <div class="partido-equipo">

                <img
                    src="/img/banderas/${partido.local.bandera}"
                    class="bandera"
                    onerror="this.src='/img/no-image.png'">

                <span>

                    ${partido.local.nombre}

                </span>

            </div>

            <div class="vs">

                VS

            </div>

            <div class="partido-equipo">

                <img
                    src="/img/banderas/${partido.visitante.bandera}"
                    class="bandera"
                    onerror="this.src='/img/no-image.png'">

                <span>

                    ${partido.visitante.nombre}

                </span>

            </div>

            <div>

                <span class="estado ${partido.estado.toLowerCase()}">

                    ${partido.estado.replaceAll("_"," ")}

                </span>

            </div>

            <div class="equipo-acciones">

                <button
                    class="btn-editar"
                    onclick="editar(${partido.id})">

                    <i class="bi bi-pencil-fill"></i>

                </button>

                <button
                    class="btn-eliminar"
                    onclick="eliminar(${partido.id})">

                    <i class="bi bi-trash-fill"></i>

                </button>

            </div>

        </div>

        `;

    });

}

//nloque dos

function nuevoPartido(){

    tituloModal.innerText = "Nuevo Partido";

    form.reset();

    idPartido.value = "";

    grupo.value = "";

    local.innerHTML =
        "<option value=''>Seleccione...</option>";

    visitante.innerHTML =
        "<option value=''>Seleccione...</option>";

    abrirModal();

}

async function guardarPartido(e){

    e.preventDefault();

    if(grupo.value===""){

        Swal.fire({

            icon:"warning",

            title:"Seleccione un grupo"

        });

        return;

    }

    if(local.value===""){

        Swal.fire({

            icon:"warning",

            title:"Seleccione el equipo local"

        });

        return;

    }

    if(visitante.value===""){

        Swal.fire({

            icon:"warning",

            title:"Seleccione el equipo visitante"

        });

        return;

    }

    if(local.value===visitante.value){

        Swal.fire({

            icon:"warning",

            title:"El local y el visitante no pueden ser el mismo equipo"

        });

        return;

    }

    const partido={

        grupoId:Number(grupo.value),

        localId:Number(local.value),

        visitanteId:Number(visitante.value),

        fechaHora:fechaHora.value

    };

    try{

        let response;

        if(idPartido.value===""){

            response=await fetch(API,{

                method:"POST",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(partido)

            });

        }else{

            response=await fetch(API+"/"+idPartido.value,{

                method:"PUT",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(partido)

            });

        }

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Partido guardado",

            timer:1200,

            showConfirmButton:false

        });

        cerrarModal();

        cargarPartidos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo guardar"

        });

    }

}

async function editar(id) {

    const partido = partidos.find(p => p.id === id);

    console.log(partido);

    if (!partido) {
        Swal.fire({
            icon: "error",
            title: "No se encontró el partido"
        });
        return;
    }

    tituloModal.innerText = "Editar Partido";

    idPartido.value = partido.id;

    if (partido.grupo) {

        grupo.value = partido.grupo.id;

        await cargarEquipos(partido.grupo.id);

    } else {

        grupo.value = "";

        await cargarEquipos("");

    }

    if (partido.local) {
        local.value = partido.local.id;
    }

    if (partido.visitante) {
        visitante.value = partido.visitante.id;
    }

    if (partido.fechaHora) {
        fechaHora.value = partido.fechaHora.substring(0, 16);
    }

    abrirModal();
}

async function eliminar(id){

    const r = await Swal.fire({

        title:"¿Eliminar partido?",

        text:"Esta acción no podrá deshacerse.",

        icon:"warning",

        showCancelButton:true,

        confirmButtonText:"Eliminar",

        cancelButtonText:"Cancelar",

        confirmButtonColor:"#d33"

    });

    if(!r.isConfirmed){

        return;

    }

    try{

        const response = await fetch(

            API+"/"+id,

            {

                method:"DELETE"

            }

        );

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Partido eliminado",

            timer:1200,

            showConfirmButton:false

        });

        cargarPartidos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo eliminar"

        });

    }

}

function filtrarPartidos(){

    const texto=buscar.value.toLowerCase();

    const filtrados=partidos.filter(p=>

        p.local.nombre.toLowerCase().includes(texto)

        ||

        p.visitante.nombre.toLowerCase().includes(texto)

        ||

        p.grupo.nombre.toLowerCase().includes(texto)

    );

    renderTabla(filtrados);

}

//bloque 3

function abrirModal(){

    modal.classList.add("show");

}

function cerrarModal(){

    modal.classList.remove("show");

}

window.editar = editar;

window.eliminar = eliminar;