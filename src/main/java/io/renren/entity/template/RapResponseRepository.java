package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("仓库类")
public class RapResponseRepository {
    @ApiModelProperty("结果")
    private RapRepository data;
}
