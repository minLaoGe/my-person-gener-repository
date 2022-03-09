/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.renren.config.MongoManager;
import io.renren.dao.GeneratorDao;
import io.renren.dao.MongoDBGeneratorDao;
import io.renren.entity.ColumnEntity;
import io.renren.entity.TableEntity;
import io.renren.entity.mongo.MongoDefinition;
import io.renren.factory.MongoDBCollectionFactory;
import io.renren.utils.GenUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.RRException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysGeneratorService {
    @Autowired
    private GeneratorDao generatorDao;

    @Value("${spring.thymeleaf.prefix}")
    private  String  path;
    @Value("${renren.database}")
    private  String  database;
    @Value("${templates.allfile}")
    private List<String> allFiles;

    public PageUtils queryList(Query query) {
        Page<?> page = PageHelper.startPage(query.getPage(), query.getLimit());
        List<Map<String, Object>> list = generatorDao.queryList(query);
        int total = (int) page.getTotal();
        if (generatorDao instanceof MongoDBGeneratorDao) {
            total = MongoDBCollectionFactory.getCollectionTotal(query);
        }
        return new PageUtils(list, total, query.getLimit(), query.getPage());
    }

    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }


    public byte[] generatorCode(String[] tableNames) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        List<TableEntity> tableEntitys=new ArrayList<TableEntity>();
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            if ("mysql".equals(database)){
                //生成表 DDL语句
                Map<String, String> tableStruct = queryTableStruct(tableName);
                table.putAll(tableStruct);
            }


            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            TableEntity tableEntity = GenUtils.generatorCode(table, columns, zip, path);
            tableEntitys.add(tableEntity);
        }

        // add By Tom.min
        generTableStruct(zip, tableEntitys);
        if (MongoManager.isMongo()) {
            GenUtils.generatorMongoCode(tableNames, zip,path);
        }


        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private void generTableStruct(ZipOutputStream zip, List<TableEntity> tableEntity) throws IOException {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        List<String> sqls = tableEntity.stream().map(s -> s.getTableCreates()).collect(Collectors.toList());

        stringObjectHashMap.put("tableCreates",sqls);
        VelocityContext context = new VelocityContext(stringObjectHashMap);
        //渲染模板
        StringWriter sw = new StringWriter();
        for (String allFile : allFiles) {
            Template tpl = Velocity.getTemplate(path+"/"+allFile, "UTF-8");
            tpl.merge(context, sw);
            GenUtils.addToZip(zip,tableEntity.get(0),path+"/"+allFile,sw);

        }

    }

    private Map<String, String> queryTableStruct(String tableName) {
         return generatorDao.queryTableStruct(tableName);
    }

    //add by Tom.min
    public List<TableEntity> generateTemplate(String[] tableNames){
        List<TableEntity> tableEntityList = new ArrayList<>();
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            boolean hasBigDecimal = false;
            boolean hasList = false;

            //列信息
            List<ColumnEntity> columsList = new ArrayList<>();
            //表信息
            TableEntity tableEntity = new TableEntity();
            Configuration config = GenUtils.getConfig();
            //生成代码
            Map<String, Object> stringObjectMap = GenUtils.getStringObjectMap(table, columns, config, hasBigDecimal, hasList, columsList, tableEntity);
            tableEntityList.add(tableEntity);
        }
        return tableEntityList;
    }
}
