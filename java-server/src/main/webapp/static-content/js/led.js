var arduino = arduino || {};

(function($, mod) {
    
    function setLed(index, isOn) {
        console.log('setting led ' + index + ' to value ' + isOn);
        $.ajax(
            arduino.LED_AJAX_URL,
            {
                "data" : JSON.stringify(
                    {
                        "ledNumber" : index,
                        "ledOn" : isOn
                    }),
                "contentType" : "application/json",
                "dataType" : "json",
                "type" : "POST"
            }
        );
    }
    
    $('.ledCheckbox').each(function(idx) {
        var $this = $(this);
        $this.click(function(e) {
            setLed(idx + 1, $this.is(':checked'));
        });
    });
    
})(jQuery, arduino);