/*

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.ems.common.exception.SearchException;
import com.herocheer.ems.common.request.ListParams;
import com.herocheer.ems.common.request.SearchCondition;
import com.herocheer.module.core.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

*/
/**
 * 查询辅助类
 *
 * @author hejk
 * @date 2019/9/29 14:14
 *//*


public class SearchUtils {

    private SearchUtils() {
    }

    */
/**
     * 得到搜索条件
     *
     * @param request
     * @param params
     *         查询的参数
     * @param <T>
     *
     * @return
     *//*

    public static <T extends Serializable> SearchCondition<T> getSearchCondition(HttpServletRequest request, ListParams<T> params, Class<T> clazz) {

        String json = QueryUtils.getBodytxt(request);
        if (StringUtils.isEmpty(json)) {
            throw new SearchException("无效参数查询请求");
        }

        SearchCondition<T> search = null;
        try {

            params = JSON.parseObject(json, new TypeReference<ListParams<T>>() {
            });
            QueryWrapper wrapper = QueryUtils.getWrapper(json, clazz);

            search = new SearchCondition<>(params, wrapper, clazz);
        } catch (Exception e) {
            throw new SearchException(e.getMessage());
        }
        return search;
    }

   */
/**
     * 得到搜索条件
     *
     * @param request
     * @param params
     *         查询的参数
     * @param <T>
     *
     * @return
     *//*

    public static <T extends Serializable> SearchCondition<T> getSearchConditionAndContainsNoTableFildesAnnotaion(HttpServletRequest request, ListParams<T> params, Class<T> clazz) {

        String json = QueryUtils.getBodytxt(request);
        if (StringUtils.isEmpty(json)) {
            throw new SearchException("无效参数查询请求");
        }

        SearchCondition<T> search = null;
        try {

            params = JSON.parseObject(json, new TypeReference<ListParams<T>>() {
            });
            QueryWrapper wrapper = QueryUtils.getWrapper(json, clazz);

            search = new SearchCondition<>(params, wrapper, clazz);
        } catch (Exception e) {
            throw new SearchException(e.getMessage());
        }
        return search;
    }

    //copy by tom.min
    public static <T extends Serializable> SearchCondition<T> getSearchConditionUpLevel(HttpServletRequest request, ListParams<T> params, Class<T> clazz,String prefix) {

        String json = QueryUtils.getBodytxt(request);
        if (StringUtils.isEmpty(json)) {
            throw new SearchException("无效参数查询请求");
        }

        SearchCondition<T> search = null;
        try {

            params = JSON.parseObject(json, new TypeReference<ListParams<T>>() {
            });
            QueryWrapper wrapper = QueryUtils.getWrapperUpLevel(json, clazz,prefix);

            search = new SearchCondition<>(params, wrapper, clazz);
        } catch (Exception e) {
            throw new SearchException(e.getMessage());
        }
        return search;
    }

}
*/
