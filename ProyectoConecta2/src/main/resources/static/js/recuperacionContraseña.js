window.recuperarContraseña = async function () {
    let email = document.getElementById("txtEmail").value.toLowerCase();

    // Validar si el email está vacío
    if (email === "") {
        return alert("Ingrese una dirección de correo por favor.");
    }

    // Preparar los datos de la solicitud
    let datos = { email };

    try {
        // Hacer la solicitud al backend
        const response = await fetch("api/recuperarUsuario", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(datos),
        });

        // Verificar si hubo un error HTTP
        if (!response.ok) {
            throw new Error(
                `Error en la solicitud: ${response.status} ${response.statusText}`
            );
        }

        // Procesar la respuesta JSON
        const usuario = await response.json();

        if (usuario && usuario.preguntaDeSeguridad) {
            // Caso: usuario encontrado
            alert(`Email ${email} encontrado.\nSu pregunta de seguridad es: "${usuario.preguntaDeSeguridad}"`);
            mostrarUsuarioEnFront(usuario); // Pasar usuario como parámetro
        } else {
            // Caso: usuario no encontrado
            alert("Usuario no encontrado. Verifique la dirección de correo.");
        }
    } catch (error) {
        // Manejo de errores
        alert("Usuario no encontrado. Verifique la dirección de correo.");
    }
};

function mostrarUsuarioEnFront(usuario) {
    // Limpiar y cambiar el propósito del campo de texto
    const emailField = document.getElementById("txtEmail");
    emailField.value = "";
    emailField.placeholder = "Ingrese su respuesta de seguridad";

    // Cambiar el evento del botón para que verifique la respuesta
    const boton = document.getElementById("botonEnviado");
    boton.onclick = function () {
        verificar(usuario);
    };
}

function verificar(usuario) {
    const respuestaUsuario = document.getElementById("txtEmail").value.trim();

    if (respuestaUsuario === usuario.respuestaDeSeguridad) {
        alert(`Respuesta verificada.\nSu contraseña es: ${usuario.password}`);
        window.location.href = "/login.html";
    } else {
        alert("Respuesta de seguridad incorrecta. Adiós, intruso XD");
        window.location.href = "/login.html";
    }
}
