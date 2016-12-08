<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Alternative Airport Data</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="../CSS-Files/layout.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
        <li class="active"><a href="">Define Alternative Airport</a></li>
      </ul>
    </div>
  </div>
</nav>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-8 text-left"> 
      <form action="${pageContext.request.contextPath}/defineAirportServlet" method="get">
			<h1>Alternative Airport Data</h1>
			Name<input class="form-control" type="text" name="airport"><br>
			*Flight Rules
			<select name="rules">
				<option value="" selected>none</option>
				<option value="IFR">IFR</option>
				<option value="VFR">VFR</option>
			</select><br>
			*Weatherconditions
			<select name="conditions">
				<option value="" selected>none</option>
				<option value="IMC">IMC</option>
				<option value="VMC">VMC</option>
			</select>
			<hr>
	<input class="btn btn-success" type="submit" value="Submit">
	</form>
    </div>
  </div>
</div>
</body>
</html>