document.getElementById('add_form').addEventListener('submit', function (e) {
    e.preventDefault();

    let inputSearch = document.getElementById("add_friend");

    addFriendApi(inputSearch)
});

function addFriendApi(inputSearch){
    return fetch('http://localhost:8080/addFriend',{
        method: 'POST',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify(inputSearch)
    })
        .then(response => {
            if(response.status === 409){
                throw new Error("User not found");
            } else {
                addThisUser(response);
            }
        })
        .then(result => result)
        .catch(err => {
            console.log(err);
            throw err;
        });
}