var arduino = arduino || {};

(function($, mod) {
	$("#temperature").click(function() {
		$.ajax(
            arduino.TEMP_AJAX_URL,
            {
                "data" : 
                    {
                        "arduinoIP" : $("[name=arduinoBoardIP]").val(),
                        "arduinoPort" : $("[name=arduinoBoardPort]").val()
                    },
                "contentType" : "application/json",
                "dataType" : "json",
                "type" : "GET"
            }
	        );
	});
})(jQuery, arduino);