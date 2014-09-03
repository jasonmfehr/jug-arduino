var arduino = arduino || {};

(function($, mod) {
	function processSuccess(data) {
		$("#currentTemp").text(data.temperatureOuputTO.temperature);
	}
	
	function processError(xhr, status, error) {
		console.log(status);
		console.log(error);
	}
	
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
                "type" : "GET", 
                "success" : processSuccess,
                "error" : processError
            }
        );
	});
})(jQuery, arduino);