init = function () {
    var ws = new WebSocket("ws://localhost:8091/getCachInfo");
    ws.onopen = function (event) {

    }
    ws.onmessage = function (event) {
        var cachInfo = JSON.parse(event.data);
        document.getElementById("hitCount").innerHTML = cachInfo.hitCount;
        document.getElementById("missCount").innerHTML = cachInfo.missCount;
    }
    ws.onclose = function (event) {
    }
};

