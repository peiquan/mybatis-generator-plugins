package com.yy.mybatis_generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 为 java field 添加 @JsonIgnore
 * 使用方式如下，在 table 下添加属性
 * <property name="jsonIgnoreFields" value="videoPwd;extraType;"/>
 *
 * 多个属性之间使用 ; 隔开
 */
public class JsonIgnoreGenerator extends PluginAdapter {

    private String jsonIgnoreFields;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String  jsonIgnoreFields = introspectedTable.getTableConfiguration().getProperty("jsonIgnoreFields");
        if (jsonIgnoreFields != null && jsonIgnoreFields.contains(field.getName())){
            field.addJavaDocLine("@JsonIgnore");
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnore");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
