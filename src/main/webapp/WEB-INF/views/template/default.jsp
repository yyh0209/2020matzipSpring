<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/res/css/common.css">
<title>${title}</title>
<!-- 문자열로 값을 전달해줌 -->
</head>
<body>
	<div id="container">
		<!-- 달러{}는 서블릿의  setAttribute의 속성값을 받는다. -->
		<jsp:include page="/WEB-INF/views/${view}.jsp"></jsp:include>
		<!-- 파일이 열린다. -->
	</div>
</body>
</html>