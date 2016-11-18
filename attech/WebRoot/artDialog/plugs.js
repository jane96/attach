/* artDialog plugs.js
 * Date: 2010-08-26
 */

/*
『备注』
1、Function 里面this指针将指向到对话框内部API；
2、除了API文档公开的内部API方法外，插件还可以使用：

	ui		保存着对话框DOM对象
	data	保存着传入的配置参数
	cache	插件临时变量缓存接口


『插件开发示例』
art.fn.dialog.plug['plugName'] = {
	template: '',			// 内容模板
	defaults: {},			// 默认配置参数
	init: function(){},		// 对话框初始化后执行的函数
	close: function(){}		// 关闭对话框执行的函数
};
*/

(function($){


// 嵌入式框架
// 支持同域自适应框架大小
// 支持加载动画
$.fn.dialog.plug.iframe =  {
	template: '<iframe id="<%=id%>content" src="<%=content%>" class="ui_content" frameborder="0" allowtransparency="true"></iframe>',
	init: function(){
		var api = this;
		api.data.effect = false;
		api.loading.on();
		api.cache.iframeLoad = function(){
			
			// 探测iframe内部是否可以被获取，通常只有跨域的下获取会失败
			if (api.data.width === 'auto' && api.data.height === 'auto') try{
				var doc = $.doc(api.ui.content[0].contentWindow);
				api.size(doc.width, doc.height);
			}catch (_){};
			
			// IE6、7获取iframe大小后才能使用百分百大小
			api.ui.content.css({
				'width': '100%',
				'height': '100%'
			});

			api.data.left === 'center' && api.data.top === 'center' && api.position('center','center');
			api.loading.off();
		};
		
		api.ui.content.bind('load', api.cache.iframeLoad);
	},
	close: function(){
		var iframe = this.ui.content;
		iframe.unbind('load', this.cache.iframeLoad);

		// 重要！需要重置iframe地址，否则下次出现的对话框在IE6、7无法聚焦input
		this.ui.content[0].src = 'javascript:false';
	}
};


// 图片
// 预加载获取图片大小
$.fn.dialog.plug.img = {
	template: '<img id="<%=id%>content" class="ui_content" width="80" height="60" />',
	defaults: {
		title: '图片查看'
	},
	init: function(){
		var api = this;
		
		api.data.effect = false;
		api.loading.on();

		// 图片预先加载
		var loadImg = function (url, fn){
			var img = new Image();
			img.src = url;
			if (img.complete){
				fn.call(img);
			}else{
				img.onload = function(){
					fn.call(img);
				};
			};
		};

		var fn = function(){
			var width = this.width,
				height = this.height;

			if (api.data.width === 'auto' && api.data.height === 'auto') api.size(width, height, function(){
				var img = api.ui.content[0];
				img.src = api.data.content;
				img.width = width;
				img.height = height;
			});

			api.data.left === 'center' && api.data.top === 'center' && api.position('center', 'center');

			api.loading.off();
		};
		
		loadImg(api.data.content, fn);
	}
};

// ajax
$.fn.dialog.plug.ajax = {
	template: '<div id="<%=id%>content" class="aui_content"><div class="aui_noContent"></div></div>',
	init: function(){
		var api = this;
		
		api.data.effect = false;
		api.loading.on();
		var ajax = window.XMLHttpRequest ?
			new XMLHttpRequest() :
			new ActiveXObject('Microsoft.XMLHTTP');
			
		api.cache.ajaxLoad = ajax.onreadystatechange = function(){
			if(ajax.readyState == 4 && ajax.status == 200){
				var content = ajax.responseText;
				
				api.ui.content[0].innerHTML = content;
				api.data.left === 'center' && api.data.top === 'center' && api.position('center', 'center');

				api.loading.off();
			};
		};
		ajax.open('GET', api.data.content, 1);
		ajax.send(null);
	},
	close: function(){
		this.cache.ajaxLoad = null;
	}
};


})(art);



// 给jQuery增加"artDialog"插件
/*(function($){
	if (!$) return;
	
	$.fn.artDialog = function(options, yesFn, noFn){
		this.each(function(){		
			return $(this).artDialog(options, yesFn, noFn);
		});
	};
	$.extend({
		artDialog : art().dialog
	});
})(window.jQuery);*/
