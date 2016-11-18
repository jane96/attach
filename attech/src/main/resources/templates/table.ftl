<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">  
<hibernate-mapping>  
    <class table="temp_${entity.templateName}" entity-name="${entity.templateName}">  
         <id name="id" column="id" type="java.lang.String">  
            <generator class="uuid"/>  
        </id>
       <#if entity.elementValues?exists>
            <#list entity.elementValues as attr>
             <#if attr.valueName == "id">
        	<#else>
        <property name="${attr.valueName}" <#if attr.binding??&&attr.binding!="string"&& attr.binding!="">length="${attr.binding}"</#if>  column="C_${attr.valueName}" type="java.lang.String" />
             </#if>
           </#list>
           <!--<property  name="createBy"  column="c_createBy" type="java.lang.String" />
           <property  name="createDate"  column="c_createDate" type="java.util.Date" />
           <property  name="updateBy"  column="c_updateBy" type="java.lang.String" />
           <property  name="updateDate"  column="c_updateDate" type="java.util.Date" />-->
        </#if>
    </class>  
</hibernate-mapping>  