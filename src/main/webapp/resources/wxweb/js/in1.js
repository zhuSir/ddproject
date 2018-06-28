$(function(){			 
// 			 	 var v=$('.main input').val();
// 			 	 if(!v){
//			      $('.main button').attr('disabled', true) 
//			      $('.main button').click(function(){
//			   				$('.main button').css('background','#D0D0D0');
//			   				$(this).css('background','#ff8100');
//			   			})
//			    }else{
//			     $('.main button').attr('disabled', false)
//			      $('.main button').css('background','#D0D0D0');
//			    }
			　　　　$("input[type='text']").each(function(){  //遍历input标签，判断是否有内容未填写
			　　　　　　var vl=$(this).val();
			　　　　　　if(vl==""){
			　　　　　　　　$('.main button').attr('disabled', true);
			      	   $('.main button').css('background','#D0D0D0');
			　　　　　　}
					else{
						$('.main button').attr('disabled', false); 
			      		$('.main button').click(function(){
			   				$('.main button').css('background','#D0D0D0');
			   				$(this).css('background','#ff8100');
			   			})
					}
			　　　　});
   		})