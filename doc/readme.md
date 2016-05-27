# mybatis generator 使用参考

这里给出 mybatis generator 简易的使用参考，方便大家提高管理 mybatis generator 生成的文件。

## 配置介绍

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="MySQLTables" targetRuntime="MyBatis3">

        <property name="suppressTypeWarnings" value="true"/>
        <property name="mybatis.generator.overwrite " value="true"/>
        <property name="javaFileEncoding" value="UTF-8"/>


        <!-- 项目基础 baseMapper 的生成 -->
        <plugin type="com.yy.mybatis_generator.plugins.BaseMapperPlugin" >
            <property name="baseMapperType" value="com.zhiniu8.core.dao.base.BaseMapper" />
        </plugin>
        <!-- 分页查询的支持-->
        <plugin type="com.yy.mybatis_generator.plugins.MySQLPaginationPlugin" />
        <!-- baseRecord toString 方法的生成 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
        <!-- baseRecord builder 对象的生成 -->
        <plugin type="com.yy.mybatis_generator.plugins.BuilderPlugin" />

        <!-- baseRecord 字段注释的生成 -->
        <commentGenerator type="com.yy.mybatis_generator.plugins.DefaultCommentGenerator">
            <property name="suppressAllComments" value="true"/>
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://ip:port/yyfinance_pre?useUnicode=true&amp;characterEncoding=utf8"
                        userId="username" password="password">
        </jdbcConnection>


        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <javaModelGenerator targetPackage="com.zhiniu8.core.domain"
                            targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="mapper/generate"
                         targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.zhiniu8.core.dao"
                             targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <table schema="yyfinance_pre" tableName="flex_teacher"
               domainObjectName="FlexTeacher" enableCountByExample="true"
               enableUpdateByExample="true" enableDeleteByExample="true"
               enableSelectByExample="true" selectByExampleQueryId="true"
               enableDeleteByPrimaryKey="true" enableInsert="true"
               enableSelectByPrimaryKey="true" enableUpdateByPrimaryKey="true">
            <property name="useActualColumnNames" value="false"/>
            <generatedKey column="flex_teacher_id" sqlStatement="mysql" identity="true" type="post" />
        </table>
        ...
    </context>
</generatorConfiguration>
```

## 使用方式
### baseMapper 的使用
```
<plugin type="com.yy.mybatis_generator.plugins.BaseMapperPlugin" >
    <property name="baseMapperType" value="com.zhiniu8.core.dao.base.BaseMapper" />
</plugin>
```

baseMapper 需要项目自己编写，mybatis generator 不负责生成，baseMapper 样例如下:
```
/**
 * mybatis 通用 dao 模板。
 * 注: 在此接口列出的所有方法，对于具体的子类，不一定每个方法都可以使用，具体是否可以使用取决于 生成 的 xml 文件是否包含对应的 sql 语句
 * @param <T> mybatis generator 生成 baseRecord 对象
 * @param <PK> baseRecord 对象的主键，只支持单主键
 * @param <E> mybatis generator 生成 example 对象
 */
public interface BaseMapper <T,PK extends Serializable,E> {

    int countByExample(E example);

    int deleteByExample(E example);

    int deleteByPrimaryKey(PK id);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByExample(E example);

    T selectByPrimaryKey(PK id);

    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    int updateByExample(@Param("record") T record, @Param("example") E example);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
```

生成的 FlexTeacherMapper 如下:
```
public interface FlexTeacherMapper extends BaseMapper<FlexTeacher, Long, FlexTeacherExample> {
}
```

注意: FlexTeacherMapper 只会生成一次，若程序检测到当前 FlexTeacherMapper 文件已存在，则不重新生成，也就不会有覆盖问题。而 FlexTeacher,FlexTeacherExample 和对应的 xml 文件，每一次都会重新生成(新的覆盖旧的)。


### builder 的使用
```
FlexTeacher obj = FlexTeacher.builder()
                .description("test")
                .uid(50075059L)
                .nickName("朱培权")
                .build();
```

### 分页查询
```
FlexTeacherExample flexTeacherExample = new FlexTeacherExample();
flexTeacherExample.createCriteria().andUidEqualTo(50075059L);
flexTeacherExample.limit(0L,10L);

List<FlexTeacher> list = flexTeacherMapper.selectByExample(flexTeacherExample);
```

## maven 的使用
```
...
<plugins>
    <!--mvn mybatis-generator:generate-->
    <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.2</version>
        <configuration>
            <verbose>true</verbose>
            <overwrite>true</overwrite>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.6</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-core</artifactId>
                <version>1.3.2</version>
            </dependency>
            <dependency>
                <groupId>com.yy.mybatis_generator.help</groupId>
                <artifactId>mybatis_generator_help</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>
    </plugin>
<plugins>
...
```

运行命令 mvn mybatis-generator:generate 即可生成对应的文件。


## 附
mybatis generator 更多的插件的请参考: [这里](http://www.mybatis.org/generator/reference/plugins.html)

更完美的例子:

http://git.oschina.net/free/Mybatis_PageHelper