const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

if (usuario.rol !== "ROLE_ADMIN") {
    window.location.href = "/dashboard";
}

const API = "http://localhost:8081/grupos";

const contenedor = document.getElementById("contenedorGrupos");

const modal = document.getElementById("modalGrupo");

const form = document.getElementById("formGrupo");

const tituloModal = document.getElementById("tituloModal");

const idGrupo = document.getElementById("idGrupo");

const nombre = document.getElementById("nombre");

const buscar = document.getElementById("buscarGrupo");

let grupos = [];

document
    .getElementById("btnNuevoGrupo")
    .addEventListener("click", nuevoGrupo);

document
    .getElementById("cerrarModal")
    .addEventListener("click", cerrarModal);

document
    .getElementById("btnCancelar")
    .addEventListener("click", cerrarModal);

form.addEventListener("submit", guardarGrupo);

buscar.addEventListener("keyup", filtrarGrupos);

cargarGrupos();

async function cargarGrupos(){

    const response = await fetch(API);

    grupos = await response.json();

    renderGrupos(grupos);

}

function renderGrupos(lista){

    contenedor.innerHTML = "";

    lista.forEach(grupo=>{

        let equiposHTML = "";

        if(grupo.equipos.length === 0){

            equiposHTML = `

                <div class="sin-equipos">

                    Todavía no hay equipos asignados.

                </div>

            `;

        }else{

            grupo.equipos.forEach(equipo=>{

                equiposHTML += `

                    <div class="equipo-item">

                        <div class="equipo-info">

                            <img
                                src="/img/banderas/${equipo.bandera}"
                                class="bandera">

                            <div>

                                <strong>${equipo.nombre}</strong>

                                <span>${equipo.codigoFifa}</span>

                            </div>

                        </div>

                        <div class="equipo-puntos">

                            0 pts

                        </div>

                    </div>

                `;

            });

        }

        contenedor.innerHTML += `

            <div class="grupo-card">

                <div class="grupo-header">

                    <h2>

                        <i class="bi bi-diagram-3-fill"></i>

                        Grupo ${grupo.nombre}

                    </h2>

                </div>

                <div class="grupo-body">

                    ${equiposHTML}

                </div>

                <div class="grupo-footer">

                    <button
                        class="btn-clasificar">

                        <i class="bi bi-arrow-right-circle-fill"></i>

                        Pasar de fase

                    </button>

                    <div>

                        <button
                            class="btn-editar"
                            onclick="editar(${grupo.id})">

                            <i class="bi bi-pencil-fill"></i>

                        </button>

                        <button
                            class="btn-eliminar"
                            onclick="eliminar(${grupo.id})">

                            <i class="bi bi-trash-fill"></i>

                        </button>

                    </div>

                </div>

            </div>

        `;

    });

}

function filtrarGrupos(){

    const texto = buscar.value.toLowerCase();

    const filtrados = grupos.filter(g =>

        g.nombre.toLowerCase().includes(texto)

    );

    renderGrupos(filtrados);

}

function nuevoGrupo(){

    tituloModal.innerText = "Nuevo Grupo";

    form.reset();

    idGrupo.value = "";

    abrirModal();

}

function abrirModal(){

    modal.classList.add("show");

}

function cerrarModal(){

    modal.classList.remove("show");

}

async function guardarGrupo(e){

    e.preventDefault();

    if(nombre.value.trim()===""){

        Swal.fire({

            icon:"warning",

            title:"Ingrese el nombre del grupo"

        });

        return;

    }

    const grupo={

        nombre:nombre.value.trim().toUpperCase()

    };

    try{

        let response;

        if(idGrupo.value===""){

            response = await fetch(API,{

                method:"POST",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(grupo)

            });

        }else{

            response = await fetch(API+"/"+idGrupo.value,{

                method:"PUT",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(grupo)

            });

        }

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Grupo guardado",

            timer:1200,

            showConfirmButton:false

        });

        cerrarModal();

        cargarGrupos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo guardar el grupo"

        });

    }

}

function editar(id){

    const grupo = grupos.find(g=>g.id===id);

    if(!grupo){

        return;

    }

    tituloModal.innerText="Editar Grupo";

    idGrupo.value=grupo.id;

    nombre.value=grupo.nombre;

    abrirModal();

}

async function eliminar(id){

    const r = await Swal.fire({

        title:"¿Eliminar grupo?",

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

            title:"Grupo eliminado",

            timer:1200,

            showConfirmButton:false

        });

        cargarGrupos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo eliminar"

        });

    }

}

window.editar = editar;

window.eliminar = eliminar;