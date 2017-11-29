<%@ page contentType="text/html; charset=UTF-8" %>         
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){

});
</script>
</head>
<body>
<div class="container">
	<div class="page-header text-center">
		<h2><i class="fa fa-users"></i> 스마트 홈 IoT </h2>
	</div>
	<table class="table table-striped table-hover">
		<tr>
			<th>code</th>
			<th>value</th>
			<th>time</th>
		</tr>
		<c:forEach var="device" items="${list}" varStatus="status">
			<tr>
				<td>
					<a href="view?deviceCode=${device.deviceCode}">
						${device.deviceCode}
					</a>
				</td>
				<td>	
				${device.deviceValue}
				</td>
				<td>${device.deviceTime}</td>
			</tr>
		</c:forEach>
	</table>
</div>
</body>
</html>
