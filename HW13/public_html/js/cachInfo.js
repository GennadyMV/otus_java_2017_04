function refresh() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {
            var cachInfo = JSON.parse(this.response);
            document.getElementById("hitCount").innerHTML = cachInfo.hitCount;
            document.getElementById("missCount").innerHTML = cachInfo.missCount;
        }
    };

    xhttp.open("GET", "/warAppl/getCachInfo", true);
    xhttp.send();
}