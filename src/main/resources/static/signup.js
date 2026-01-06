document.getElementById("signupForm").addEventListener("submit",function(e)
{
    e.preventDefault();

    let username = document.getElementById("username");
    let password = document.getElementById("password");
    let confirmPassword = document.getElementById("confirmPassword");
    let email = document.getElementById("email");
    let passwordError = document.getElementById("passwordError");
    let confirmPasswordError = document.getElementById("confirmPasswordError");
    let message = document.getElementById("message");

    let userSignUpObject = {
        username : username.value,
        password : password.value,
        email : email.value
    }

    if(!validatePassword(password.value)){
        passwordError.textContent = "Password is not valid";
        return;
    }else{
        passwordError.textContent = "";
    }

    if(!isEqual(password.value,confirmPassword.value)){
        confirmPasswordError.textContent = "Both passwords are not equal";
        return;
    }else{
        confirmPasswordError.textContent = "";
    }

    signUpApi(userSignUpObject)
        .then(result => {
            message.style.color = "green";
            message.textContent = "SignUp successfull";
            window.location.href = 'http://localhost:8080/login';
        })
        .then(error => {
            message.style.color = "red";
            message.textContent = error.message;
        });
});

document.getElementById("password").addEventListener("focus",function(){
    document.getElementById("passwordHint").style.display = "block";
})

document.getElementById("password").addEventListener("blur",function(){
    document.getElementById("passwordHint").style.display = "none";
})

document.getElementById("password").addEventListener("input",function(){
    let password = this.value;
    let passwordStrength = document.getElementById("passwordStrength");
    let strength = getPasswordStrength(password);

    passwordStrength.innerHTML = '';
    if (strength) {
        let strengthBar = document.createElement('div');
        strengthBar.className = strength;
        passwordStrength.appendChild(strengthBar);
    }
})

function validatePassword(password){
    let regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
}

function getPasswordStrength(password){
    if(password.length < 8){
        return "Weak";
    }
    if (password.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/)) {
        return "Strong";
    }
    else{
        return "Medium";
    }
}

function isEqual(password,confirmPassword){
    return password === confirmPassword;
}

function signUpApi(userSignUpObject){
    return fetch('http://localhost:8080/signup',{
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userSignUpObject),
        credentials: "include"
    })
        .then(response => {
            if(response.status === 409){
                throw new Error("Username already exists");
            }
            else if (!response.ok) {
                throw new Error("SignUp failed");
            } else {
                return response.json();
            }
        })
        .then(result => result)
        .catch(err => {
            console.log(err);
            throw err;
        });
}