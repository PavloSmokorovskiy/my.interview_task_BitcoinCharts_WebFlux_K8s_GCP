const dataDisplay = document.getElementById('dataDisplay');

export const updateDataDisplay = (data) => {
    const content = document.createElement('p');
    if (data.message) {
        content.textContent = `Message: ${data.message}`;
    } else {
        content.textContent = `Time: ${new Date(data.t).toLocaleTimeString()} - Price: ${data.p.toFixed(2)} USD`;
    }
    dataDisplay.appendChild(content);

    while (dataDisplay.children.length > 14) {
        dataDisplay.removeChild(dataDisplay.children[0]);
    }
};
