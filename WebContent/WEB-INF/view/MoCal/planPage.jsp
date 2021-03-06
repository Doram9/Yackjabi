<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="poly.util.KeyUtil"%>
<%@ page import="poly.util.CmmUtil"%>

<%@ page import="poly.dto.VoteInfoDTO"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.ArrayList" %>
<%
	List<String> roomlist = (List<String>) request.getAttribute("roomlist");
	if(roomlist == null) {
		roomlist = new ArrayList<>();
	}
%>

<%
	String roomcode = CmmUtil.nvl((String)request.getAttribute("roomcode"));
	List<VoteInfoDTO> userlist = (List<VoteInfoDTO>) request.getAttribute("userlist");
	String title = CmmUtil.nvl((String)request.getAttribute("title"));
	String yyyymm = CmmUtil.nvl((String)request.getAttribute("yyyymm"));
	String deadline = CmmUtil.nvl((String)request.getAttribute("deadline"));
	String firdate = CmmUtil.nvl((String)request.getAttribute("firdate"));
	String secdate = CmmUtil.nvl((String)request.getAttribute("secdate"));
	String thidate = CmmUtil.nvl((String)request.getAttribute("thidate"));
	
	if(userlist == null) {
		userlist = new ArrayList<>();
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href='css/fullcal/main.css' rel='stylesheet' />

    <style>
        .ui-datepicker-calendar {
            display: none;
        }

        .calendar * {
            border: solid 1px black;
        }

        .date {
            width: 50px;
            height: 50px;
        }

        #login {
            border: 0;
        }

        /* ??????????????? */
        .fc-scrollgrid-sync-inner .fc-col-header-cell-cushion {
            color: black;
        }

        .fc-daygrid-day-frame .fc-daygrid-day-top .fc-daygrid-day-number {
            color: black;
        }

        /* ????????? */
        .fc-day-sat .fc-scrollgrid-sync-inner .fc-col-header-cell-cushion {
            color: blue;
        }

        .fc-day-sat .fc-daygrid-day-frame .fc-daygrid-day-top .fc-daygrid-day-number {
            color: blue;
        }

        /* ????????? */
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
            	<button class="btn btn-secondary col-6 offset-2" onclick="kakaoLogout()">????????????</button>
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
                <a class="list-group-item list-group-item-action list-group-item-light p-3" href="#!">?????????</a>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                    ??? ?????? ????????????
                </button>
            </div>
        </div>
        <!-- Page content wrapper-->
        <div id="page-content-wrapper">
            <!-- Top navigation-->
            <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
                <div class="container-fluid">
                    <button class="navbar-toggler" id="sidebarToggle"><span class="navbar-toggler-icon"></span></button>
                </div>
            </nav>
            <!-- Page content-->
            <div class="container-fluid">
                <div class="row">
                    <h1 class="display-6 mt-3 col-12"><%=title %></h1>
                </div>
                <div class="row mt-2 justify-content-center">
                    <div class="mt-3 col-5">???????????? ?????? ???</div>
                    <div class="mt-2 col-12">
                        <ol class="list-group list-group-numbered">
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold"><%=firdate %></div>
                                </div>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold"><%=secdate %></div>
                                </div>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold"><%=thidate %></div>
                                </div>
                            </li>
                        </ol>
                    </div>
                </div>
                <div class="row mt-2 justify-content-center">
                    <div class="col-3 offset-4">?????? ??????</div>
                    <button class="btn btn-warning col-3 offset-1" onclick="kakaoInvite()" id="create-kakao-link-btn">?????? <i class="bi bi-share"></i></button>
                </div>
                <div class="row mt-2 g-2">
                	<%
	            		for(VoteInfoDTO pDTO : userlist) {
	            			String username = pDTO.getUsername();
	            			boolean votetf = pDTO.isVotetf();
	            			String checktag = "";
	            			if(votetf) {
	            				checktag = "<i class=\"bi bi-check2-circle\" style=\"color: red\"></i>";
	            			}
	            			
	            	%>
	            			<div class="col-4">
		                        <div class="p-2 border bg-light rounded-pill"><%= username %><%= checktag %></div>
		                    </div>
	            			
	            	<%
	            		}
	            	%>

                </div>

                <div class="row mt-3 justify-content-center">
                    <button type="button" class="btn btn-outline-danger col-10" id="voteBtn" data-bs-toggle="modal" data-bs-target="#voteModal"></button>
                </div>
                <div class="row mt-3 justify-content-center">
                    <div class="col-4">??????????????????</div>
					  <div class="time col-4">
					  	<span id="d-day-day">00</span>
					    <span class="col">???</span>
					    <span id="d-day-hour">00</span>
					    <span class="col">??????</span>
					    <span id="d-day-min">00</span>
					    <span class="col">???</span>
					    <span id="d-day-sec">00</span>
					    <span class="col">???</span>
					  </div>
                </div>
                <div class="row mt-2 justify-content-evenly">
                    <a type="button" class="btn btn-outline-primary col-3" href="index.do">?????????</a>
                    
                    <a type="button" class="btn btn-outline-dark col-3" onclick="deleteRoom('<%= roomcode %>', '<%= title %>')">??? ??????</a>
                </div>

            </div>
        </div>
    </div>

    <!-- ---------------------???????????????--------------------------------- -->
    <!-- Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form class="modal-content" action="mkPlan.do" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">??? ??????</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="mb-3">
                        <label for="exampleFormControlInput1" class="form-label">??????</label>
                        <input type="text" name="title" class="form-control" autocomplete="off" id="exampleFormControlInput1" placeholder="~~??? ?????????" required>

                    </div>
                    <p>????????? ???, ??? <input type="text" name="month" autocomplete="off" id="datepicker" required></p>
                    <p>?????? ??????</p>
                    <select name="deadline" class="form-select form-select-sm" aria-label=".form-select-sm example">
                        <option value="1">????????? 1???</option>
                        <option value="3">????????? 3???</option>
                        <option value="5">????????? 5???</option>
                    </select>
                </div>


                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">??????</button>
                    <button type="submit" class="btn btn-primary">??????</button>
                </div>
            </form>
        </div>
    </div>

    <!-- ---------------------????????????--------------------------------- -->
    <!-- Modal -->
    <div class="modal fade" id="voteModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form class="modal-content" action="planPage.html">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">????????????</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div id="modalCal" class="mt-2"></div>
                    <!-- Page content-->
                    <div class="container-fluid">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" checked>
                            <label class="form-check-label" for="inlineRadio1">???????????????</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2">
                            <label class="form-check-label" for="inlineRadio2">??????????????????</label>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">??????</button>
                    <button type="button" class="btn btn-danger" onclick="vote()">??????</button>
                </div>
            </form>
        </div>
    </div>

	<!-- Bootstrap core JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Core theme JS-->
    <script src="js/scripts.js"></script>

	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>

    <!-- ?????????????????? j?????? -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <!-- ???????????? -->
    <script src='js/fullcal/main.js'></script>
    <script src='js/fullcal/locales-all.js'></script>
	
	<script>
		Kakao.init('kakaoKey');
		let data = 'invite.do?roomcode=<%= roomcode %>&title=<%= title %>';
		function kakaoInvite() {
			Kakao.Link.sendCustom({
				  templateId: 70212,
				  templateArgs: {
					  'path': data
				  }
				});
		}
		
	</script>
	
	<script>
        
        function kakaoLogout() {
        	location.href='logout.do';
        }
        
    </script>
    
    <script>
    	function deleteRoom(roomcode, title) {
    		let answer = confirm("????????? ?????? ?????????????????????????(?????? ????????? ????????? ?????????????????? ???????????? ????????????.)");
    		if(answer) {
    			location.href = 'delPlan.do?roomcode=' + roomcode + "&title=" + title;
    		}
    	}
    
    </script>

    <script>
	    let posdays = new Set();
	    let negdays = new Set();
	    let redcol = 'rgba(232, 38, 37, 0.9)';
	    let greencol = 'rgba(113, 232, 42, 0.9)';
        $('#voteModal').on('shown.bs.modal', function(e) { //?????? ????????? ????????? ???????????? function
            let calendarEl = document.getElementById('modalCal');
            let calendar = new FullCalendar.Calendar(calendarEl, {
                locale: 'ko',
                headerToolbar: {
                    left: '',
                    center: 'title',
                    right: ''
                },
                initialDate: "<%= yyyymm %>",
                height: 'auto',
                initialView: 'dayGridMonth',
                selectable: false,
                dateClick: function DateClick(info) {
                	let day = String(info.dateStr);
                	if(document.getElementById("inlineRadio1").checked) {
                		if(info.dayEl.style.backgroundColor == greencol) {
                			posdays.delete(day);
                			info.dayEl.style.backgroundColor = '';
                			console.log(info.dayEl.style.backgroundColor);
                		} else if(info.dayEl.style.backgroundColor == redcol) {
                			negdays.delete(day);
                			posdays.add(day);
                			info.dayEl.style.backgroundColor = greencol;
                			console.log(info.dayEl.style.backgroundColor);
                		} else {
                			posdays.add(day);
                			info.dayEl.style.backgroundColor = greencol;
                			console.log(info.dayEl.style.backgroundColor);
                		}
                	} else {
        				if(info.dayEl.style.backgroundColor == redcol) {
        					negdays.delete(day);
                			info.dayEl.style.backgroundColor = '';
                			console.log(info.dayEl.style.backgroundColor);
                		} else if(info.dayEl.style.backgroundColor == greencol) {
                			posdays.delete(day);
                			negdays.add(day);
                			info.dayEl.style.backgroundColor = redcol;
                			console.log(info.dayEl.style.backgroundColor);
                		} else {
                			negdays.add(day);
                			info.dayEl.style.backgroundColor = redcol;
                			console.log(info.dayEl.style.backgroundColor);
                		}
                		
                	}
                }
            });
            calendar.render();
        });
         
        function vote() {
        	let sendPosdays = Array.from(posdays);
        	let sendNegdays = Array.from(negdays);
        	let roomcode = String("<%= roomcode %>");
        	let obj = JSON.stringify({"posdays" : sendPosdays, "negdays" : sendNegdays, "roomcode" : roomcode});
        	let url = "planPage.do?id=" + "<%= roomcode %>";
        	$.ajax({
        		url: "voteDate.do",
        		contentType: 'application/json',
        		type: 'post',
        		data : obj,
        		contentType: "application/json; charset=utf-8",
        		dataType: "text",
        		success: function(data) {
        			alert("??????");
        			location.href = url;
        		},
        		error: function(error) {
        			alert("??????");
        			location.href = url;
        		}
        		
        	});
        }
        
    </script>
	
	<script>
		<% 
			String dl = deadline.replace(".",",");
		%>
		const countDownTimer = function () {
			var _vDate = new Date(<%= dl %>); // ?????? ?????? ??????
			var _second = 1000;
			var _minute = _second * 60;
			var _hour = _minute * 60;
			var _day = _hour * 24;
			var timer;
			let voteBtn = document.getElementById("voteBtn");
			function showRemaining() {
				var now = new Date();
				var distDt = _vDate - now;
				if (distDt < 0) { //?????? ????????????
					clearInterval(timer);
					voteBtn.textContent = "????????????";
					voteBtn.className = "btn btn-outline-secondary col-10";
					voteBtn.disabled = true;
					return;
				}
				voteBtn.textContent = "????????????";
				voteBtn.className = "btn btn-outline-danger col-10";
				voteBtn.disabled = false;
				var days = Math.floor(distDt / _day);
				var hours = Math.floor((distDt % _day) / _hour);
				var minutes = Math.floor((distDt % _hour) / _minute);
				var seconds = Math.floor((distDt % _minute) / _second);
				document.getElementById("d-day-day").textContent = days;
				document.getElementById("d-day-hour").textContent = hours;
				document.getElementById("d-day-min").textContent = minutes;
				document.getElementById("d-day-sec").textContent = seconds;
			}
			timer = setInterval(showRemaining, 1000);
		}
		countDownTimer();

	
	</script>

    <script>
        $.datepicker.setDefaults({
            dateFormat: 'yy-mm',
            prevText: '?????? ???',
            nextText: '?????? ???',
            monthNames: ['1???', '2???', '3???', '4???', '5???', '6???', '7???', '8???', '9???', '10???', '11???', '12???'],
            monthNamesShort: ['1???', '2???', '3???', '4???', '5???', '6???', '7???', '8???', '9???', '10???', '11???', '12???'],
            dayNames: ['???', '???', '???', '???', '???', '???', '???'],
            dayNamesShort: ['???', '???', '???', '???', '???', '???', '???'],
            dayNamesMin: ['???', '???', '???', '???', '???', '???', '???'],
            showMonthAfterYear: true,
            yearSuffix: '???',
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            onClose: function(dateText, inst) {
                $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
            }
        });


        $(function() {
            $("#datepicker").datepicker();
        });
    </script>
</body>

</html>