document.addEventListener("DOMContentLoaded", () => {
            document.getElementById("uu").textContent =localStorage.user;
});

const botonPublicar = document.querySelector('.boton-publicar');
const campoTexto = document.querySelector('.campo-texto');

// Función para manejar la publicación
const manejarPublicacion = async () => {
    const contenido = campoTexto.value; // Obtenemos el valor del textarea
    document.querySelector('.boton-publicar');

    if (contenido.trim() === "") {
        alert("El campo de texto está vacío. Por favor escribe algo.");
        return;
    }

    // Creamos el objeto que queremos enviar
    const datosPublicacion = {
        contenido: contenido
    };

    try {
        console.log('Esta es tu publicacion:', datosPublicacion);
        // Realizamos la solicitud fetch
        const response = await fetch('/api/publicacionesNuevas', {
            method: 'POST', // Indicamos que es una solicitud POST
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json', // Indicamos el tipo de contenido que enviamos
                'Authorization': localStorage.token,
            },
            body: JSON.stringify(datosPublicacion) // Convertimos el objeto a JSON
        });

        if (!response.ok) {
            throw new Error('Error al publicar. Intenta nuevamente.');
        }

        alert('¡Tu publicación fue creada exitosamente!');
        campoTexto.value = ""; // Limpiamos el campo de texto
        location.reload();

    } catch (error) {
        console.error('Error:', error);
        alert('Hubo un error al intentar publicar.');
    }
};

// Agregamos el evento al botón
botonPublicar.addEventListener('click', manejarPublicacion);
