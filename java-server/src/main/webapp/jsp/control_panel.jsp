<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url value="/static/js/" var="scriptDir" />
<c:url value="/static/css/" var="cssDir" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${cssDir}/jug-iot.css">
    <title>JUG Remote Board Control Panel</title>
</head>
<body>
<div id="mainContent" class="buffered-frame">
    <h1>Remote Board Control Panel</h1>
    <p>
        <label for="remoteBoardIP">Remote Board IP Address: <input type="text" name="remoteBoardIP" id="remoteBoardIP" /></label>
        <br />
        <label for="remoteBoardPort">Remote Board Port: <input type="number" name="remoteBoardPort" id="remoteBoardPort" /></label>
    </p>
    
    <hr size="5" />
    
    <p>
	    <h2>Light Emiting Diode (LED) Controller</h2>
	    <fieldset>
	        <legend>The checkboxes below each correspond to a LED on the remote board.  Check and uncheck them to see the LEDs light up or go dark.</legend>
	        <c:forEach begin="0" end="3" var="ledIdx">
	            <label for="led${ledIdx}"></label><input type="checkbox" name="led${ledIdx}" id="led${ledIdx}" title="LED ${ledIdx}" value="led${ledIdx}" class="ledCheckbox" /> LED ${ledIdx}</label>
	            <br />
	        </c:forEach>
	    </fieldset>
    </p>
    
    <hr size="5" />
    
    <p>
	    <h2>Temperature Sensor</h2>
	    <p>Display the current temperature that the temperature sensor attached to the remote board is detecting.  Click the button to get a new reading in real time.</p>
	    <p>The current temperature is: <span id="currentTemp"></span> <sup>o</sup>C</p>
	    <input type="button" name="temperature" id="temperature" value="Get Temperature" /> 
    </p>
    
    <hr size="5" />
    
    <p>
        <h2>Selectable Color LED</h2>
        <div id="colorLedValues">
	        <p><label for="rgbRed">Red: <input type="range" name="rgbRed" id="rgbRed" min="0" max="255" /></label><span></span></p>
	        <p><label for="rgbGreen">Green: <input type="range" name="rgbGreen" id="rgbGreen" min="0" max="255" /></label><span></span></p>
	        <p><label for="rgbBlue">Blue: <input type="range" name="rgbBlue" id="rgbBlue" min="0" max="255" /></label><span></span></p>
        </div>
    </p>
</div>

    <script type="text/javascript" src="${scriptDir}/jquery.min.js"></script>
    <script type="text/javascript" src="${scriptDir}/led.js"></script>
    <script type="text/javascript" src="${scriptDir}/temp.js"></script>
    <script type="text/javascript" src="${scriptDir}/color.js"></script>
</body>
</html>