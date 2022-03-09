/*


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

*/
/**
 * @author Ku_ker
 * 条件查询请求
 * @date 2019/4/9 14:02
 *//*


public class PageRequest<T> implements java.io.Serializable {

    private static final long serialVersionUID = 8347214428521377454L;


    @ApiModelProperty("当前页")
    private long page = 1;

    @ApiModelProperty("每页显示条数，默认 10")
    private long pageSize = 10;

    @ApiModelProperty("SQL 排序 ASC 数组")
    private String[] ascs;

    @ApiModelProperty("SQL 排序 DESC 数组")
    private String[] descs;

    public PageRequest() {
        // to do nothing
    }

    */
/**
     * 分页构造函数
     *
     * @param page
     *         当前页
     * @param pageSize
     *         每页显示条数
     *//*

    public PageRequest(long page, long pageSize) {
        if (page > 1) {
            this.page = page;
        }
        this.pageSize = pageSize;
    }


    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public String[] getAscs() {
        return ascs;
    }

    public void setAscs(String[] ascs) {
        this.ascs = ascs;
    }

    public String[] getDescs() {
        return descs;
    }

    public void setDescs(String[] descs) {

        this.descs = descs;
    }


    */
/**
     * 将字段封装到List中
     *
     * @param clazz
     *         排序的对象
     * @param list
     *         排序列表
     * @param descField
     *         字段名称列表
     *//*

    private void getOrderByFiled(Class clazz, List<String> list, List<String> descField) {
        if (null == descField || 0 == descField.size()) {
            return;
        }

        //遍历字段
        for (String fieldStr : descField) {
            getOrderByFiled(clazz, list, fieldStr);
        }

    }

    */
/**
     * 查询字段是否存在，有存在则加入到list中
     *
     * @param clazz
     *         排序的对象
     * @param list
     *         排序列表
     * @param fieldStr
     *         字段名称
     *//*

    private void getOrderByFiled(Class clazz, List<String> list, String fieldStr) {

        try {
            //查找字段
            Field field = clazz.getDeclaredField(fieldStr);
            //查找字段上的@TableField注解
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField) {
                list.add(tableField.value());

            }
        } catch (NoSuchFieldException e) {
            clazz = clazz.getSuperclass();
            if (null != clazz) {
                getOrderByFiled(clazz, list, fieldStr);
            }
        }

    }

    */
/**
     * 得到分页的信息
     *
     * @param clazz
     *         分页的对象
     *
     * @return
     *//*

    public Page<T> getPageObj(Class clazz) {

        Page<T> tPage = new Page<>(getPage(), getPageSize());

        //单独处理倒序
        if (null != getDescs()) {
            List<String> descList = new ArrayList<>();
            getOrderByFiled(clazz, descList, Arrays.asList(getDescs()));
            if (0 != descList.size()) {
                tPage.setDesc(descList.toArray(new String[descList.size()]));
            }
        }

        //单独处理正序
        if (null != getAscs()) {

            List<String> ascList = new ArrayList<>();
            getOrderByFiled(clazz, ascList, Arrays.asList(getAscs()));
            if (0 != ascList.size()) {
                tPage.setAsc(ascList.toArray(new String[ascList.size()]));
            }
        }

        return tPage;
    }
}
*/
