<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="poly.util.KeyUtil"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Simple Sidebar - Start Bootstrap Template</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="css/styles.css" rel="stylesheet" />
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href='css/fullcal/main.css' rel='stylesheet' />

    <style>
        #kakaoLogin {
            border: 0;
            outline: 0;
        }
    </style>
</head>

<body>
    <div class="d-flex" id="wrapper">
        <!-- Page content wrapper-->
        <div id="page-content-wrapper">
            <!-- Page content-->
            <div class="container-fluid">
            	<div class="row justify-content-center">
            		<div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
					  <div class="carousel-indicators">
					    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
					    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
					    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
					  </div>
					  <div class="carousel-inner">
					    <div class="carousel-item active">
					      <img src="..." class="d-block w-100" alt="...">
					      <div class="carousel-caption d-none d-md-block">
					        <h5>First slide label</h5>
					        <p>Some representative placeholder content for the first slide.</p>
					      </div>
					    </div>
					    <div class="carousel-item">
					      <img src="..." class="d-block w-100" alt="...">
					      <div class="carousel-caption d-none d-md-block">
					        <h5>Second slide label</h5>
					        <p>Some representative placeholder content for the second slide.</p>
					      </div>
					    </div>
					    <div class="carousel-item">
					      <img src="..." class="d-block w-100" alt="...">
					      <div class="carousel-caption d-none d-md-block">
					        <h5>Third slide label</h5>
					        <p>Some representative placeholder content for the third slide.</p>
					      </div>
					    </div>
					  </div>
					  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Previous</span>
					  </button>
					  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
					    <span class="carousel-control-next-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Next</span>
					  </button>
					</div>
                	<button class="btn mt-5" id="kakaoLogin" onclick="location.href='login.do'"><img src="img/kakao_login_medium.png"/></button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap core JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Core theme JS-->
    <script src="js/scripts.js"></script>





</body>

</html>