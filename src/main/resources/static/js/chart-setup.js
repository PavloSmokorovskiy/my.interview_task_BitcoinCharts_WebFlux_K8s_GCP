const ctx = document.getElementById('cryptoChart').getContext('2d');
let latestPrice = null;
let latestTime = new Date();

Chart.register(ChartDataLabels);

function formatDataLabel(value) {
    return `${value.y.toFixed(2)}`;
}

function formatYAxisLabel(value, ctx) {
    return `${value.y.toFixed(2)} USD`;
}

const datasetSettings = {
    label: 'BTC-USD',
    backgroundColor: 'rgba(255, 0, 0, 1)',
    borderColor: 'rgba(255, 0, 0, 1)',
    fill: false,
    lineTension: 0.5,
    borderWidth: 2,
    datalabels: {
        align: 'end',
        anchor: 'end',
        backgroundColor: 'rgba(255, 255, 255, 0)',
        borderRadius: 3,
        color: 'black',
        font: {
            weight: 'bold'
        },
        formatter: formatDataLabel
    }
};

const axisSettings = {
    x: {
        type: 'time',
        time: {
            unit: 'second',
            tooltipFormat: 'h:mm:ss a',
            displayFormats: {
                second: 'h:mm:ss a'
            }
        },
        ticks: {
            autoSkip: true,
            maxTicksLimit: 20,
            callback: function (value, index, values) {
                return new Date(value).toLocaleTimeString('en-US', {
                    hour: 'numeric',
                    minute: '2-digit',
                    second: '2-digit',
                    hour12: true
                }).toLowerCase();
            }
        },
        min: () => Date.now() - 14000,
        max: () => Date.now()
    },
    y: {
        beginAtZero: false,
        grace: '5%'
    }
};

const chartOptions = {
    responsive: false,
    maintainAspectRatio: true,
    scales: axisSettings,
    plugins: {
        legend: {
            display: true
        },
        datalabels: {
            color: '#444',
            display: true,
            font: {
                weight: 'bold'
            },
            formatter: formatYAxisLabel
        }
    }
};

const cryptoChart = new Chart(ctx, {
    type: 'line',
    data: {
        datasets: [datasetSettings]
    },
    options: chartOptions
});
