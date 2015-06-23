(function($){
	$(function(){
	  
	  // Slider functionality
	  
	    // slide knob to position function
	    (function( $ ){
         $.fn.slideToPos = function() {
           var left = $(this).position().left + ($(this).width() / 2) - ($(".menu a.knob").width() / 2) - 2;
           $(".menu a.knob").css("left", left);
           var before = $(this).parent().parent().parent().children(".bar").first().children(".before").first();
           before.css("width", left + 20).css("backgroundColor", $($(this).attr("href")).css("backgroundColor"));
           return this;
         }; 
      })( jQuery );
	 	  
	 	$(".menu ul li.active a").slideToPos();     
    $(".menu ul li a").click(function(e) {
      e.preventDefault();
      $(this).slideToPos();
      $('html, body').animate({ scrollTop: $(this.hash).offset().top }, 400);
    });
    
    
    // Scroll Spy
    $(window).scroll(function() {
      var top = $(window).scrollTop() + 100; // Take into account height of fixed menu
      $(".container").each(function() {
        var c_top = $(this).offset().top;
        var c_bot = c_top + $(this).height();
        var hash = $(this).attr("id");
        var li_tag = $('a[href$="' + hash + '"]').parent();
        if ((top > c_top) && (top < c_bot)) {
          
          if (li_tag.hasClass("active")) {
            return false;
          }
          else {
            li_tag.siblings().andSelf().removeClass("active");
            li_tag.addClass("active");
            $(".menu ul li.active a").slideToPos();  
        	}
        }
      });
    });
	
	}); // end of document ready
})(jQuery); // end of jQuery name space