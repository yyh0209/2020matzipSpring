<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="sectionContainerCenter">
	<div>
	<div class="msg">${msg}</div>
		<form id="frm" class="frm" action="/user/join" method="post">
			<div id="idChkResult" class="msg"></div>
		<!-- 아이디중복검사. -->
			<div><input type="text" name="user_id" placeholder="아이디">
				<button type="button" onclick="chkId()">아이디 중복체크</button> 
				<!-- id 중복체크 했는지 아닌지를 자바스크립트로 작성하라. -->
			</div>
			<div><input type="password" name="user_pw" placeholder="비밀번호"></div>
			<div><input type="password" name="user_pwre" placeholder="비밀번호확인"></div>
			<div><input type="text" name="nm" placeholder="이름"></div>
			<div><input type="submit" value="회원가입"></div>
		</form>
		<div>
			<a href="/user/login">로그인</a>
			<!--로그인 버튼을 누르면 restmap으로 이동한다.  -->
		</div>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>
		function chkId() {
			const user_id = frm.user_id.value
			
			axios.post('/user/ajaxIdChk', {
				'user_id':user_id
			}).then(function(res) {
				console.log(res);
				if (res.data == '2') { //아이디 없음
					idChkResult.innerText = '사용할수 있는 아이디 입니다.';
				} else if (res.data == '3') {
					//3번일때는 db의 값이 중복됨을 알림
					idChkResult.innerText = '이미 사용중입니다..';
					//idChkResult를 innerText를 이용해서 사용하는 환경이라면 속성값을 이용해서 문자열을 집어넣는것.
				}
			})
		}
	</script>
</div>