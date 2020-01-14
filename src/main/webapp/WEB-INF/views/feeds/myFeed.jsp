<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title></title>
<script src="https://code.jquery.com/jquery-3.4.1.js"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
	integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
	integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
	crossorigin="anonymous"></script>

<script>
	$(function() {
		$("#registerFeed").on("click", function() {
			location.href = "writeFeed";
		})
	})
</script>
<style>
	#feedList{
		border:2px solid red;
	}
</style>
</head>
<body>

	<div id="wrapper">
		<div>
			<label>프로필 사진</label> <br><img src="${pageContext.request.contextPath}/resources/images/default_profile_img.png"

				id="setProfile" style="width: 500px;border-radius:50%;">

			<br>
			<br>
			<label>닉네임</label> <p>${dto.nickname } </p><br>
			
			<label>상태메세지</label> <p>${dto.profile_msg} </p> 
			
		</div>
		<button type="button" id="changeProfile">프로필 편집</button>
		<button type="button" id="changeInfo">회원 정보 편집</button><br>
		<button type="button" id="friendsList">친구 목록</button>
		<br><button id="registerFeed">게시물 등록</button>
		
		<div id="feedList">
		<c:choose>
			<c:when test="${fn:length(list) ==0}">
				게시물이 없습니다.
			</c:when>
			<c:otherwise>
				<table>
					<tr>
						<td>글번호
						<td>글제목
						<td>글내용
					</tr>
					<c:forEach items="${list }" var="list">
						<tr>
							<td>${list.feed_seq }
							<td><a href="/feed/detailView?feed_seqS=${list.feed_seq }">${list.title }</a>
							<td><a href="/feed/detailView?feed_seqS=${list.feed_seq }">${list.title }</a>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
		</div>
		
	</div>


	<!-- 친구요청 모달을 열기 위한 버튼 -->
	<button type="button" class="btn btn-primary btn-lg" id="openModalBtn">
		친구 요청</button>
	<!-- 친구요청 모달 영역 -->
	<div id="modalBox" class="modal fade" id="myModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">친구 관계 설정</h4>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>

				</div>
				
				<div class="modal-body">
				<form action="${pageContext.request.contextPath}/friend/friendRequest?to_id=${dto.email}" method="post" id="goReqFri">
					<input type=radio name="relation" value="1"> 아는 사람<br>
					<input type=radio name="relation" value="2"> 친구<br>
					<input type=radio name="relation" value="3"> 절친<br>
					<input type=radio name="relation" value="4"> x새끼<br>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="identifyModalBtn">확인</button>
					<button type="button" class="btn btn-default" id="closeModalBtn">취소</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 친구 목록 모달 영역 -->
	<div id="modalBox2" class="modal fade" id="myModal2" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">친구 목록</h4>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>

				</div>
				<div style="text-align: center;">
					친구 검색 : <input type=text placeholder=이름,닉네임 id="searchFriends" value="">
				</div>
				<div class="modal-body2"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="identifyModalBtn2">확인</button>
					<button type="button" class="btn btn-default" id="closeModalBtn2">취소</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$("#changeProfile").on("click", function() {
		location.href = "${pageContext.request.contextPath}/member/goMyProfile";
	})
	$("#changeInfo").on("click", function() {
		location.href = "${pageContext.request.contextPath}/member/goMyInfo";
	})
		// 친구 모달 버튼에 이벤트를 건다.	
		$('#friendsList').on('click',function() {

				$('.frInfo').remove();
							$.ajax({
									url : "${pageContext.request.contextPath}/friend/selectFndList",
									type : "POST",
									dataType : "json",
									success : function(res) {
									console.log(res);
									if (res.waitlist != null) {
										var waitlist = JSON.parse(res.waitlist);
												for (var j = 0; j < waitlist.length; j++) {
													$('.modal-body2').append("<div class=frInfo>"+ waitlist[j].email+ "  <button type=button class=frInfo id=acceptfr name="+waitlist[j].email+">친구 추가</button><button type=button class=frInfo id=cancelfr name="+waitlist[j].email+">취소</button></div>");
												}
											}
											if (res.list != null) {
												var list = JSON.parse(res.list);
												for (var j = 0; j < list.length; j++) {
													$('.modal-body2').append("<div class=frInfo>"+ list[j].email+ "  <button type=button class=frInfo id=cutfr name="+list[j].email+">친구 끊기</button></div>");
												}
											}
											// get the ajax response data
											// var data = res.body;

											// update modal content here
											// you may want to format data or 
											// update other modal elements here too
											// 		                console.log(changedStr.waitlist);
											// 		                console.log();

											// show modal
											$('#modalBox2').modal('show');

											//친구수락 로직~
											$("#acceptfr").on("click",function() {
												var yr_id = $(this).attr("name");
																console.log(yr_id);
																$.ajax({
																			url : "${pageContext.request.contextPath}/friend/acceptFndRequest",
																			type : "POST",
																			data : {
																				yr_id : yr_id
																			},
																			dataType : "text",
																			success : function(res) {
																				console.log(res);
																				console.log(yr_id);
																				$('#friendsList').click();

																				//$('.modal-body2').append("<div class=frInfo>"+list[j].email+"  <button type=button class=frInfo id=cutfr name="+list[j].email+">친구 끊기</button></div>");

																				// show modal

																			},
																			error : function(request,status,error) {													
																				console.log("ajax call went wrong:"+ request.responseText);
																			}
																		})
															});
											//친구 끊기
											$("#cutfr").on("click",	function() {
																var yr_id = $(this).attr("name");
																console.log(yr_id);
																$.ajax({
																			url : "${pageContext.request.contextPath}/friend/cutFndRelation",
																			type : "POST",
																			data : {
																				yr_id : yr_id
																			},
																			dataType : "text",
																			success : function(res) {
																				console.log(res);
																				console.log(yr_id);
																				$('#friendsList').click();

																				//$('.modal-body2').append("<div class=frInfo>"+list[j].email+"  <button type=button class=frInfo id=cutfr name="+list[j].email+">친구 끊기</button></div>");

																				// show modal

																			},
																			error : function(request,status,error) {
																				console.log("ajax call went wrong:"+ request.responseText);
																			}
																		})
															});
											//친구 검색
											$('#searchFriends').on('keyup',function() {
												var search = $(this).val();
												console.log(search);
												$('.frInfo').remove();
															$.ajax({
																	url : "${pageContext.request.contextPath}/friend/searchFndList",
																	type : "POST",
																	dataType : "json",
																	data:{
																		search:search
																	},
																	success : function(res) {
																	
																	console.log(res);
																	if (res.waitlist != null) {
																		var waitlist = JSON.parse(res.waitlist);
																				for (var j = 0; j < waitlist.length; j++) {
																					$('.modal-body2').append("<div class=frInfo>"+ waitlist[j].email+ "  <button type=button class=frInfo id=acceptfr name="+waitlist[j].email+">친구 추가</button><button type=button class=frInfo id=cancelfr name="+waitlist[j].email+">취소</button></div>");
																				}
																			}
																			if (res.list != null) {
																				var list = JSON.parse(res.list);
																				for (var j = 0; j < list.length; j++) {
																					$('.modal-body2').append("<div class=frInfo>"+ list[j].email+ "  <button type=button class=frInfo id=cutfr name="+list[j].email+">친구 끊기</button></div>");
																				}
																			}
																			// get the ajax response data
																			// var data = res.body;

																			// update modal content here
																			// you may want to format data or 
																			// update other modal elements here too
																			// 		                console.log(changedStr.waitlist);
																			// 		                console.log();

																			// show modal
																			
																			//친구 검색
																		},
																		error : function(request, status, error) {
																			console.log("ajax call went wrong:"
																					+ request.responseText);
																		}
																	});

														});
										},
										error : function(request, status, error) {
											console.log("ajax call went wrong:"
													+ request.responseText);
										}
									});

						});
		$('#closeModalBtn2').on('click', function() {

			$('#modalBox2').modal('hide');

		});
		$('#identifyModalBtn2').on('click', function() {

			$('#modalBox2').modal('hide');
		});

		$('#openModalBtn').on('click', function() {
			$('#modalBox').modal('show');
		});
		// 모달 안의 취소 버튼에 이벤트를 건다.	
		$('#closeModalBtn').on('click', function() {
			$('#modalBox').modal('hide');
		});
		$('#identifyModalBtn').on('click', function() {
			$("#goReqFri").submit();
			$('#modalBox').modal('hide');
		});

		//친구추가 ,취소 ,끊기
	</script>


</body>
</html>