<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url value="/static/js/" var="scriptDir" />
<c:url value="/static/css/" var="cssDir" />
<c:url value="/led/set-led" var="ledAjaxUrl" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${cssDir}/bootstrap.min.css">
    <link rel="stylesheet" href="${cssDir}/bootstrap-theme.min.css">
<title>JUG Arduino Control Panel</title>
</head>
<body>
    <h1>Arduino Control Panel</h1>
    
    <h2>Light Emiting Diode (LED) Controller</h2>
    <fieldset>
        <p>The checkboxes below each correspond to a LED on the Arduino board.  Check and uncheck them to see the LEDs light up or go dark.</p>
        <c:forEach begin="1" end="4" var="ledIdx">
            <label for="led${ledIdx}"></labeL><input type="checkbox" name="led${ledIdx}" id="led${ledIdx}" title="LED ${ledIdx}" value="led${ledIdx}" class="ledCheckbox"></input> LED ${ledIdx}</label>
            <br />
        </c:forEach>
    </fieldset>
    
    <h2>Temperature Sensor</h2>
    <p>Display the current temperature that the temperature sensor attached to the Arduino is detecting.  Click the button to get a new reading in real time.</p>
    
    <script type="text/javascript">
        var arduino = arduino || {};
        (function(mod) {
        	mod.LED_AJAX_URL = '${ledAjaxUrl}';
        })(arduino);
    </script>
    <script type="text/javascript" src="${scriptDir}/jquery.min.js"></script>
    <script type="text/javascript" src="${scriptDir}/bootstrap.min.js"></script>
    <script type="text/javascript" src="${scriptDir}/led.js"></script>
</body>
</html>