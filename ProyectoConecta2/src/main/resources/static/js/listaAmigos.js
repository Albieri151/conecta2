// Call the dataTables jQuery plugin
$(document).ready(function() {
    asignarUser();
    cargarAmigos();
  $('#listadoAmigos').DataTable();
});

async function cargarAmigos() {

    // Realiza la solicitud con fetch
    const request = await fetch('api/seguidores', {
        method: 'POST',
        headers: getHeaders()
    });

    // Obtiene los datos en formato JSON
    const listaUsuarios = await request.json();

    let usuariosHTML='';
    for(let usuario of listaUsuarios){
        let usuarioHTML = `
        <tr>
            <td>${usuario.usuario}</td>
            <td>${usuario.nombre} ${usuario.apellido}</td>
            <td>${usuario.email}</td>
            <td>${usuario.edad}</td>
            <td>0${usuario.phone}</td>
            <td>
                <a href="#" onclick="eliminarSeguidor(${usuario.id})" class="btn btn-danger btn-circle btn-sm">
                    <i class="fas fa-trash"></i>
                </a>
            </td>
        </tr>`;

        usuariosHTML += usuarioHTML;
    }

    document.querySelector('#listadoAmigos tbody').outerHTML = usuariosHTML;
}

function getHeaders(){
    return {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': localStorage.token
            }
}

function asignarUser(){
return document.getElementById("us").innerHTML =localStorage.user;
}

async function eliminarSeguidor(id){
    if(!confirm('¿Desea dejar de seguir a este usuario?')){
        return;
    }

    const request = await fetch('api/eliminarSeguido/'+id, {
            method: 'DELETE',
            headers: getHeaders()
        });
    location.reload()
}