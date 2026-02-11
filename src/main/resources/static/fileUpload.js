document.getElementById('uploadForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const fileInput = document.getElementById('fileUpload');
    let message = document.getElementById("message");
    const file = fileInput.files[0];
    const fileName = file.name;
    console.log("File is ",fileName);

    uploadFile(file)
        .then(response => {
            if(!response){
                message.style.color = "red";
                message.textContent = "Failed to upload your file";
                console.log("Failed to upload ")
            }
            else{
                message.style.color = "green";
                message.textContent = "File uploaded successfully";
                console.log("Uploaded successfully")
            }
        })
        .catch(err => {
            console.log(err);
            throw err;
        });

});

function uploadFile(file){
    const formData = new FormData();
    formData.append("file",file);

    return fetch("http://localhost:8080/upload",{
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if(response.status === 409){
                throw new Error("Failed to upload your file");
            }
            else{
                return true;
            }
        })
        .catch(err => {
            console.log(err);
            throw err;
        });
}
