<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Feed</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<style>
.dBtn {
	box-shadow: 0.5px 0.5px 0.5px 0.5px gray;
	border-radius: 5px 5px 5px 5px;
}

#searchDiv {
	text-align: right;
}
</style>
</head>
<body>
	<div class="container mt-5">
		<div class="row">
			<div class="col-md-2 col-sm-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<h3 class="panel-title">Menu</h3>
					</div>
					<!-- 사이드바 메뉴목록1 -->
					<ul class="list-group">
						<li class="list-group-item"><a href="${pageContext.request.contextPath}/admin/memberList.do">멤버관리</a></li>
						<li class="list-group-item"><a href="${pageContext.request.contextPath}/admin/blackList.do">블랙리스트관리</a></li>
						<li class="list-group-item"><a href="${pageContext.request.contextPath}/admin/totalFeedList.do">피드관리</a></li>
						<li class="list-group-item"><a href="${pageContext.request.contextPath}/admin/declarationList.do">신고관리</a></li>
					</ul>
				</div>
			</div>
			<div class="col-md-10 col-sm-12">
				<div class="row">
					<div class="col">
						<h3>신고게시물 관리</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12" id="searchDiv">
						<form action="${pageContext.request.contextPath}/admin/searchForDeclare.do"
							method="post" id="searchF">
							<select id="searchTag" name="searchTag">
								<option value="to_id">신고당한사람</option>
								<option value="from_id">신고자</option>
								<option value="declare_reason">이유</option>
							</select> <input type="text" id="search" name="search">
							<button type="submit" id="searchBtn" class="btn-secondary">검색</button>
						</form>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<table class="table table-hover">
							<thead>
								<tr>
									<th>feed_seq</th>
									<th>reported person</th>
									<th>declare_reason</th>
									<th>reporter</th>
									<th>delete</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${declarationList}" var="ddto">
									<tr>
										<td><a href="${pageContext.request.contextPath}/feed/detailView?feed_seqS=${ddto.feed_seq}">${ddto.feed_seq}</a></td>
										<td>${ddto.to_id}</td>
										<td>${ddto.declare_reason}</td>
										<td>${ddto.from_id}</td>
										<c:choose>
										<c:when test="${ddto.delete_feed == 'N'}">
										<td><button type="button" class="dBtn btn-dark"
												id="${ddto.feed_seq}">삭제</button></td>
										</c:when>
										<c:when test="${ddto.delete_feed == 'Y'}">
										<td><button type="button" class="dBtn btn-danger"
												id="b_${ddto.feed_seq}">블랙경고</button></td>
										</c:when>
									<%-- 	<c:otherwise>
										<td><button type="button" class="dBtn btn-dark"
												id="${ddto.feed_seq}">삭제</button></td>
										</c:otherwise> --%>
										</c:choose>
									</tr>
								</c:forEach>
								<tr align=center>
									<td colspan=5>${pageNavi}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".dBtn").on("click",function() {
			var feed = $(this).attr("id");
			$.ajax({
				url : "${pageContext.request.contextPath}/admin/deleteDeclareFeedProc.do",
				type : "post",
				data : {
				feed : feed
				}
			}).done(function(resp) {
					if (resp == feed) {
					alert("게시물이 삭제되었습니다.");
					location.reload();
					} else {
					alert("게시물 삭제에 실패하셨습니다.");
					location.reload();
					}
			}).fail(function(a, b, c) {
					console.log(a);
					console.log(b);
					console.log(c);
			});
		});
	</script>
</body>
</html>