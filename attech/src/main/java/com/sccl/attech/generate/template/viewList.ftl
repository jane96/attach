<section id="page-title">
    <div class="row">
        <div class="col-sm-8">
            <h3 class="mainTitle">${functionName}管理</h3>
        </div>
    </div>
</section>

<div class="container-fluid container-fullw bg-white">
    <div class="row">
        <div class="col-md-12">
            <div ng-controller="${className}ListController">
                <div class="row">
                    <div class="col-md-12">
                        <div class="margin-bottom-20">
                            <button class="btn btn-wide btn-primary" ng-if="hasRole('${permissionPrefix}:edit')" ui-sref="app.menu${moduleName}_${className}_edit()">
                            <i class="ti-plus"></i> 新建${functionName}
                            </button>
                            <!-- Single button -->
                            <div class="btn-group" dropdown is-open="status.isopen">
                                <button type="button" class="btn btn-primary btn-o dropdown-toggle" dropdown-toggle ng-disabled="disabled">
                                    <i class="ti-more"></i>更多<span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li>
                                        <a href ng-if="hasRole('${permissionPrefix}:edit')" 
                                         ng-click="edit(selv,(data | filter:{'$selected': true}).length)">编辑</a>
                                    </li>
                                    <li> <a href ng-click="export()">导出Excel文件</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
 
                    <div class="row">
                    <div class="col-md-12 searform">
                        <div class="row form-horizontal formPadding">
                           <div class="col-md-9 formInfo">
                             <div class="row">
                             <#list fields  as field>
                             <#if field_index gt 1 ><#break/> </#if>  
	                              <div class="col-md-6">
	                                  <label class="col-md-3 control-label minpeople">${field.remark}:</label>
	                                  <div class="col-md-9">
			                              <input type="text"  class="form-control" name="params.${field.name}" class="text-small"  id="params_${field.name}" ng-model="params.${field.name}">
	                                  </div>
	                              </div>
	                          </#list>   
                             </div>
                             <#if fields?size gt 2 >
                             <div ng-show="showCheckbox">
                               	 <#list fields  as field>
	                             	<#if field_index gt 1 >  
		                              <div class="col-md-6">
		                                  <label class="col-md-3 control-label minpeople">${field.remark}:</label>
		                                  <div class="col-md-9">
				                              <input type="text"  class="form-control" name="params.${field.name}" class="text-small"  id="params_${field.name}" ng-model="params.${field.name}">
		                                  </div>
		                              </div>
		                             </#if>
	                          </#list>   
                             </div>
                            </#if>
                           </div>
                            
                            <div class="pull-right col-md-3">
                                <div class="formBtn" dropdown>
                                    <div class="btn-group" dropdown="">
											<button type="button" class="btn btn-default open-message-search" ct-toggle="on" target="wrap-options" ng-click="tableParams.reload()">
													<i class="ti-search"></i>
											</button>
											<button type="button" class="btn btn-default dropdown-toggle" dropdown-toggle="" aria-haspopup="true" aria-expanded="false">
												<span class="caret"></span>
												<span class="sr-only">按钮</span>
											</button>
											<ul class="dropdown-menu pull-right dropdown-light" role="menu">															
												<li> 
													<a ng-click="tableParams.sorting({id:'desc'})" class="">清除排序</a>                                            
												</li>
												<li>
													<a ng-click="tableParams.reload()">刷新数据</a>
												</li>
											</ul>
						          </div>
						          <#if fields?size gt 2 >
                                     <button class="btn btn-primary" ng-click="showCheckbox=!showCheckbox;" ng-init="showCheckbox=false" ng-model="showCheckbox" >{{(showCheckbox?'收起更多':'查询更多')}}</button>
                                  </#if>
                                </div>
                            </div>
                        </div>
                       </div>
                   </div>
                  

                
                <div class="reponsiveTable" id="no-more-tables">
                    <table ng-table="tableParams"  class="table ng-table-rowselected">
                        <tbody ng-repeat="p in data">
                            <tr id="tr{{p.id}}" ng-click="selected(p)" ng-class="{'success': p.$selected}">
                                <td class="rowTd" data-title="'序号'" new-data-title="序号">
                                    {{(tableParams.page()-1)*tableParams.count()+$index+1}}
                                </td>
                                <#list fields as field>
                                <td class="rowTd" new-data-title="${field.remark}" data-title="'${field.remark}'" >{{p.${field.name}}}</td>
                                </#list>
                                <td class="rowTd">
                                    <div class="pull-right margin-right-10">
                                        <div class="btn-group pull-right choosebtn7" dropdown>
                                            <button type="button" class="btn btn-default btn-xs dropdown-toggle" dropdown-toggle>
                                                <span class="ti-settings"></span>
                                                <span class="sr-only">按钮</span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li>
                                                    <a ng-click="edit(p,1);"> 查看</a>
                                                </li>
                                                <li class="divider"></li>
                                                <li ng-if="hasRole('${permissionPrefix}:edit')">
                                                    <a ng-click="edit(p,1);"> 编辑 </a>
                                                </li>
                                                <li ng-if="hasRole('${permissionPrefix}:edit')" class="divider"></li>
                                                <li ng-if="hasRole('${permissionPrefix}:edit')">
                                                    <a ng-click="delete(p,1)"> 删除</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/assets/js/${moduleName}/${className}ListCtrl.js"/>