'use strict';
/** 
  * controller for Validation Form example
*/
app.controller('ValidationCtrl', ["$scope", "$state", "$timeout", "SweetAlert", function ($scope, $state, $timeout, SweetAlert) {

    $scope.master = $scope.myModel;
    $scope.form = {
		
        submit: function (form) {
            var firstError = null;
            if (form.$invalid) {

                var field = null, firstError = null;
                for (field in form) {
                    if (field[0] != '$') {
                        if (firstError === null && !form[field].$valid) {
                            firstError = form[field].$name;
                        }

                        if (form[field].$pristine) {
                            form[field].$dirty = true;
                        }
                    }
                }

                angular.element('.ng-invalid[name=' + firstError + ']').focus();
               // SweetAlert.swal("请正确填写后，再提交", "Errors are marked with a red, dashed border!", "error");
                return;

            } else {
                SweetAlert.swal("温馨提示!", "提交成功!", "success");
                //your code for submit
            }

        },
        reset: function (form) {

            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);

        }
    };

}]);
