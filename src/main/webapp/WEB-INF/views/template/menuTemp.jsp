<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
<link rel="stylesheet" type="text/css" href="/res/css/common.css">
<c:forEach items="${css}" var="item">
	<link rel="stylesheet" type="text/css" href="/res/css/${item}.css">
</c:forEach>
</head>
<body>
	<div id="container">
		<header>
			<div id="headerLeft">
				<c:if test="${loginUser != null}">
					<div id="containerPImg">
						<c:choose>
							<c:when test="${loginUser.profile_img != null}">
								<!-- res는  resource-->
								<img class="pImg" src="/res/img/user/${loginUser.i_user}/${loginUser.profile_img}">
							</c:when>
							<c:otherwise>
								<img class="pImg" src="/res/img/default_profile.jpg">
							</c:otherwise>
						</c:choose>
					</div>
					<div class="ml5">${loginUser.nm}님환영합니다.</div>
					<div class="ml15" id="headerLogout"><a href="/user/logout">로그아웃</a></div>
				</c:if>
				<c:if test="${loginUser==null}">
					<div class="ml15" id="headerLogout"><a href="/user/login">로그인</a></div>
				</c:if>
			</div>
			<div id="headerRight">
				<a href="/rest/map">지도</a> 
				<c:if test="${loginUser != null}">
				<!-- 로그인상태가 됐을때 -->
				<a class="ml15" href="/rest/reg">등록</a>
				</c:if>
				<!-- 로그인상태가 아닐때. -->
				<c:if test="${loginUser == null}">
				<a class="ml15" href="#" onclick="alert('로그인이 필요합니다.')">등록</a>
				</c:if>
				<a class="ml15" href="/user/restFavorite">찜</a>
				<!-- 찜의 경로  -->
			</div>
		</header>
		<section>
			<jsp:include page="/WEB-INF/views/${view}.jsp"></jsp:include>
		</section>
		<footer> 회사정보 </footer>
	</div>
	<script type="text/javascript">
	
	</script>
</body>
</html>