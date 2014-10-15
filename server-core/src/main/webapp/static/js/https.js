var protocol = window.location.protocol;
if (protocol.indexOf("https") == -1) {
    var href = window.location.href.replace("http", "https");
    window.location.href = href;
}