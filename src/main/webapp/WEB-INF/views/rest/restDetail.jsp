<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!-- 추천메뉴 -->
<div class="recMenuContainer">
	<c:forEach items="${recommendMenuList}" var="item">
		<div class="recMenuItem" id="recMenuItem_${item.seq}">
			<!-- 참고사진 -->
			<div class="pic">
				<c:if test="${item.menu_pic != null and item.menu_pic != ''}">
					<img src="/res/img/restaurant/${data.i_rest}/${item.menu_pic}">
				</c:if>
			</div>
			<!-- 메뉴정보들 -->
			<div class="info">
				<div class="nm">${item.menu_nm}</div>
				<div class="price">
					<fmt:formatNumber type="number" value="${item.menu_price}" />
				</div>
			</div>
			<!-- 추천메뉴삭제  i_rest,seq의 pk값을 찾아서.-->
			<c:if
				test="${loginUser.i_user == data.i_user && item.menu_pic != null}">
				<div class="delIconContainer"
					onclick="delRecMenu(${data.i_rest}, ${item.seq})">
					<span class="material-icons"> close </span>
					<!-- 하트표시! -->
					
				</div>
			</c:if>
		</div>
	</c:forEach>
</div>

<!-- 가게들 -->
<div id="sectionContainerCenter">
	<div>
		<c:if test="${loginUser.i_user == data.i_user}">
			<h2>- 추천메뉴 -</h2>
			<div>
				<button onclick="isDel()">가게삭제</button>
				<!-- 인코딩 파일을 바이너리로 바꾼다. -->
				<div><button type="button" onclick="addRecMenu()">추천메뉴 추가</button></div>
				<form id="recFrm" action="/rest/recMenus" enctype="multipart/form-data" method="post">
					<input type="hidden" name="i_rest" value="${data.i_rest}">
					<div id="recItem"></div>
					<div>
						<input type="submit" value="등록">
					</div>
				</form>
			</div>
			<!-- 추천메뉴 -->
			<h2>- 메뉴 -</h2>
			<div>
				<form id="menuFrm" action="/rest/addMenusProc"
					enctype="multipart/form-data" method="post">
					<input type="hidden" name="i_rest" value="${data.i_rest}">
					<input type="file" name="menu_pic" multiple>
					<div>
						<input type="submit" value="등록">
					</div>
				</form>
			</div>
		</c:if>
		<div class="restaurant-detail">
			<div id="detail-header">
				<div class="restaurant_title_wrap">
					<span class="title">
						<h1 class="restaurant_name">${data.nm}</h1>
					</span>
				</div>
				<!-- 좋아요누름. -->
				<div class="status branch_none">
				</div>
			</div>
			<div>
				<table>
					<caption>레스토랑 상세정보 정보</caption>
					<tbody>
						<tr>
							<th>주소</th>
							<td>${data.addr}</td>
						</tr>
						<tr>
							<th>카테고리</th>
							<td>${data.cd_category_nm}</td>
						</tr>
						<tr>
							<th>메뉴</th>
							<td>
							<!-- fn: jstl의 function을 가리킴. -->
								<div class="menuList">
								<c:if test="${fn.length(menuList)>0}">
									<c:forEach var="i" begin="0" end="${fn.length(menuList) > 3 ? 2 : fn:length(menuList) - 1}">
										<div class="menuItem">
											<img src="/resource/img/restaurant/${data.i_rest}/menu/${menuList[i].menu_pic}">
										</div>
									</c:forEach>
									</c:if>
									<c:if test="${fn:length(menuList) > 3}">
										<div class="menuItem bg_black">
											<div class="moreCnt">
												+${fn:length(menuList)-3} 
											<!-- 3이 이미지 보기를 제한하는 길이. -->
											</div>
										</div>
									</c:if>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

	<script>
	function delRecMenu(seq){
		if(!confirm('삭제하시겠습니까?')){
			return
		}
		console.log('seq : ' + seq);
		//통신하는 컨트롤러.
		axios.get('/rest/ajaxDelRecMenu',{
			params:{
				i_rest: ${data.i_rest},
				seq:seq
			}
		//params: 붙이는건 포스트.
		}).then(function(res){
			console.log(res);
			if(res.data == 1){
				//element삭제
				var ele = document.querySelector('#recMenuItem_'+seq)
				ele.remove(); //id값 +seq을 삭제.
			}
		})
	}
		var idx = 0; //사진을 한개씩 추가할때마다 늘어난다.
		//메뉴더하기
		
		//추천메뉴 더하기 기능
		function addRecMenu() {
			var div = document.createElement('div');
			//이름
			var inputNm = document.createElement('input');
			inputNm.setAttribute("type", "text");
			inputNm.setAttribute('name', 'menu_nm');
			//가격
			var inputPrice = document.createElement('input');
			inputPrice.setAttribute("type", "number");
			inputPrice.setAttribute('name', 'menu_price');
			//사진
			var inputPic = document.createElement('input');
			inputPic.setAttribute("type", "file");
			inputPic.setAttribute('name', 'menu_pic' + idx++); //1씩 더한다.
			//setAttribute:
			//createElement:속성값을 자바스크립트에서 만든다.
			//appendchild 는 문자열만 결합
			//append
			div.append('메뉴:'); //선택된 요소의 마지막에 새로운 요소나 콘텐츠를 추가한다.
			div.append(inputNm);
			div.append('가격:');
			div.append(inputPrice);
			div.append('사진:');
			div.append(inputPic); //div
			//메뉴에다 이름을 합친다.
			//div+div 새로운 div를 만들지 않고 덮어씀.
	//recItem은 id 값으로 append를 하면 콘솔창에 에러가 뜬다..
			recItem.append(div);
	//문제가 생김. addRecMenu에다 정의하지 않음.
		}
		addRecMenu(); //바로호출

		function isDel() {
			if (confirm('삭제하시겠습니까?')) {
				location.href = '/rest/del?i_rest=${data.i_rest}'; //restDel의 키값
			}
		}
		
	</script>
</div>