import { openWebSocket, closeWebSocket } from './websocket.js';

export let isClosed = false;

export function toggleConnection() {
    const toggleBtn = document.getElementById('toggleConnection');
    if (!isClosed) {
        toggleBtn.textContent = 'Open Connection';
        isClosed = true;
        closeWebSocket();
    } else {
        toggleBtn.textContent = 'Close Connection';
        isClosed = false;
        openWebSocket();
    }
}

document.getElementById('toggleConnection').addEventListener('click', toggleConnection);
