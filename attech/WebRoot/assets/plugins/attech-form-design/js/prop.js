function propsave(editing){
	if (editing) {
		formdata.propKey = oldData.id;
		data.id = formdata.propKey;
        $.ajax({
            type: "post",
            url: "/a/form/template/propupdate",
            data: formdata,
            dataType: "json",
            success: function(response){
            	if(response.status == "success"){
            		fc.updateContorl(editing, data);
            		delete UE.plugins['formcontrols'].editing
                	dialog.close();
            	}else{
            		alert("系统错误，保存失败");
            	}
            },
            error: function(){
            	alert("请检查网络，保存失败");
            }
        });
	} else {
		formdata.propKey = Math.uuid(32);
		data.id = formdata.propKey;
		$.ajax({
			type : "post",
			url : "/a/form/template/propsave",
			data : formdata,
			dataType : "json",
			success : function(response) {
				if (response.status == "success") {
					fc.addControl(data);
					dialog.close();
				} else {
					alert("系统错误，保存失败");
				}
			},
			error : function() {
				alert("请检查网络，保存失败");
			}
		});
	}
}