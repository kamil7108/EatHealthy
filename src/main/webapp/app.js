(function() {
    const API_URL = 'http://localhost:8080/authentication';
    const TODO_API_URL = `${API_URL}/todos`;

    const todoText = document.getElementById('todoText');

    fetch(TODO_API_URL)
      .then(processOkResponse)
      .then(todos => todos.forEach(createNewTodo));

    document.getElementById('addTodo').addEventListener('click', (event) => {
      event.preventDefault();
      fetch(TODO_API_URL, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ text: todoText.value })
      })
        .then(processOkResponse)
        .then(createNewTodo)
        .then(() => todoText.value = '')
        .catch(console.warn);
    });



    function createNewTodo(todo) {
      const label = document.createElement('label');
      label.classList.add('pure-checkbox');
      const checkbox = document.createElement('input');
      checkbox.type = 'checkbox';
      checkbox.checked = todo.done;
      checkbox.addEventListener('click',
        (event) => {
          event.preventDefault();
          fetch(`${TODO_API_URL}/${todo.id}`, { method: 'PUT' })
            .then(processOkResponse)
            .then(updatedTodo => checkbox.checked = !!updatedTodo.done)
            .catch(console.warn);
        });
      label.appendChild(checkbox);
      label.appendChild(document.createTextNode(` ${todo.text}`));
      document.getElementById('allTodos').appendChild(label);
    }

    initWelcomeForm();

    function initWelcomeForm() {
      const CODE_TO_EMOJI = {
        'pl': '🇵🇱',
        'en': '🇺🇸',
        'de': '🇩🇪'
      };
      fetch(`${API_URL}/langs`)
        .then(processOkResponse)
        .then(langArr => {
          document.getElementById('langs').innerHTML = langArr.map(lang => `
              <label class="pure-radio">
                <input type="radio" name="lang" value="${lang.id}">
                ${CODE_TO_EMOJI[lang.code]}
              </label>
          `).join('\n');
          initWelcomeFormClick();
        });
    }

    function initWelcomeFormClick() {
      const welcomeForm = document.getElementById('welcomeForm');

      document.getElementById('btn').addEventListener('click', (event) => {
        event.preventDefault();
        const formObj = {
          name: welcomeForm.elements.name.value,
          lang: welcomeForm.elements.lang.value
        };
        fetch(`${API_URL}?${new URLSearchParams(formObj)}`)
          .then(response => response.text())
          .then((text) => {
            document.getElementById('welcome').innerHTML = `
                <h1>${text}</h1>
            `;
            welcomeForm.remove();
            document.getElementById('todoForm').style.display = 'block';
          });
      });
    }

    function processOkResponse(response = {}) {
      if (response.ok) {
        return response.json();
      }
      throw new Error(`Status not 200 (${response.status})`);
    }
  })();