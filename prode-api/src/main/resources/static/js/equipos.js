const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

if (usuario.rol !== "ROLE_ADMIN") {
    window.location.href = "/dashboard";
}

const API = "http://localhost:8081/equipos";

const tabla = document.getElementById("tablaEquipos");

const modal = document.getElementById("modalEquipo");

const form = document.getElementById("formEquipo");

const tituloModal = document.getElementById("tituloModal");

const idEquipo = document.getElementById("idEquipo");

const nombre = document.getElementById("nombre");

const codigoFifa = document.getElementById("codigoFifa");

const grupo = document.getElementById("grupo");

const bandera = document.getElementById("bandera");

const preview = document.getElementById("previewBandera");

const buscar = document.getElementById("buscarEquipo");

let equipos = [];
let banderaActual = "";

document.getElementById("btnNuevoEquipo")
    .addEventListener("click", nuevoEquipo);

document.getElementById("cerrarModal")
    .addEventListener("click", cerrarModal);

document.getElementById("btnCancelar")
    .addEventListener("click", cerrarModal);

form.addEventListener("submit", guardarEquipo);

buscar.addEventListener("keyup", filtrarEquipos);

bandera.addEventListener("change", mostrarPreview);

cargarEquipos();
cargarGrupos();
async function cargarEquipos() {

    const response = await fetch(API);

    equipos = await response.json();

    renderTabla(equipos);

}
async function cargarGrupos(){

    const response = await fetch(
        "http://localhost:8081/grupos"
    );

    const grupos = await response.json();

    const select = document.getElementById("grupo");

    select.innerHTML="";

    grupos.forEach(g=>{

        select.innerHTML += `

            <option value="${g.id}">

                Grupo ${g.nombre}

            </option>

        `;

    });

}
function renderTabla(lista){

    tabla.innerHTML = "";

    document.getElementById("contadorEquipos").innerText =
        `Mostrando ${lista.length} equipo${lista.length !== 1 ? "s" : ""}`;

    lista.forEach(equipo => {

        tabla.innerHTML += `

        <div class="equipo-row">

            <div class="equipo-bandera">

                <img
                    src="/img/banderas/${equipo.bandera}"
                    class="bandera"
                    onerror="this.src='/img/no-image.png'">

            </div>

            <div class="equipo-nombre">

                ${equipo.nombre}

            </div>

            <div class="equipo-fifa">

                ${equipo.codigoFifa}

            </div>

            <div class="equipo-grupo">

                <span class="grupo-pill">

                    ${equipo.grupo ? equipo.grupo.nombre : "-"}

                </span>

            </div>

            <div class="equipo-acciones">

                <button
                    class="btn-editar"
                    onclick="editar(${equipo.id})">

                    <i class="bi bi-pencil-fill"></i>

                </button>

                <button
                    class="btn-eliminar"
                    onclick="eliminar(${equipo.id})">

                    <i class="bi bi-trash-fill"></i>

                </button>

            </div>

        </div>

        `;

    });

}

function filtrarEquipos(){

    const texto = buscar.value.toLowerCase();

    const filtrados = equipos.filter(e =>

        e.nombre.toLowerCase().includes(texto) ||

        e.codigoFifa.toLowerCase().includes(texto)

    );

    renderTabla(filtrados);

}

function nuevoEquipo(){

    tituloModal.innerText = "Nuevo Equipo";

    form.reset();

    idEquipo.value = "";

    preview.src="/img/no-image.png";

    banderaActual = "";

    bandera.value = "";

    grupo.selectedIndex = 0;

    abrirModal();

}

function abrirModal(){

    modal.classList.add("show");

}

function cerrarModal(){

    modal.classList.remove("show");

}

function mostrarPreview(){

    if(bandera.files.length===0){

        preview.src="/img/no-image.png";

        return;

    }

    preview.src = URL.createObjectURL(

        bandera.files[0]

    );

}

async function guardarEquipo(e){

    e.preventDefault();

    if(nombre.value.trim()===""){

    Swal.fire({

        icon:"warning",

        title:"Ingrese un nombre"

    });

    return;

}

if(codigoFifa.value.trim()===""){

    Swal.fire({

        icon:"warning",

        title:"Ingrese el código FIFA"

    });

    return;

}

const equipo = {

    nombre: nombre.value.trim(),

    codigoFifa: codigoFifa.value.trim().toUpperCase(),

    bandera: bandera.files.length > 0
        ? bandera.files[0].name
        : banderaActual,

    grupoId: Number(grupo.value)

};

    try{

        let response;

        if(idEquipo.value===""){

            response = await fetch(API,{

                method:"POST",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(equipo)

            });

        }else{

            response = await fetch(API+"/"+idEquipo.value,{

                method:"PUT",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(equipo)

            });

        }

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Equipo guardado",

            timer:1300,

            showConfirmButton:false

        });

        cerrarModal();

        cargarEquipos();

    }catch(err){

        Swal.fire({

            icon:"error",

            title:"No se pudo guardar"

        });

    }

}

async function editar(id){

    const equipo = equipos.find(e=>e.id===id);

    if(!equipo){

        return;

    }

    tituloModal.innerText="Editar Equipo";

    idEquipo.value=equipo.id;

    nombre.value=equipo.nombre;

    codigoFifa.value=equipo.codigoFifa;
    grupo.value = equipo.grupo.id;

    preview.src="/img/banderas/"+equipo.bandera;

    banderaActual = equipo.bandera;

    bandera.value = "";

    abrirModal();

}

async function eliminar(id){

    const r = await Swal.fire({

        title:"¿Eliminar equipo?",

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

        const response = await fetch(API+"/"+id,{

            method:"DELETE"

        });

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Equipo eliminado",

            timer:1200,

            showConfirmButton:false

        });

        cargarEquipos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo eliminar"

        });

    }

}

window.editar=editar;

window.eliminar=eliminar;