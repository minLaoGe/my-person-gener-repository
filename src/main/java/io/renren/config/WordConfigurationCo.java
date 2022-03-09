package io.renren.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

@Configuration
public class WordConfigurationCo {
    @Bean
    public freemarker.template.Configuration wordConfiguration(){
        freemarker.template.Configuration result = new freemarker.template.Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        result.setDefaultEncoding("utf-8");
        //设置模板加载器
        result.setClassForTemplateLoading(this.getClass(), "/template/code");
        return result;
    }
}
