document.getElementById("loginForm").addEventListener("submit",function(e)
{
    e.preventDefault();

    let email = document.getElementById("login_email");
    let password = document.getElementById("login_pass");
    let message = document.getElementById("message");

    let userLoginObject = {
        loginEmail : email.value,
        loginPassword : password.value
    }

    loginApi(userLoginObject)
        .then(result => {
                message.style.color = "green";
                message.textContent = "Login successfull";
                window.location.href = '/upload';
        })
        .catch(error => {
            message.style.color = "red";
            message.textContent = "Login failed";
        });

});

function loginApi(userLoginObject){
    return fetch('http://localhost:8080/login',{
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userLoginObject),
        credentials: "include"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Login failed");
            }
            else{
                return response.json();
            }
        })
        .then(result => {
            return result;
        })
        .catch(err => {
            console.log(err);
            throw err;
        });
}