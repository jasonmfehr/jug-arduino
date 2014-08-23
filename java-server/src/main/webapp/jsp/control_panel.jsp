<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url value="/static/js/" var="scriptDir" />
<c:url value="/static/css/" var="cssDir" />
<c:url value="/led/set-led" var="ledAjaxUrl" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${cssDir}/jug-arduino.css">
<title>JUG Arduino Control Panel</title>
</head>
<body>
<div id="mainContent" class="buffered-frame">
    <h1>Arduino Control Panel</h1>
    <p>
        <%-- TODO: Bootstrap for alignment --%>
        <label for="arduinoBoardIP">Arduino Board IP Address:</label>
        <input type="text" name="arduinoBoardIP" id="arduinoBoardIP" />
        <br />
        <label for="arduinoBoardPort">Arduino Board Port:</label>
        <input type="text" name="arduinoBoardPort" id="arduinoBoardPort" />
    </p>
    
    <hr size="5" />
    
    <h2>Light Emiting Diode (LED) Controller</h2>
    <fieldset>
        <legend>The checkboxes below each correspond to a LED on the Arduino board.  Check and uncheck them to see the LEDs light up or go dark.</legend>
        <c:forEach begin="0" end="3" var="ledIdx">
            <label for="led${ledIdx}"></labeL><input type="checkbox" name="led${ledIdx}" id="led${ledIdx}" title="LED ${ledIdx}" value="led${ledIdx}" class="ledCheckbox"></input> LED ${ledIdx}</label>
            <br />
        </c:forEach>
    </fieldset>
    
    <h2>Temperature Sensor</h2>
    <p>Display the current temperature that the temperature sensor attached to the Arduino is detecting.  Click the button to get a new reading in real time.</p>
</div>

    <script type="text/javascript">
        var arduino = arduino || {};
        (function(mod) {
        	mod.LED_AJAX_URL = '${ledAjaxUrl}';
        })(arduino);
    </script>
    <script type="text/javascript" src="${scriptDir}/jquery.min.js"></script>
    <script type="text/javascript" src="${scriptDir}/led.js"></script>
</body>
</html>