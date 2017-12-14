<%@ page contentType="text/html; charset=UTF-8" %>         
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Author : 이종석 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/dashboard.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	setTimeout("location.reload()", 15000);
});
</script>
</head>
<body>
<div class="container">
	<div class="page-header text-center main-page">
		<h2><i class="fa fa-home"></i> 스마트 홈 IoT 대시보드</h2>
	</div>
	<div class="row text-center">
	<c:forEach var="data" items="${list}" varStatus="status">
		<div class="col-lg-6 col-lg-10">
	        <div class="thumbnail">
	        	<a href="${pageContext.request.contextPath}/dashboard/view?deviceCode=${data.deviceCode}&sensorCode=${data.sensorCode}">
	        		<img id="deviceImg" src="${pageContext.request.contextPath}/resources/image/device${data.deviceCode}.png" alt="Generic placeholder thumbnail">
	        	</a>
          		<table class="table table-striped table-hover table-bordered">
					<tr>
						<th>기기명</th>
						<th>센서 종류</th>
						<th>측정 값</th>
						<th>기기 상태</th>
						<th>자동 모드</th>
						<th>측정 시간</th>
					</tr>
					<tr>
						<td>${data.deviceName}</td>
						<td>${data.sensorType}</td>
						<td>${data.sensorValue}${data.sensorUnits}</td>
						<td>
						<c:choose>
							<c:when test="${data.deviceState eq 1}" >
								<i class="fa fa-toggle-on"></i>
							</c:when>
							<c:otherwise>
								<i class="fa fa-toggle-off"></i>
							</c:otherwise>
						</c:choose> 
						</td>
						<td>
						<c:choose>
							<c:when test="${data.autoMode eq 1}" >
								<i class="fa fa-toggle-on"></i>
							</c:when>
							<c:otherwise>
								<i class="fa fa-toggle-off"></i>
							</c:otherwise>
						</c:choose> 
						</td>
						<td><fmt:formatDate value="${data.regDate}" pattern="yyyy-MM-dd kk:mm:ss"/></td>
					</tr>
				</table>
        	</div>
    	</div>
    </c:forEach>
    </div>
</div>
</body>
</html>
