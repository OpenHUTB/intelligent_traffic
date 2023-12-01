const WebSocket = require('ws');

// Create a new WebSocket server on port 8080
const wss = new WebSocket.Server({ port: 8088 });

wss.on('connection', (ws) => {
    console.log('A new client connected.');

    // Handle incoming messages
    ws.on('message', (message) => {
        console.log('received: %s', message);
    });

    // Send a welcome message to the connected client
    ws.send('Welcome to the WebSocket server!');

});

console.log('WebSocket server started on ws://localhost:8088');