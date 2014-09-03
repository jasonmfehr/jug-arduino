var arduino = arduino || {};

(function($, mod) {
    
    function updateLedColor() {
    	console.log("Setting LED color to: " + $("#rgbRed").val() + "," + $("#rgbGreen").val() + "," + $("#rgbBlue").val());
    	/*
        $.ajax(
            mod.LED_AJAX_URL,
            {
                "data" : JSON.stringify(
                    {
                        "ledNumber" : index,
                        "ledOn" : isOn, 
                        "arduinoIP" : arduinoIP,
                        "arduinoPort" : arduinoPort
                    }),
                "contentType" : "application/json",
                "dataType" : "json",
                "type" : "POST"
            }
        );
        */
    }

    function init() {
	    $(".rgbled").change(function() {
	    	updateLedColor();
	    });
	    
	    updateLedColor();
    }
    
    $(document).ready(function() {
    	init();
    });
    
})(jQuery, arduino);