$(document).ready(function() {

});


async function registro() {
    let datos = {};
    datos.nombre = document.getElementById("txtNombre").value;
    datos.apellido = document.getElementById("txtApellido").value;
    datos.usuario = document.getElementById("txtUser").value;
    datos.genero = document.getElementById("txtGenero").value.toLowerCase();
    datos.email = document.getElementById("txtEmail").value.toLowerCase();
    datos.password = document.getElementById("txtPassword").value;
    datos.preguntaDeSeguridad = document.getElementById("txtPreguntaSeguridad").value.toLowerCase();
    datos.respuestaDeSeguridad = document.getElementById("idRespuestaSeguridad").value.toLowerCase();
    datos.phone = parseInt(document.getElementById("idNumeroTelefono").value);
    datos.fechaDeNacimiento = document.getElementById("txtFechaNacimiento").value;

    // Validación del email
    if (!esEmailValido(datos.email)) {
        return alert("Por favor, ingresa una dirección de correo válida.");
    }

    // Validación de la fecha de nacimiento
    if (datos.fechaDeNacimiento > obtenerFechaActual()) {
        return alert("Fecha seleccionada superior a la actual");
    }

    // Verifica campos vacíos
    let camposVacios = [];
    for (let clave in datos) {
        if (datos[clave] === '') {
            camposVacios.push(clave);
        }
    }
    if (camposVacios.length > 0) {
        return alert('Los siguientes campos están vacíos: ' + camposVacios.join(', '));
    }

try {
    const response = await fetch('/registrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage); // Lanza el mensaje del backend
    }

    alert("Usuario registrado con éxito");
    window.location.href = "/login.html";
} catch (error) {
    alert(`Error: ${error.message}`); // Muestra el mensaje del backend
}


    // Función para obtener la fecha actual en formato 'YYYY-MM-DD'
    function obtenerFechaActual() {
        const hoy = new Date();
        const year = hoy.getFullYear();
        const month = String(hoy.getMonth() + 1).padStart(2, '0');
        const day = String(hoy.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    // Función para validar el formato de email
    function esEmailValido(email) {
        const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return patronEmail.test(email);
    }
}

