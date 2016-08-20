<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Hello World</title>
</head>
<body>
	Hello <br/>
	<%
	out.println("your ip is : "+request.getRemoteAddr());
	%>
	<%-- this is a comment for jsp--%>
	今天的日期是: <%= (new java.util.Date()).toLocaleString()%>
	<%int day = 1; %>
	<% if (day == 1 | day == 7) { %>
      <p>今天是周末</p>
	<% } else { %>
      <p>今天不是周末</p>
	<% } %>
	<%int fontSize = 1; %>
	<h3>For 循环实例</h3>
	<%for (fontSize = 1; fontSize <= 3; fontSize++){ %>
   		<font color="green" size="<%= fontSize %>">
    	菜鸟教程
   		</font><br />
	<%}%>
	
	<h3>While 循环实例</h3>
	<%while (fontSize <= 3){ %>
	 <font color="green" size="<%= fontSize %>">
	  菜鸟教程
	 </font><br />
	<%fontSize++;%>
	<%}%>
</body>
</html>