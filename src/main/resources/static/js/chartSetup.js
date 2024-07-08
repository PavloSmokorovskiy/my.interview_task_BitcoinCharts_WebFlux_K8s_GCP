const CHART_CONTAINER_ID = 'cryptoChart';
const CHART_TYPE = 'line';
const CHART_LABEL = 'BTC-USD';
const CHART_BACKGROUND_COLOR = 'rgba(255, 0, 0, 1)';
const CHART_TICK_LIMIT = 20;
const TIME_RANGE = 14000;

const ctx = document.getElementById(CHART_CONTAINER_ID).getContext('2d');

Chart.register(ChartDataLabels);

function formatDate(value) {
    return `${value.y.toFixed(2)}`;
}

function formatYAxis(value) {
    return `${value.y.toFixed(2)} USD`;
}

const datasetSettings = {
    label: CHART_LABEL,
    backgroundColor: CHART_BACKGROUND_COLOR,
    borderColor: CHART_BACKGROUND_COLOR,
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
        formatter: formatDate
    }
};

const axisSettings = getAxisSettings();
const chartOptions = getChartOptions(axisSettings);

const cryptoChart = new Chart(ctx, {
    type: CHART_TYPE,
    data: {
        datasets: [datasetSettings]
    },
    options: chartOptions
});

function getAxisSettings() {
    return {
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
                maxTicksLimit: CHART_TICK_LIMIT,
                callback: function (value) {
                    return new Date(value).toLocaleTimeString('en-US', {
                        hour: 'numeric',
                        minute: '2-digit',
                        second: '2-digit',
                        hour12: true
                    }).toLowerCase();
                }
            },
            min: () => Date.now() - TIME_RANGE,
            max: () => Date.now()
        },
        y: {
            beginAtZero: false,
            grace: '5%'
        }
    };
}

function getChartOptions(axisSettings) {
    return {
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
                formatter: formatYAxis
            }
        }
    };
}
