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
                "http://localhost:8081/auth/login",
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

                alert("Email o contraseña incorrectos");
                return;
            }

            const data = await response.json();

            localStorage.setItem(
                "usuario",
                JSON.stringify(data)
            );

            alert("Bienvenido " + data.nombre);

            window.location.href = "/dashboard";

        } catch (error) {

            console.error(error);

            alert("Error al conectar con el servidor");
        }
    });