<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%-- 
   <jsp:useBean id="user" class="malgorzata.keska.JSP"
        scope="session"></jsp:useBean> --%>
Time: 
<%=(new java.util.Date()).toString() %>
<br>
Your browser is: 
<%=request.getHeader("user-agent") %>
<br>
Your IP address is: 
<%=request.getRemoteAddr() %>
<br>
DNS name:
<%=request.getHeader("host") %>


<%-- <%out.println((new java.util.Date()).toString());%> --%>
</body>
</html>