(function($) {
    
    function updateLedColor(remoteBoardIP, remoteBoardPort, redValue, greenValue, blueValue) {
    	console.log("Setting LED color to: " + redValue + "," + greenValue + "," + blueValue);
        $.ajax(
            "colorled/set-color",
            {
                "data" : JSON.stringify(
                    {
                        "remoteBoardIP" : remoteBoardIP,
                        "remoteBoardPort" : remoteBoardPort,
                        "red" : redValue,
                        "green" : greenValue,
                        "blue" : blueValue
                    }),
                "contentType" : "application/json",
                "dataType" : "json",
                "type" : "POST"
            }
        );
    }

    function init() {
    	$("#colorLedValues input").each(function() {
    		var $this = $(this),
    		    $displaySpan = $($this.parent().siblings(":first(span)"));
    		
    		$displaySpan.text($this.val());
    		
    		$this.change(function() {
    			updateLedColor($("#remoteBoardIP").val(), $("#remoteBoardPort").val(), $("#rgbRed").val(), $("#rgbGreen").val(), $("#rgbBlue").val());
    		});
    		
    		$this.mousemove(function() {
    			$displaySpan.text($this.val());
    		});
    	});
    }
    
    $(document).ready(function() {
    	init();
    });
    
})(jQuery);