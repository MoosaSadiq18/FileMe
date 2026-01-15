let ws,currentUser

function connect(){
    ws = new WebSocket("ws://localhost:8080/hello");
    ws.onmessage = function (e){
        console.log(e);
        displayMessage(e.data);
    }

    document.getElementById("connectButton").disabled = true
    document.getElementById("connectButton").value = "Connected"
    document.getElementById("name").disabled = true;
    currentUser = document.getElementById("name").value;
    console.log(currentUser);
}



function displayMessage(data){
    let messages = document.getElementById("messages");
    let messageData = JSON.parse(data);
    let newMessage = document.createElement("div");
    newMessage.className = "incoming-message";
    newMessage.innerHTML = messageData.name + " : " + messageData.message;
    messages.appendChild(newMessage);
}

function sendMessage(){
    if(ws == undefined){
        return;
    }

    let messages = document.getElementById("messages");
    let messageText = document.getElementById("message").value;
    document.getElementById("message").value = "";
    let name = document.getElementById("name").value;
    let messageObject = {
        name: name,
        message: messageText,
    };

    let newMessage = document.createElement("div");
    newMessage.className = "outgoing-message";
    newMessage.innerHTML = messageText + " : " + currentUser;
    messages.appendChild(newMessage);

    ws.send(JSON.stringify(messageObject));
}