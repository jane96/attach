$(function() {
	// @Todo: 考虑是否定时保存编辑器数据至本地
	UE.dom.dtd.IC = UE.dom.dtd.ic = $.extend(UE.dom.dtd.div, { IC: 1})
	window.ue = UE.getEditor("editor", {
		initialFrameWidth: '100%',
		initialFrameHeight: '500',
		// minFrameWidth: "840",
		minFrameHeight: "500",
		focus: true,
		topOffset: 0,
		autoHeightEnabled: true,
		iframeCssUrl: Attech.app.g("ASSET_URL") + "/css/formeditor.css",
		toolbars: [UEDITOR_CONFIG.mode['full'][0].concat("|", "jsext", "cssext", "macro")],
		labelMap: {
			'jsext': Attech.l('WF.JS_EXT'),
			'cssext': Attech.l('WF.CSS_EXT'),
			'macro': Attech.l('WF.MACRO_EXT')
		}
		// 添加下面路径会导致弹窗样式变化，不添加下面路径时会出现css文件路径出错的问题
		// themePath: "../../../../../../static/js/lib/ueditor/themes/"
	});

	ue.ready(function() {
		// ue.document.createElement("ic");
		// 自定义标签 IC, IE8下需要引入html5shiv
		if($.browser.msie && $.browser.version === "8.0") {		
			ue.window.html5 = { elements: "ic" }
			U.loadFile(ue.document, {
				tag: "script",
				id: "html5",
				src:  Attech.app.g("ASSET_URL") + "/js/html5shiv.js"
			})
		}

		ue.document.body.className = "grid-container";
		// 新建控件事件(编辑控件)
		$(document).on("click", "ic", function() {
			var controlType = $.attr(this, "data-type");
			if (controlType) {
				ue.execCommand("ic" + controlType);
			}
		});

		// 设置光标至最后一行
		if(ue.getContent()){
			ue.setContent('<p></p>', true, true);
			ue.focus(true);
		}


		Ui.fillHeight('form_editor', $(window));

		window.onresize = function() {
			Ui.fillHeight('form_editor', $(window));
		}
		// 此处fire contentchange, 以触发autoHeight
		setTimeout(function() {
			ue.fireEvent("contentchange")
		}, 0)
	});
	ue.formid = Attech.app.getPageParam("formid");

	Attech.evt.add({
		/*
		 * 生成历史版本
		 * @returns {Boolean}*
		 */
		"saveVersion": function() {
			if (!ue.hasContents()) {
				alert(Attech.l('WF.EMPTY_FORM_CONTENT'));
				return false;
			}
			ue.sync();
			$('#form_design_op').val('version');
			$('#save_form').submit();
		},
		/**
		 * 查看历史版本
		 * @returns {undefined}
		 */
		"viewVersion": function() {
			var d = Ui.dialog({
				id: "d_version",
				title: Attech.l("HISTORICAL_EDITION"),
				init: function() {
					FormDesigner.formEdition.init();
				},
				cancel: true
			});
			$.get(Attech.app.url('workflow/formversion/index', {id: Attech.app.getPageParam("formid")}), function(res) {
				if (res.isSuccess) {
					// 用于生成历史版本选项
					FormDesigner.formEditionSelect.updateOptions(res.list);
					d.content(document.getElementById("dialog_edition"));
				}
			}, "json");
		},
		/**
		 * 预览表单
		 * @returns {undefined}
		 */
		"previewForm": function() {
			window.open(Attech.app.url('workflow/formtype/preview', {formid: Attech.app.getPageParam("formid")}));
		},
		/**
		 * 保存表单
		 * @returns {Boolean}
		 */
		"saveForm": function() {
			if (!ue.hasContents()) {
				alert(Attech.l('WF.EMPTY_FORM_CONTENT'));
				return false;
			}
			ue.sync();
			$('#save_form').submit();
		}
	});

});

