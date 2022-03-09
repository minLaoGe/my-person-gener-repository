/*
package io.renren.utils;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import io.renren.modules.society.anno.MExcell;
import io.renren.modules.society.entity.ExcelEntity;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * @Author: Tom.Min
 * @Date: 2021/8/24 16:08
 * @Desc:
 *//*

public class PoiUtils {
    static  final  String path="D:\\c.xlsx";
    public static List<ExcelEntity> excel(Class<ExcelEntity> aclass) throws NoSuchFieldException, IllegalAccessException {
        ExcelReader reader = ExcelUtil.getReader(path);
        List<Map<String,Object>> readAll = reader.readAll();
        Class<ExcelEntity> TClass = ExcelEntity.class;
        Field[] declaredFields = TClass.getDeclaredFields();
        HashMap<String, Object> strinMap = new HashMap<>();
        for (Field declaredField : declaredFields) {
            MExcell annotation = declaredField.getAnnotation(MExcell.class);
            if (!Objects.isNull(annotation)){
                strinMap.put(annotation.value(),declaredField.getName());
            }
        }
        List<ExcelEntity> excelEntities = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : readAll) {
            ExcelEntity excelEntity = new ExcelEntity();
            for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                String head = stringObjectEntry.getKey();
                //最终设置的值
                Object value = stringObjectEntry.getValue();
                if (StringUtils.isEmpty(value)){
                    continue;
                }
                if (head!=null) {
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(head);
                    head = m.replaceAll("");
                }
                if (Objects.isNull( strinMap.get(head))){
                    continue;
                }
                String filedName = strinMap.get(head).toString();
                Class<? extends ExcelEntity> aClass = excelEntity.getClass();
                Field declaredField = aClass.getDeclaredField(filedName);
                declaredField.setAccessible(true);
                MExcell annotation = declaredField.getAnnotation(MExcell.class);

                //对具有replace属性的字段的进行处理
                if (!StringUtils.isEmpty(annotation.mreplace())&&annotation.mreplace().length>0){
                    String[] mreplace = annotation.mreplace();
                    HashMap<Object, Object> map = new HashMap<>();
                    for (String reStr : mreplace) {
                        String[] split = reStr.split("-");
                        map.put(split[0],split[1]);
                    }

                    if (!Objects.isNull(map.get(value))){
                        Class<?> type = declaredField.getType();
                        try {
                            Method m1 = type.getDeclaredMethod("valueOf",String.class);
                            // Java 反射调用静态方法 只需要将invoke方法的第一个参数设为null即可！
                            value = m1.invoke(null, map.get(value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        value=null;
                    }
                }

                //对具有formate属性字段,进行处理
                if (!StringUtils.isEmpty(annotation.format())&&!StringUtils.isEmpty(value)){
                    String format = annotation.format();
                    SimpleDateFormat formatDate = new SimpleDateFormat(format);
                    try {
                        Date parse = formatDate.parse(value.toString());
                        value=parse;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.err.println("出错了，日期转发错误");
                        return null;
                    }
                }


                //对不同类型的字段进行转化
                if (!Objects.isNull(value)&&!declaredField.getType().toString().equals(value.getClass().toString())){
                    Class<?> type = declaredField.getType();
                    try {
                        Method m1 = type.getDeclaredMethod("valueOf",String.class);
                        // Java 反射调用静态方法 只需要将invoke方法的第一个参数设为null即可！
                        value = m1.invoke(null, value.toString());
                    } catch (Exception e) {
                        try {
                            Method m1 = type.getDeclaredMethod("valueOf",Object.class);
                            // Java 反射调用静态方法 只需要将invoke方法的第一个参数设为null即可！
                            value = m1.invoke(null, value);
                        } catch (Exception e2) {
                            try {
                                Class f=piPeiClass(value);
                                Method m1 = type.getDeclaredMethod("valueOf",f);
                                // Java 反射调用静态方法 只需要将invoke方法的第一个参数设为null即可！
                                value = m1.invoke(null, value);
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }


                declaredField.set(excelEntity,value);
            }
            excelEntities.add(excelEntity);
        }
        return excelEntities;
    }
    private static Class piPeiClass(Object value) {
        if (value instanceof Long){
            return long.class;
        }else  if (value instanceof Double){
            return double.class;
        }else if (value instanceof Float){
            return float.class;
        }
        return null;
    }
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        List<ExcelEntity> excel = excel(ExcelEntity.class);
        System.out.println(excel);
    }
}
*/
