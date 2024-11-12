// Call the dataTables jQuery plugin
$(document).ready(function() {
    cargarAmigos();
  $('#listadoAmigos').DataTable();
});

async function cargarAmigos() {
    // Realiza la solicitud con fetch
    const request = await fetch('Amigos', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });

    // Obtiene los datos en formato JSON
    const listaUsuarios = await request.json();

    let usuariosHTML='';
    for(let usuario of listaUsuarios){
        let usuarioHTML = `<tr><td>${usuario.usuario}</td><td>${usuario.nombre} ${usuario.apellido}</td><td>${usuario.email}</td>
            <td>${usuario.edad}</td><td>${usuario.phone}</td>
            <td>
                <a href="#" class="btn btn-danger btn-circle btn-sm"><i class="fas fa-trash"></i></a>
            </td></tr>`;
        usuariosHTML += usuarioHTML;
    }

    document.querySelector('#listadoAmigos tbody').outerHTML = usuariosHTML;
}