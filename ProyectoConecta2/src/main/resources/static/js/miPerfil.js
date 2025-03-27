// Función para obtener los datos del perfil
async function fetchProfileData() {
  try {
    const response = await fetch('api/miPerfil', {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token,
      },
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
    actualizar(data)
    document.getElementById("us").innerHTML = data.usuario;
    localStorage.user = data.usuario;
  } catch (error) {
    console.error('Error:', error);
  }
}
function actualizar(data){
    document.getElementById('seguidos').textContent = data.siguiendo;
    document.getElementById('seguidores').textContent = data.seguidores;
    document.getElementById('publicaciones').textContent = data.publicaciones;}

// Función para actualizar los datos del perfil
async function updateProfileData() {
  try {
    const profileData = {
      nombre: document.querySelector('input[placeholder="Nombre"]').value,
      apellido: document.querySelector('input[placeholder="Apellido"]').value,
      genero: document.querySelector('select').value,
      email: document.querySelector('input[placeholder="Email"]').value,
      usuario: document.querySelector('input[placeholder="Username"]').value,
      password: document.querySelector('input[placeholder="Password"]').value,
      preguntaDeSeguridad: document.querySelector('input[placeholder="Pregunta de seguridad"]').value,
      respuestaDeSeguridad: document.querySelector('input[placeholder="Respuesta de seguridad"]').value,
      fechaDeNacimiento: document.querySelector('input[placeholder="Fecha de nacimiento"]').value,
      phone: document.querySelector('input[placeholder="Teléfono"]').value
    };

    const response = await fetch('api/profile', {
      method: 'PUT',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token,
      },
      body: JSON.stringify(profileData)
    });

    if (!response.ok) {
      throw new Error('Error al actualizar el perfil verifique sus datos');
    }
    alert("Usuario actualizado exitosamente")
    location.reload();
    document.getElementById("us").innerHTML = profileData.usuario;
  } catch (error) {
    console.error('Error:', error);
  }
}

// Llamar a fetchProfileData al cargar la página
document.addEventListener('DOMContentLoaded', fetchProfileData);

// Añadir evento al botón de actualización
document.querySelector('button[type="submit"]').addEventListener('click', (e) => {
  e.preventDefault(); // Prevenir el comportamiento por defecto del botón
  updateProfileData();
});

