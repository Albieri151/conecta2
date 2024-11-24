document.addEventListener('DOMContentLoaded', () => {
  console.log("Página cargada, llamando a fetchProfileData...");
  fetchProfileData();
});


// Función para obtener los datos del perfil
async function fetchProfileData() {
  try {
    const response = await fetch('api/buscarUser', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token,

      },
      body: JSON.stringify({ userBuscado: localStorage.userBuscado }),
       cache: 'no-store', // Desactiva el uso de la caché
    });

    if (!response.ok) {
      throw new Error('Error al obtener los datos del perfil');
    }


    const data = await response.json();
    // Asignar los datos a los campos del formulario
    document.querySelector('input[placeholder="Nombre"]').value = data.nombre;
    document.querySelector('input[placeholder="Apellido"]').value = data.apellido;
    document.getElementById("txtGenero").value = data.genero.toLowerCase();
    document.querySelector('input[placeholder="Email"]').value = data.email;
    document.querySelector('input[placeholder="Username"]').value = data.usuario;
    document.querySelector('input[placeholder="Password"]').value = data.password;
    document.querySelector('input[placeholder="Pregunta de seguridad"]').value = data.preguntaDeSeguridad;
    document.querySelector('input[placeholder="Respuesta de seguridad"]').value = data.respuestaDeSeguridad;
    let fechaNaci = String(data.fechaDeNacimiento);
    document.getElementById("txtDate").value = fechaNaci;
    document.getElementById("telf").value = "0"+data.phone;
    document.getElementById("seguidos").textContent  = data.siguiendo
    document.getElementById("seguidores").textContent  = data.seguidores;
    document.getElementById("publicaciones").textContent  = data.publicaciones;
    document.getElementById("us").innerHTML = localStorage.user;
    localStorage.mk = data.id;

    document.querySelector('input[placeholder="Nombre"]').readOnly=true;
    document.querySelector('input[placeholder="Apellido"]').readOnly=true;
    document.getElementById("txtGenero").disabled=true;
    document.querySelector('input[placeholder="Email"]').readOnly=true;
    document.querySelector('input[placeholder="Username"]').readOnly=true;
    document.querySelector('input[placeholder="Password"]').readOnly=true;
    document.querySelector('input[placeholder="Pregunta de seguridad"]').readOnly=true;
    document.querySelector('input[placeholder="Respuesta de seguridad"]').readOnly=true;
    document.getElementById("txtDate").readOnly=true;
    document.getElementById("telf").readOnly=true;
  } catch (error) {
    console.error('Error:', error);
  }
}
async function seguir() {
    const boton = document.getElementById("botonSeguir"); // Obtén el botón
    boton.disabled = true; // Deshabilita el botón para evitar múltiples clics
    boton.textContent = "Procesando..."; // Cambia el texto temporalmente

    try {
        const response = await fetch('api/seguirUsuario', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': localStorage.token,
            },
            body: JSON.stringify(localStorage.mk),
            cache: 'no-store', // Evita usar caché
        });

        if (response.ok) {
            boton.textContent = "Siguiendo"; // Actualiza el texto si la solicitud fue exitosa
            boton.disabled = true; // Mantén el botón deshabilitado (ya no se puede seguir nuevamente)
        } else if (response.status === 409) { // Manejo de error por duplicado
            alert("Ya estás siguiendo a este usuario.");
            boton.textContent = "Siguiendo";
        } else {
            throw new Error("Error al seguir usuario");
        }
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al procesar tu solicitud.");
        boton.textContent = "Intentar de nuevo";
        boton.disabled = false; // Rehabilita el botón para intentarlo de nuevo
    }
}


