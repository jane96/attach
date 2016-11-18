<section id="page-title">
    <div class="row">
        <div class="col-sm-8">
            <h3 class="mainTitle">${functionName}管理</h3>
        </div>
    </div>
</section>
<div ng-controller="${className}FormCtrl">
	<form role="form" id="form1" name="form1" ng-submit="submit()" class="form-horizontal" novalidate>
		<fieldset>
			<div class="row">
				<#list fields as field>
				<div class="row" ng-class="{'has-error':form1.name.$dirty && form1.name.$invalid, 'has-success':form1.name.$valid}">
					<label class="control-label col-md-3 col-sm-3 textRight">
						${field.remark}
					</label>
					<div class="col-md-5 col-sm-5">
						<input type="text" placeholder="${field.remark}2-200个字符" ng-model="p.${field.name}" name="${field.name}" class="form-control" ng-minlength=2 ng-maxlength=200 required >
						<div class="error-messages" ng-if="form1.${field.name}.$dirty" ng-messages="form1.${field.name}.$error" ng-messages-include="pages/common/error-message.html"></div>
					</div>
				    <div class="col-md-4 hidden-xs hidden-sm">
					     <label>请输入${field.remark}</label>
					 </div>
				</div>
				</#list>
				<div class="row" ng-class="{'has-error':form1.name.$dirty && form1.name.$invalid, 'has-success':form1.name.$valid}">
					<label class="control-label col-md-3 col-sm-3 textRight">
						备注
					</label>
					<div class="col-md-5 col-sm-5">
						<textarea placeholder="备注2-200个字符" ng-model="p.remarks" name="remarks" class="form-control"></textarea>
					</div>
				</div>
			</div>
		</fieldset>
							
		<div class="modal-footer">
			<button ng-if="hasRole('${permissionPrefix}:edit')" class="btn btn-primary btn-o" type="submit">保存</button>
			<button class="btn btn-danger btn-o"  type="button" onclick="history.go(-1)">返回</button>
		</div>
	</form>
</div>
<script type="text/javascript" src="/assets/js/${moduleName}/${className}FormCtrl.js"/>