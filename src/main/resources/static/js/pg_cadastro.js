const formCadastro = document.getElementById('form-cadastro');

formCadastro.addEventListener('submit', async(e) => {
    e.preventDefault();

    const dados = Object.fromEntries(new FormData(formCadastro).entries());

    try {
        const resp = await fetch("http://localhost:8080/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dados)
        });

        if (resp.ok) {
            alert("Usuário cadastrado com sucesso!");
            // window.location.href = "/";

            formCadastro.reset();
        } else {
            const erro = await resp.text();
            alert("Erro ao cadastrar: " + erro);
        }
    } catch (err) {
        alert("Erro de conexão com o servidor: " + err.message);
    }
})