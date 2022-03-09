package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("所有仓库的实体类")
public class RapRepository {
    @ApiModelProperty("创建人")
    private Long creatorId;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("所有模块")
    private List<RapModule> modules;
}
