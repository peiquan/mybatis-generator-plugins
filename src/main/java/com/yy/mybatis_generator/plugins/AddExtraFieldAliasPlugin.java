package com.yy.mybatis_generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * 为指定的java filed 添加 fieldAlias
 * 使用方式如下：在 table 下添加属性
 * <property name="addExtraFieldAlias" value="videoPwd_videoPwdAlias;xxx_xxxAlias"/>
 *
 * _ 符号前原来 field 名， 符号后为 field 别名。
 */
public class AddExtraFieldAliasPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String  addExtraFieldAlias = introspectedTable.getTableConfiguration().getProperty("addExtraFieldAlias");
        if (addExtraFieldAlias != null && addExtraFieldAlias.contains(field.getName())){
            int index = addExtraFieldAlias.indexOf(field.getName() + "_");
            if (index >= 0) {
                String alias = addExtraFieldAlias.substring(index);
                if (alias.contains(";")) {
                    alias = alias.substring(alias.indexOf("_") + 1, alias.indexOf(";"));
                } else {
                    alias = alias.substring(alias.indexOf("_") + 1);
                }
                Field aliasFiled = new Field(alias,field.getType());
                aliasFiled.setVisibility(field.getVisibility());
                topLevelClass.addField(aliasFiled);
                String camelAlias = alias.substring(0,1).toUpperCase() + alias.substring(1);
                // 添加 alias 的 get set方法
                Method getMethod = new Method("get" + camelAlias);
                getMethod.setReturnType(field.getType());
                getMethod.setVisibility(JavaVisibility.PUBLIC);
                getMethod.addBodyLine("return this." + aliasFiled.getName() + ";");
                topLevelClass.addMethod(getMethod);
                Method setMethod = new Method("set" + camelAlias);
                setMethod.addParameter(new Parameter(aliasFiled.getType(),alias));
                setMethod.setVisibility(JavaVisibility.PUBLIC);
                setMethod.addBodyLine("this." + aliasFiled.getName() + " = " + alias + ";");
                topLevelClass.addMethod(setMethod);

            }
        }                // 复制一个 field 出来，这里测复制不太完善，最好的方式是克隆

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
