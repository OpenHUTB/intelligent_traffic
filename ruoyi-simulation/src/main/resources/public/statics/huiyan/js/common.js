function generateUUID() {
    var d = new Date().getTime();
    if (window.performance && typeof window.performance.now === "function") {
        d += performance.now();
    }
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid;
}
var Sender = {
    sendPost: function (url, body, ak_id, ak_secret) {
        var result = "";
        var realUrl = new URL(url);
        var method = "POST";
        var accept = "application/json";
        var content_type = "application/json;chrset=utf-8";
        var path = realUrl.pathname;
        var date = new Date();
        var host = realUrl.host;
        // 1.对body做MD5+BASE64加密
        var bodyMd5 = hex_md5(body);
        var bodyBASE64 = base64encode(utf16to8(bodyMd5));
        var uuid = generateUUID();
        var stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
            + "x-acs-signature-method:HMAC-SHA1\n"
            + "x-acs-signature-nonce:" + uuid + "\n"
            + "x-acs-version:2021-09-08\n"
            + path;
        // 2.计算 HMAC-SHA1
        var signature = CryptoJS.HmacSHA1(stringToSign, ak_secret);
        // 3.得到 authorization header
        var authHeader = "acs " + ak_id + ":" + signature;
        // 打开和URL之间的连接
        var xhr = new XMLHttpRequest();
        xhr.open('post', url );
        // 设置通用的请求属性
        xhr.setRequestHeader("Accept", accept);
        xhr.setRequestHeader("Content-Type", content_type);
        xhr.setRequestHeader("Content-MD5", bodyMd5);
        xhr.setRequestHeader("Authorization", authHeader);
        xhr.setRequestHeader("x-acs-signature-nonce", uuid);
        xhr.setRequestHeader("x-acs-signature-method", "HMAC-SHA1");
        xhr.setRequestHeader("x-acs-version", "2021-09-08");
        // 发送请求参数
        xhr.send(body);
        xhr.onreadystatechange = function () {
            // 这步为判断服务器是否正确响应
            if (xhr.readyState == 4 && xhr.status == 200) {
                console.log(xhr.responseText);
            }
        };
    }
}
