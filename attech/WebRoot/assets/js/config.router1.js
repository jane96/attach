'use strict';

/**
 * Config for the router
 */
app.config(['$stateProvider', '$urlRouterProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$ocLazyLoadProvider', 'JS_REQUIRES',
function ($stateProvider, $urlRouterProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $ocLazyLoadProvider, jsRequires) {

    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.directive;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;
    app.value = $provide.value;

    // LAZY MODULES

    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: jsRequires.modules
    });

    // APPLICATION ROUTES
    // -----------------------------------
    // For any unmatched url, redirect to /app/dashboard
    $urlRouterProvider.otherwise("/app/dashboard");
    //

    //jQuery.get("a/enterprises/enterprises/findEnterpriseByUser").success( function(response) {
    	//alert(response);
   // });	
    
    // Set up the states
    $stateProvider.state('app', {
        url: "/app",
        templateUrl: "pages/app.html",
        resolve: loadSequence('modernizr', 'moment', 'angularMoment', 'uiSwitch', 'perfect-scrollbar-plugin', 'toaster', 'ngAside', 'vAccordion', 'sweet-alert', 'chartjs', 'tc.chartjs', 'oitozero.ngSweetAlert', 'chatCtrl','noticeNavBarCtrl','noticeInboxCtrl','alarmNavBarCtrl','alarmInboxCtrl',"truncate"),
        abstract: true
    }).state('app.dashboard',   {
        url: "/dashboard",
        templateUrl: "pages/dashboard.html",
        resolve: loadSequence('jquery-sparkline', 'dashboardCtrl'),
        title: '我的工作台',
        ncyBreadcrumb: {
            label: '我的工作台'
        }
    })
    <#list menus as menu>
    <#if menu.href??>
    	.state('app.menu${(menu.permission!menu.id)?replace(':','_')}', {
	        url: "<#if menu.target??>${menu.target}</#if><#if !(menu.target??)>/${menu.href}</#if>",
	        templateUrl: "<#if menu.href??>${menu.href}</#if>",
	        title: "${menu.name}",
	        resolve: ${menu.plugins!"loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl')"}	
		})
    </#if>
    </#list>
    .state('app.mybaidumap', {
        url: "/baidumap",
        templateUrl: "pages/baidumap/index.html",
        resolve: loadSequence('uimapCtrl','ztree','public_fenye'),
        title: "baidumap"
    }).state('app.myupload', {
        url: "/upload",
        templateUrl: "pages/upload/index.html",
        resolve: loadSequence('angularFileUpload','myUploadCtrl'),
        title: "上传文件-示例"
     })
   .state('app.myformupload', {
        url: "/formupload",
        templateUrl: "pages/upload/form.html",
        resolve: loadSequence('ngFileUpload','ui.select','myFromUploadCtrl'),
        title: "上传文件-示例"
     })
     .state('app.gpsexp', {
    	 url: "/gpsexp",
    	 templateUrl: "pages/upload/33.html",
    	 //resolve: loadSequence('ngFileUpload','ui.select','myFromUploadCtrl'),
    	 title: "gps-示例"
     })
	.state('app.pages.users', {
        url: '/users',
        templateUrl: "pages/pages_users.html",
        title: '用户管理',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('ngTable','angularBootstrapNavTree')
     })
     .state('app.pages.group', {
         url: '/group',
         templateUrl: "pages/pages_group.html",
         title: '群组管理',
         icon: 'ti-layout-media-left-alt',
 		resolve: loadSequence('ngTable','angularBootstrapNavTree') 
      }).state('app.pages.flowdesign3', {
         url: '/flowDesign',
         templateUrl: "pages/Flowdesign3/index.html",
         title: '流程设计',
         icon: 'ti-layout-media-left-alt',
 		resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl') 
      }).state('app.pages.exampleList', {
          url: '/exampleList/:bbb',
          templateUrl: "pages/example/exampleList.html",
          title:'表单实例查询',
          icon: 'ti-layout-media-left-alt',
  		resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl') 
       })
	.state('app.workflow', {
        url: '/workflow',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'UI Elements',
        ncyBreadcrumb: {
            label: 'UI Elements'
        }       
    })
    .state('app.workflow.flowList', {
        url: '/flowList',
        templateUrl: "pages/workflow/flowList.html",
        title: '待办列表',
        icon: 'ti-layout-media-left-alt',
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl')
     })
    .state('app.workflow.flowStart', {
        url: '/flowStart',
        templateUrl: "pages/workflow/flowStart.html",
        title: '发起流程',
        icon: 'ti-layout-media-left-alt',
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ngFileUpload','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl', 'angularFileUpload', 'uploadCtrl')
     })
     .state('app.workflow.flowDeal', {
        url: '/flowDeal',
        templateUrl: "pages/workflow/flowDeal.html",
        title: '流程处理',
        icon: 'ti-layout-media-left-alt',
        params: {
        	'flowObj': null
        },
        resolve: loadSequence('flow','ngFileUpload','ui.select', 'ngTable','angularBootstrapNavTree', 'treeCtrl', 'angularFileUpload', 'uploadCtrl')
     })
	.state('app.workflow.formDesign', {
        url: '/formDesign',
        templateUrl: "pages/workflow/formDesign/list.html",
        title: '表单设计',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('ngTable','angularBootstrapNavTree') 
     })
     .state('app.workflow.formSave', {
        url: '/formSave',
        templateUrl: "pages/workflow/formDesign/formSave.html",
        title: '数据表单',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('ngTable') 
     })
     .state('app.ui', {
        url: '/ui',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'UI Elements',
        ncyBreadcrumb: {
            label: 'UI Elements'
        }       
    }).state('app.ui.elements', {
        url: '/elements',
        templateUrl: "pages/ui_elements.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'Elements'
        }
    }).state('app.ui.buttons', {
        url: '/buttons',
        templateUrl: "pages/ui_buttons.html",
        title: 'Buttons',
        resolve: loadSequence('spin', 'ladda', 'angular-ladda', 'laddaCtrl'),
        ncyBreadcrumb: {
            label: 'Buttons'
        }
    }).state('app.ui.links', {
        url: '/links',
        templateUrl: "pages/ui_links.html",
        title: 'Link Effects',
        ncyBreadcrumb: {
            label: 'Link Effects'
        }
    }).state('app.ui.icons', {
        url: '/icons',
        templateUrl: "pages/ui_icons.html",
        title: 'Font Awesome Icons',
        ncyBreadcrumb: {
            label: 'Font Awesome Icons'
        },
        resolve: loadSequence('iconsCtrl')
    }).state('app.ui.lineicons', {
        url: '/line-icons',
        templateUrl: "pages/ui_line_icons.html",
        title: 'Linear Icons',
        ncyBreadcrumb: {
            label: 'Linear Icons'
        },
        resolve: loadSequence('iconsCtrl')
    }).state('app.ui.modals', {
        url: '/modals',
        templateUrl: "pages/ui_modals.html",
        title: 'Modals',
        ncyBreadcrumb: {
            label: 'Modals'
        },
        resolve: loadSequence('asideCtrl')
    }).state('app.ui.toggle', {
        url: '/toggle',
        templateUrl: "pages/ui_toggle.html",
        title: 'Toggle',
        ncyBreadcrumb: {
            label: 'Toggle'
        }
    }).state('app.ui.tabs_accordions', {
        url: '/accordions',
        templateUrl: "pages/ui_tabs_accordions.html",
        title: "Tabs & Accordions",
        ncyBreadcrumb: {
            label: 'Tabs & Accordions'
        },
        resolve: loadSequence('vAccordionCtrl')
    }).state('app.ui.panels', {
        url: '/panels',
        templateUrl: "pages/ui_panels.html",
        title: 'Panels',
        ncyBreadcrumb: {
            label: 'Panels'
        }
    }).state('app.ui.notifications', {
        url: '/notifications',
        templateUrl: "pages/ui_notifications.html",
        title: 'Notifications',
        ncyBreadcrumb: {
            label: 'Notifications'
        },
        resolve: loadSequence('toasterCtrl', 'sweetAlertCtrl')
    }).state('app.ui.treeview', {
        url: '/treeview',
        templateUrl: "pages/ui_tree.html",
        title: 'TreeView',
        ncyBreadcrumb: {
            label: 'Treeview'
        },
        resolve: loadSequence('angularBootstrapNavTree', 'treeCtrl')
    }).state('app.ui.media', {
        url: '/media',
        templateUrl: "pages/ui_media.html",
        title: 'Media',
        ncyBreadcrumb: {
            label: 'Media'
        }
    }).state('app.ui.nestable', {
        url: '/nestable2',
        templateUrl: "pages/ui_nestable.html",
        title: 'Nestable List',
        ncyBreadcrumb: {
            label: 'Nestable List'
        },
        resolve: loadSequence('jquery-nestable-plugin', 'ng-nestable', 'nestableCtrl')
    }).state('app.ui.typography', {
        url: '/typography',
        templateUrl: "pages/ui_typography.html",
        title: 'Typography',
        ncyBreadcrumb: {
            label: 'Typography'
        }
    }).state('app.table', {
        url: '/table',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Tables',
        ncyBreadcrumb: {
            label: 'Tables'
        }
    }).state('app.table.basic', {
        url: '/basic',
        templateUrl: "pages/table_basic.html",
        title: 'Basic Tables',
        ncyBreadcrumb: {
            label: 'Basic'
        }
    }).state('app.table.responsive', {
        url: '/responsive',
        templateUrl: "pages/table_responsive.html",
        title: 'Responsive Tables',
        ncyBreadcrumb: {
            label: 'Responsive'
        }
    }).state('app.table.data', {
        url: '/data',
        templateUrl: "pages/table_data.html",
        title: 'ngTable',
        ncyBreadcrumb: {
            label: 'ngTable'
        },
        resolve: loadSequence('ngTable', 'ngTableCtrl')
    }).state('app.table.export', {
        url: '/export',
        templateUrl: "pages/table_export.html",
        title: 'Table'
    }).state('app.form', {
        url: '/form',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Forms',
        ncyBreadcrumb: {
            label: 'Forms'
        }
    }).state('app.form.elements', {
        url: '/elements',
        templateUrl: "pages/form_elements.html",
        title: 'Forms Elements',
        ncyBreadcrumb: {
            label: 'Elements'
        },
        resolve: loadSequence('ui.select', 'monospaced.elastic', 'ui.mask', 'touchspin-plugin', 'selectCtrl','angularBootstrapNavTree', 'treeCtrl','jquery-nestable-plugin')
    }).state('app.form.xeditable', {
        url: '/xeditable',
        templateUrl: "pages/form_xeditable.html",
        title: 'Angular X-Editable',
        ncyBreadcrumb: {
            label: 'X-Editable'
        },
        resolve: loadSequence('xeditable', 'checklist-model', 'xeditableCtrl')
    }).state('app.form.texteditor', {
        url: '/editor',
        templateUrl: "pages/form_text_editor.html",
        title: 'Text Editor',
        ncyBreadcrumb: {
            label: 'Text Editor'
        },
        resolve: loadSequence('ckeditor-plugin', 'ckeditor', 'ckeditorCtrl')
    }).state('app.form.wizard', {
        url: '/wizard',
        templateUrl: "pages/form_wizard.html",
        title: 'Form Wizard',
        ncyBreadcrumb: {
            label: 'Wizard'
        },
        resolve: loadSequence('wizardCtrl')
    }).state('app.form.validation', {
        url: '/validation',
        templateUrl: "pages/form_validation.html",
        title: 'Form Validation',
        ncyBreadcrumb: {
            label: 'Validation'
        },
        resolve: loadSequence('validationCtrl')
    }).state('app.form.cropping', {
        url: '/image-cropping',
        templateUrl: "pages/form_image_cropping.html",
        title: 'Image Cropping',
        ncyBreadcrumb: {
            label: 'Image Cropping'
        },
        resolve: loadSequence('ngImgCrop', 'cropCtrl')
    }).state('app.form.upload', {
        url: '/file-upload',
        templateUrl: "pages/form_file_upload.html",
        title: 'Multiple File Upload',
        ncyBreadcrumb: {
            label: 'File Upload'
        },
        resolve: loadSequence('angularFileUpload', 'uploadCtrl')
    }).state('app.pages', {
        url: '/pages',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Pages',
        ncyBreadcrumb: {
            label: 'Pages'
        }
    }).state('app.pages.user', {
        url: '/user',
        templateUrl: "pages/pages_user_profile.html",
        title: 'User Profile',
        ncyBreadcrumb: {
            label: 'User Profile'
        },
        resolve: loadSequence('flow', 'userCtrl')
    }).state('app.pages.invoice', {
        url: '/invoice',
        templateUrl: "pages/pages_invoice.html",
        title: 'Invoice',
        ncyBreadcrumb: {
            label: 'Invoice'
        }
    }).state('app.pages.timeline', {
        url: '/timeline',
        templateUrl: "pages/pages_timeline.html",
        title: 'Timeline',
        ncyBreadcrumb: {
            label: 'Timeline'
        },
        resolve: loadSequence('ngMap')
    }).state('app.pages.calendar', {
        url: '/calendar',
        templateUrl: "pages/pages_calendar.html",
        title: 'Calendar',
        ncyBreadcrumb: {
            label: 'Calendar'
        },
        resolve: loadSequence('moment', 'mwl.calendar', 'calendarCtrl')
    }).state('app.pages.messages', {
        url: '/messages',
        templateUrl: "pages/pages_messages.html",
        resolve: loadSequence('truncate', 'htmlToPlaintext', 'inboxCtrl')
    }).state('app.pages.messages.inbox', {
        url: '/inbox/:inboxID',
        templateUrl: "pages/pages_inbox.html",
        controller: 'ViewMessageCrtl'
    }).state('app.pages.blank', {
        url: '/blank',
        templateUrl: "pages/pages_blank_page.html",
        ncyBreadcrumb: {
            label: 'Starter Page'
        }
	}).state('app.pages.setting', {
        url: '/setting',
        templateUrl: "pages/pages_setting.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'Setting'
        }
    }).state('app.pages.announcement', {
        url: '/announcement',
        templateUrl: "pages/pages_announcement.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'Announcement'
        }
     }).state('app.pages.file', {
        url: '/file',
        templateUrl: "pages/pages_file.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'File'
        },
//        resolve: loadSequence('angularFileUpload','myUploadCtrl')
    }).state('app.pages.user_index', {
        url: '/user_index',
        templateUrl: "pages/pages_user_index.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'pages/user_index'
        }
//      resolve: loadSequence('asideCtrl'),
     }).state('app.pages.treedicts', {
        url: '/treedicts',
        templateUrl: "pages/pages_treedicts.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl'),
        ncyBreadcrumb: {
            label: 'pages/treedicts'
        }
     }).state('app.pages.propenum', {
        url: '/propenum',
        templateUrl: "pages/form/prop_enum.html",
        title: '表单字段',
        icon: 'ti-layout-media-left-alt',
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl'),
        ncyBreadcrumb: {
            label: 'pages/propenum'
        }
     }).state('app.pages.common', {
        url: '/common',
        templateUrl: "pages/pages_common.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'pages/common'
        },
        resolve: loadSequence('asideCtrl','ngTable')
     })//-------待办已办
    .state('app.pages.handle', {
        url: '/handle',
        templateUrl: "pages/pages_handle.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'pages/handle'
        },
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl')
     }).state('app.pages.handledetails', {
        url: '/handledetails',
        templateUrl: "pages/pages_handledetails.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'pages/handledetails'
        },
        resolve: loadSequence('flow', 'userCtrl', 'selectCtrl','ui.select', 'ngTable','asideCtrl','angularBootstrapNavTree', 'treeCtrl')
    }).state('app.pages.post', {
        url: '/post',
        templateUrl: "pages/pages_post.html",
        title: 'User Profile',
        ncyBreadcrumb: {
            label: 'User Profile'
        },
        resolve: loadSequence('flow', 'userCtrl','ngTable','angularBootstrapNavTree')
     })
    //-------修改密码
    .state('app.updatePwd', {
        url: '/updatePwd',
        templateUrl: "pages/user/updatePwd.html",
        title: '修改密码'
    })//-------消息管理相关
     .state('app.noticeInbox', {
        url: '/noticeInbox',
        templateUrl: "pages/message/notice_inbox.html",
        title: '通知收件箱',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('truncate', 'htmlToPlaintext','noticeInboxCtrl') 
	
    })//-----收件箱中通知详情
     .state('app.noticeInbox.inbox', {
        url: '/inbox/:inboxID/:receiverId',
        templateUrl: "pages/message/notice_inbox_detail.html",
        controller: 'ViewMessageCrtl'
        
    }).state('app.alarmInbox', {
        url: '/alarmInbox',
        templateUrl: "pages/message/alarm_inbox.html",
        title: '告警记录',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('truncate', 'htmlToPlaintext','alarmInboxCtrl') 
	
    })//-----告警记录详情
     .state('app.alarmInbox.inbox', {
        url: '/inbox/:inboxID/:alarmId',
        templateUrl: "pages/message/alarm_inbox_detail.html",
        controller: 'ViewAlarmCrtl'
        
    })//-----已经变成动态菜单
    .state('app.utilities', {
        url: '/utilities',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Utilities',
        ncyBreadcrumb: {
            label: 'Utilities'
        }
    }).state('app.utilities.search', {
        url: '/search',
        templateUrl: "pages/utility_search_result.html",
        title: 'Search Results',
        ncyBreadcrumb: {
            label: 'Search Results'
        }
    }).state('app.utilities.pricing', {
        url: '/pricing',
        templateUrl: "pages/utility_pricing_table.html",
        title: 'Pricing Table',
        ncyBreadcrumb: {
            label: 'Pricing Table'
        }
    }).state('app.maps', {
        url: "/maps",
        templateUrl: "pages/maps.html",
        resolve: loadSequence('ngMap', 'mapsCtrl'),
        title: "Maps",
        ncyBreadcrumb: {
            label: 'Maps'
        }
    }).state('app.charts', {
        url: "/charts",
        templateUrl: "pages/charts.html",
        resolve: loadSequence('chartjs', 'tc.chartjs', 'chartsCtrl'),
        title: "Charts",
        ncyBreadcrumb: {
            label: 'Charts'
        }
    }).state('app.documentation', {
        url: "/documentation",
        templateUrl: "pages/documentation.html",
        title: "Documentation",
        ncyBreadcrumb: {
            label: 'Documentation'
        }
    }).state('error', {
        url: '/error',
        template: '<div ui-view class="fade-in-up"></div>'
    }).state('error.404', {
        url: '/404',
        templateUrl: "pages/utility_404.html",
    }).state('error.500', {
        url: '/500',
        templateUrl: "pages/utility_500.html",
    })

	// Login routes

	.state('login', {
	    url: '/login',
	    template: '<div ui-view class="fade-in-right-big smooth"></div>',
	    abstract: true
	}).state('login.signin', {
	    url: '/signin',
	    templateUrl: "pages/login_login.html"
	}).state('login.forgot', {
	    url: '/forgot',
	    templateUrl: "pages/login_forgot.html"
	}).state('login.registration', {
	    url: '/registration',
	    templateUrl: "pages/login_registration.html"
	}).state('login.lockscreen', {
	    url: '/lock',
	    templateUrl: "pages/login_lock_screen.html"
	})
	
	
	
	// 办公管理
	.state('app.pages.draftTitle', {
	    url: '/draftTitle',
	    templateUrl: "pages/doc/draftTitle.html",
        title: '底稿标题',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow','ui.select'),
        icon: 'ti-layout-media-left-alt'
	})
	.state('app.pages.seal', {
	    url: '/seal',
	    templateUrl: "pages/doc/seal.html",
        title: '个人签章',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow'),
        icon: 'ti-layout-media-left-alt'
	})//统计详情
	.state('app.menu38fc3de9eac6448b8be6ae0bd3d82fff', {
	        url: "/taskList/5/:taskId?",
	        templateUrl: "pages/task/view.html",
	        title: "任务详情",
	        resolve: loadSequence('ui.select', 'monospaced.elastic', 'ui.mask', 'touchspin-plugin', 'selectCtrl','ngTable','userCtrl','flow','selectCtrl','asideCtrl','angularBootstrapNavTree', 'treeCtrl','paramService','formBuild','toaster','ngFileUpload','gpsService'),controller:function($scope,$stateParams){$scope.listType=5;$scope.taskId=$stateParams.taskId}	
		})
	
	.state('app.pages.publicSeal', {
	    url: '/publicSeal',
	    templateUrl: "pages/doc/publicSeal.html",
        title: '单位印章',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','ngFileUpload'),
        icon: 'ti-layout-media-left-alt'
	})
	
	.state('app.pages.redTemplate', {
	    url: '/redTemplate',
	    templateUrl: "pages/doc/redTemplate.html",
        title: '套红模板',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','ui.select','ngFileUpload'),
        icon: 'ti-layout-media-left-alt'
	})
	
	.state('app.pages.codeLetter', {
	    url: '/codeLetter',
	    templateUrl: "pages/doc/codeLetter.html",
        title: '代字管理',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow','ui.select'),
        icon: 'ti-layout-media-left-alt'
	})
    
	.state('app.pages.formControlManage', {
	    url: '/formControlManage',
	    templateUrl: "pages/form/formControlManage.html",
        title: '表单管理',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow','ui.select'),
        icon: 'ti-layout-media-left-alt'
	})
	.state('app.pages.buttonTest', {
	    url: '/buttonTest',
	    templateUrl: "pages/doc/buttonTest.html",
        title: '按钮操作',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow','ui.select'),
        icon: 'ti-layout-media-left-alt'
	})
	.state('app.pages.processMonitoring', {
	    url: '/processMonitoring',
	    templateUrl: "pages/processMonitoring.html",
        title: '流程监控',
        resolve: loadSequence('ngTable','angularBootstrapNavTree','userCtrl','flow','ui.select'),
        icon: 'ti-layout-media-left-alt'
	});

    // Generates a resolve object previously configured in constant.JS_REQUIRES (config.constant.js)
    function loadSequence() {
        var _args = arguments;
        return {
            deps: ['$ocLazyLoad', '$q',
			function ($ocLL, $q) {
			    var promise = $q.when(1);
			    for (var i = 0, len = _args.length; i < len; i++) {
			        promise = promiseThen(_args[i]);
			    }
			    return promise;

			    function promiseThen(_arg) {
			        if (typeof _arg == 'function')
			            return promise.then(_arg);
			        else
			            return promise.then(function () {
			                var nowLoad = requiredData(_arg);
			                if (!nowLoad)
			                    return $.error('Route resolve: Bad resource name [' + _arg + ']');
			                return $ocLL.load(nowLoad);
			            });
			    }

			    function requiredData(name) {
			        if (jsRequires.modules)
			            for (var m in jsRequires.modules)
			                if (jsRequires.modules[m].name && jsRequires.modules[m].name === name)
			                    return jsRequires.modules[m];
			        return jsRequires.scripts && jsRequires.scripts[name];
			    }
			}]
        };
    }
}]);