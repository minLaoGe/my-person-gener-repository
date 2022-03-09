package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
@ApiModel("接口文档中的接口属性")
public class RapDataProperty {

    @ApiModelProperty("属性的id")
    private Long id;
    @ApiModelProperty("属性的名称")
    private String name;
    @ApiModelProperty("属性的默认值")
    private String value;
    @ApiModelProperty("属性的描述")
    private String description;
    @ApiModelProperty("属性所属模块的id")
    private Long moduleId;
    @ApiModelProperty("属性所属仓库的id")
    private Long repositoryId;
    @ApiModelProperty("是否必填")
    private String required="true";
    @ApiModelProperty("数据的类型")
    private String type;
    @ApiModelProperty("创建人id")
    private String creatorId;


    private String scope;
    private int pos;
    private String rule;
    private Long parentId;
    private Long priority;
    private Long interfaceId;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    @ApiModelProperty("子属性")
    List<RapDataProperty> children;

    public String getRequired() {
        if (StringUtils.isEmpty(this.required)){
            return "true";
        }
        return required;
    }


}
