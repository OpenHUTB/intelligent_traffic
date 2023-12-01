import React, { useEffect, useState, useContext } from 'react';

export default function WebsoketManager(children) {
    const [data, setData] = useState(null);

    useEffect(() => {
        const socket = new WebSocket('ws://localhost:8088');

        socket.onopen = () => {
            console.log('Connected to the server');
        };

        socket.onmessage = (event) => {
            const receivedData = JSON.parse(event.data);
            setData(receivedData);
        };

        socket.onclose = () => {
            console.log('Disconnected from the server');
        };

        return () => {
            socket.close();
        };
    }, []);
    return (
        <>
            {React.Children.map(children, child => {
                // Pass WebSocket data as props to children
                return React.cloneElement(child, { websocketData: data });
            })}
        </>
    );
}




