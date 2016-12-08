<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Homepage</title>
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
        <li class="active"><a href="home.jsp">Home</a></li>
        <li><a href="flight_input.jsp">Define Flight</a></li>
        <li><a href="flight_overview.jsp">Overview</a></li>
        <li><a href="https://en.wikipedia.org/wiki/NOTAM">General Inforamtion</a></li>
      </ul>
    </div>
  </div>
</nav>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-8 text-left"> 
      <h1>Welcome</h1>
      <p>This program is used to filter NOTAM's for Pilots. If you don't know what NOTAM's are read the text below.</p>
      <hr>
      <h3>General Information</h3>
      <p>
      NOTAM is a notice filed with an aviation authority to alert aircraft pilots of potential hazards along a flight route or at a location that could affect the safety of the flight.
      NOTAMs are unclassified notices or advisories distributed by means of telecommunication that contain information concerning the establishment, conditions or change in any aeronautical facility, service, procedure or hazard, the timely knowledge of which is essential to personnel and systems concerned with flight operations.
      </p>
      <p>For more inforamtion on NOTAM's please go to "General Information" in the menu bar.</p>
    </div>
  </div>
</div>
</body>
</html>