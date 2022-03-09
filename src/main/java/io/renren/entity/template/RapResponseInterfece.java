package io.renren.entity.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("返回数")
public class RapResponseInterfece {
    @ApiModelProperty("结果")
    private RapDataEntity data;
}
