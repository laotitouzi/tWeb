<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>老T投资</title>
</head>
<body>
	<%-- 	${ sessionScope.hello} --%>
	<form action="./user/save" method="post">
		<input type="hidden" name="token" value="${token}" /> <input
			type="text" name="name" value="" /><br /> <input type="submit"
			value="submit" />
	</form>
</body>
</html>