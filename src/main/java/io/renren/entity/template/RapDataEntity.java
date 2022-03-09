package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("接口文档中的一个接口的信息")
public class RapDataEntity {
    @ApiModelProperty("接口文档中的 一个接口的id")
    private Long id;
    @ApiModelProperty("接口文档中的 一个接口的名字")
    private String name;
    @ApiModelProperty("接口文档中的 一个接口的链接地址")
    private String url;
    @ApiModelProperty("接口文档中的 一个接口的请求方法")
    private String method;
    @ApiModelProperty("接口文档中的 一个接口的描述")
    private String description;
    @ApiModelProperty("接口文档中的 一个接口的创建人id")
    private Long creatorId;
    @ApiModelProperty("接口文档中的 一个接口的模块id")
    private Long moduleId;
    @ApiModelProperty("接口文档中的 一个接口的所属的仓库的id")
    private Long repositoryId;

    @ApiModelProperty("接口文档中的 一个接口的返回体")
    private List<RapDataProperty> responseProperties;
    @ApiModelProperty("接口文档中的 一个接口的请求属性")
    private List<RapDataProperty> properties;
    @ApiModelProperty("接口文档中的 一个接口的请求属性")
    private List<RapDataProperty> requestProperties;
}
