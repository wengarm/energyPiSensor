<%--
  Created by IntelliJ IDEA.
  User: wengarm
  Date: 10/30/17
  Time: 1:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Momentalna spotreba</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css">
    <script src="../jquery-3.2.1.min.js"></script>
</head>
<body>
<div id="canvas">
    <div id="login">
        <form name="loginForm" id="loginForm" action="/mainServlet?login" method="POST">
            Meno <input autocomplete="off" type="text" name="username" id="username" placeholder="meno">
            <br>
            Heslo <input autocomplete="off" type="password" name="password" id="password" placeholder="heslo">
            <br>
            <input id="loginSubmit" type="submit" name="submit" value="Login">
        </form>
    </div>
    <div id="globalValue">

    </div>
    <br>
    <div id="newMultiplier">
        <form name="newMultiplierForm" id="newMultiplierForm" action="/mainServlet?change=multiplier" method="POST">
            Novy nasobitel <input autocomplete="off" type="text" name="multiplier" placeholder="Novy nasobitel">
            <input id="multiplierSubmit" type="submit" name="submit" value="Zmenit">
        </form>
        <form name="newIntervalForm" id="newIntervalForm" action="/mainServlet?change=interval" method="POST">
            Novy interval [m] <input autocomplete="off" type="text" name="interval" placeholder="Novy interval [m]">
            <input id="intervalSubmit" type="submit" name="submit" value="Zmenit">
        </form>
    </div>
    <br>
    <div id="list">

    </div>
</div>


<script>


    $(document).ready(function () {
        $('#multiplierSubmit').click(function (e) {
            e.preventDefault();

            $('#login').css('visibility', 'visible');
        });

        var loginForm = $('#loginForm');
        loginForm.submit(function (e) {
            e.preventDefault();

            var multiplierForm = $('#newMultiplierForm');
            var username = $('#username').val();
            var password = $('#password').val();

            $.ajax({
                type: multiplierForm.attr('method'),
                url: multiplierForm.attr('action'),
                data: multiplierForm.serialize(),
                headers: {
                    "Authorization": "Basic " + btoa(username + ":" + password)
                },
                cache: false,
            });
            multiplierForm.trigger("reset");
            $('#login').css('visibility', 'hidden');
            loginForm.trigger("reset");
        });

        var intervalForm = $('#newIntervalForm');
        intervalForm.submit(function (e) {
            e.preventDefault();

            $.ajax({
                type: intervalForm.attr('method'),
                url: intervalForm.attr('action'),
                data: intervalForm.serialize(),
                cache: false
            });
            intervalForm.trigger("reset");
        });


        setInterval(function () {
            $.getJSON("mainServlet", 'print=global', function (resp) {
                $("#globalValue").html("<p style=\"font-size: 20px;\">Spotreba: " + resp.value + "</p>");
                $("#globalValue").append("<p style=\"font-size:14px;\">nasobitel: " + resp.multiplier + "</p>");
                $("#globalValue").append("<p style=\"font-size:14px;\">interval: " + resp.interval + " m</p>")
            });

            $.getJSON("mainServlet", 'print=list', function (data) {
                $("#list").html("");
                console.log(data);
                $.each(data, function (i, v) {
                    $("#list").append("<p>" + v + "</p>");
                });
            });
        }, 100);

    });
</script>

</body>
</html>
