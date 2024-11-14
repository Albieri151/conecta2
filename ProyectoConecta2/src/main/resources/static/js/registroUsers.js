$(document).ready(function() {

});


async function registro() {

    let datos={};
    datos.nombre = document.getElementById("txtNombre").value;
    datos.apellido = document.getElementById("txtApellido").value;
    datos.usuario = document.getElementById("txtUser").value;
    datos.genero = document.getElementById("txtGenero").value;
    datos.email = document.getElementById("txtEmail").value;
    datos.password = document.getElementById("txtPassword").value;
    datos.preguntaDeSeguridad = document.getElementById("txtPreguntaSeguridad").value;
    datos.respuestaDeSeguridad = document.getElementById("idRespuestaSeguridad").value;
    datos.phone = parseInt(document.getElementById("idNumeroTelefono").value);
    datos.fechaDeNacimiento = document.getElementById("txtFechaNacimiento").value;

    let camposVacios = [];
    for (let clave in datos){
        if (datos[clave] === ''){
            camposVacios.push(clave);
        }
    }
    if (camposVacios.length > 0){
        return alert('Los siguientes campos están vacíos porfavor llenar: ' + camposVacios.join(', '));
    }


    // Realiza la solicitud con fetch
    const request = await fetch('registrar', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    });

    // Obtiene los datos en formato JSON
    const registro = await request.json();

}