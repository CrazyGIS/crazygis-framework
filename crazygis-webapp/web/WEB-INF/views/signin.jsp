<%@ page import="com.crazygis.web.page.PageResource" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <!--网站logo-->
    <link rel="shortcut icon" href="<%=PageResource.formatSrc("/content/icons/128.ico")%>" type="image/x-icon" />
    <title>用户登录</title>
    <%=PageResource.cssLink(
        "/content/Site.css",
        "/content/Menu.css")%>
    <style type="text/css">
        body {
            color: #ffffff;
            background-color: #585858;
        }

        input[type='text'], input[type='password'] {
            width: 200px;
            border: none 0px;
            height: 30px;
            background-color: #6CB6DB;
        }

        input[type='text']:focus, input[type='password']:focus {
            background-color: #CDEEF7;
            color: #000000;
            text-decoration: none;
        }

        .button {
            width: 202px;
            height: 30px;
            line-height: 30px;
            background-color: #0CACBC;
        }

        .button:hover, .button:active {
            background-color: #FFFFFF;
            color: #000000;
        }

        #loginPanel {
            position: absolute;
            background-color: #9eacb3;
            border: 1px solid #D0D9DD;
        }

            #loginPanel h1#title {
                position: absolute;
                top:0px;
                font-size: 32px;
                width: 420px;
                height: 100px;
                overflow: hidden;
                left: 50%;
                margin-left: -225px;
                filter: Alpha(opacity=0);
                opacity: 0;
                text-align: center;
                /*color: #FFFFFF !important;*/
            }

            #loginPanel img.bgImage {
                position: absolute;
                left: 0px;
                top: 0px;
            }

            #loginPanel div#dynamicBG {
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
            }

            #loginPanel div#staticBG {
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
            }

        div#loginForm {
            position: absolute;
            width: 240px;
            height: 200px;
            left: 50%;
            margin-left: -120px;
            top: 50%;
            filter: Alpha(opacity=0);
            opacity: 0;
        }

        div.panel-background {
            position: absolute;
            width: 100%;
            top: 0px;
            bottom: 0px;
            background-color: #000000;
            filter: Alpha(opacity=0);
            opacity: 0;
        }
        div.panel-content {
            position: absolute;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        #loginPanel div.form-content {
            bottom: 65px;
            width: 200px;
            margin-top: 0px;
            margin-left: auto;
            margin-right: auto;
        }

        #loginPanel div.form-content span {
            line-height: 24px;
        }

        #loginPanel div.form-content .button-panel {
            width: 100%;
            text-align: right;
            margin-top: 24px;
        }

        #errorPanel {
            width: 200px;
            height: 30px;
            text-align: center;
            display: none;
            bottom: 20px;
        }

            #errorPanel span {
                line-height: 30px;
                color: #FF0033;
            }
    </style>
    <%=PageResource.scriptLink(
        "/scripts/jquery-1.11.3.min.js",
        "/scripts/UI/ui.core.js",
        "/scripts/UI/ui.color.js",
        "/scripts/UI/ui.effect.js"
    )%>
    <script type="text/javascript">
        ui.docReady(function (e) {
            $("#username").focus();

            var errorMessage = null;
            <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
            errorMessage = "${SPRING_SECURITY_LAST_EXCEPTION.message}";
            switch (errorMessage) {
                case "Bad credentials":
                    errorMessage = "用户名或密码错误";
                    break;
                case "SysUser is disabled":
                    errorMessage = "此用户已被禁用";
                    break;
                default:
                    errorMessage = "此用户无任何权限";
                    break;
            }
            </c:if>

            imageInfo.init();
            imageInfo.showError(errorMessage);
        }, ui.eventPriority.pageReady);
    </script>
    <%=PageResource.cssLink(
        "/content/themes/metro.color/ui.metro.Default.css"
    )%>
</head>
<body>
<!-- 为了解决当登陆失败后，再次刷新页面，SPRING_SECURITY_LAST_EXCEPTION值仍然存在的问题 -->
<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
<div id="loginPanel">
    <img class="bgImage" src="<%=PageResource.formatSrc("/content/images/wt/login.jpg")%>" alt="" />
    <div class="image-name"></div>
    <%--<div id="staticBG">--%>
        <%--<img src="<%=PageResource.formatSrc("/content/images/wave.png")%>" style="width:100%;height:100%;" alt="" />--%>
    <%--</div>--%>
    <%--<div id="dynamicBG"></div>--%>
    <div style="width: 161px;height: 161px;">
        <img src="<%=PageResource.formatSrc("/content/images/wt/logo.png")%>" style="width:100%;height:100%;" alt="" />
    </div>
    <h1 id="title" class="font-highlight">江苏省地方海事水上交通安全监测预警(VITS)系统</h1>
    <div id="loginForm">
        <div class="panel-background"></div>
        <div class="panel-content">
            <div class="form-content">
                <form role="form" name='form' action="/login" method='POST'>
                    <span>用户名:</span><br />
                    <input class="input-text" type="text" id="username" name="username" />
                    <span>密码:</span><br />
                    <input class="input-text" type="password" id="password" name="password" />
                    <div class="button-panel">
                        <input class="button" type="submit" value="登录" />
                    </div>
                </form>
                <div id="errorPanel"></div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    ; (function () {
        var imageInfo = window.imageInfo = {
            init: function () {
                this.loginPanel = $("#loginPanel");
                this.loginForm = $("#loginForm");
                this.bgImg = this.loginPanel.find(".bgImage");
                this.title = $("h1#title");

                this.changeWidth = 1440;
                this.changeHeight = 960;

                this.smallWidth = 958;
                this.smallHeight = 512;
                this.bigWidth = 1366;
                this.bigHeight = 768;

                this.onResize(true);
                var that = this;
                ui.resize(function() {
                    that.onResize();
                }, ui.eventPriority.bodyResize);

                var dynamicDiv = $("#dynamicBG"),
                    canvas = null;
                if (ui.core.isSupportCanvas()) {
                    $("#staticBG").css("display", "none");
                    canvas = $("<canvas style='width:100%; height: 100%' />");
                    dynamicDiv.append(canvas);
                    window.wave = ui.createEffects(canvas, "wave", {
                        speed: 0.01,
                        waterHeight: 1.12,
                        level: 0.07,
                        color: "#00CCCC",
                        bgColor: "#336699"
                    });
                    window.wave.start();
                }
            },
            onResize: function () {
                var width = ui.core.root.clientWidth;
                var height = ui.core.root.clientHeight;
                var flag = arguments[0],
                    titleTop, formTop;
                if (width < this.changeWidth && height < this.changeHeight) {
                    if (this.panelWidth !== this.smallWidth || this.panelHeight !== this.smallHeight) {
                        this.panelWidth = this.smallWidth;
                        this.panelHeight = this.smallHeight;
                        titleTop = 48;
                        formTop = 310;
                        flag = true;
                    }
                } else {
                    if (this.panelWidth !== this.bigWidth || this.panelHeight !== this.bigHeight) {
                        this.panelWidth = this.bigWidth;
                        this.panelHeight = this.bigHeight;
                        titleTop = 80;
                        formTop = 450;
                        flag = true;
                    }
                }
                this.clientWidth = width;
                this.clientHeight = height;
                imageInfo.setLoginPanelLocation();
                if (flag) {
                    imageInfo.setLoginPanelSize();
                    this.elementAnimate(titleTop, formTop);
                }
            },
            setLoginPanelLocation: function () {
                var left = (this.clientWidth - this.panelWidth) / 2,
                    top = (this.clientHeight - this.panelHeight) / 2;
                if (left < 0) {
                    left = 0;
                }
                this.loginPanel.css({
                    "top": top + "px",
                    "left": left + "px"
                });
            },
            setLoginPanelSize: function () {
                this.loginPanel.css({
                    "width": this.panelWidth + "px",
                    "height": this.panelHeight + "px"
                });
                this.bgImg.css({
                    "width": this.panelWidth + "px",
                    "height": this.panelHeight + "px"
                });
                this.startLeft = parseInt(this.clientWidth * 0.3, 10);
            },
            elementAnimate: function(titleTop, formTop) {
                if(!this.elemAnimator) {
                    this.elemAnimator = ui.animator({
                        target: this.title,
                        ease: ui.AnimationStyle.easeTo,
                        onChange: function(val, elem) {
                            elem.css("top", val + "px");
                        }
                    }).addTarget({
                        target: this.title,
                        ease: ui.AnimationStyle.easeFrom,
                        onChange: function(val, elem) {
                            elem.css({
                                "filter": "Alpha(opacity=" + val + ")",
                                "opacity": val / 100
                            });
                        }
                    }).addTarget({
                        target: this.loginForm,
                        ease: ui.AnimationStyle.easeTo,
                        onChange: function(val, elem) {
                            elem.css("top", val + "px");
                        }
                    }).addTarget({
                        target: this.loginForm,
                        ease: ui.AnimationStyle.easeFrom,
                        onChange: function(val, elem) {
                            elem.css({
                                "filter": "Alpha(opacity=" + val + ")",
                                "opacity": val / 100
                            });
                        }
                    });
                    this.elemAnimator.duration = 500;
                }

                this.elemAnimator.stop();
                var option = this.elemAnimator[0];
                option.begin = parseFloat(option.target.css("top"), 10);
                option.end = titleTop;
                option = this.elemAnimator[1];
                option.begin = 0;
                option.end = 100;
                option = this.elemAnimator[2];
                option.begin = formTop + 200;
                option.end = formTop;
                option = this.elemAnimator[3];
                option.begin = 0;
                option.end = 100;
                this.elemAnimator.start();
            },
            showError: function(errorMessage) {
                if(ui.core.type(errorMessage) === "string" && errorMessage.length > 0) {
                    $("#errorPanel")
                        .css("display", "block")
                        .html("<span>" + errorMessage + "</span>");
                }
            }
        };
    })();
</script>
</body>
</html>