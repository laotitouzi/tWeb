<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>uploadFile</title>

<script type="text/javascript">
	function form_submit() {
		var text =document.getElementById("csvTradeFile").value;
		if(text == ""){
			alert("文件名不能为空");
		//	return false;
		}
		return true;
	}
</script>

</head>
<body>

	<form id="form1" action="../file/upload" method="POST"
		enctype="multipart/form-data">
		导入文件名：:<input id = "csvTradeFile" type="file" name="csvTradeFile" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" onclick="return form_submit()"
			value="上 &nbsp;&nbsp;传" />
	</form>
</body>
</html>