(function($) {
    
    function setLed(index, isOn, remoteBoardIP, remoteBoardPort) {
        console.log("setting led " + index + " to value " + isOn + " on remote board with address " + remoteBoardIP + ":" + remoteBoardPort);
        $.ajax(
            "led/set-led",
            {
                "data" : JSON.stringify(
                    {
                        "ledNumber" : index,
                        "ledOn" : isOn, 
                        "remoteBoardIP" : remoteBoardIP,
                        "remoteBoardPort" : remoteBoardPort
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
	    		setLed(idx, $this.is(':checked'), $("#remoteBoardIP").val(), $("#remoteBoardPort").val());
	    	});
	    });
	    
	    $("#remoteBoardIP").val("192.168.1.7");
	    $("#remoteBoardPort").val("51420");
    }
    
    $(document).ready(function() {
    	init();
    });
    
})(jQuery);