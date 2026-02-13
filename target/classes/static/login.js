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
            if(result === 409){
                message.style.color = "red";
                message.textContent = "Someone is already logged in";
            }
            else {
                message.style.color = "green";
                message.textContent = "Login successfull";
                window.location.href = '/upload';
            }
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
    })
        .then(response => {
            if (response.status === 409) {
                return 409;
            }
            if (!response.ok) {
                throw new Error("Failed");
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