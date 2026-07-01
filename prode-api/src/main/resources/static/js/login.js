document
    .getElementById("loginForm")
    .addEventListener("submit", async function (e) {

        e.preventDefault();

        const email =
            document.getElementById("email").value;

        const password =
            document.getElementById("password").value;

        try {

            const response = await fetch(
                "/auth/login",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        email,
                        password
                    })
                }
            );

            if (!response.ok) {

                Swal.fire({
                    icon: "error",
                    title: "No se pudo iniciar sesión",
                    text: "Email o contraseña incorrectos.",
                    confirmButtonColor: "#1E88E5"
                });
                return;
            }

            const data = await response.json();
                
            localStorage.setItem(
                "usuario",
                JSON.stringify(data)
            );

            await Swal.fire({
                icon: "success",
                title: "¡Bienvenido!",
                text: data.nombre,
                timer: 1400,
                showConfirmButton: false
            });

            if(data.rol === "ROLE_ADMIN"){

                window.location.href="/admin";

            }else{

                window.location.href="/dashboard";

            }

        } catch (error) {

            console.error(error);

            Swal.fire({
                icon: "error",
                title: "Servidor no disponible",
                text: "No fue posible conectar con el backend.",
                confirmButtonColor: "#d33"
            });
        }
    });