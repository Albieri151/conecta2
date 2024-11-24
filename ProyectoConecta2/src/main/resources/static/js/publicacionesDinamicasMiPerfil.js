document.addEventListener("DOMContentLoaded", async () => {
    const publicacionesContainer = document.getElementById("publicaciones-container");

    // Fetch para obtener las publicaciones
    const response = await fetch('/api/publicacionesNuevas', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
    });
    const publicaciones = await response.json();

    // Generar dinámicamente los divs de publicaciones
    publicaciones.forEach(pub => {
        pub.liked = false; // Inicialmente no tiene like
        pub.likesCount = pub.cantidadMeGusta || 0; // Total de likes
        pub.commentsCount = pub.cantidadComentarios || 0; // Total de comentarios
        console.log("Estoy testeando los comentarios bro "+pub.comentarios)
        const publicacionDiv = document.createElement("div");
        publicacionDiv.className = "publicacion";
        publicacionDiv.innerHTML = `
            <div class="info-publicacion">
                <div class="fecha">${new Date(pub.fechaPublicacion).toLocaleString()}</div>
                <div class="autor">${pub.creadorPublicacion}</div>
            </div>
            <div class="contenido">${pub.contenidoPublicacion}</div>
            <div class="acciones">
                <div class="stats">
                    <span class="likes-count">${pub.likesCount} Likes</span>
                    <span class="comments-count">${pub.commentsCount} Comentarios</span>
                </div>
                <div class="botones">
                    <button class="like-btn">${pub.liked ? 'No me gusta' : 'Me gusta'}</button>
                    <button class="comentar-btn">Comentar</button>
                </div>
            </div>


            <div class="campo-comentario" style="display: none;">
                <textarea id="campoComentarios${pub.id}" placeholder="Escribe tu comentario..."></textarea>
                <button class="enviar-comentario">Enviar</button>
            </div>
        `;

        // Botón "Me gusta"
        const likeBtn = publicacionDiv.querySelector(".like-btn");
        const likesCountSpan = publicacionDiv.querySelector(".likes-count");

        likeBtn.addEventListener("click", async () => {
            const action = pub.liked ? 'DELETE' : 'POST';

            const response = await fetch('/api/nuevoLike', {
                method: action,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token,
                    "pub_id": pub.id
                },
            });

            if (response.ok) {
                const updatedData = await response.text();
                console.log(updatedData);
                pub.liked = !pub.liked; // Alternar el estado

                const responseLikes = await fetch(`/api/nuevoLike?pub_id=${pub.id}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.token,
                    },
                });

                const likesData = await responseLikes.json();
                pub.likesCount = likesData.totalLikes; // Actualizar el total desde la respuesta

                likeBtn.textContent = pub.liked ? 'No me gusta' : 'Me gusta';
                likesCountSpan.textContent = `${pub.likesCount} Likes`;
            } else {
                console.error('Error actualizando likes');
            }
        });

        // Mostrar modal al hacer clic en los likes
        likesCountSpan.addEventListener("click", async () => {
            try {
                const response = await fetch(`/api/nuevoLike?pub_id=${pub.id}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.token,
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    console.log('Usuarios que dieron like:', data);
                    pub.likesCount = data.totalLikes;
                    if (Array.isArray(data.usernames)) {
                        showLikesModal(data.usernames);
                    } else {
                        console.error('El formato de los datos no es un array:', data);
                    }
                } else {
                    console.error('Error al obtener la lista de usuarios:', response.status);
                }
            } catch (error) {
                console.error('Error en la petición:', error);
            }
        });

        // Mostrar modal de likes
        function showLikesModal(users) {
            const previouslyFocusedElement = document.activeElement;

            const modal = document.createElement('div');
            modal.className = 'custom-modal';
            modal.setAttribute('role', 'dialog');
            modal.setAttribute('aria-modal', 'true');
            modal.setAttribute('aria-hidden', 'false');
            modal.style.position = 'fixed';
            modal.style.top = '0';
            modal.style.left = '0';
            modal.style.width = '100%';
            modal.style.height = '100%';
            modal.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
            modal.style.display = 'flex';
            modal.style.justifyContent = 'center';
            modal.style.alignItems = 'center';

            // Desactivar fondo
            setModalAccessibility(modal, true);

            const modalContent = document.createElement('div');
            modalContent.style.backgroundColor = 'white';
            modalContent.style.padding = '20px';
            modalContent.style.borderRadius = '8px';
            modalContent.style.width = '300px';
            modalContent.style.textAlign = 'center';
            modalContent.style.position = 'relative';

            const title = document.createElement('h2');
            title.textContent = 'Usuarios que dieron me gusta';
            modalContent.appendChild(title);

            const userList = document.createElement('ul');
            userList.style.listStyle = 'none';
            userList.style.padding = '0';

            users.forEach(user => {
                const listItem = document.createElement('li');
                listItem.textContent = user;
                listItem.style.margin = '12px 0';
                userList.appendChild(listItem);
            });

            modalContent.appendChild(userList);

            const closeButton = document.createElement('span');
            closeButton.textContent = '×';
            closeButton.style.position = 'absolute';
            closeButton.style.top = '10px';
            closeButton.style.right = '10px';
            closeButton.style.fontSize = '20px';
            closeButton.style.cursor = 'pointer';
            closeButton.addEventListener('click', () => {
                closeModal(modal, previouslyFocusedElement);
            });

            modalContent.appendChild(closeButton);
            modal.appendChild(modalContent);

            modal.addEventListener('click', event => {
                if (event.target === modal) {
                    closeModal(modal, previouslyFocusedElement);
                }
            });

            document.body.appendChild(modal);
            modalContent.focus();
        }

        function closeModal(modal, previouslyFocusedElement) {
            document.body.removeChild(modal);
            setModalAccessibility(modal, false);

            if (previouslyFocusedElement) {
                previouslyFocusedElement.focus();
            }
        }

        function setModalAccessibility(modal, isVisible) {
            modal.setAttribute('aria-hidden', isVisible ? 'false' : 'true');
            document.body.querySelectorAll('*:not(.modal):not(script)').forEach(el => {
                if (isVisible) {
                    el.setAttribute('inert', '');
                } else {
                    el.removeAttribute('inert');
                }
            });
        }


        // Mostrar modal al hacer clic en los comentarios
        const commentsCountSpan = publicacionDiv.querySelector(".comments-count");
        commentsCountSpan.addEventListener("click", () => {
            console.log(pub.comentarios)
            showCommentsModal(pub.comentarios);
        });

        function showCommentsModal(publicacion) {
                const modal = document.createElement("div");
                modal.className = "modal";
                modal.style.position = "fixed";
                modal.style.top = "0";
                modal.style.left = "0";
                modal.style.width = "100%";
                modal.style.height = "100%";
                modal.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
                modal.style.display = "flex";
                modal.style.justifyContent = "center";
                modal.style.alignItems = "center";

                const modalContent = document.createElement("div");
                modalContent.style.backgroundColor = "white";
                modalContent.style.padding = "20px";
                modalContent.style.borderRadius = "8px";
                modalContent.style.width = "400px";
                modalContent.style.maxHeight = "80%";
                modalContent.style.overflowY = "auto";
                modalContent.style.position = "relative";

                const closeButton = document.createElement("span");
                closeButton.textContent = "×";
                closeButton.style.position = "absolute";
                closeButton.style.top = "10px";
                closeButton.style.right = "10px";
                closeButton.style.fontSize = "20px";
                closeButton.style.cursor = "pointer";
                closeButton.addEventListener("click", () => {
                    document.body.removeChild(modal);
                });

                const title = document.createElement("h2");
                title.textContent = "Comentarios:";

                const commentsContainer = document.createElement("div");
                if (publicacion.length > 0) {
                    publicacion.forEach(com => {
                        const commentDiv = document.createElement("div");
                        commentDiv.style.marginBottom = "15px";
                        commentDiv.style.borderBottom = "1px solid #ddd";
                        commentDiv.style.padding = "10px";

                        const commentHeader = document.createElement("div");
                        commentHeader.style.display = "flex";
                        commentHeader.style.justifyContent = "space-between";
                        commentHeader.style.alignItems = "center";

                        const authorName = document.createElement("strong");
                        authorName.textContent = com.nombreUser;

                        const deleteButton = document.createElement("button");
                        deleteButton.textContent = "Eliminar";
                        deleteButton.style.color = "red";
                        deleteButton.style.border = "none";
                        deleteButton.style.background = "none";
                        deleteButton.style.cursor = "pointer";

                        let cons = com.com_id;
                        deleteButton.addEventListener("click", async () => {
                            console.log("Este es el id del comentario: "+cons)
                            // Hacer la petición para eliminar el comentario en el servidor
                            try {
                                const response = await fetch("api/nuevoComentario", {
                                    method: 'DELETE',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Authorization': localStorage.token // Si necesitas autenticarte con un token
                                    },
                                    body: JSON.stringify(cons)
                                });

                                if (response.ok) {

                                    alert("Comentario eliminado con éxito.");
                                    location.reload()
                                } else {
                                    // Si la respuesta no es exitosa, mostrar un error
                                    const errorData = await response.json();
                                    console.error("Error al eliminar comentario:", errorData);
                                    alert("Hubo un error al eliminar el comentario.");
                                }
                            } catch (error) {
                                console.error("Error al realizar la petición:", error);
                                alert("Error de conexión. No se pudo eliminar el comentario.");
                            }
                        });


                        commentHeader.appendChild(authorName);
                        commentHeader.appendChild(deleteButton);

                        const commentContent = document.createElement("p");
                        commentContent.textContent = com.contenido;

                        commentDiv.appendChild(commentHeader);
                        commentDiv.appendChild(commentContent);
                        commentsContainer.appendChild(commentDiv);
                    });
                } else {
                    commentsContainer.textContent = "No hay comentarios para esta publicación.";
                }

                modalContent.appendChild(closeButton);
                modalContent.appendChild(title);
                modalContent.appendChild(commentsContainer);
                modal.appendChild(modalContent);

                document.body.appendChild(modal);
        }

        // Botón "Comentar"
        const comentarBtn = publicacionDiv.querySelector(".comentar-btn");
        const campoComentario = publicacionDiv.querySelector(".campo-comentario");

        comentarBtn.addEventListener("click", () => {
            campoComentario.style.display = campoComentario.style.display === "flex" ? "none" : "flex";
        });

        // Enviar comentario
        const enviarComentarioBtn = publicacionDiv.querySelector(".enviar-comentario");
        enviarComentarioBtn.addEventListener("click", async () => {
            const textArea = campoComentario.querySelector("textarea");
            const contenidoComentario = textArea.value;
            if (contenidoComentario.trim() === "") {
                alert("El comentario no puede estar vacío.");
                return;
            }
            const response = await fetch('/api/nuevoComentario', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.token
                },
                body: JSON.stringify({ pub_id: pub.id, contenido: contenidoComentario })
            });

            if (response.ok) {
                pub.commentsCount++;
                commentsCountSpan.textContent = `${pub.commentsCount} Comentarios`;
                alert("Comentario enviado.");

                textArea.value = ""; // Limpia el campo de texto
                campoComentario.style.display = "none"; // Oculta el campo de comentario
                window.location.href = "/blank.html";
            } else {
                alert("Hubo un error al enviar el comentario.");
            }
        });

        publicacionesContainer.appendChild(publicacionDiv);
    });

});
