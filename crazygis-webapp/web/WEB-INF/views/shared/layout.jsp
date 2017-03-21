<%--
    Created by 松 on 2015/5/26.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="java.util.*" %>
<%@ page import="com.crazygis.web.page.PageResource" %>
<%@ page import="com.crazygis.web.utils.WebUtils" %>
<%@ page import="com.crazygis.security.model.User" %>
<%@ page import="com.crazygis.web.UserContext" %>
<%@ page import="com.crazygis.security.IFunction" %>
<%@ page import="com.crazygis.security.springsecurity.model.SystemFunction" %>
<%@ page import="com.crazygis.security.springsecurity.model.SystemMenu" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.crazygis.web.WebManager" %>

<c:set var="ctxp" value="${pageContext.request.contextPath}"></c:set>
<%
    User user = UserContext.getCurrentUser();
    String username = "";
    if(user != null) {
        username = user.getUsername();
    }
    String currentFunctionCode = WebManager.getCurrentFunctionCode(request);
    if(StringUtils.isEmpty(currentFunctionCode)) {
        currentFunctionCode = "00";
    }
    String currentParentCode = WebManager.getParentFunctionCode(currentFunctionCode);
    while (WebManager.hasParent(currentParentCode)) {
        currentParentCode = WebManager.getParentFunctionCode(currentParentCode);
    }
    boolean isHomePage = ("00".equals(currentFunctionCode));
    boolean noMenu = false;

    /**
     * 生成菜单--start
     */
    List<IFunction> menus = new ArrayList<>();
    List<IFunction> subMenus = null;
    SystemMenu menu = null;
    SystemMenu subMenu = null;
    // 首页
    menu = new SystemMenu();
    menu.setFunctionId(UUID.randomUUID().toString().toLowerCase());
    menu.setFunctionCode("00");
    menu.setFunctionName("电子海图");
    menu.setEnabled(true);
    menu.setIcon("/content/icons/icon_home.png");
    menu.setUrl("/index");

    menus.add(menu);

    // 图层管理
    menu = new SystemMenu();
    menu.setFunctionId(UUID.randomUUID().toString().toLowerCase());
    menu.setFunctionCode("01");
    menu.setFunctionName("图层管理");
    menu.setEnabled(true);
    menu.setIcon("/content/icons/icon_layer.png");

    subMenus = new ArrayList<>();
    // 点图层管理
    subMenu = new SystemMenu();
    subMenu.setFunctionId(UUID.randomUUID().toString().toLowerCase());
    subMenu.setFunctionCode("01_01");
    subMenu.setFunctionName("点图层管理");
    subMenu.setEnabled(true);
    //subMenu.setUrl("/layer/point");
    subMenu.setUrl("/building");

    subMenus.add(subMenu);
    // 线图层管理
    subMenu = new SystemMenu();
    subMenu.setFunctionId(UUID.randomUUID().toString().toLowerCase());
    subMenu.setFunctionCode("01_02");
    subMenu.setFunctionName("线图层管理");
    subMenu.setEnabled(true);
    //subMenu.setUrl("/layer/linestring");
    subMenu.setUrl("/building");

    subMenus.add(subMenu);
    // 面图层管理
    subMenu = new SystemMenu();
    subMenu.setFunctionId(UUID.randomUUID().toString().toLowerCase());
    subMenu.setFunctionCode("01_03");
    subMenu.setFunctionName("面图层管理");
    subMenu.setEnabled(true);
    //subMenu.setUrl("/layer/polygon");
    subMenu.setUrl("/building");

    subMenus.add(subMenu);

    menu.setChildren(subMenus);

    menus.add(menu);
    /**
     * 生成菜单--end
     */

    String currentClass = null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!--网站logo-->
    <link rel="shortcut icon" href="<%=PageResource.formatSrc("/content/icons/128.ico")%>" type="image/x-icon" />
    <title><%=WebUtils.getTitle(request)%></title>
    <sec:csrfMetaTags />

    <%=PageResource.cssLink(
            "/content/Site.css",
            "/content/Project.css",
            "/content/Menu.css"
    )%>

    <%=PageResource.scriptLink(
            "/scripts/jquery-1.11.3.min.js",
            "/scripts/UI/ui.core.js",
            "/scripts/UI/ui.color.js",
            "/scripts/Page/ui.headWave.js",
            "/scripts/Page/ui.masterpage.js",
            "/scripts/Page/ui.messagebox.js",
            "/scripts/Page/ui.menubar.js"
    )%>

    <sitemesh:write property='ex-section.headjs'/>

    <script type="text/javascript">
        // 全局变量
        masterpage.ctxPath = "${ctxp}";

        masterpage.isHomePage = "<%=isHomePage%>";
        masterpage.noMenu = <%=WebUtils.getNoMenu(request)%>;
        masterpage.Name = "<%=username%>";
        <%--masterpage.Department = "<%=user.getDepartment()%>";--%>
        <%--masterpage.Position = "<%=user.getPosition()%>";--%>
        masterpage.init();
    </script>

    <sitemesh:write property='ex-section.excss'/>
    <%=PageResource.cssLink(
            "/content/themes/metro.color/ui.metro.WaterBlue.css"
    )%>
    <style id="GlobalThemeChangeStyle" type="text/css">

    </style>
</head>
<body>
<header id="head" class="head-color">
    <img id="staticBG" src="/content/images/staticBG.png" style="position:absolute;top:0px;left:0px;height:48px;width:100%;min-width:1920px;" />
    <div id="dynamicBG" style="position:absolute; width: 100%; height: 100%; top: 0px; left: 0px;"></div>
    <h1 id="title" style="width: 500px;">
        <a id="menuBtn" class="menu-icon" href="javascript:void(0)">
            <b class="menu-icon-background title-color"></b>
            <img src="../content/icons/menu-button.png" alt="" />
        </a><span style="margin-left:8px;">CrazyGIS Framework DEMO</span>
    </h1>
    <ul id="headerTags">
    </ul>
    <ul id="headerCtrls">
        <li>
            <div id="user" class="user-color" title="userName">
                <img src="<%=PageResource.formatSrc("/content/images/protrait.png")%>" class="cover" />
            </div>
        </li>
    </ul>
    <br style="clear:both;" />
</header>
<section id="body">
    <menu class="navigation-panel">
        <dl class="menu-list">
            <%
                if(menus != null) {
                    for(IFunction m : menus) {
                        menu = (SystemMenu)m;
                        if(menu.getChildren() == null || menu.getChildren().size() == 0) {
                            currentClass = menu.getFunctionCode().equals(currentFunctionCode) ? "class=\"current-menu background-highlight selected-menu\"" : "";
                        } else {
                            currentClass = menu.getFunctionCode().equals(currentFunctionCode) ? "class=\"current-menu background-highlight\"" : "";
                        }
            %>
            <dt <%=currentClass%>>
                <b class="menu-item-background">
                    <b class="menu-item-color"></b>
                </b>
                <u class="menu-item-container">
                    <i class="icon">
                        <img src="<%=request.getContextPath() + menu.getIcon()%>" alt="" />
                    </i><span><%=menu.getFunctionName()%></span>
                    <%
                        if(menu.getChildren() == null || menu.getChildren().size() == 0) {
                    %>
                    <a href="<%=request.getContextPath() + WebManager.formatUrlByFunctionCode(menu.getUrl(), menu.getFunctionCode())%>"></a>
                    <%
                    } else {
                    %>
                    <i class="allow fa fa-angle-down"></i>
                    <%
                        }
                    %>
                </u>
            </dt>
            <%
                if(menu.getChildren() != null) {
            %>
            <dd <%=currentClass%>>
                <ul>
                    <%
                        for(IFunction sm : menu.getChildren()) {
                            subMenu = (SystemMenu)sm;
                            currentClass = subMenu.getFunctionCode().equals(currentFunctionCode) ? "class=\"selected-menu\"" : "";
                    %>
                    <li <%=currentClass%>>
                        <b class="menu-item-background">
                            <b class="menu-item-color"></b>
                        </b>
                        <u class="menu-item-container">
                            <span><%=subMenu.getFunctionName()%></span>
                            <a href="${ctxp}<%=WebManager.formatUrlByFunctionCode(subMenu.getUrl(), subMenu.getFunctionCode())%>"></a>
                        </u>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </dd>
            <%
                    }
                }
            %>
            <%
                }
            %>
        </dl>
    </menu>
    <div class="content-container">
        <sitemesh:write property='body'/>
    </div>
</section>

<sitemesh:write property='ex-section.exjs'/>
</body>
</html>