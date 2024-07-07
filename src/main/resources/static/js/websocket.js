const ws = new WebSocket('ws://localhost:8080/crypto-feed');

const setupWebSocketHandlers = () => {
    ws.onopen = () => console.log('WebSocket connection established');

    ws.onmessage = (event) => {
        console.log('Data received:', event.data);
        processWebSocketMessage(event.data);
    };

    ws.onerror = (error) => console.error('WebSocket error:', error);
    ws.onclose = () => console.log('WebSocket connection closed');
};

const processWebSocketMessage = (data) => {
    try {
        const messages = JSON.parse(data);
        messages.forEach(updateChartData);
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
        if (latestPrice !== null) {
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

setupWebSocketHandlers();
updateChartEverySecond();
