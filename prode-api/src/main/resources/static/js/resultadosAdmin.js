const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
    window.location.href = "/login";
}

if (usuario.rol !== "ROLE_ADMIN") {
    window.location.href = "/dashboard";
}

const API = "http://localhost:8081/partidos";

const tabla = document.getElementById("tablaResultados");

const modal = document.getElementById("modalResultado");

const form = document.getElementById("formResultado");

const tituloModal = document.getElementById("tituloModal");

const idPartido = document.getElementById("idPartido");

const golesLocal = document.getElementById("golesLocal");

const golesVisitante = document.getElementById("golesVisitante");

const nombreLocal = document.getElementById("nombreLocal");

const nombreVisitante = document.getElementById("nombreVisitante");

const buscar = document.getElementById("buscarResultado");

const filtroFase = document.getElementById("filtroFase");

const banderaLocal = document.getElementById("banderaLocal");

const banderaVisitante = document.getElementById("banderaVisitante");

let partidos = [];

document.getElementById("cerrarModal")
    .addEventListener("click", cerrarModal);

document.getElementById("btnCancelar")
    .addEventListener("click", cerrarModal);

form.addEventListener("submit", guardarResultado);

buscar.addEventListener("keyup", filtrarResultados);
filtroFase.addEventListener("change", cargarPartidos);
cargarPartidos();

async function cargarPartidos(){

    let url = API;

    const fase = filtroFase.value;

    if(fase !== ""){

        url += "?fase=" + fase;

    }

    const response = await fetch(url);

    partidos = await response.json();

    renderTabla(partidos);

}

function renderTabla(lista){

    tabla.innerHTML = "";

    document.getElementById("contadorResultados").innerText =
        `Mostrando ${lista.length} partido${lista.length != 1 ? "s" : ""}`;

    lista.forEach(partido=>{

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

                <strong style="margin:0 12px;">VS</strong>

                <img
                    src="/img/banderas/${partido.visitante.bandera}"
                    class="bandera"
                    onerror="this.src='/img/no-image.png'">

                <span>

                    ${partido.visitante.nombre}

                </span>

            </div>

            <div class="resultado">

                ${partido.golesLocal ?? "-"}

                :

                ${partido.golesVisitante ?? "-"}

            </div>

            <div>

                <span class="estado ${partido.estado.toLowerCase()}">

                    ${partido.estado.replaceAll("_"," ")}

                </span>

            </div>

            <div class="equipo-acciones">

                <button
                    class="btn-resultado"
                    onclick="registrar(${partido.id})">

                    <i class="bi bi-pencil-fill"></i>

                </button>

            </div>

        </div>

        `;

    });

}

function filtrarResultados(){

    const texto = buscar.value.toLowerCase();

    const filtrados = partidos.filter(p =>

        p.local.nombre.toLowerCase().includes(texto)

        ||

        p.visitante.nombre.toLowerCase().includes(texto)

        ||

        (p.grupo?.nombre ?? "").toLowerCase().includes(texto)

    );

    renderTabla(filtrados);

}

async function registrar(id){

    const partido = partidos.find(

        p => p.id === id

    );

    if(!partido){

        return;

    }

    tituloModal.innerText = "Registrar Resultado";

    idPartido.value = partido.id;

    nombreLocal.innerText = partido.local.nombre;

    nombreVisitante.innerText = partido.visitante.nombre;

    banderaLocal.src =
        "/img/banderas/" + partido.local.bandera;

    banderaVisitante.src =
        "/img/banderas/" + partido.visitante.bandera;

    golesLocal.value = partido.golesLocal ?? "";

    golesVisitante.value = partido.golesVisitante ?? "";

    abrirModal();

}

async function guardarResultado(e){

    e.preventDefault();

    if(golesLocal.value===""){

        Swal.fire({

            icon:"warning",

            title:"Ingrese los goles del equipo local"

        });

        return;

    }

    if(golesVisitante.value===""){

        Swal.fire({

            icon:"warning",

            title:"Ingrese los goles del equipo visitante"

        });

        return;

    }

    const resultado={

        golesLocal:Number(golesLocal.value),

        golesVisitante:Number(golesVisitante.value)

    };

    try{

        const response = await fetch(

            API+"/"+idPartido.value+"/resultado",

            {

                method:"POST",

                headers:{

                    "Content-Type":"application/json"

                },

                body:JSON.stringify(resultado)

            }

        );

        if(!response.ok){

            throw new Error();

        }

        Swal.fire({

            icon:"success",

            title:"Resultado registrado",

            timer:1300,

            showConfirmButton:false

        });

        cerrarModal();

        cargarPartidos();

    }catch(e){

        Swal.fire({

            icon:"error",

            title:"No se pudo registrar el resultado"

        });

    }

}

function abrirModal(){

    modal.classList.add("show");

}

function cerrarModal(){

    modal.classList.remove("show");

    form.reset();

}

window.registrar = registrar;