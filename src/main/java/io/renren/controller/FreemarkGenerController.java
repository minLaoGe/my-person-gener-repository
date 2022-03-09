package io.renren.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.renren.entity.template.WordTableTemplate;
import io.renren.others.remote.HtppApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/free/word")
@Api("合成word文档")

public class FreemarkGenerController {

    @Resource(name = "wordConfiguration")
    private  Configuration configuration;


   /* @GetMapping("/gener1")
    @ApiOperation(value="导出用户doc", httpMethod = "POST",produces="application/json",notes = "导出用户doc")
    public String gener1(HttpServletResponse response) throws Exception {
        ArrayList<WordTableTemplate> wordTableTemplates = new ArrayList<>();
        WordTableTemplate wordTableTemplate = new WordTableTemplate();
        wordTableTemplate.setAddress("是否打开附件");
        wordTableTemplate.setHead("adsf");
        wordTableTemplate.setMethod("responses");

        ArrayList<WordTableRequestModel> wordTableRequestModels = new ArrayList<>();
        ArrayList<WordTableResponsModel> wordTableResponsModels = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            WordTableRequestModel wordTableRequestModel = new WordTableRequestModel();
            wordTableRequestModel.setParam("responses");
            wordTableRequestModel.setNumType("sdf");
            wordTableRequestModel.setStatus("sdf");
            wordTableRequestModels.add(wordTableRequestModel);

            WordTableResponsModel responseModer = new WordTableResponsModel();
            responseModer.setResponseName("士大夫");
            responseModer.setResponseType("sdffds");
            responseModer.setResponseStatus("sdf");
            wordTableResponsModels.add(responseModer);
        }
        wordTableTemplate.setRequests(wordTableRequestModels);
        wordTableTemplate.setResponses(wordTableResponsModels);
        wordTableTemplates.add(wordTableTemplate);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userTables",wordTableTemplates);
        Template template = configuration.getTemplate("freemarkTemplate.ftl", "utf-8");

        //输出
        File outFile = new File("classpath: UserInfoTest.doc");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));

        try {
            template.process(dataModel,out);
            out.flush();
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return  "success";
    }*/
}
