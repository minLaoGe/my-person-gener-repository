/*
package io.renren.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

*/
/*
*
 * @Author: Tom.Min
 * @Date: 2021/8/4 10:27
 * @Desc:
  *//*


@Slf4j
public class ReqParam<T> {
    Map<String, Object> page;

    Map<String,Object> params;
    private  final  String EQ="EQ";
    private  final  String LT="LT";
    private  final  String GT="GT";
    private  final  String LE="LE";
    private final  String GE="GE";
    private final  String NOTIN="NOTIN";
    private final  String IN="IN";
    private  final  String NE="NE";
    private final  String LIKE="LIKE";
    private final  String RLIKE="RLIKE";
    private final  String LLIKE="LLIKE";
    //只有一个
    private final  String SPILT="SPILT";
    private final  String ORDERDESC="ORDERDESC";
    private final  String ASC="ASC";

    public QueryWrapper<T> getQueryWrapper(Class<T> clazz){
        QueryWrapper<T> wrapper = gettQueryWrapper();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        ifEnableDelete(clazz, wrapper);
        composeQueryWrapper(clazz, wrapper, entries);
        return wrapper;
    }


    private QueryWrapper<T> gettQueryWrapper() {
        return new QueryWrapper<>();
    }

    */
/**
     * 组装QueryWrapper条件
     * @param clazz
     * @param wrapper
     * @param entries
     *//*

    private void composeQueryWrapper(Class<T> clazz, QueryWrapper<T> wrapper, Set<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            try {
                String[] propArr = entry.getKey().split("_");
                String condition = propArr[0];
                String prop = propArr[1];
                Field declaredField = clazz.getDeclaredField(prop);
                declaredField.setAccessible(true);

                TableField annotation = declaredField.getAnnotation(TableField.class);
                if (Objects.isNull(annotation)){
                    continue;
                }
                String anno = annotation.value();
                String key=anno;
                Object value = entry.getValue();
                if (StringUtils.isEmpty(value)){
                    continue;
                }
                if (StringUtils.isEmpty(key)){
                    composeHavingCondition(wrapper,condition,prop,value);
                }else {
                    composeCondition(wrapper, condition, key, value);
                }
            } catch (Exception e) {
            }

        }
    }

    private void composeHavingCondition(QueryWrapper<T> wrapper, String condition, String key, Object value) {
        String sign = switchCondition(condition);
        if (Objects.isNull(sign)) return;
        wrapper.having(key +sign+"   {0} " ,value);
    }
    public String switchCondition(String sign){
        switch (sign){
            case EQ:
                return " = ";
            case  LT:
                return " < ";
            case GT:
                return " > ";
            case  LE:
                return " <= ";
            case GE:
                return " >= ";
            case NOTIN:
                return  " not in ";
            case IN:
                return  " in ";
            case NE:
                return  " <> ";
            default:
                return null;
        }

    }





    private void ifEnableDelete(Class<T> clazz, QueryWrapper<T> wrapper) {
        Field declaredField2 = null;
        try {
            declaredField2 = clazz.getDeclaredField("ifDelete");
            if (!Objects.isNull(declaredField2)){
                wrapper.ne("if_delete",1);
            }
        } catch (NoSuchFieldException e) {

        }
        try {
            declaredField2 = clazz.getDeclaredField("ifDetele");
            if (!Objects.isNull(declaredField2)){
                wrapper.ne("if_detele",1);
            }
        } catch (NoSuchFieldException e) {

        }
    }

    private void composeCondition(QueryWrapper<T> wrapper, String condition, String key, Object value) {
        switch (condition){
            case EQ:
                wrapper.eq(key, value);
                break;
            case  LT:
                wrapper.lt(key, value);
                break;
            case GT:
                wrapper.gt(key, value);
                break;
            case  LE:
                wrapper.le(key, value);
                break;
            case GE:
                wrapper.ge(key, value);
                break;
            case NOTIN:
                List notIn = (ArrayList) value;
                wrapper.notIn(key, notIn);
                break;
            case IN:
               List in = (ArrayList) value;
                wrapper.in(key, in);
                break;
            case NE:
                wrapper.ne(key, value);
                break;
            case LIKE:
                wrapper.like(key, value);
                break;
            case SPILT:
                List value1 = (List) value;
                wrapper.and(s-> value1.stream().forEach(code->s.or().apply("FIND_IN_SET({0},"+key+")",code)));
                break;
            case ORDERDESC:
                wrapper.orderByDesc(key);
                break;
            case ASC:
                wrapper.orderByAsc(key);
                break;
        }
    }

    public Map<String, Object> getPage() {
//         if(Objects.isNull(this.page)) page=new HashMap<>();
         return page;
    }

    private void setPage(Map<String, Object> page) {
        this.page = page;
    }


    public IPage<T> disPageParams(){
        return new Query<T>().getPage2(this.getPage());
    }
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


}
*/
