var socket;
var connected = false;
var isScroll = false;
var host = 'ws://localhost:8080/chat';
var connectAgain = true;

var Packets = {
	Incoming: {
		ChatMessage: 1,
		UpdateUsersCount: 2,
		Authentification: 3,
		Kicked: 4
	},
	Outgoing: {
		ChatEvent: 1,
		LoginEvent: 2
	}
};

function PacketListener(id, callback) {
	var id = id;
	var callback = callback;

	this.getId = function() {
		return id;
	}

	this.execute = function(payload) {
		return callback(payload);
	}
}

function PacketListenerHandler() {
	var listeners = [];

	this.add = function(listener) {
		listeners.push(listener);
	}

	this.call = function(id, payload) {
		for(var i = 0; i < listeners.length; i++) {
			var listener = listeners[i];
			if(listener.getId() == id) {
				listener.execute(payload);
			}
		}
	}
}

var packetListenerHandler = new PacketListenerHandler();

packetListenerHandler.add(new PacketListener(Packets.Incoming.ChatMessage, function(data) {
	//notify('Notification', 'New message!');
	addMessage(data['username'], data['message'], data['time'], data['prefix'] != null ? data['prefix'] : null, data['prefixName'] != null ? data['prefixName'] : null, data['prefixColor'] != null ? data['prefixColor'] : null);
}));

packetListenerHandler.add(new PacketListener(Packets.Incoming.UpdateUsersCount, function(data) {
	jQuery('#users-online-count > .count').html(data['count']);
}));

packetListenerHandler.add(new PacketListener(Packets.Incoming.Authentification, function(data) {
	if(data['authentificated'] == true) {
		jQuery('#login').hide();
							
		jQuery('#login-username').val('');
		jQuery('#login-password').val('');
		jQuery('#login-msg').html('');
							
		jQuery('#main').show();
	} else {
		jQuery('#login-msg').html('<div class="alert alert-danger mb10">Username or password are wrong.</div>');
	}
}));

packetListenerHandler.add(new PacketListener(Packets.Incoming.Kicked, function(data) {
	jQuery('#login').hide();
	jQuery('#main').show();
	connectAgain = data['status'];
}));

function addMessage(username, message, time, prefix, prefixName, prefixColor) {
	if(time == null) {
		time = '';
	} else {
		time = '<span class="chat-time">' + time + '</span>';
	}
	
	if(prefix == null && prefixName == null) {
		jQuery('#messages').prepend('<li><b>' + escapeHtml(username) + '</b>' + time + escapeHtml(message) + '</li>');
	} else if(prefix != null && prefixName == null) {
		jQuery('#messages').prepend('<li><b class="prefix-' + prefix + '">' + escapeHtml(username) + '</b>' + time + escapeHtml(message) + '</li>');
	} else {
		if(prefixColor != null) {
			prefixColor = ' style="color: #' + prefixColor + '"';
		} else {
			prefixColor = '';
		}
		
		jQuery('#messages').prepend('<li><b><span class="prefix-' + prefix + '"' + prefixColor + '>[' + prefixName + ']</span> ' + escapeHtml(username) + '</b>' + time + escapeHtml(message) + '</li>');
	}
	
	if(!isScroll) {
		isScroll = true;
		
		jQuery('#messages').animate({
			scrollTop: 0
		}, 500, function() {
			isScroll = false;
		});
	}
}

document.addEventListener('DOMContentLoaded', function() {
	if(!('Notification' in window)) {
		return;
	}

	if(Notification.permission !== 'granted') {
		Notification.requestPermission();
	}
});

function notify(title, message) {
	if(!('Notification' in window)) {
		return;
	}
	
	var notification = new Notification(title, {
		body: message
	});
}

function escapeHtml(text) {
	var map = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		'"': '&quot;',
		"'": '&#039;'
	};

	return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}

jQuery(document).ready(function() {
	jQuery('#chat-form').submit(function(event) {
		var message = jQuery(this).serializeArray()[0].value;
					
		if(connected && message.length > 0) {
			var data = {
				'header': Packets.Outgoing.ChatEvent
			};
							
			data['data'] = {
				'message': message
			};
							
			socket.send(JSON.stringify(data));
			jQuery('#text-message').val('');
							
			console.log('send: ' + JSON.stringify(data));
		}
					
		event.preventDefault();
	});
	
	jQuery('#login-form').submit(function(event) {
		var login_username = jQuery(this).serializeArray()[0].value;
		var login_password = jQuery(this).serializeArray()[1].value;
		
		if(connected) {
			if(login_username.length > 0 && login_password.length > 0) {
				var data = {
					'header': Packets.Outgoing.LoginEvent
				};
				
				data['data'] = {
					'username': login_username,
					'password': login_password
				};
								
				socket.send(JSON.stringify(data));
				
				console.log('send: ' + JSON.stringify(data));
			} else {
				jQuery('#login-msg').html('<div class="alert alert-danger mb10">Enter your username and password.</div>');
			}
		}
					
		event.preventDefault();
	});
	
	connect();
});

function tryAgain() {
	jQuery('.after-connection').hide();
	jQuery('.before-connection').show();
		
	if(connected) {
		jQuery('#login').show();
	}
	
	jQuery('#main').hide();
	jQuery('#main').hide();
	jQuery('#messages').empty();
	
	if(socket != null) {
		socket.close();
	}
	
	socket = null;
				
	setTimeout(function() {
		console.log('Connection failed, try again...');
	
		connect();
	}, 3000);
}

function connect() {
	if(!connected) {
		jQuery('.after-connection').hide();
		jQuery('.before-connection').show();
	
		isScroll = false;
		
		if('WebSocket' in window) {
			socket = new WebSocket(host);
					   
			socket.onopen = function() {
				connected = true;
				
				jQuery('.after-connection').show();
				jQuery('.before-connection').hide();
			};
						
			socket.onmessage = function(msg) {
				console.log('received' + msg.data);
							
				try {
					var obj = JSON.parse(msg.data);
					var header = parseInt(obj['header']);
					var data = obj['data'];
					
					return packetListenerHandler.call(header, data);
				} catch(ex) {
					console.log(ex);
				}
			};
						
			socket.onerror = function(msg) {
				connected = false;
				
				if(connectAgain) {
					tryAgain();
				}
			};
						
			socket.onclose = function() {
				connected = false;
				
				if(connectAgain) {
					tryAgain();
				}
			};
		} else {
			alert('Your browser does not support HTML5 WebSockets.');
		}
	}
}