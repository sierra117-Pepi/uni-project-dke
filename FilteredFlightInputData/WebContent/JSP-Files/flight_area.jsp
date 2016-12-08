<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Define Area</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://openlayers.org/en/v3.19.1/css/ol.css" type="text/css">
  <link rel="stylesheet" href="../CSS-Files/layout.css" type="text/css">
  <script src="https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL"></script>
  <script src="https://openlayers.org/en/v3.19.1/build/ol.js"></script>
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
        <li class="active"><a href="flight_area.jsp">Define Area</a></li>
      </ul>
    </div>
  </div>
</nav>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-8 text-left"> 
    <form action="${pageContext.request.contextPath}/defineAreaServlet" method="get">
			<h1>Area data</h1>
			<div id="map" class="map"></div>
			<div class="mapInput">
				<input id="mapp" type="hidden"/>
			</div>
		    <label>Choose figure to draw</label>
		    <select name="type" id="type">
		        <option value="LineString">LineString</option>
		        <option value="Polygon">Polygon</option>
		        <option value="None" selected>None</option>
		    </select>
		    <script>
		      var raster = new ol.layer.Tile({
		        source: new ol.source.OSM()
		      });
		
		      var source = new ol.source.Vector({wrapX: false});
		
		      var vector = new ol.layer.Vector({
		        source: source
		      });
		
		      var map = new ol.Map({
		        layers: [raster, vector],
		        target: 'map',
		        view: new ol.View({
		          center:  ol.proj.transform([0.12755, 51.507222], 'EPSG:4326', 'EPSG:3857'),
		          zoom: 4
		        })
		      });
				
		      var typeSelect = document.getElementById('type');
		
		      var draw; // global so we can remove it later
		      function addInteraction() {
		        var value = typeSelect.value;
		        if (value !== 'None') {
		          draw = new ol.interaction.Draw({
		            source: source,
		            type: /** @type {ol.geom.GeometryType} */ (typeSelect.value)
		          });
		          map.addInteraction(draw);
		        }
		      }
		
		
		      /**
		       * Handle change event.
		       */
		      typeSelect.onchange = function() {
		        map.removeInteraction(draw);
		        addInteraction();
		      };
		
		      addInteraction();
		      
		      document.getElementById("mapp").value = map.getInteractions();
		    </script>
		    <br>
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
			<hr>
		<input class="btn btn-success" type="submit" value="Submit">
	</form>
    </div>
  </div>
</div>
</body>
</html>