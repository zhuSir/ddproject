$(function(){
	$("#top").click(function() {
			      $("html,body").animate({scrollTop:0}, 500);
			  }); 
	 $(window).scroll(function() {
              if($(window).scrollTop() >= 100) {
                 $('#top').show();
              }else{  
                 $('#top').hide();
              }
           })
})
