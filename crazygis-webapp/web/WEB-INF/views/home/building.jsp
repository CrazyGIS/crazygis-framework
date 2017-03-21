<%@ page import="com.crazygis.web.utils.WebUtils" %>
<%@ page import="com.crazygis.web.page.PageResource" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //设置Title
    WebUtils.setTitle(request, "建设中...");
%>
<ex-section id="exjs">
    <script type="text/javascript">
        masterpage.ready(function () {
            masterpage.resize(function () {
                var height = masterpage.contentBodyHeight;
                $("#contentPanel").css("height", height + "px");
                $("#textInfo").css("margin-top", (height - 74) / 2 + "px");
            });
        });
    </script>
</ex-section>
<body>
    <div id="contentPanel" style="overflow:hidden;height:100%;position:relative;">
        <div id="textInfo" style="background-image:url('<%=PageResource.formatSrc("/content/images/Building.png")%>'); width:762px; height:74px; margin-left:auto; margin-right:auto;"></div>
    </div>
</body>
