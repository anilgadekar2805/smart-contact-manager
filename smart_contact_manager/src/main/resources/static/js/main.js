
const toggleSidebar = ()=>{

    if($('.sidebar').is(":visible")){
        
        $('.sidebar').css("display", "none");
        $('.content').css("margin-left", "1%");
  //      $('.content').css("padding-left", "18%");
    }else{
        $('.sidebar').css("display", "block");
        $('.content').css("margin-left", "18%");
//		$('.content').css("padding-left", "1%");
    }
}