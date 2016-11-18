/* License: MIT.
 * Copyright (C) 2013, 2014, 2015, Uri Shaked.
 */

'use strict';

module.exports = function (config) {
	config.set({
		basePath: '',
		frameworks: ['jasmine'],
		logLevel: config.LOG_INFO,
		browsers: ['PhantomJS'],
		autoWatch: true,
		reporters: ['dots', 'coverage'],
		files: [
			'plugins/angular/angular.js',
			'plugins/moment/moment.js',
			'plugins/moment/{locale,lang}/fr.js',
			'plugins/moment-timezone/moment-timezone.js',
			'angular-moment.js',

			// angular-mocks defines a global variable named 'module' which confuses moment-timezone.js.
			// Therefore, it must be included after moment-timezone.js.
			'plugins/angular-mocks/angular-mocks.js',

			'tests.js'
		],
		preprocessors: {
			'angular-moment.js': 'coverage'
		},
		coverageReporter: {
			type: 'lcov',
			dir: 'coverage/'
		}
	});
};
