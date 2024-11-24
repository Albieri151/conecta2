document.getElementById('search-input').addEventListener('input', async function() {
  const query = this.value;

  if (query.length > 0) { // Solo buscar si hay texto
    try {
      const response = await fetch('api/buscador', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': localStorage.token,
        },
        body: JSON.stringify({ usuariosBuscados: query }),
      });
      const data = await response.json();
      displayResults(data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  } else {
    clearResults();
  }
});

function displayResults(results) {
    const resultsContainer = document.getElementById('search-results');
    resultsContainer.innerHTML = ''; // Limpiar resultados previos

    if (results.length > 0) {
        resultsContainer.style.display = 'block'; // Mostrar resultados
    } else {
        resultsContainer.style.display = 'none'; // Ocultar si no hay resultados
    }

    // Usar slice para limitar los resultados a 5
    results.slice(0, 5).forEach(item => {
        const div = document.createElement('div');
        div.textContent = item.usuario || 'Usuario no disponible'; // Asegurarse de que el campo no sea null

        // Estilo opcional para identificar los divs como clicables
        div.style.cursor = 'pointer';
        div.style.padding = '10px';
        div.style.border = '1px solid #ccc';
        div.style.margin = '5px 0';

        // Agregar el evento click al div
        div.addEventListener('click', () => {
            handleDivClick(item); // Pasar el objeto completo o solo el dato que necesites
        });

        resultsContainer.appendChild(div);
    });
}

// Función para manejar el clic en un div
function handleDivClick(item) {
    localStorage.userBuscado = item.usuario;
    window.location.href = "/usuarioBuscado.html";
}


function clearResults() {
    const resultsContainer = document.getElementById('search-results');
    resultsContainer.innerHTML = '';
    resultsContainer.style.display = 'none'; // Ocultar el contenedor cuando esté vacío
}
document.addEventListener('DOMContentLoaded', recargarPerfil);
function recargarPerfil(){
    document.getElementById("us").innerHTML = localStorage.user;
}
