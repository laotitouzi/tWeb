<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>老T投资</title>
</head>
<body>
	<table>
		<c:forEach items="${orders}" var="record" begin="0" varStatus="status">
			<tr>
				<td>${record.createDate}</td>
				<td>${record.buyer}</td>
				<td>${record.goodsName}</td>
				<td>${record.fee}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>