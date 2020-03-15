var ws;
function hello(){
    ws = new WebSocket("ws://192.168.0.81:8080/ydtcontrol/websocket");
    ws.onopen = function(evn){
        console.log(evn);
    };
    ws.onmessage = function(evn){
        console.log(evn.data);
        var dv = document.getElementById("dv");
        dv.innerHTML+=evn.data;
    };
    ws.onclose = function(){
        console.log("关闭");
    };

};
function subsend(){
    var msg = document.getElementById("msg").value;
    ws.send(msg);
    document.getElementById("msg").value = "";
}

var player = null;

function loadStream(url) {
    player.setMediaResourceURL(url);
}

function getlink(url) {
    return "/index.jsp?src=" + encodeURIComponent(url);
}

function jsbridge(playerId, event, data) {
    if (player == null) {
        player = document.getElementById(playerId);
    }
    switch (event) {
        case "onJavaScriptBridgeCreated":
            listStreams(teststreams, "streamlist");
            break;
        case "timeChange":
        case "timeupdate":
        case "progress":
            break;
        default:
            console.log(event, data);
    }
}

// Collect query parameters in an object that we can
// forward to SWFObject:

var pqs = new ParsedQueryString();
var parameterNames = pqs.params(false);
var parameters = {
    src: "rtmp://192.168.0.81:1935/rtmplive/room",
    // src: "rtmp://47.111.160.59:1935/rtmplive/lxf",
    autoPlay: "true",
    verbose: true,
    controlBarAutoHide: "true",
    controlBarPosition: "bottom",
    poster: "images/poster.png",
    javascriptCallbackFunction: "jsbridge",
    plugin_hls: "/flashlsOSMF.swf",
    hls_minbufferlength: -1,
    hls_maxbufferlength: 30,
    hls_lowbufferlength: 3,
    hls_seekmode: "KEYFRAME",
    hls_startfromlevel: -1,
    hls_seekfromlevel: -1,
    hls_live_flushurlcache: false,
    hls_info: true,
    hls_debug: false,
    hls_debug2: false,
    hls_warn: true,
    hls_error: true,
    hls_fragmentloadmaxretry: -1,
    hls_manifestloadmaxretry: -1,
    hls_capleveltostage: false,
    hls_maxlevelcappingmode: "downscale"
};

for (var i = 0; i < parameterNames.length; i++) {
    var parameterName = parameterNames[i];
    parameters[parameterName] = pqs.param(parameterName) ||
        parameters[parameterName];
}

var wmodeValue = "direct";
var wmodeOptions = ["direct", "opaque", "transparent", "window"];
if (parameters.hasOwnProperty("wmode")) {
    if (wmodeOptions.indexOf(parameters.wmode) >= 0) {
        wmodeValue = parameters.wmode;
    }
    delete parameters.wmode;
}

// Embed the player SWF:
swfobject.embedSWF(
    "GrindPlayer.swf"
    , "GrindPlayer"
    , 640
    , 480
    , "10.1.0"
    , "expressInstall.swf"
    , parameters
    , {
        allowFullScreen: "true",
        wmode: wmodeValue
    }
    , {
        name: "GrindPlayer"
    }
);
