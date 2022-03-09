package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("word模板的属性")
public class WordTableTemplate {
        @ApiModelProperty("模块的名称")
        private String head;
        @ApiModelProperty("模块的请求地址")
        private String address;
        @ApiModelProperty("模块的请求方式")
        private String method;
        @ApiModelProperty("所有的请求参数")
        private List<RapDataProperty>  requests;
        @ApiModelProperty("所有的响应参数")
        private List<RapDataProperty>  responses;

}
