package com.yy.mybatis_generator.plugins;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.File;
import java.util.List;

public class DeleteExistedXmlPlugin extends PluginAdapter {

    public boolean validate(List<String> warnings) {
        return true;
    }
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        // 删除已存在 xml 文件，让每一次生成的 xml 文件都是最新的
        FullyQualifiedJavaType baseRecordJavaType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());


        String xmlFilePaht = getContext().getSqlMapGeneratorConfiguration().getTargetProject()
                + "/" + getContext().getSqlMapGeneratorConfiguration().getTargetPackage()
                + "/" + baseRecordJavaType.getShortName() + "Mapper.xml";
        File xmlFile = new File(xmlFilePaht);
        if (xmlFile.exists()){
            Boolean flag = xmlFile.delete();
            if (flag) {
                System.out.println("delete " + xmlFilePaht);
            } else {
                System.out.println("don't delete " + xmlFilePaht);
            }
        }

        return super.contextGenerateAdditionalXmlFiles(introspectedTable);
    }

}
