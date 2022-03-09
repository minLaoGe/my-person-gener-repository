package io.renren.entity.template;

import lombok.Data;

import java.util.List;

@Data
public class RapUpdateReqBody {
    private List<RapDataProperty> properties;
    private RapUpdateSummary summary;
}
