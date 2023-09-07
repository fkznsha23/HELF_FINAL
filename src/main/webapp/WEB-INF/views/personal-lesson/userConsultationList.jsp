<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="kr">

<head>
    <meta charset="utf-8">
    <title>HELF</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Free HTML Templates" name="keywords">
    <meta content="Free HTML Templates" name="description">

    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&family=Rubik:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="/resources/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="/resources/lib/animate/animate.min.css" rel="stylesheet">

    <!-- Customized Bootstrap Stylesheet -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="/resources/css/style.css" rel="stylesheet">
    <!-- Date Picker  -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <!-- Date Picker  -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <!-- Datetime Picker -->
    <link rel="stylesheet" type="text/css" media="screen" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-datetimepicker/2.5.20/jquery.datetimepicker.min.css">
</head>

<body>
    <!-- Spinner Start -->
    <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
        <div class="spinner"></div>
    </div>
    <!-- Spinner End -->


    <!-- Topnavbar Start -->
   	<jsp:include page="/WEB-INF/views/common/topnavbar.jsp" />
    <!-- Topnavbar End -->


    <!-- Navbar Start -->
    <div class="container-fluid position-relative p-0 h-10 ">
		<jsp:include page="/WEB-INF/views/common/navbar.jsp">
			<jsp:param name="menu" value="수업"/>
		</jsp:include>
    <!-- Navbar End -->
        <div class="container-fluid bg-primary py-5 bg-header" style="margin-bottom: 10px;">
            <div class="row py-5">
                <div class="col-12 pt-lg-5 mt-lg-5 text-center">
                    <h1 class="display-4 text-white animated zoomIn">CONSULTATIONLIST</h1>
                    <a href="" class="h5 text-white">상담내역</a>
                </div>
            </div>
        </div>
    </div>


    <div class="modal fade" id="searchModal" tabindex="-1">
        <div class="modal-dialog modal-fullscreen">
            <div class="modal-content" style="background: rgba(9, 30, 62, .7);">
                <div class="modal-header border-0">
                    <button type="button" class="btn bg-white btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body d-flex align-items-center justify-content-center">
                    <div class="input-group" style="max-width: 600px;">
                        <input type="text" class="form-control bg-transparent border-primary p-3" placeholder="Type search keyword">
                        <button class="btn btn-primary px-4"><i class="bi bi-search"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid mt-5 wow fadeInUp" data-wow-delay="0.1s">
        <div class="container ">
            <div class="section-title text-start position-relative pb-3 mb-5" style="max-width: 600px;">
                <h1 class="fw-bold text-primary text-uppercase">상담신청 내역</h1>
                <h5 class="mb-0">상담을 신청한 강사를 확인하세요</h5>
            </div>
            <c:choose>
	            <c:when test="${empty consultations}">
	        		<div><strong>조회된 회원이 없습니다.</strong></div>
	    		</c:when>
	    		<c:otherwise>
		            <div class="row g-0">
			            <c:forEach var="trainer" items="${trainers}">
			                <div class="col-lg-4 wow slideInUp" data-wow-delay="0.3s">
			                    <div class="bg-white rounded shadow position-relative m-2" style="z-index: 1;">
			                        <div class="border-bottom mb-4">
									    <h5 class="text-primary mb-2"></h5>
									    <c:choose>
									        <c:when test="${consultation.consultations.consultationStatus eq '대기중'}">
									            <div class="text-success"><strong>신규회원</strong></div>
									        </c:when>
									        <c:when test="${consultation.consultations.consultationStatus eq '수강중'}">
									            <div class="text-primary"><strong>예약회원</strong></div>
									        </c:when>
									    </c:choose>
									    <div class="col d-flex flex-row align-items-center justify-content-start ml-0 bg-light" >
									        <img src="/resources/img/${trainer.trainerFile}" style="width: 30%; margin-right: 20px;" class="img-fluid"/>
									        <h4 class="text-uppercase text-primary">${trainer.user.name} 강사</h4>
									    </div>
									</div>
			                        <div class="p-5 pt-0 ">
										<div class="d-flex justify-content-between mb-3 text-primary" style="font-size: 20px;"><strong>주요경력</strong></div>
											<c:forEach var="career" items="${trainer.careers}">
											    <div class="d-flex justify-content-start mb-3">
											        <fmt:formatDate value="${career.careerStartDate}" pattern="yyyy-MM" /> ~ <fmt:formatDate value="${career.careerEndDate}" pattern="yyyy-MM" />
											        <span style="font-weight: bold; margin-left: 10px;">${career.careerName}</span>
											    </div>
											</c:forEach>
			                            <div class="d-flex justify-content-center">
			                            	<button type="button" class="btn btn-primary mt-2" data-bs-toggle="modal" data-bs-target="#myModal" >신청내역 조회</button>
			                            </div>
			                        </div>
			                    </div>
			                </div>
			            </c:forEach>
		            </div>
            	</c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg"> 
 			<div class="modal-content">
				<div class="modal-header bg-light">
	        		<h5 class="modal-title text-primary" id="staticBackdropLabel"></h5>
			        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      	</div>
		      	<div class="modal-body">
		      		<table class="table" id="table-personal-lessons" >
						<thead>
                        	<tr class="text-center">
	                            <th style="width:10%">번호</th>
	                            <th style="width:30%">요청날짜</th>
	                            <th style="width:20%">요청시간</th>
	                            <th style="width:20%">상태</th>
	                            <th style="width:20%"></th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				<div class="modal-footer">
				  	<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
    <!-- Lesson Register Form End  -->

    <!-- Footer End -->
	<jsp:include page="/WEB-INF/views/common/footernavbar.jsp" />

    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square rounded back-to-top"><i class="bi bi-arrow-up"></i></a>


    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="/resources/lib/wow/wow.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-datetimepicker/2.5.20/jquery.datetimepicker.full.min.js"></script>
    <script src="/resources/lib/easing/easing.min.js"></script>
    <script src="/resources/lib/waypoints/waypoints.min.js"></script>
    <script src="/resources/lib/counterup/counterup.min.js"></script>
    <script src="/resources/lib/owlcarousel/owl.carousel.min.js"></script>

    <!-- Template Javascript -->
	<script src="/resources/js/main.js"></script>
    
<script>


</script>

    
</body>

</html>