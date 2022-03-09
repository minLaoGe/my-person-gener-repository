/*

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.module.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

*/
/**
 * @author Ku_ker
 * 条件查询的辅助类
 * @date 2019/5/17 10:39
 *//*


public class QueryUtils {

    private QueryUtils() {
    }

    private static final Logger log = LoggerFactory.getLogger(QueryUtils.class);

    */
/**
     * 使用_为分隔符，目前支持如下：
     * <p>
     * eq : =               如：（键：eq_name 值：kuker）
     * li : like            如：（键：li_name 值：kuker）
     * ol : or like         如：（键：ol_name 值：kuker）
     * nl : not like        如：（键：nl_name 值：kuker）
     * in : in              如：（键：in_name 值：[kuker,qaqa]）
     * on : not in          如：（键：on_name 值：[kuker,qaqa]）
     * ll : %_              如：（键：ll_name 值：kuker）
     * lr : _%              如：（键：lr_name 值：kuker）
     * ge : >=              如：（键：ge_age 值：20）
     * gt : >               如：（键：gt_age 值：20）
     * le : <=              如：（键：le_age 值：20）
     * lt : <               如：（键：lt_age 值：20）
     * ne : !=              如：（键：ne_age 值：20）
     * bt : between and     如：（键：bt_createTime 值：2019-01-01 12:00:00_2019-02-02-12:00:00）
     * nbt : not between    如：（键：nbt_createTime 值：2019-01-01 12:00:00_2019-02-02-12:00:00）
     *//*

    private final static String SPLIT_REGEX = "_";

    */
/**
     * 参数对象
     *//*

    private static final String PARAMS_OBJ = "params";

    public static final String EQ = "eq";
    public static final String LI = "li";
    public static final String OL = "ol";
    public static final String NL = "nl";
    public static final String IN = "in";
    public static final String ON = "on";
    public static final String LL = "ll";
    public static final String LR = "lr";
    public static final String GE = "ge";
    public static final String GT = "gt";
    public static final String LE = "le";
    public static final String LT = "lt";
    public static final String NE = "ne";
    public static final String BT = "bt";
    public static final String NBT = "nbt";


    */
/**
     * between的值长度
     *//*

    private static final int BETWEEN_VALUES_LENGTH = 2;
    */
/**
     * id查询
     *//*

    private static final String ID = "id";

    */
/**
     * 得到查询条件,默认使用like
     *
     * @param json
     *         请求的json
     * @param clazz
     *         查询的对象
     *
     * @return
     *//*

    public static QueryWrapper getWrapper(String json, Class clazz) {

        QueryWrapper query = new QueryWrapper();
        Map<String, String> map = getRequestMap(json);

        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            //判断是否为空
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }

            if (isFilter(key)) {
                getWrapperByRules(query, clazz, key, entry.getValue());
            } else {
                //没遵循规则的按默认查询
                createQuery(query, clazz, key, entry.getValue());
            }


        }

        return query;
    }
    //copy by tom.min
    public static QueryWrapper getWrapperUpLevel(String json, Class clazz,String prefix) {

        QueryWrapper query = new QueryWrapper();
        Map<String, String> map = getRequestMap(json);

        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            //判断是否为空
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }

            if (isFilter(key)) {
                getWrapperByRules(query, clazz, key, entry.getValue());
            } else {
                //没遵循规则的按默认查询
                createQuery(query, clazz, key, entry.getValue(),prefix);
            }
            if ("fwzCodes".equals(key)){
                List<String> strings = Arrays.asList(entry.getValue());
                query.in("f.code",strings);
            }

        }

        return query;
    }

    */
/**
     * 得到查询条件,默认使用like
     *
     * @param json
     *         请求的json
     * @param object
     *         查询的对象
     *
     * @return
     *//*

    public static QueryWrapper getWrapper(String json, Object object) {

        QueryWrapper query = new QueryWrapper();
        Map<String, String> map = getRequestMap(json);
        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            //判断是否为空
            String value = map.get(key);
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if (isFilter(key)) {
                getWrapperByRules(query, object.getClass(), key, map.get(key));
            } else {
                //没遵循规则的按默认查询
                createListQuery(object, object.getClass(), query);
            }

        }

        return query;
    }


    public static String getBodytxt(HttpServletRequest request) {
        String str;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = request.getReader();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return sb.toString();
    }

    */
/**
     * 创建一个like的查询语句
     *
     * @param key
     *         键
     * @param clazz
     *         class对象
     * @param query
     *         查询实体
     *//*

    public static void createQuery(QueryWrapper query, Class clazz, String key, String value) {

        if (ID.equals(key)) {
            query.eq(ID, value);
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {

            if (field.getName().equals(key)) {

                TableField tableField = field.getAnnotation(TableField.class);
                if (null != tableField) {
                    setLikeOrEq(query, value, field, tableField);
                }
                return;
            }

        }
        //查找父类
        if (null != clazz.getSuperclass()) {
            createQuery(query, clazz.getSuperclass(), key, value);
        }
    }
    public static void createQuery(QueryWrapper query, Class clazz, String key, String value,String prefix) {

        if (ID.equals(key)) {
            query.eq(ID, value);
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {

            if (field.getName().equals(key)) {

                TableField tableField = field.getAnnotation(TableField.class);
                if (null != tableField) {
                    setLikeOrEq(query, value, field, tableField,prefix);
                }
                return;
            }

        }
        //查找父类
        if (null != clazz.getSuperclass()) {
            createQuery(query, clazz.getSuperclass(), key, value,prefix);
        }
    }


    private static void setLikeOrEq(QueryWrapper query, Object value, Field field, TableField tableField) {

        //判断值是否为空
        if (!"".equals(value) && null != value) {

            //判断是否为String类型
            if (field.getType() == String.class) {

                //且的模糊查找
                query.like(tableField.value(), value);
            } else {
                //非String类型使用=
                query.eq(tableField.value(), value);
            }
        }
    }

     private static void setLikeOrEq(QueryWrapper query, Object value, Field field, TableField tableField,String preffix) {

        //判断值是否为空
        if (!"".equals(value) && null != value) {

            //判断是否为String类型
            if (field.getType() == String.class) {
                if (StringUtils.isEmpty(preffix)){
                    //且的模糊查找
                    query.like(tableField.value(), value);
                }else {
                    //且的模糊查找
                    query.like(preffix+"."+tableField.value(), value);
                }

            } else {
                if (StringUtils.isEmpty(preffix)){
                    //非String类型使用=
                    query.eq(tableField.value(), value);
                }else {
                    //非String类型使用=
                    query.eq(preffix+"."+tableField.value(), value);
                }
            }
        }
    }

    */
/**
     * 创建一个like的查询语句
     *
     * @param object
     *         数据对象
     * @param clazz
     *         class对象
     * @param query
     *         查询实体
     *//*

    public static void createListQuery(Object object, Class clazz, QueryWrapper query) {

        for (Field field : clazz.getDeclaredFields()) {

            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField) {
                try {
                    //设置值的读写
                    field.setAccessible(true);
                    Object value = field.get(object);
                    setLikeOrEq(query, value, field, tableField);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
        }
        //查找父类
        if (null != clazz.getSuperclass()) {
            createListQuery(object, clazz.getSuperclass(), query);
        }
    }

    */
/**
     * 过滤空字符串
     *
     * @param object
     *
     * @return
     *//*

    public static QueryWrapper getOrLikeWrapper(Object object) {

        Class clazz = object.getClass();

        QueryWrapper query = new QueryWrapper();
        createListQuery(object, clazz, query);
        return query;
    }

    */
/**
     * 通过规则获取获取查询对象
     *
     * @param wrapper
     *         要构建的查询对象
     * @param clazz
     *         Class
     * @param key
     *         键
     * @param values
     *         值
     *//*

    private static void getWrapperByRules(QueryWrapper wrapper, Class clazz, String key, String... values) {

        String[] keys = key.split(SPLIT_REGEX);
        String fieldValue = getTableFieldValue(clazz, keys[1]);
        if (null == fieldValue) {
            return;
        }
        switch (keys[0]) {

            case EQ:
                // =
                wrapper.eq(fieldValue, values[0]);
                break;
            case LI:
                // like
                wrapper.like(fieldValue, values[0]);
                break;
            case OL:
                // or like
                wrapper.or(true).like(fieldValue, values[0]);
                break;
            case NL:
                // not like
                wrapper.notLike(fieldValue, values[0]);
                break;
            case IN:
                // in
                wrapper.in(fieldValue, values);
                break;
            case ON:
                // not in
                wrapper.notIn(fieldValue, values);
                break;
            case LL:
                // %_
                wrapper.likeLeft(fieldValue, values[0]);
                break;
            case LR:
                // _%
                wrapper.likeRight(fieldValue, values[0]);
                break;
            case GE:
                // >=
                wrapper.ge(fieldValue, values[0]);
                break;
            case GT:
                // >
                wrapper.gt(fieldValue, values[0]);
                break;
            case LE:
                // <=
                wrapper.le(fieldValue, values[0]);
                break;
            case LT:
                // <
                wrapper.lt(fieldValue, values[0]);
                break;
            case NE:
                // !=
                wrapper.ne(fieldValue, values[0]);
                break;
            case BT:
                // between 如：（键：bt_createTime 值：2019-01-01 12:00:00_2019-02-02-12:00:00）
                between(wrapper, fieldValue, values);
                break;
            case NBT:
                // not between 如：（键：bt_createTime 值：2019-01-01 12:00:00_2019-02-02-12:00:00）
                notBetween(wrapper, fieldValue, values);
                break;

            default:
                break;
        }

    }

    */
/**
     * 字段的between查询
     *
     * @param wrapper
     *         查询对象
     * @param fieldValue
     *         数据库字段名称
     * @param values
     *         查询的值
     *//*

    private static void between(QueryWrapper wrapper, String fieldValue, String... values) {

        String[] times = values[0].split(SPLIT_REGEX);
        if (BETWEEN_VALUES_LENGTH == times.length) {
            wrapper.between(fieldValue, times[0], times[1]);
        }
    }


    */
/**
     * 字段的notBetween查询
     *
     * @param wrapper
     *         查询对象
     * @param fieldValue
     *         数据库字段名称
     * @param values
     *         查询的值
     *//*

    private static void notBetween(QueryWrapper wrapper, String fieldValue, String... values) {

        String[] times = values[0].split(SPLIT_REGEX);
        if (BETWEEN_VALUES_LENGTH == times.length) {
            wrapper.notBetween(fieldValue, times[0], times[1]);
        }
    }

    */
/**
     * class字段名称得到数据库的字段名称
     *
     * @param clazz
     *         Class
     * @param fieldStr
     *         class字段名称
     *
     * @return
     *//*

    private static String getTableFieldValue(Class clazz, String fieldStr) {

        String value = null;
        try {
            //查找字段
            Field field = clazz.getDeclaredField(fieldStr);
            //查找字段上的@TableField注解
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField) {
                value = tableField.value();
            }
        } catch (NoSuchFieldException e) {

            //子类找不到，递归查找父类
            clazz = clazz.getSuperclass();
            if (null != clazz) {
                return getTableFieldValue(clazz, fieldStr);
            }
        }

        return value;
    }

    */
/**
     * 是否走过滤
     *
     * @param key
     *         键
     *
     * @return
     *//*

    private static boolean isFilter(String key) {
        return key.contains(SPLIT_REGEX);
    }

    */
/**
     * 得到请求参数的map
     *
     * @param json
     *         请求的json
     *
     * @return
     *//*

    public static Map<String, String> getRequestMap(String json) {

        JSONObject object = JSON.parseObject(json);
        JSONObject wrapperObj = object.getJSONObject(PARAMS_OBJ);
        Iterator<String> it = wrapperObj.keySet().iterator();
        Map<String, String> map = new HashMap<>();
        while (it.hasNext()) {

            // 获得key
            String key = it.next();
            String value = wrapperObj.getString(key);
            map.put(key, value);
        }
        return map;
    }

    */
/**
     * 创建map查询
     *
     * @param map
     * @param object
     *
     * @return
     *//*

    public static QueryWrapper getMapWrapper(Map<String, String> map, Object object) {
        Class clazz = object.getClass();
        QueryWrapper query = new QueryWrapper();
        createMapQuery(map, query, clazz);
        return query;

    }

    public static void createMapQuery(Map<String, String> map, QueryWrapper query, Class clazz) {
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val)) {
                String[] str = key.split("_");
                String prefix = str[0];
                if (str.length <= 1) {
                    continue;
                }
                checkQueryParam(str, val, query, clazz, prefix);
            }
        }
    }

    private static void checkQueryParam(String[] str, String val, QueryWrapper query, Class clazz, String prefix) {
        if (str.length == 2 && val != null) {
            String textField = matchField(clazz, str[1]);
            // 进行常用的查询条件操作,可进行扩展
            if (null != textField) {
                if ("eq".equals(prefix)) {
                    query.eq(textField, val);
                } else if ("le".equals(prefix)) {
                    query.le(textField, val);
                } else if ("ge".equals(prefix)) {
                    query.ge(textField, val);
                } else if ("like".equals(prefix)) {
                    query.like(textField, val);
                }
            }
            // 进行区间查询操作
        } else if ("bt".equals(prefix) && str.length == 3) {
            String textField = matchField(clazz, str[2]);
            if (null != textField) {
                if ("start".equals(str[1])) {
                    query.ge(textField, val);
                } else if ("end".equals(str[1])) {
                    query.le(textField, val);
                }
            }
        }
    }

    */
/**
     * 匹配数据库字段
     *
     * @param clazz
     * @param s
     *
     * @return
     *//*

    private static String matchField(Class clazz, String s) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            TableField tableField = fields[i].getAnnotation(TableField.class);
            if (null != tableField) {
                //设置值的读写
                fields[i].setAccessible(true);
                if (s.equals(fields[i].getName())) {
                    return tableField.value();
                }
            }
        }
        //查找父类
        if (null != clazz.getSuperclass()) {
            return matchField(clazz.getSuperclass(), s);
        }
        return null;
    }

}
*/
