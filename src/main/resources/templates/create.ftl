<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://cdn.bootcss.com/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
</head>
<body>
<h1>hello world~~~</h1>
<div id="payQRCode"></div>
<script>
    jQuery("#payQRCode").qrcode({
        text: "${codeUrl}",
    })
</script>
<script>
    $(function () {
        // 2s 查询一次
        setInterval(function () {
            $.ajax({
                type: 'GET',
                url: '/pay/queryByOrderId',
                data: {'orderId': '${orderId}'},
                dataType: 'json',
                success: function (result) {
                    console.log(result)
                    if(result.platformStatus != null && result.platformStatus === 'SUCCESS') {
                        location.href = '${returnUrl}'
                    }
                },
                error: function (result) {
                    alert('Unknown Error')
                }
            })
        }, 2000)
    })
</script>
</body>
</html>