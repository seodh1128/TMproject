<%@ page contentType="text/html; charset=UTF-8" %>         
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<div class="page-header">
		<h2><i class="fa fa-search"></i> ${list["0"].deviceName} 상세보기 </h2>
	</div>
	<table class="table">
		<tr>
			<th>센서 종류</th>
			<th>측정 값</th>
			<th>기기 상태</th>
			<th>기기 모드</th>
			<th>측정 시간</th>
		</tr>
		<c:forEach var="data" items="${list}" varStatus="status">
			<tr>
				<td>${data.sensorType}</td>
				<td>${data.sensorValue}${data.sensorUnits}</td>
				<td>${data.deviceState}</td>
				<td>${data.autoMode}</td>
				<td><fmt:formatDate value="${data.regDate}" pattern="yyyy-MM-dd kk:mm:ss"/></td>
			</tr>
		</c:forEach>
	</table>
	<div class="text-center">
		<a class="btn btn-primary" href="list"><i class="fa fa-list"> 목록 </i></a>
	</div>
</div>
</body>
</html>
