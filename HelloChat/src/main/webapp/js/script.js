/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
		var serviceLocation = "ws://localhost:8080/HelloChat/form/";
		var webSocket = new WebSocket(serviceLocation + "finance" + "/" + "chafiq");;
		
		
		/*function processOpen() {
			var room = document.getElementById('room').value;
			webSocket.onmessage = processMessage;
		}
		*/
		
		webSocket.onmessage = function processMessage(formMessage) {
			var json = JSON.parse(formMessage.data);
			
			if(json.message != "FormMessageUsers") document.getElementById('messages').value += json.sender + ': ' + json.message + '\n';
			else{
				if(json.users != null){
					users.value = "";
					var i = 0;
					while(i<json.users.length) users.value += json.users[i++] + '\n';
				}
			}
		}
		
		function send() {
			var message = document.getElementById('message');
            var room = document.getElementById('room');
			webSocket.send(JSON.stringify({'message' : message.value, 'received' : null, 'sender' : room.value}));
			message.value = "";
			room.disabled = 'true';
		}
		
		window.onbeforeunload = function() {
			webSocket.onclose = function() {
				webSocket.close();
			}
		};