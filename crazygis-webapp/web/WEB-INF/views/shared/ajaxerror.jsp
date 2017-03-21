<%@ page import="com.crazygis.web.ApplicationError" %>
<%@ page import="com.crazygis.web.utils.WebUtils" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ApplicationError error = WebUtils.getApplicationError(request);
    String message = error.getErrorInformation() + " " + error.getErrorMessage();
%>
{"Message": "<%=message%>", "message": "<%=message%>"}