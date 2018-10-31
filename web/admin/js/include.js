$(function () {
	$.ajax({
		type:"GET",
		url:"../admin",
		data:{action:"loginStatus"},
		dataType:"json",
		success:function(ResultInfo){
			if(ResultInfo.flag){
				if(ResultInfo.data!=null){  	//有账户已经登陆
					$("#name").html(ResultInfo.data.name);
				}else{							//直接访问后台网站，没登陆的情况
				}
			}else{								//服务器炸裂
			}
		}
	})
});