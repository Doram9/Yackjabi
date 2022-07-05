<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="poly.util.KeyUtil"%>
<%@ page import="poly.util.CmmUtil"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%
	List<String> roomlist = (List<String>) request.getAttribute("roomcode");
	List<String> event = (List<String>) request.getAttribute("event");
	if(roomlist == null) {
		roomlist = new ArrayList<>();
	}
	if(event == null) {
		event = new ArrayList<>();
	}
%>
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

        .calendar * {
            border: solid 1px black;
        }

        #kakaoLogin {
            border: 0;
            outline: 0;
        }

        /* 월화수목금 */
        .fc-scrollgrid-sync-inner .fc-col-header-cell-cushion {
            color: black;
        }

        .fc-daygrid-day-frame .fc-daygrid-day-top .fc-daygrid-day-number {
            color: black;
        }

        /* 토요일 */
        .fc-day-sat .fc-scrollgrid-sync-inner .fc-col-header-cell-cushion {
            color: blue;
        }

        .fc-day-sat .fc-daygrid-day-frame .fc-daygrid-day-top .fc-daygrid-day-number {
            color: blue;
        }

        /* 일요일 */
        .fc-day-sun .fc-scrollgrid-sync-inner .fc-col-header-cell-cushion {
            color: red;
        }

        .fc-day-sun .fc-daygrid-day-frame .fc-daygrid-day-top .fc-daygrid-day-number {
            color: red;
        }
    </style>
</head>

<body>
    <div class="d-flex" id="wrapper">
        <!-- Sidebar-->
        <div class="border-end bg-white" id="sidebar-wrapper">
            <div class="sidebar-heading border-bottom bg-light" id="userInfo">
            <div class="row">
            	<div class="col-4"><%= session.getAttribute("username") %></div>
            	<button class="btn btn-secondary col-6 offset-2" onclick="kakaoLogout()">로그아웃</button>
            </div>
            	
            </div>
            <div class="list-group list-group-flush">
            	<%
            		for(String titleNcode : roomlist) {
            			String parse[] = titleNcode.split("\\*");
            			String titles = parse[0];
            			String code = parse[1];
            			
            	%>
            			<a class="list-group-item list-group-item-action list-group-item-light p-3" href="planPage.do?id=<%= code %>"><%= titles %></a>
            			
            	<%
            		}
            	%>
                <a class="list-group-item list-group-item-action list-group-item-light p-3" href="#!">내정보</a>
                <button type="button" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#inviteCode">
                    초대 코드 입력하기
               	</button>
                
            </div>
        </div>
        <!-- Page content wrapper-->
        <div id="page-content-wrapper">
            <!-- Top navigation-->
            <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
                <div class="container-fluid">
                    <button class="navbar-toggler" id="sidebarToggle"><span class="navbar-toggler-icon"></span></button>
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                    약속 추가하기
                	</button>
                </div>
            </nav>
            <!-- Page content-->
            <div class="container-fluid">
                <div id="calendar" class="mt-2"></div>
            </div>
        </div>
    </div>



    
    <!-- Event Modal -->
    <div class="modal fade" id="addEvent" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form class="modal-content" onsubmit="makeEvent(event)">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">새 일정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="mb-3">
                        <label for="exampleFormControlInput1" class="form-label">일정 제목</label>
                        <input type="text" name="title" class="form-control" autocomplete="off" id="title" required>

                    </div>
                    <label for="exampleFormControlInput1" class="form-label">시작날짜</label>
                    <input type="text" name="month" autocomplete="off" id="startdatepicker" required>
                    <br />
                    <label for="exampleFormControlInput1" class="form-label">종료날짜</label>
                    <input type="text" name="month" autocomplete="off" id="enddatepicker">
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary">생성</button>
                </div>
            </form>
        </div>
    </div>
  

	<!-- mkRoom Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form class="modal-content" action="mkPlan.do" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">새 약속</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="mb-3">
                        <label for="exampleFormControlInput1" class="form-label">제목</label>
                        <input type="text" name="title" class="form-control" autocomplete="off" id="exampleFormControlInput1" placeholder="~~에 만나요" required>

                    </div>
                    <p>만나는 년, 월 <input type="text" name="month" autocomplete="off" id="monthpicker" required></p>
                    <p>투표 기한</p>
                    <select name="deadline" class="form-select form-select-sm" aria-label=".form-select-sm example">
                        <option value="1">앞으로 1일</option>
                        <option value="3">앞으로 3일</option>
                        <option value="5">앞으로 5일</option>
                    </select>
                </div>


                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary">생성</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- inputCode Modal -->
    <div class="modal fade" id="#inviteCode" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form class="modal-content" onsubmit="inputCode()">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">새 일정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="mb-3">
                        <label for="exampleFormControlInput1" class="form-label">초대 코드</label>
                        <input type="text" name="code" class="form-control" autocomplete="off" id="inviteCode" required>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary">참가</button>
                </div>
            </form>
        </div>
    </div>


    <!-- Bootstrap core JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Core theme JS-->
    <script src="js/scripts.js"></script>

    <!-- 데이트피커용 j쿼리 -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    
    <!-- monthpicker -->
    <script src="js/jquery.mtz.monthpicker.js"></script>

    <!-- 풀캘린더 -->
    <script src='js/fullcal/main.js'></script>
    <script src='js/fullcal/locales-all.js'></script>



    <script>
        
        function kakaoLogout() {
        	location.href='logout.do';
        }
        
        function inputCode() {
        	let code = document.getElementById('inviteCode').value;
        	
        	let obj = JSON.stringify({"roomcode" : code});
        	$.ajax({
        		url: "invite.do",
        		contentType: 'application/json',
        		type: 'post',
        		data : obj,
        		contentType: "application/json; charset=utf-8",
        		dataType: "text",
        		success: function(data) {
        			location.href = 'index.do';
        		},
        		error: function(error) {
        			location.href = 'index.do';
        		}
        		
        	});
        }
        
    </script>
	
    <script>
        let start_x;
        let end_x;
        const canvas = document.getElementById('calendar');
        canvas.addEventListener('touchstart', touchStart);
        canvas.addEventListener('touchend', touchEnd);


        function preMonth() {
            document.getElementsByClassName('fc-prev-button fc-button fc-button-primary')[0].click();
        }

        function nextMonth() {
            document.getElementsByClassName('fc-next-button fc-button fc-button-primary')[0].click();
        }

        function touchStart(event) {
            start_x = event.touches[0].pageX;
        }

        function touchEnd(event) {
            end_x = event.changedTouches[0].pageX;
            if (start_x > end_x && (start_x - end_x) > 100) { //터치했을때 x좌표가 터치를 뗄 때 x좌표보다 100이상 크면 다음달 보여주기
                nextMonth();
            } else if (start_x < end_x && (end_x - start_x) > 100) { //터치했을때 x좌표가 터치를 뗄 때 x좌표보다 100넘게 작으면 지난달 보여주기
                preMonth();
            }
        }


        //document.addEventListener('DOMContentLoaded', function() {
            let calendarEl = document.getElementById('calendar');
            let calendar = new FullCalendar.Calendar(calendarEl, {
                locale: 'ko',
                headerToolbar: {
                    left: 'title',
                    center: '',
                    right: 'prev,next,today'
                },
                height: 'auto',
                selectable: true,
                dateClick: function(info) {
                	$("#startdatepicker").datepicker();
                    $("#enddatepicker").datepicker();
                	
                	$("#startdatepicker").datepicker("setDate", info.dateStr);
                
                    $('#startdatepicker').datepicker("option", "maxDate", $("#enddatepicker").val());
                    
                    $('#startdatepicker').datepicker("option", "onClose", function ( selectedDate ) {
                        $("#enddatepicker").datepicker( "option", "minDate", selectedDate );
                    });
                    
                    $('#enddatepicker').datepicker("option", "minDate", $("#startdatepicker").val());
                	
                	$('#addEvent').modal('show');
                },
                weekends: true,
                events: [
                		<%
                			for(int i = 0; i < event.size(); i++) {
                				String dd = event.get(i);
             				%>		
                				{<%=dd %>},
             				
             				<%
                			}
                		%>

                ],
                eventClick: function(info) {
                	let title = info.event.startStr;
                	let start = info.event.endStr;
                	let end = info.event.endStr;
                	let obj = JSON.stringify({"title" : title, "start" : start, "end" : end});
                	let answer = confirm("일정을 삭제하시겠습니까?");
                	if(answer) {
                		$.ajax({
                    		url: "delEvent.do",
                    		contentType: 'application/json',
                    		type: 'post',
                    		data : obj,
                    		contentType: "application/json; charset=utf-8",
                    		dataType: "text",
                    		success: function(data) {
                    			location.href = 'index.do';
                    		},
                    		error: function(error) {
                    			location.href = 'index.do';
                    		}
                    		
                    	});
                	}
                }
            });
            calendar.render();
            
            
        //});
        
        function makeEvent(event) {
        	event.preventDefault();
        	
        	let title = document.getElementById('title').value;
        	let start = document.getElementById('startdatepicker').value;
        	let end = document.getElementById('enddatepicker').value;
        	
        	
        	let obj = JSON.stringify({"title" : title, "start" : start, "end" : end});
        	$.ajax({
        		url: "addEvent.do",
        		contentType: 'application/json',
        		type: 'post',
        		data : obj,
        		contentType: "application/json; charset=utf-8",
        		dataType: "text",
        		success: function(data) {
        			location.href = 'index.do';
        		},
        		error: function(error) {
        			location.href = 'index.do';
        		}
        		
        	});
		}
    </script>

    <script>
        $.datepicker.setDefaults({
            dateFormat: 'yy-mm-dd',
            prevText: '이전 달',
            nextText: '다음 달',
            monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            dayNames: ['일', '월', '화', '수', '목', '금', '토'],
            dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
            dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
            showMonthAfterYear: true,
            yearSuffix: '년',
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true
            
        });


    </script>
    
    <script>
    	let now = new Date();
    	let options = {
    			pattern: 'yyyy-mm',
    			selectedYear : now.getFullYear(),
    			startYear: now.getFullYear(),
    			finalYear: now.getFullYear() + 5,
    			monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    			openOnFocus: true,
    			disableMonths: []
    	};
    	
    	$("#monthpicker").monthpicker(options);
    	
    	
    </script>


</body>

</html>