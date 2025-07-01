let socket;

export const initWebSocket = (onMessage) => {
    socket = new WebSocket("ws://localhost:8088");

    socket.onmessage = (event) => {

        onMessage(event);
    };

    socket.onerror = (error) => {
        console.error("WebSocket error:", error);
    };

    socket.onclose = () => {
        console.log("WebSocket connection closed");
    };
};

export const closeWebSocket = () => {
    if (socket) {
        socket.close();
    }
};