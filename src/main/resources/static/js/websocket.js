let ws;
let isPaused = false;

document.getElementById('toggleConnection').addEventListener('click', function() {
    if (!isPaused) {
        this.textContent = 'Resume Connection';
        isPaused = true;
        if (ws) {
            ws.close();
        }
    } else {
        this.textContent = 'Pause Connection';
        isPaused = false;
        setupWebSocket();
    }
});

const setupWebSocket = () => {
    ws = new WebSocket('ws://localhost:8080/crypto-feed');
    ws.onopen = () => {
        console.log('WebSocket connection established');
        updateDataDisplay({ t: new Date(), message: 'WebSocket connection established' });
    };
    ws.onmessage = (event) => {
        console.log('Received message:', event.data);
        if (!isPaused) processWebSocketMessage(event.data);
    };
    ws.onerror = (error) => {
        console.error('WebSocket error:', error);
        updateDataDisplay({ t: new Date(), message: 'WebSocket error: ' + error.message });
    };
    ws.onclose = () => {
        console.log('WebSocket connection closed');
        updateDataDisplay({ t: new Date(), message: 'WebSocket connection closed' });
    };
};

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
        if (latestPrice !== null && !isPaused) {
            const now = new Date();
            addDataToChart(now, latestPrice);
            removeOldDataFromChart();
            cryptoChart.update({ preservation: true });
        }
    }, 1000);
};

const addDataToChart = (time, price) => {
    cryptoChart.data.datasets[0].data.push({ x: time, y: price });
};

const removeOldDataFromChart = () => {
    const cutoffTime = Date.now() - 15000;
    cryptoChart.data.datasets[0].data = cryptoChart.data.datasets[0].data.filter(point => point.x >= cutoffTime);
};

const dataDisplay = document.getElementById('dataDisplay');

const updateDataDisplay = (data) => {
    const content = document.createElement('p');
    if (data.message) {
        content.textContent = `Message: ${data.message}`;
    } else {
        content.textContent = `Time: ${new Date(data.t).toLocaleTimeString()} - Price: ${data.p.toFixed(2)} USD`;
    }
    dataDisplay.appendChild(content);
    dataDisplay.scrollTop = dataDisplay.scrollHeight;
};

setupWebSocket();
updateChartEverySecond();
