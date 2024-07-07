import {updateDataDisplay} from './data-display.js';
import {connectionState} from './toggle-сonnection.js';

let ws;
let latestPrice;
let latestTime = new Date();

export function openWebSocket() {
    ws = new WebSocket('ws://localhost:8080/crypto-feed');
    ws.onopen = handleWebSocketOpen;
    ws.onmessage = handleWebSocketMessage;
    ws.onerror = handleWebSocketError;
    ws.onclose = handleWebSocketClose;
}

export function closeWebSocket() {
    if (ws) {
        ws.close();
    }
}

const logAndDisplayMessage = (message, error) => {
    console.log(message, error ? error : '');
    updateDataDisplay({t: new Date(), message: `${message} ${error ? error.message : ''}`});
};

const handleWebSocketOpen = () => {
    connectionState.isOpen = true;
    logAndDisplayMessage('WebSocket connection established');
};

const handleWebSocketClose = () => {
    connectionState.isOpen = false;
    logAndDisplayMessage('WebSocket connection closed');
};

const handleWebSocketMessage = (event) => {
    console.log('Received message:', event.data);
    if (connectionState.isOpen) processWebSocketMessage(event.data);
};

const handleWebSocketError = (error) => {
    logAndDisplayMessage('WebSocket error:', error);
};

const processWebSocketMessage = (data) => {
    try {
        const messages = JSON.parse(data);
        if (!Array.isArray(messages)) {
            console.error('Expected an array of messages, received:', messages);
            return;
        }
        messages.forEach(updateChartDataAndDisplay);
    } catch (error) {
        console.error('Error parsing data:', error);
    }
};

const updateChartDataAndDisplay = (message) => {
    updateChartData(message);
    updateDataDisplay(message);
};

const updateChartData = (data) => {
    latestPrice = data.p;
    latestTime = new Date(data.t);
};

const updateChartEverySecond = () => {
    setInterval(() => {
        if (latestPrice !== null && connectionState.isOpen) {
            const now = new Date();
            addDataToChart(now, latestPrice);
            removeOldDataFromChart();
            cryptoChart.update({preservation: true});
        }
    }, 1000);
};

const addDataToChart = (time, price) => {
    cryptoChart.data.datasets[0].data.push({x: time, y: price});
};

const removeOldDataFromChart = () => {
    const cutoffTime = Date.now() - 14000;
    cryptoChart.data.datasets[0].data = cryptoChart.data.datasets[0].data
        .filter(point => point.x >= cutoffTime);
};

openWebSocket();
updateChartEverySecond();
