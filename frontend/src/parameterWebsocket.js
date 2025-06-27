import store from './store';
import { updateParameters } from 'stores/parameterSlice';
const parameterWebsocket = new WebSocket('ws://localhost:80/simulation/websocket/');
parameterWebsocket.onopen = () => {
    console.log('parameterWebSocket connection opened');
};
parameterWebsocket.onmessage = (event) => {
    //dealing with the received data;
    console.log('parameterWebSocket message received:', event.data);
    const updateParameters = JSON.parse(event.data);
    store.dispatch(updateParameters(updateParameters));
}
parameterWebsocket.onerror = (error) => {
    console.error('parameterWebSocket error:', error);
};
parameterWebsocket.onclose = () => {
    console.log('parameterWebSocket connection closed');
};

export default parameterWebsocket;