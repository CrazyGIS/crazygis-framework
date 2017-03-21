<%@ page import="com.crazygis.web.ApplicationError" %>
<%@ page import="com.crazygis.web.utils.WebUtils" %>
<%@ page import="com.crazygis.web.page.PageResource" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ApplicationError error = WebUtils.getApplicationError(request);
    int statusCode = error.getHttpStatusCode();

    String bgColor = "#0CACBC";
    String hoverColor = "#0A96A4";
    String activeColor = "#09808C";

    String emotion = "";
    String info = error.getErrorInformation();
    String errorMessage = error.getErrorMessage();
    if(statusCode == 404) {
        bgColor = "#888888";
        hoverColor = "#666666";
        activeColor = "#555555";
        emotion = ":-o";
    } else if(statusCode == 401 || statusCode == 403) {
        emotion = ":-)";
    } else if(statusCode >= 500) {
        bgColor = "#990000";
        hoverColor = "#711414";
        activeColor = "#4C2727";
        emotion = ":-(";
    } else {
        emotion = ":-D";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=pixel_value" />
        <title>错误</title>
        <%= PageResource.scriptLink("/scripts/jquery-1.11.3.min.js")%>
        <script type="text/javascript">
            $(document).ready(function () {
                var resizeFunc = function () {
                    var clientHeight = document.documentElement.clientHeight,
                        height = Math.floor(clientHeight / 3);
                    $("#errorWindow").css({
                        "height": height + "px",
                        "top": (clientHeight - height) / 2 + "px"
                    });
                };
                $(window).resize(resizeFunc);
                resizeFunc();
            });
        </script>
        <%=PageResource.cssLink(
            "/content/Site.css"
        )%>
        <style type="text/css">
            body {
                background-color: #F1F1F1;
            }

            div#errorWindow {
                position: absolute;
                color: #FFFFFF;
                background-color: <%=bgColor%>;
                width: 100%;
                height: 300px;
            }

            .error-title {
                position: absolute;
                top: 0px;
                width: 100%;
                font-size: 30px;
                height: 48px;
                line-height: 48px;
            }

            span.emotion {
                font-family: 'Segoe UI';
                margin-left: 20px;
            }

            .error-message {
                position:absolute;
                width:100%;
                top:48px;
                bottom:48px;
                overflow:auto;
            }

            .error-message p {
                margin-left:20px;
                width:auto;
            }

            .error-ctrl-panel {
                position: absolute;
                bottom: 0px;
                width: 100%;
                text-align: right;
                height: 48px;
                line-height: 48px;
            }

            .error-ctrl-panel p {
                margin-left:20px;
                width:auto;
            }

            a.link-button {
                display: inline-block;
                width: 100px;
                height: 24px;
                line-height: 24px;
                text-align: center;
                text-decoration: none;
                border: 2px solid #FFFFFF;
                color: #FFFFFF;
                background-color:transparent;
                padding: 0px;
                margin: 0px;
                font-size: 9pt;
                margin-right:20px;
            }

            a.link-button:hover {
                border:2px solid #FFFFFF;
                background-color:<%=hoverColor%>;
                color:#FFFFFF;
            }

            a.link-button:active {
                background-color:<%=activeColor%>;
            }
        </style>
    </head>
    <body>
        <div id="errorWindow">
            <div class="error-title">
                <span class="emotion"><%=emotion%></span>
                <span><%=info%></span>
            </div>
            <div class="error-message">
                <p><%=errorMessage%></p>
            </div>
            <div class="error-ctrl-panel">
                <a class="link-button" href="javascript:void(0)" onclick="javascript:window.history.go(-1)">返回</a>
                <a class="link-button" href="/index">首页</a>
            </div>
        </div>
    </body>
</html>
