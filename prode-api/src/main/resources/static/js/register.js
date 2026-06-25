document
    .getElementById("registerForm")
    .addEventListener("submit", async function (e) {

        e.preventDefault();

        const nombre =
            document.getElementById("nombre").value;

        const email =
            document.getElementById("email").value;

        const password =
            document.getElementById("password").value;

        const confirmPassword =
            document.getElementById("confirmPassword").value;

        if (password !== confirmPassword) {

            Swal.fire({
                icon: "warning",
                title: "Contraseñas diferentes",
                text: "Las contraseñas deben coincidir.",
                confirmButtonColor: "#F4B400"
            });

            return;
        }

        try {

            const response = await fetch(
                "http://localhost:8081/auth/register",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        nombre,
                        email,
                        password
                    })
                }
            );

            if (!response.ok) {

                const mensaje = await response.text();

                Swal.fire({
                    icon: "error",
                    title: "No se pudo registrar",
                    text: mensaje,
                    confirmButtonColor: "#d33"
                });

                return;
            }

            await Swal.fire({
                icon: "success",
                title: "¡Registro exitoso!",
                text: "Ya podés iniciar sesión.",
                confirmButtonColor: "#1E88E5"
            });

            window.location.href = "/login";

        } catch (error) {

            console.error(error);

            alert("Error al conectar con el servidor.");

        }

    });