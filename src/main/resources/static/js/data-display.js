export const updateDataDisplay = (data) => {
    const content = document.createElement('p');
    content.textContent = generateContentText(data);
    dataDisplay.appendChild(content);
    maintainChildrenLimit();
};

const generateContentText = (data) => {
    if (data.message) {
        return `Message: ${data.message}`;
    }
    return `Time: ${new Date(data.t).toLocaleTimeString()} - Price: ${data.p.toFixed(2)} USD`;
};

const maintainChildrenLimit = () => {
    while (dataDisplay.children.length > 14) {
        dataDisplay.removeChild(dataDisplay.children[0]);
    }
};
