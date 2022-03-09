package io.renren.utils;

import io.renren.config.MongoManager;
import io.renren.emun.DBTypeEnum;
import io.renren.entity.ColumnEntity;
import io.renren.entity.TableEntity;
import io.renren.entity.mongo.MongoDefinition;
import io.renren.entity.mongo.MongoGeneratorEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */

public class GenUtils {

    private static String currentTableName;
    private static Configuration configuration;


    private static  String database;

    @Value("${renren.database}")
    public void setConfigEvn(String cEnv) {
        GenUtils.database = cEnv;
    }

    public static List<String> getTemplates(String path) {
        List<String> templates = new ArrayList<String>();
        templates.add(path+"/Entity.java.vm");
//        if ("template".equals(path)){

//        }

        //不是oracle放入这么多模板
        if (!DBTypeEnum.ORACLE.getName().equals(Test5Config.getCurrentEnv())){
            templates.add(path+"/ControllerService.java.vm");
            templates.add(path+"/index.vue.vm");
            templates.add(path+"/add-or-update.vue.vm");
            templates.add(path+"/entityJson.json.vm");
            templates.add(path+"/Service.java.vm");
            templates.add(path+"/ServiceImpl.java.vm");
            templates.add(path+"/Controller.java.vm");
            templates.add(path+"/menu.sql.vm");
            templates.add(path+"/view.vue.vm");
            templates.add(path+"/Dao.xml.vm");
        }else if (DBTypeEnum.ORACLE.getName().equals(Test5Config.getCurrentEnv())){
            templates.add(path+"/DaoOrc.xml.vm");
        }


        templates.add(path+"/Dao.java.vm");

        if (MongoManager.isMongo()) {
            // mongo不需要mapper、sql   实体类需要替换
            templates.remove(0);
            templates.remove(1);
            templates.remove(2);
            templates.add(path+"/MongoEntity.java.vm");
        }
        return templates;
    }

    public static List<String> getMongoChildTemplates(String path) {
        List<String> templates = new ArrayList<String>();
        templates.add(path+"/MongoChildrenEntity.java.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static TableEntity generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip,String path

    ) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        boolean hasList = false;



        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        //表信息
        TableEntity tableEntity = new TableEntity();

        Map<String, Object> map = getStringObjectMap(table, columns, config, hasBigDecimal, hasList, columsList, tableEntity);
        VelocityContext context = new VelocityContext(map);
        //add By tom.min 添加建表语句
        String create_table = table.get("Create Table");
        tableEntity.setTableCreates(create_table);

        //获取模板列表
        List<String> templates = getTemplates(path);
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                addToZip(zip,  tableEntity, template, sw);
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
        return tableEntity;
    }

    public static void addToZip(ZipOutputStream zip,  TableEntity tableEntity, String template, StringWriter sw) throws IOException {
        //添加到zip
        zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getTableName(), tableEntity.getClassName(), getConfig().getString("package"), getConfig().getString("moduleName"))));
        IOUtils.write(sw.toString(), zip, "UTF-8");
        IOUtils.closeQuietly(sw);
        zip.closeEntry();
    }

    public static Map<String, Object> getStringObjectMap(Map<String, String> table, List<Map<String, String>> columns, Configuration config, boolean hasBigDecimal, boolean hasList, List<ColumnEntity> columsList, TableEntity tableEntity) {
        //表名转换成Java类名
        String className = tableToJava(table.get("tableName"), config.getStringArray("tablePrefix"));

        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));

        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            String length = column.getOrDefault("length", "");

            if (!org.springframework.util.StringUtils.isEmpty(length)){
                columnEntity.setLength(Long.valueOf(length));
            }else {
                columnEntity.setLength(0L);
            }
            if (DBTypeEnum.ORACLE.getName().equals(Test5Config.getCurrentEnv())){
                columnEntity.setJdbcType(config.getString("orc."+column.get("dataType")));
            }


            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), columnToJava(columnEntity.getDataType()));
            columnEntity.setAttrType(attrType);


            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(columnEntity.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("id_type", config.getString("id_type"));
        map.put("commonUtilsPackName", config.getString("commonUtilsPackName"));
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("dicUrl", config.getString("dicUrl"));


        //判断是否是选择框 设置选择框里面的数据
        if (DBTypeEnum.MYSQL.getName().equals(database)){
            judgeIfSelect(tableEntity, config);
            if (tableEntity.getKeyString().length()>3){
                map.put("dicKeys", tableEntity.getKeyString().toString());
            }else {
                map.put("dicKeys",null);
            }
        }
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("controlerName", tableEntity.getTableName().toLowerCase().replace("_","/"));
        map.put("perssionName", tableEntity.getTableName().toLowerCase().replace("_",":"));
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("getAndSet", config.getBoolean("getAndSet"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));





        return map;
    }




    //判断是否有选择框
    private static void judgeIfSelect(TableEntity tableEntity,Configuration config) {
        List<ColumnEntity> columns1 = tableEntity.getColumns();
        tableEntity.setKeyString(new StringBuilder().append("["));
        Optional.ofNullable(tableEntity.getComments()).ifPresent(s->tableEntity.setComments(s.trim()));
        Optional.ofNullable(columns1).ifPresent(s-> s.stream().forEach(item->
                {
                    String comments = item.getComments();
                    //dic关键字
                    String keyStr = config.getString("dicKeyWord");
                    boolean ifContainsDicKey = comments.contains(keyStr);

                    //判断是否是选择框
                    if (comments.endsWith("sel")&&!ifContainsDicKey) {
                        item.setIfSelect(true);
                        int i = comments.indexOf("(");
                        int last = comments.lastIndexOf(")");

                        //获取括号里面的内容  所在端(1-治理,3-协同)  =>  1-治理,3-协同
                        String substring = comments.substring(i + 1, last);
                        StringBuilder stringBuilder = new StringBuilder();
                        // [1-治理,3-协同]
                        String[] split = substring.split(",");
                        stringBuilder.append("[");
                        //选择框的map
                        Map<Object, Object> selMap = new HashMap<>();
                        for (String s1 : split) {
                            stringBuilder.append("{ \n");
                            String[] number = s1.split("-");
                            String key = number[0];
                            stringBuilder.append("value: '"+key+"', \n");
                            String value = number[1];
                            stringBuilder.append("label: '"+value+"', \n");
                            selMap.put(key,value);

                            stringBuilder.append("},");
                        }
                        item.setValueMap(selMap);
                        int i1 = stringBuilder.lastIndexOf(",");
                        stringBuilder.deleteCharAt(i1);
                        stringBuilder.append("]");
                        String lastComment = stringBuilder.toString();
                        item.setSelectData(lastComment);
                            //判断是否是去查字典表
                    }else if (comments.endsWith("sel")&&ifContainsDicKey){
                        StringBuilder keyString = tableEntity.getKeyString();
                        //拼接字典 keyWords
                        int i2 = comments.indexOf(keyStr);
                        i2+=keyStr.length();
                        int i3 = comments.indexOf(";", i2);
                        String dicKey = comments.substring(i2+1, i3);
                        keyString.append("'"+dicKey+"',");
                        item.setIfNeedDic(true);
                        item.setDicKey(dicKey);
                        item.setIfSelect(true);
                    }

                    //对label进行拼装;
                    String comments1 = item.getComments();
                    if (comments1.contains(";")){
                        int i = comments1.indexOf(";");
                        item.setLable(comments1.substring(0,i));
                    }else{
                        item.setLable(comments1);
                    }
                }
         ));
        //最后拼接字符串
        if (!Objects.isNull(tableEntity.getKeyString())&&tableEntity.getKeyString().length()>3){
            tableEntity.getKeyString().deleteCharAt(tableEntity.getKeyString().length() - 1);
            tableEntity.getKeyString().append("]");
        }
    }

    /**
     * 生成mongo其他实体类的代码
     */
    public static void generatorMongoCode(String[] tableNames, ZipOutputStream zip,String path) {
        for (String tableName : tableNames) {
            MongoDefinition info = MongoManager.getInfo(tableName);
            currentTableName = tableName;
            List<MongoGeneratorEntity> childrenInfo = info.getChildrenInfo(tableName);
            childrenInfo.remove(0);
            for (MongoGeneratorEntity mongoGeneratorEntity : childrenInfo) {
                generatorChildrenBeanCode(mongoGeneratorEntity, zip,path);
            }
        }
    }

    private static void generatorChildrenBeanCode(MongoGeneratorEntity mongoGeneratorEntity, ZipOutputStream zip,String path) {
        //配置信息
        Configuration config = getConfig();
        boolean hasList = false;
        //表信息
        TableEntity tableEntity = mongoGeneratorEntity.toTableEntity();
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getStringArray("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));
        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : mongoGeneratorEntity.getColumns()) {
            ColumnEntity columnEntity = new ColumnEntity();
            String columnName = column.get("columnName");
            if (columnName.contains(".")) {
                columnName = columnName.substring(columnName.lastIndexOf(".") + 1);
            }
            columnEntity.setColumnName(columnName);
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), columnToJava(columnEntity.getDataType()));
            columnEntity.setAttrType(attrType);



            if (!hasList && "array".equals(columnEntity.getExtra())) {
                hasList = true;
            }
            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getMongoChildTemplates(path);
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template,tableEntity.getTableName(), tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }

    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                  if (tableName.startsWith(tablePrefix)){
                    tableName = tableName.replaceFirst(tablePrefix, "");
                }
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public synchronized static Configuration getConfig() {
        try {
            if (Objects.isNull(configuration)){
                configuration= new PropertiesConfiguration("generator.properties");
            }

            return configuration;
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template,String tableName, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        String[] split = template.split("/");
        String templateName = split[1];
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        if (templateName.equals("MongoChildrenEntity.java.vm")) {
            return packagePath + "entity" + File.separator + "inner" + File.separator + currentTableName+ File.separator + splitInnerName(className)+ "InnerEntity.java";
        }
        if (templateName.equals("Entity.java.vm") || templateName.equals("MongoEntity.java.vm")) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }

        if (templateName.equals("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (templateName.equals("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (templateName.equals("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (templateName.equals("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }
        if (templateName.equals("ControllerService.java.vm")) {
            return packagePath + "controller" + File.separator + className + "SController.java";
        }

        if (templateName.equals("Dao.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }
        if (templateName.equals("DaoOrc.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }

        if (templateName.equals("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (templateName.equals("index.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }

        if (templateName.equals("add-or-update.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
        }
        if (templateName.equals("view.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-view.vue.vm";
        }
        if (templateName.equals("entityJson.json.vm")) {
            return "json"+ File.separator + className+ "_entityJson.json";
        }
        if (templateName.equals("AllCreateTable.sql.vm")) {
            return  "allCreateTable.sql";
        }


        return null;
    }

    private static String splitInnerName(String name){
          name = name.replaceAll("\\.","_");
          return name;
    }

}
