/*

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.herocheer.ems.common.util.Const;

import java.io.Serializable;

*/
/**
 * 查询条件
 *
 * @author hejk
 * @date 2019/9/29 14:09
 *//*


public class SearchCondition<T extends Serializable> implements Serializable {

    private PageRequest<T> pageRequest;

    private QueryWrapper wrapper;

    private Class clazz;

    private T params;

    public SearchCondition() {
    }

    public SearchCondition(ListParams params, QueryWrapper wrapper, Class<T> clazz) {
        if (null != params.getParams()) {
            this.params = JSON.parseObject(params.getParams().toString(), clazz);
        }
        //避免空指针异常
        if (null == this.params) {
            try {
                this.params = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.pageRequest = params.getPage();
        this.wrapper = wrapper;
        this.clazz = clazz;
    }

    public SearchCondition(PageRequest<T> pageRequest, QueryWrapper wrapper, Class clazz) {
        this.pageRequest = pageRequest;
        this.wrapper = wrapper;
        this.clazz = clazz;
    }

    public Page<T> getPage() {
        if (null == pageRequest || null == clazz) {
            return null;
        }

        return pageRequest.getPageObj(clazz);
    }

    public T getParams() {
        return params;
    }

    public PageRequest<T> getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest<T> pageRequest) {
        this.pageRequest = pageRequest;
    }

    */
/**
     * 得到未删除的的查询
     *
     * @return
     *//*

    public QueryWrapper getDelWrapper() {
        if (null == wrapper) {
            return null;
        }

        wrapper.eq(Const.DELETE_STATE, 0);
        return wrapper;
    }

    public QueryWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(QueryWrapper wrapper) {
        this.wrapper = wrapper;
    }
}
*/
