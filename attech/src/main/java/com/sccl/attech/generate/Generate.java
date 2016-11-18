/**
 * @(#)Generate.java     1.0 2015-7-15 16:41:49
 * Copyright 2015 bjth, Inc. All rights reserved.
 * myattech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.FileUtils;
import com.sccl.attech.common.utils.FreeMarkers;
import com.sccl.attech.common.utils.IdGen;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 代码生成器.
 * 
 * @author sccl
 * @version 2013-06-21
 */
public class Generate {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(Generate.class);

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {

		// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================1412914

		// 主要提供基本功能模块代码生成。
		// 目录生成结构：{packageName}/{moduleName}/{dao,entity,service,web}/{subModuleName}/{className}
		
		// packageName 包名，这里如果更改包名，请在applicationContext.xml和srping-mvc.xml中配置base-package、packagesToScan属性，来指定多个（共4处需要修改）。
		String packageName = "com.sccl.attech.modules";
		
		String moduleName = "mobil";			// 模块名，例：sys
		String subModuleName = "";				// 子模块名（可选） 
		String className = "updateApp";			// 类名，例：user
		List<GenerateField> fields =Lists.newArrayList();
		fields.add(new GenerateField("name","名称"));
		fields.add(new GenerateField("type","类型"));
		fields.add(new GenerateField("version","版本"));
		fields.add(new GenerateField("path","路径"));
		
		String classAuthor = "zzz";		// 类作者，例：zhaozz
		String functionName = "App升级";			// 功能名，例：用户

		// 是否启用生成工具
		//Boolean isEnable = false;
		Boolean isEnable = true;
		
		// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================
		
		if (!isEnable){
			logger.error("请启用代码生成工具，设置参数：isEnable = true");
			return;
		}
		
		if (StringUtils.isBlank(moduleName) || StringUtils.isBlank(moduleName) 
				|| StringUtils.isBlank(className) || StringUtils.isBlank(functionName)){
			logger.error("参数设置错误：包名、模块名、类名、功能名不能为空。");
			return;
		}
		
		// 获取文件分隔符
		String separator = File.separator;
		
		// 获取工程路径
		File projectPath = new DefaultResourceLoader().getResource("").getFile();
		while(!new File(projectPath.getPath()+separator+"src"+separator+"main").exists()){
			projectPath = projectPath.getParentFile();
		}
		logger.info("Project Path: {}", projectPath);
		
		// 模板文件路径
		String tplPath = StringUtils.replace(projectPath+"/src/main/java/com/sccl/attech/generate/template", "/", separator);
		logger.info("Template Path: {}", tplPath);
		
		// Java文件路径
		String javaPath = StringUtils.replaceEach(projectPath+"/src/main/java/"+StringUtils.lowerCase(packageName), 
				new String[]{"/", "."}, new String[]{separator, separator});
		logger.info("Java Path: {}", javaPath);
		
		// 视图文件路径
		String viewPath = StringUtils.replace(projectPath+"/WebRoot/pages", "/", separator);
		logger.info("View Path: {}", viewPath);
		String resPath = StringUtils.replace(projectPath+"/WebRoot/assets/js", "/", separator);
		logger.info("Res Path: {}", resPath);
		
		// 代码模板配置
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(new File(tplPath));

		// 定义模板变量
		Map<String, Object> model = Maps.newHashMap();
		model.put("packageName", StringUtils.lowerCase(packageName));
		model.put("moduleName", StringUtils.lowerCase(moduleName));
		model.put("fields", fields);
		model.put("subModuleName", StringUtils.isNotBlank(subModuleName)?"."+StringUtils.lowerCase(subModuleName):"");
		model.put("className", StringUtils.uncapitalize(className));
		model.put("ClassName", StringUtils.capitalize(className));
		model.put("classAuthor", StringUtils.isNotBlank(classAuthor)?classAuthor:"Generate Tools");
		model.put("classVersion", DateUtils.getDate());
		model.put("functionName", functionName);
		model.put("tableName", model.get("moduleName")+(StringUtils.isNotBlank(subModuleName)
				?"_"+StringUtils.lowerCase(subModuleName):"")+"_"+model.get("className"));
		model.put("urlPrefix", model.get("moduleName")+(StringUtils.isNotBlank(subModuleName)
				?"/"+StringUtils.lowerCase(subModuleName):"")+"/"+model.get("className"));
		model.put("viewPrefix", //StringUtils.substringAfterLast(model.get("packageName"),".")+"/"+
				model.get("urlPrefix"));
		model.put("permissionPrefix", model.get("moduleName")+(StringUtils.isNotBlank(subModuleName)
				?":"+StringUtils.lowerCase(subModuleName):"")+":"+model.get("className"));

		// 生成 Entity
		Template template = cfg.getTemplate("entity.ftl");
		String content = FreeMarkers.renderTemplate(template, model);
		String filePath = javaPath+separator+model.get("moduleName")+separator+"entity"
				+separator+StringUtils.lowerCase(subModuleName)+separator+model.get("ClassName")+".java";
		writeFile(content, filePath);
		logger.info("Entity: {}", filePath);
		
		// 生成 Dao
		template = cfg.getTemplate("dao.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath+separator+model.get("moduleName")+separator+"dao"+separator
				+StringUtils.lowerCase(subModuleName)+separator+model.get("ClassName")+"Dao.java";
		writeFile(content, filePath);
		logger.info("Dao: {}", filePath);
		
		// 生成 Service
		template = cfg.getTemplate("service.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath+separator+model.get("moduleName")+separator+"service"+separator
				+StringUtils.lowerCase(subModuleName)+separator+model.get("ClassName")+"Service.java";
		writeFile(content, filePath);
		logger.info("Service: {}", filePath);
		
		// 生成 Controller
		createJavaFile(subModuleName, separator, javaPath, cfg, model);
		
		// 生成 ViewForm
		template = cfg.getTemplate("viewForm.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = viewPath//+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				+separator+model.get("className")+"Form.html";
		writeFile(content, filePath);
		logger.info("ViewForm: {}", filePath);
		
		// 生成 ViewFormJs
		template = cfg.getTemplate("viewFormJs.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = resPath//+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				+separator+model.get("className")+"FormCtrl.js";
		writeFile(content, filePath);
		logger.info("ViewFormJs: {}", filePath);
		
		// 生成 ViewList
		template = cfg.getTemplate("viewList.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = viewPath//+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				+separator+model.get("className")+"List.html";
		writeFile(content, filePath);
		logger.info("ViewList: {}", filePath);
		
		// 生成 ViewListJs
		template = cfg.getTemplate("viewListJs.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = resPath//+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				+separator+model.get("className")+"ListCtrl.js";
		writeFile(content, filePath);
		logger.info("ViewList: {}", filePath);
		
		// 生成 菜单sql
		template = cfg.getTemplate("sql.ftl");
		model.put("uid", IdGen.uuid());
		model.put("uid1", IdGen.uuid());
		content = FreeMarkers.renderTemplate(template, model);
		filePath = viewPath//+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				+separator+model.get("className")+".sql";
		writeFile(content, filePath);
		logger.info("ViewList: {}", filePath);
		
		logger.info("Generate Success.");
	}

	/**
	 * 创建 java file.
	 * 
	 * @param subModuleName the sub module name
	 * @param separator the separator
	 * @param javaPath the java 路径
	 * @param cfg the cfg
	 * @param model the model
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void createJavaFile(String subModuleName, String separator, String javaPath, Configuration cfg, Map<String, Object> model) throws IOException {
		Template template;
		String content;
		String filePath;
		template = cfg.getTemplate("controller.ftl");
		content = FreeMarkers.renderTemplate(template, model);
		filePath = javaPath+separator+model.get("moduleName")+separator+"web"+separator
				+StringUtils.lowerCase(subModuleName)+separator+model.get("ClassName")+"Controller.java";
		writeFile(content, filePath);
		logger.info("Controller: {}", filePath);
	}
	
	/**
	 * 将内容写入文件.
	 * 
	 * @param content the content
	 * @param filePath the file 路径
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileUtils.createFile(filePath)){
				FileOutputStream fos = new FileOutputStream(filePath);
			Writer writer = new OutputStreamWriter(fos,"UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(content);
				bufferedWriter.close();
				writer.close();
			}else{
				logger.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
