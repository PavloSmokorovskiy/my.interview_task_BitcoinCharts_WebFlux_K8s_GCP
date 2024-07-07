import {updateDataDisplay} from './data-display.js';
import {isClosed} from './toggle-сonnection.js';

let ws;

export function openWebSocket() {
    ws = new WebSocket('ws://localhost:8080/crypto-feed');
    ws.onopen = () => {
        console.log('WebSocket connection established');
        updateDataDisplay({t: new Date(), message: 'WebSocket connection established'});
    };
    ws.onmessage = (event) => {
        console.log('Received message:', event.data);
        if (!isClosed) processWebSocketMessage(event.data);
    };
    ws.onerror = (error) => {
        console.error('WebSocket error:', error);
        updateDataDisplay({t: new Date(), message: 'WebSocket error: ' + error.message});
    };
    ws.onclose = () => {
        console.log('WebSocket connection closed');
        updateDataDisplay({t: new Date(), message: 'WebSocket connection closed'});
    };
}

export function closeWebSocket() {
    if (ws) {
        ws.close();
    }
}

const processWebSocketMessage = (data) => {
    try {
        const messages = JSON.parse(data);
        if (!Array.isArray(messages)) {
            console.error('Expected an array of messages, received:', messages);
            return;
        }
        messages.forEach((message) => {
            updateChartData(message);
            updateDataDisplay(message);
        });
    } catch (error) {
        console.error('Error parsing data:', error);
    }
};

const updateChartData = (data) => {
    latestPrice = data.p;
    latestTime = new Date(data.t);
};

const updateChartEverySecond = () => {
    setInterval(() => {
        if (latestPrice !== null && !isClosed) {
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
    cryptoChart.data.datasets[0].data = cryptoChart.data.datasets[0].data.filter(point => point.x >= cutoffTime);
};

openWebSocket();
updateChartEverySecond();
