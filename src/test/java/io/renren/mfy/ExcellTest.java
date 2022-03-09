package io.renren.mfy;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.renren.controller.SysGeneratorController;
import io.renren.entity.TableEntity;
import io.renren.entity.template.*;
import io.renren.others.remote.HtppApi;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.swagger.annotations.ApiModelProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcellTest {

    @Autowired
    private HtppApi htppApi;
    @Test
    public void test2(){

        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContent.xml");
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @ApiModelProperty("拥有模块者的id")
    @Value("${rap.programmersId}")
    private List<Long> ids;
    @Resource(name = "wordConfiguration")
    private Configuration configuration;

    @Test
    public void updateAll()throws Exception{
        RapResponseRepository repositoryInformation = htppApi.getRepositoryInformation(25L, true);
        List<RapModule> modules = repositoryInformation.getData().getModules();
        //拥有ids的所有人的模块
        List<RapModule> moduleOfSpecialIds = modules.stream().filter(s -> ids.contains(s.getCreatorId())).collect(Collectors.toList());
        //需要生成人的id
        Long mainId = ids.get(0);
        moduleOfSpecialIds.stream().forEach(s->{
            //获取 mainId 下的所有写过的接口
            List<RapDataEntity> collect = s.getInterfaces().stream().filter(item -> item.getCreatorId().equals(mainId)).collect(Collectors.toList());
            collect.forEach(interfaces-> {
                //获取接口的id
                Long id = interfaces.getId();
                //根据接口的id去查详细的信息
                RapResponseInterfece interfaceInformation = htppApi.getInterfaceInformation(id);
                RapDataEntity data = interfaceInformation.getData();

                List<RapDataProperty> requestProperties = data.getRequestProperties();
                ArrayList<RapDataProperty> allRequestPro = new ArrayList<>();
                addPropotys(requestProperties, allRequestPro);
                List<RapDataProperty> responseProperties = data.getResponseProperties();
                ArrayList<RapDataProperty> allResponsPro = new ArrayList<>();
                addPropotys(responseProperties, allResponsPro);

                allResponsPro.addAll(allRequestPro);
                List<RapDataProperty> needUpdate = new ArrayList<>();
                for (RapDataProperty rapDataProperty : allResponsPro) {
                    if (StringUtils.isEmpty(rapDataProperty.getDescription())&&!StringUtils.isEmpty(rapDataProperty.getValue())){
                        String value = rapDataProperty.getValue();
                        if (StringUtils.isEmpty(value)){
                            int type = value.lastIndexOf("类型是");
                            String substring = value.substring(type + 3);
                            rapDataProperty.setType(substring);
                        }
                        rapDataProperty.setDescription(rapDataProperty.getValue().replaceAll(";", "").replaceAll(" ", "").replace("类型是",""));
                        needUpdate.add(rapDataProperty);
                    }
                }
                System.out.println(needUpdate);
                if (needUpdate.size()>0){
                    RapUpdateReqBody rapUpdateReqBody = new RapUpdateReqBody();
                    //必须要所有的请求因为更新是批量更新
                    rapUpdateReqBody.setProperties(allResponsPro);
                    RapUpdateSummary rapUpdateSummary = new RapUpdateSummary();
                    rapUpdateSummary.setPosFilter(1L);
                    rapUpdateSummary.setBodyOption("FORM_DATA");
                    rapUpdateReqBody.setSummary(rapUpdateSummary);
                    HashMap update = htppApi.update(id, rapUpdateReqBody);
                    System.out.println(update);
                }

            });
        });

    }
    @Autowired
    private SysGeneratorController controller;
    @Value("${rap.prefix}")
    private List<String>  tableNames;
    @Value("${rap.tableName}")
    private String  cheseTableName;
    @Value("${rap.tableTemplateName}")
    private String  tableTemplateName;
    @Value("${rap.interfaceName}")
    private String  interfaceNames;
    @Value("${rap.templateName}")
    private String  templateName;

    /**
     * 生成表结构
     */
    @Test
    public void test4() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit",999999989);
        map.put("page", 1);

        R list = controller.list(map);
        List<Map<String,Object>> list1 =(List<Map<String,Object>>)(((PageUtils) list.get("page")).getList());
        String tables = list1.stream().filter(s -> tableNames.stream().anyMatch(item -> s.getOrDefault("tableName", "").toString().startsWith(item))).map(item2 -> item2.getOrDefault("tableName", "").toString()).collect(Collectors.joining(","));

        List<TableEntity> template = controller.template(tables);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userTables",template);
        this.outPutFiles(dataModel,tableTemplateName,cheseTableName);
    }
    @Test
    public void test3() throws Exception{

        RapResponseRepository repositoryInformation = htppApi.getRepositoryInformation(25L, true);
        List<RapModule> modules = repositoryInformation.getData().getModules();
        //拥有ids的所有人的模块
        List<RapModule> moduleOfSpecialIds = modules.stream().filter(s -> ids.contains(s.getCreatorId())).collect(Collectors.toList());
        //需要生成人的id
        Long mainId = ids.get(0);

        //需要导出的模板的格式
        ArrayList<WordTableTemplate> wordTableTemplates = new ArrayList<>();
        moduleOfSpecialIds.stream().forEach(s->{
            //获取 mainId 下的所有写过的接口
            List<RapDataEntity> collect = s.getInterfaces().stream().filter(item -> item.getCreatorId().equals(mainId)).collect(Collectors.toList());
            collect.forEach(interfaces-> {
                //获取接口的id
                Long id = interfaces.getId();
                //根据接口的id去查详细的信息
                RapResponseInterfece interfaceInformation = htppApi.getInterfaceInformation(id);
                RapDataEntity data = interfaceInformation.getData();


                WordTableTemplate wordTableTemplate = new WordTableTemplate();
                wordTableTemplate.setHead(data.getName());
                wordTableTemplate.setMethod(data.getMethod());
                wordTableTemplate.setAddress(data.getUrl());

                List<RapDataProperty> requestProperties = data.getRequestProperties();
                ArrayList<RapDataProperty> allRequestPro = new ArrayList<>();
                //request请求
                addPropotys(requestProperties, allRequestPro);
                List<RapDataProperty> responseProperties = data.getResponseProperties();


                //response请求
                ArrayList<RapDataProperty> allResponsPro = new ArrayList<>();
                addPropotys(responseProperties, allResponsPro);


                wordTableTemplate.setRequests(allRequestPro);
                wordTableTemplate.setResponses(allResponsPro);
                wordTableTemplates.add(wordTableTemplate);
            });
        });
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userTables",wordTableTemplates);



        outPutFiles(dataModel, templateName, interfaceNames);
    }

    private void outPutFiles(Map<String, Object> dataModel, String templateName, String fileName) throws TemplateException, IOException {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        Template template = configuration.getTemplate(templateName+".ftl", "utf-8");

        //输出
        File outFile = new File(path+ fileName+".doc");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));

        template.process(dataModel,out);
        out.flush();
        out.close();
    }

    private void addPropotys(List<RapDataProperty> responseProperties, ArrayList<RapDataProperty> allResponsPro) {
        for (RapDataProperty responseProperty : responseProperties) {
             selfConfiguration(responseProperty);
//            setDescription(responseProperty);
            List<RapDataProperty> allChild = getAllChild(responseProperty);

            allResponsPro.addAll(allChild);

            if (!allChild.contains(responseProperty)) {
                allResponsPro.add(responseProperty);
            }

        }
    }

    private void selfConfiguration(RapDataProperty responseProperty) {

        if (responseProperty.getName().equals("msg")){
            responseProperty.setDescription("状态信息");
        }else if(responseProperty.getName().equals("code")){
            responseProperty.setDescription("状态码");
        }else if (responseProperty.getName().equals("totalCount")){
            responseProperty.setDescription("数据的总记录数");
        }else if (responseProperty.getName().equals("pageSize")){
            responseProperty.setDescription("一页记录条数");
        }else if (responseProperty.getName().equals("totalPage")){
            responseProperty.setDescription("总记录数");
        }else if (responseProperty.getName().equals("currPage")){
            responseProperty.setDescription("当前页");
        }else if (responseProperty.getName().equals("id")){
            responseProperty.setDescription("主键");
        }else if (responseProperty.getName().equals("page")&&responseProperty.getType().equals("Object")){
            responseProperty.setDescription("分页参数");
        }else if (responseProperty.getName().equals("page")){
            responseProperty.setDescription("当前页");
        }else if (responseProperty.getName().equals("limit")){
            responseProperty.setDescription("页码");
        }else if (responseProperty.getName().equals("createTime")){
            responseProperty.setDescription("创建时间");
        }else if (responseProperty.getName().equals("creatorName")){
            responseProperty.setDescription("创建人");
        }else if (responseProperty.getName().equals("creatorId")){
            responseProperty.setDescription("创建人id");
        }else if (responseProperty.getName().equals("updateUserName")){
            responseProperty.setDescription("更新人");
        }else if (responseProperty.getName().equals("updateUserId")){
            responseProperty.setDescription("更新人id");
        }else if (responseProperty.getName().equals("updateTime")){
            responseProperty.setDescription("更新人id");
        }else if (responseProperty.getName().equals("params")){
            responseProperty.setDescription("所有请求条件");
        }else if (responseProperty.getName().equals("createorId")){
            responseProperty.setDescription("更新人id");
        }else if (responseProperty.getName().equals("createorName")){
            responseProperty.setDescription("创建人");
        }else if (responseProperty.getName().equals("info")){
            responseProperty.setDescription("实体类详情");
        }else if (responseProperty.getName().equals("systemDate")){
            responseProperty.setDescription("系统时间");
        }else if (responseProperty.getName().equals("list")){
            responseProperty.setDescription("实体类列表");
        }


    }


    public static void main(String[] args) {
        String path = ExcellTest.class.getClassLoader().getResource("").getPath();
        System.out.println();
    }
    public List<RapDataProperty> getAllChild(RapDataProperty rap){
        ArrayList<RapDataProperty> rapDataProperties = new ArrayList<>();
        if (Objects.isNull(rap.getChildren())||rap.getChildren().size()<1){
//            setDescription(rap);
            selfConfiguration(rap);
            rapDataProperties.add(rap);
            return rapDataProperties;
        }{
            for (RapDataProperty child : rap.getChildren()) {
                List<RapDataProperty> allChild = this.getAllChild(child);
                if (!Objects.isNull(allChild)&&allChild.size()>0){
                    rapDataProperties.addAll(allChild);
                }
            }
        }
        return rapDataProperties;
    }

}
