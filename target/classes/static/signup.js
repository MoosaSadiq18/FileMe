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

    if(username.value===""||password.value===""||confirmPassword.value===""||email.value==""){
        return;
    }


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
        return 'Strong';
    }
    else{
        return "Medium";
    }
}

function isEqual(password,confirmPassword){
    return password === confirmPassword;
}

    function initOtp(email) {    //new 1
        const otpInputs = document.querySelectorAll(".otp");
        otpInputs.forEach((input, index) => {
            input.addEventListener("input", () => {
                if (!/^\d$/.test(input.value)) {
                    input.value = "";
                    return;
                }
                if (index < otpInputs.length - 1) {
                    otpInputs[index + 1].focus();
                }
                const fullOtp = getCompleteOtp();
                if (fullOtp.length === otpInputs.length) {
                    submitOtp(email,fullOtp);
                    console.log(fullOtp);
                }
            });
            input.addEventListener("keydown", (e) => {
                if (e.key === "Backspace" && !input.value && index > 0) {
                    otpInputs[index - 1].focus();
                }
            });
        });
        otpInputs[0].focus();
    }

    function getCompleteOtp() {  //new 2
        let otp = "";
        document.querySelectorAll(".otp").forEach(input => {
            otp += input.value.trim();
        });
        return otp;
    }

    let otp;

    function submitOtp(email,fullOtp){
        const userOtpConfirmation = {
            email: email,
            otp: fullOtp
        };
        console.log("Sending otp to backend ", userOtpConfirmation);

        otpConfirmationApi(userOtpConfirmation)
            .then(confirmed => {
                message.style.color = "green";
                message.textContent = "SignUp successfull...";
                window.location.href = "/login";
            })
            .catch(error => {
                message.style.color = "red";
                message.textContent = "SignUp failed...";
                document.querySelectorAll(".otp").forEach(input => input.value = "");
                document.querySelector(".otp").focus();
            });
    }

function signUpApi(userSignUpObject){
    return fetch('http://localhost:8080/signup',{
        method: 'POST',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify(userSignUpObject),
        credentials: "include"
    })
        .then(response => {
            if(response.status === 409){
                throw new Error("Username already exists");
            }
            else if(response.status === 406){
                throw new Error("Email already exists");
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

function otpConfirmationApi(userOtpConfirmation){
    return fetch('http://localhost:8080/signup/otp',{
        method: 'POST',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify(userOtpConfirmation)
    })
        .then(response => {
            if(response.status === 409){
                throw new Error("Otp not verified");
            }
            else if (response.status === 201) {
                return true;
            } else {
                throw new Error("SignUp failed");
            }
        })
        .then(result => result)
        .catch(err => {
            console.log(err);
            throw err;
        });
}

    signUpApi(userSignUpObject)
        .then(result => {
            message.style.color = "blue";
            message.textContent = "Please enter the OTP sent to your email";
            document.getElementById("signup-section").style.display = "none";
            document.getElementById("otp-section").style.display = "block";
            initOtp(email.value);
        })
        .catch(error => {
            message.style.color = "red";
            message.textContent = error.message;
        });

});
