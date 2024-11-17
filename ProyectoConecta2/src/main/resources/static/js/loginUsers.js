window.inicioSesion = async function () {
    const datos = {
        email: document.getElementById("txtEmail").value.toLowerCase(),
        password: document.getElementById("txtPassword").value,
    };

    try {
        const response = await fetch('api/loginUser', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(datos),
        });

        if (!response.ok) {
            // Lanza un error si el estado HTTP no es 200-299
            const mensajeError = await response.text(); // Lee el mensaje del backend
            throw new Error(mensajeError || `Error: ${response.status}`);
        }

        // Supone que la respuesta es un JWT
        const jwt = await response.text();

        // Maneja el JWT recibido
        if (jwt === "No esta registrado") {
            alert("El usuario no está registrado. Por favor, verifica tus credenciales.");
        } else {
            alert("¡Bienvenido de vuelta!");
            localStorage.token = jwt;
            localStorage.email = datos.email;
            window.location.href = "/index.html";
        }

    } catch (error) {
        // Manejo centralizado de errores
        console.error("Error al iniciar sesión:", error);
        alert("Hubo un problema al iniciar sesión: " + error.message);
    }
};

