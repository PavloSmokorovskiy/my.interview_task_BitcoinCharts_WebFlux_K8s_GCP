import {openWebSocket, closeWebSocket} from './websocket.js';

const toggleBtn = document.getElementById('toggleConnection');

export const connectionState = {
    isOpen: false
}

export function toggleConnection() {
    if (connectionState.isOpen) {
        closeConnection();
    } else {
        openConnection();
    }
}

function closeConnection() {
    toggleBtn.textContent = 'Open Connection';
    connectionState.isOpen = false;
    closeWebSocket();
}

function openConnection() {
    toggleBtn.textContent = 'Close Connection';
    connectionState.isOpen = true;
    openWebSocket();
}

toggleBtn.addEventListener('click', toggleConnection);
