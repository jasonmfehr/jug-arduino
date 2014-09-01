var arduino = arduino || {};

(function($, mod) {
    
    function setLed(index, isOn, arduinoIP, arduinoPort) {
        console.log("setting led " + index + " to value " + isOn + " on arduino with address " + arduinoIP + ":" + arduinoPort);
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
    }

    function init() {
	    $('.ledCheckbox').each(function(idx) {
	    	var $this = $(this);
	    	$this.click(function(e) {
	    		setLed(idx, $this.is(':checked'), $("[name=arduinoBoardIP]").val(), $("[name=arduinoBoardPort]").val());
	    	});
	    });
	    
	    $("[name=arduinoBoardIP]").val("192.168.1.7");
	    $("[name=arduinoBoardPort]").val("51420");
    }
    
    $(document).ready(function() {
    	init();
    });
    
})(jQuery, arduino);