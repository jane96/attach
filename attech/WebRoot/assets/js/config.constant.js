'use strict';

/**
 * Config constant
 */
app.constant('APP_MEDIAQUERY', {
    'desktopXL': 1200,
    'desktop': 992,
    'tablet': 768,
    'mobile': 480
});
app.constant('JS_REQUIRES', {
    //*** Scripts
    scripts: {
        //*** Javascript Plugins
        'modernizr': ['assets/plugins/components-modernizr/modernizr.js'],
        'moment': ['assets/plugins/moment/min/moment.min.js'],
        'spin': 'assets/plugins/spin.js/spin.js',

        //*** jQuery Plugins
        'perfect-scrollbar-plugin': ['assets/plugins/perfect-scrollbar/js/min/perfect-scrollbar.jquery.min.js', 'assets/plugins/perfect-scrollbar/css/perfect-scrollbar.min.css'],
        'ladda': ['assets/plugins/ladda/dist/ladda.min.js', 'assets/plugins/ladda/dist/ladda-themeless.min.css'],
        'sweet-alert': ['assets/plugins/sweetalert/lib/sweet-alert.min.js', 'assets/plugins/sweetalert/lib/sweet-alert.css'],
        'chartjs': 'assets/plugins/chartjs/Chart.min.js',
        'jquery-sparkline': 'assets/plugins/jquery.sparkline.build/dist/jquery.sparkline.min.js',
        'ckeditor-plugin': 'assets/plugins/ckeditor/ckeditor.js',
        'angular-dragdrop': ['assets/plugins/angular/angular-dragdrop.min.js','static/jquery/jquery-ui.js'],
        'jquery-nestable-plugin': ['assets/plugins/jquery-nestable/jquery.nestable.js'],
        'touchspin-plugin': ['assets/plugins/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.js', 'assets/plugins/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.css'],

        //*** Controllers
        'dashboardCtrl': 'assets/js/controllers/dashboardCtrl.js',
        'iconsCtrl': 'assets/js/controllers/iconsCtrl.js',
        'vAccordionCtrl': 'assets/js/controllers/vAccordionCtrl.js',
        'ckeditorCtrl': 'assets/js/controllers/ckeditorCtrl.js',
        'laddaCtrl': 'assets/js/controllers/laddaCtrl.js',
        'ngTableCtrl': 'assets/js/controllers/ngTableCtrl.js',
        'cropCtrl': 'assets/js/controllers/cropCtrl.js',
        'asideCtrl': 'assets/js/controllers/asideCtrl.js',
        'toasterCtrl': 'assets/js/controllers/toasterCtrl.js',
        'sweetAlertCtrl': 'assets/js/controllers/sweetAlertCtrl.js',
        'mapsCtrl': 'assets/js/controllers/mapsCtrl.js',
        'chartsCtrl': 'assets/js/controllers/chartsCtrl.js',
        'calendarCtrl': 'assets/js/controllers/calendarCtrl.js',
        'nestableCtrl': 'assets/js/controllers/nestableCtrl.js',
        'validationCtrl': ['assets/js/controllers/validationCtrl.js'],
        'userCtrl': ['assets/js/controllers/userCtrl.js'],
        'selectCtrl': 'assets/js/controllers/selectCtrl.js',
        'wizardCtrl': 'assets/js/controllers/wizardCtrl.js',
        'uploadCtrl': 'assets/js/controllers/uploadCtrl.js',
        'treeCtrl': 'assets/js/controllers/treeCtrl.js',
        'inboxCtrl': 'assets/js/controllers/inboxCtrl.js',
        'noticeInboxCtrl':'assets/js/controllers/message/noticeInboxCtrl.js',
        'noticeNavBarCtrl':'assets/js/controllers/message/noticeNavBarCtrl.js',
        'alarmInboxCtrl':'assets/js/controllers/message/alarmInboxCtrl.js',
        'alarmNavBarCtrl':'assets/js/controllers/message/alarmNavBarCtrl.js',
        'xeditableCtrl': 'assets/js/controllers/xeditableCtrl.js',
        'chatCtrl': 'assets/js/controllers/chatCtrl.js',
        'uimapCtrl': 'pages/baidumap/mapCtrl.js',
        'myUploadCtrl': 'pages/upload/uploadCtrl.js',
        'myFromUploadCtrl': 'pages/upload/uploadForm.js',
        'myFromCreateCtrl': ['pages/form/fromCreateCtrl.js','assets/plugins/formbuild/css/main.css'],
        'myFromViewCtrl': ['pages/form/fromViewCtrl.js','assets/plugins/formbuild/css/main.css'],
        'paramService': 'assets/js/service/paramService.js',
        'gpsService': 'assets/js/service/gpsService.js',
        'taskFormViewCtrl': ['pages/task/js/fromTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        'taskFormDetailViewCtrl': ['pages/task/js/fromDetailTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        'workFormDetailViewCtrl': ['pages/task/js/workFromTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        'taskOverFormDetailViewCtrl': ['pages/task/js/fromOverDetailTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        'approvalFormViewCtrl': ['pages/task/js/approvalFromTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        'approvalFormDetailViewCtrl': ['pages/task/js/approvalDetailFromTaskCtrl.js','assets/plugins/formbuild/css/main.css'],
        //*** Filters
        'htmlToPlaintext': 'assets/js/filters/htmlToPlaintext.js'
    },
    //*** angularJS Modules
    modules: [{
        name: 'angularMoment',
        files: ['assets/plugins/angular-moment/angular-moment.min.js']
    }, {
        name: 'toaster',
        files: ['assets/plugins/AngularJS-Toaster/toaster.js', 'assets/plugins/AngularJS-Toaster/toaster.css']
    }, {
        name: 'angularBootstrapNavTree',
        files: ['assets/plugins/angular-bootstrap-nav-tree/dist/abn_tree_directive.js', 'assets/plugins/angular-bootstrap-nav-tree/dist/abn_tree.css']
    }, {
        name: 'angular-ladda',
        files: ['assets/plugins/angular-ladda/dist/angular-ladda.min.js']
    }, {
        name: 'ngTable',
        files: ['assets/plugins/ng-table/dist/ng-table.min.js', 'assets/plugins/ng-table/dist/ng-table.min.css']
    }, {
        name: 'ui.select',
        files: ['assets/plugins/angular-ui-select/dist/select.min.js', 'assets/plugins/angular-ui-select/dist/select.min.css', 'assets/plugins/select2/dist/css/select2.min.css', 'assets/plugins/select2-bootstrap-css/select2-bootstrap.min.css', 'assets/plugins/selectize/dist/css/selectize.bootstrap3.css']
    }, {
        name: 'ui.mask',
        files: ['assets/plugins/angular-ui-utils/mask.min.js']
    }, {
        name: 'ngImgCrop',
        files: ['assets/plugins/ngImgCrop/compile/minified/ng-img-crop.js', 'assets/plugins/ngImgCrop/compile/minified/ng-img-crop.css']
    }, {
        name: 'angularFileUpload',
        files: ['assets/plugins/angular-file-upload/angular-file-upload.min.js']
    }, {
        name: 'ngAside',
        files: ['assets/plugins/angular-aside/dist/js/angular-aside.min.js', 'assets/plugins/angular-aside/dist/css/angular-aside.min.css']
    }, {
        name: 'truncate',
        files: ['assets/plugins/angular-truncate/src/truncate.js']
    }, {
        name: 'oitozero.ngSweetAlert',
        files: ['assets/plugins/angular-sweetalert-promised/SweetAlert.min.js']
    }, {
        name: 'monospaced.elastic',
        files: ['assets/plugins/angular-elastic/elastic.js']
    }, {
        name: 'ngMap',
        files: ['assets/plugins/ngmap/build/scripts/ng-map.min.js']
    }, {
        name: 'tc.chartjs',
        files: ['assets/plugins/tc-angular-chartjs/dist/tc-angular-chartjs.min.js']
    }, {
        name: 'flow',
        files: ['assets/plugins/ng-flow/dist/ng-flow-standalone.min.js']
    }, {
        name: 'uiSwitch',
        files: ['assets/plugins/angular-ui-switch/angular-ui-switch.min.js', 'assets/plugins/angular-ui-switch/angular-ui-switch.min.css']
    }, {
        name: 'ckeditor',
        files: ['assets/plugins/angular-ckeditor/angular-ckeditor.min.js']
    }, {
        name: 'mwl.calendar',
        files: ['assets/plugins/angular-bootstrap-calendar/dist/js/angular-bootstrap-calendar.js', 'assets/plugins/angular-bootstrap-calendar/dist/js/angular-bootstrap-calendar-tpls.js', 'assets/plugins/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css']
    }, {
        name: 'ng-nestable',
        files: ['assets/plugins/ng-nestable/src/angular-nestable.js']
    }, {
        name: 'vAccordion',
        files: ['assets/plugins/v-accordion/dist/v-accordion.min.js', 'assets/plugins/v-accordion/dist/v-accordion.min.css']
    }, {
        name: 'xeditable',
        files: ['assets/plugins/angular-xeditable/dist/js/xeditable.min.js', 'assets/plugins/angular-xeditable/dist/css/xeditable.css', 'assets/js/config/config-xeditable.js']
    }, {
    	name: 'ztree',
    	files: ['assets/plugins/ztree_v3/js/jquery.ztree.all-3.5.js', 'assets/plugins/ztree_v3/css/zTreeStyle/zTreeStyle.css']
    }, {
    	name: 'ngFileUpload',
    	files: ['assets/plugins/ng-file-upload/dist/ng-file-upload-all.min.js','assets/js/service/uploadService.js']
    }, {
    	name: 'formBuild',
    	files: ['assets/plugins/formbuild/js/formService.js','assets/plugins/formbuild/js/formDirective.js']
    }, {
        name: 'checklist-model',
        files: ['assets/plugins/checklist-model/checklist-model.js']
    },{
    	name: 'artDialog',
    	files: ['artDialog/artDialog.min.js','artDialog/plugs.js']
    },{
    	name: 'public_fenye',
    	files: ['static/jstool/public.js']
    },{
    	name: 'bootstrap_xin',
    	files: ['static/login/resource/js/jquery.min.js','static/login/resource/js/bootstrap.min.js']
    }]
});
