/*

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

*/
/**
 * 列表请求参数
 *
 * @author hejk
 * @date 2019/9/16 9:19
 *//*


@ApiModel(value = "列表查询", description = "列表查询")
public class ListParams<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 5135363200129090163L;

    @ApiModelProperty(value = "分页信息")
    private PageRequest<T> page;

    @ApiModelProperty(value = "查询条件")
    private T params;

    public PageRequest<T> getPage() {
        return page;
    }

    public void setPage(PageRequest<T> page) {
        this.page = page;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
*/
