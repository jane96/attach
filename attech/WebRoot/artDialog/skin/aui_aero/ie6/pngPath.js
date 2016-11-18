// 修复IE6 png路径
// PNG在IE6使用需要使用CSS滤镜，而滤镜的路径是相对于HTML文档
// HTML文档所在的路径往往是不确定的，所以写入绝对路径是最好的办法
// 这里就是使用脚本自动写入绝对路径的方式修复这个问题
//
// 在文档header添加：
// <!--[if IE 6]><script src="skin/aero/ie6/pngPath.js"></script><![endif]-->

(function($){

	var url = function(name){
			return "_background:none;_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true,sizingMethod=crop ,src='" + $.dialog.defaults.path + '/skin/aui_aero/ie6/' + name + "');";
		},
		content = !-[1,] && !window.XMLHttpRequest ? '\
			.aui_aero .aui_left_top {' + url('aui_left_top.png') + '}\
			.aui_aero .aui_right_top {' +  url('aui_right_top.png') + '}\
			.aui_aero .aui_left_bottom {' + url('aui_left_bottom.png') + '}\
			.aui_aero .aui_right_bottom {' + url('aui_right_bottom.png') + '}\
			.aui_aero .aui_close {' + url('aui_close.png') + '}\
			.aui_aero .aui_top {' + url('aui_top.png') + '}\
			.aui_aero .aui_bottom {' + url('aui_bottom.png') + '}\
			.aui_aero .aui_left {' + url('aui_left.png') + '}\
			.aui_aero .aui_right {' + url('aui_right.png') + '}\
		' : '';

	$.addHeadCss(content);

})(art);