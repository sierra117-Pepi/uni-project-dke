<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Define Segment</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
  <link rel="stylesheet" href="../CSS-Files/layout.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
  
  <script>
  $(document).ready(function() {
    $("#datepicker1").datepicker({dateFormat:'dd/mm/yy'})
  });
  $(document).ready(function() {
	    $("#datepicker2").datepicker({dateFormat:'dd/mm/yy'});
  });
  </script>
</head>
<body>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar"></button>
      <div>
     	 <img style="width: 20%" src="http://66.media.tumblr.com/950df64b8cf2fd8dbd44e204e4be3b04/tumblr_inline_nia59zHyKF1t90ue7.gif">
      </div>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li><a href="flight_input.jsp">Define Flight</a></li>
        <li class="active"><a href="">Define Segment</a></li>
      </ul>
    </div>
  </div>
</nav>
  
<div class="container-fluid text-center">    
  <div class="row content">
  	<div class="col-sm-8 text-left"> 
	    <form action="${pageContext.request.contextPath}/defineSegmentServlet" method="get">
				<h1>Segment data</h1>
				Designator<input class="form-control" type="text" name="designator">
				<small class="form-text text-muted">Example LOWW-to-LULAR</small>
				<hr>
				<h1>Segment time</h1>
				<!-- Define beginPosition -->
				Enter time<br>
				<input id="datepicker1" name="departDate"/>
				<select name="departHour">
					<option value="hour" selected>hour</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
				</select>
				<select name="departMin">
					<option value="min" selected>min</option>
					<option value="00">00</option>
					<option value="05">05</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="25">25</option>
					<option value="30">30</option>
					<option value="35">35</option>
					<option value="40">40</option>
					<option value="45">45</option>
					<option value="50">50</option>
					<option value="55">55</option>
				</select>
				<br>
				<!-- Define endPosition --> 
				Leave time<br>
				<input id="datepicker2" name="arriveDate"/>
				<select name="arriveHour">
					<option value="hour" selected>hour</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
				</select>
				<select name="arriveMin">
					<option value="min" selected>min</option>
					<option value="00">00</option>
					<option value="05">05</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="25">25</option>
					<option value="30">30</option>
					<option value="35">35</option>
					<option value="40">40</option>
					<option value="45">45</option>
					<option value="50">50</option>
					<option value="55">55</option>
				</select>
				<hr>
				<h1>Segment detail information</h1>
				*Flight Rules
				<select name="rules">
					<option value="none" selected></option>
					<option value="IFR">IFR</option>
					<option value="VFR">VFR</option>
				</select><br>
				*Weatherconditions
				<select name="conditions">
					<option value="none" selected></option>
					<option value="IMC">IMC</option>
					<option value="VMC">VMC</option>
				</select>
				<br>
				Start position<input class="form-control" type="text" name="startposition">
				End position<input class="form-control" type="text" name="endposition">
				<hr>
		<input class="btn btn-success" type="submit" value="Submit">
		</form>
	</div>
  </div>
</div>
</body>
</html>