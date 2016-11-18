//@Debug
var Ibos = Ibos || {};
(function() {
	try {
        var allData = [];
        var localurl = window.location.protocol+"//"+window.location.host+"/";

        var ids = getUrlParam("getTaskRole");
        
        $.ajax({
            url:localurl+"/a/sys/dict/getPropTypeByIds",
            type:"GET",
            data:{ids:(ids == "" || ids == null)?"-1":ids},
            async:false,
            dataType:"json",
            success:function(data){
            	if(data != null && data.length > 0)
            	{
            		for(var i = 0 ;i<data.length;i++){
                        data[i].text=data[i].label;
                        data[i].name=data[i].label;
                        data[i].iconSkin="user";
                        data[i].type = "user";
                        data[i].enable = 1 ;
                        data[i].checked = true;
                        if(data[i].enable == 1){
                            allData.push(data[i]);
                        }
                    }
            	}

            }
        });
        
        
        
        
        
        
        
        
        
        

		var _filter = function(data, matcher){
			var results = [];
			if(!matcher || typeof matcher !== "function") {
				return data;
			}
			for (var i = 0, len = data.length; i < len; i++) {
				if(matcher(data[i])){
					results.push(data[i]);
				}
			}
			return results;
		}

		Ibos.data = {
			filter: function(matcher) {
                console.log(allData);
				return _filter(allData, matcher);
			},

			get: function() {
				var argu = arguments,
					matcher,
					ret = [];
				if(!argu.length || argu[0] == null) {
					return allData;
				}
				if(typeof argu[argu.length - 1] == "function") {
					matcher = argu[argu.length - 1];
					Array.prototype.pop.call(argu);
				}
				if(!argu.length && matcher) {
					return this.filter(matcher);
				} else {
					for(var i = 0; i < argu.length; i++) {
						var _ret = this.filter(function(data){
							return data && (data.type === argu[i] || data.iconSkin === argu[i]);
						})
						ret =  ret.concat(_ret);
					}
					return _filter(ret, matcher);
				}
			},

			getItem: function(/*id1, id2...*/) {
				var results = [],
					argu = arguments;

				for(var i = 0, len = argu.length; i < len; i++) {
					results = results.concat(this.filter(function(data){
						return data.id === argu[i];
					}))
				}

				return results;
			},

			includes: function(ins){
				return this.getItem.apply(this, ins);
			}
		}
	} catch(e) {
		throw new Error("(Ibos.data): 模板解析错误" )
	}
})();


function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}