(function($) {
	function processSuccess(data) {
		$("#currentTemp").text(data.temperatureOuputTO.temperature);
	}
	
	function processError(xhr, status, error) {
		console.log(status);
		console.log(error);
	}
	
	$("#temperature").click(function() {
		$.ajax(
            "temp/read",
            {
                "data" : 
                    {
                        "remoteBoardIP" : $("#remoteBoardIP").val(),
                        "remoteBoardPort" : $("#remoteBoardPort").val()
                    },
                "contentType" : "application/json",
                "dataType" : "json",
                "type" : "GET", 
                "success" : processSuccess,
                "error" : processError
            }
        );
	});
})(jQuery);