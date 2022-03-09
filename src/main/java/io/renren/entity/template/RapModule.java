package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("rap模块")
public class RapModule {

        @ApiModelProperty("id")
        private Long id;
        @ApiModelProperty("模块的名称")
        private String name;
        @ApiModelProperty("模块的优先级")
        private String priority;
        @ApiModelProperty("所属仓库的id")
        private String repositoryId;
        @ApiModelProperty("是否必须填写")
        private String required="true";
        @ApiModelProperty("模块描述")
        private String description;
        @ApiModelProperty("创建者的id")
        private Long creatorId;
        @ApiModelProperty("所有接口")
        private List<RapDataEntity> interfaces;
}
