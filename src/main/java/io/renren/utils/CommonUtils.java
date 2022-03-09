/*
package io.renren.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.async.SpringUtil;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.disable.DisableDic;
import io.renren.modules.health.mlg.entity.HealthInstitueEntity;
import io.renren.modules.health.mlg.service.HealthInstitueService;
import io.renren.modules.pagemodule.vo.ReqParam;
import io.renren.modules.shop.entity.TAddressTownEntity;
import io.renren.modules.shop.service.TAddressTownService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

*/
/**
 * @Author: Tom.Min
 * @Date: 2021/8/25 16:41
 * @Desc:
 *//*

public class CommonUtils {

    private static final int[] Weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,
            1 }; // 加权因子
    private static final String[] Verifycode = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
            "3", "2" }; // 校验码

    */
/**
     * 从身份证号码中获取性别
     *
     * @param idCard
     *            身份证号码
     * @return 性别,'F'为女性,'M'为男性
     *//*

    public static Byte getSexFromIdCard(String idCard) {
        if (idCard.length() == 15) { // 如果是15位,转换为18位
            idCard = proIdCard15to18(idCard);
        }
        int a = Integer.parseInt(idCard.substring(16, 17)); // 取倒数第2位
        if (a % 2 == 0) { // 1为女性,0为男性
            return 1;
        } else {
            return 0;
        }
    }
    */
/**
     * 15位身份证号码转换为18位
     *
     * @param idCard
     *            15位身份证号码
     * @return newidCard 扩充后的18位身份证号码
     *//*

    public static String proIdCard15to18(String idCard) {
        int i, j, s = 0;
        String newidCard;
        newidCard = idCard;
        newidCard = newidCard.substring(0, 6) + "19"
                + newidCard.substring(6, idCard.length());
        for (i = 0; i < newidCard.length(); i++) {
            j = Integer.parseInt(newidCard.substring(i, i + 1)) * Weight[i];
            s = s + j;
        }
        s = s % 11;
        newidCard = newidCard + Verifycode[s];
        return newidCard;
    }
    public static  boolean isXieTong(SysUserEntity entity){
        if (!Objects.isNull(entity.getStype())){
            return DisableDic.type_xt.equals(entity.getStype().toString());
        }
        return false;
    }
    public static boolean isShiCanLian(SysUserEntity userEntity){
        if (!isXieTong(userEntity)&&(Objects.isNull(userEntity.getTowncode())||userEntity.getTowncode().equals("3301"))){
            return true;
        }
        return false;
    }
    //获取健康管理系统协同端/治理端 用户的组织id
    public static  Long getUserInstatituId(){
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        HealthInstitueEntity one = getInstitute(userEntity);
        if (one == null) return null;
        return  one.getId();
    }

    private static HealthInstitueEntity getInstitute(SysUserEntity userEntity) {
        String instituteId = userEntity.getInstituteId();
        HealthInstitueService bean = SpringUtil.getBean(HealthInstitueService.class);
        HealthInstitueEntity one = bean.getOne(new QueryWrapper<HealthInstitueEntity>().eq(HealthInstitueEntity.mlgGetInstituteNumberField(), instituteId)
        .eq(HealthInstitueEntity.mlgGetIfDeleteField(),0)
        );
        if (Objects.isNull(one)) {
            return null;
        }
        return one;
    }

    //获取健康管理系统协同端/治理端 用户的组织id
    public static  Long getUserInstatituId(SysUserEntity userEntity){
        HealthInstitueEntity institute = getInstitute(userEntity);
        if (Objects.isNull(institute)){
            return null;
        }
        return  institute.getId();
    }
    //获取健康管理系统协同端/治理端 用户的组织名字
    public static  String getUserInstatituName(SysUserEntity userEntity){
        if (Objects.isNull(userEntity)){
             userEntity = ShiroUtils.getUserEntity();
        }
        if (isXieTong(userEntity)){
            HealthInstitueEntity institute = getInstitute(userEntity);
            if (Objects.isNull(institute)){
                return null;
            }
            return  institute.getInstituteName();
        }else if (isShiCanLian(userEntity)){
            return "市残联";
        }else {
            String towncode = userEntity.getTowncode();
            TAddressTownService bean = SpringUtil.getBean(TAddressTownService.class);
            TAddressTownEntity towncode1 = bean.getOne(
                    new QueryWrapper<TAddressTownEntity>()
                            .eq("towncode", towncode)
            );
            return towncode1.getTownname()+"区残联";
        }

    }
    //获取健康管理的组织
    public static HealthInstitueEntity getInstituteEntity(){
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        HealthInstitueEntity institute = getInstitute(userEntity);
        if (Objects.isNull(institute)){
            return null;
        }
        return institute;
    }
    public static String getFieldString(Class aClass,String str) {
        try {
            Field reason = aClass.getDeclaredField(str);
            TableField annotation = reason.getAnnotation(TableField.class);
            String value = annotation.value();
            return value;
        } catch (NoSuchFieldException e) {
           return null;
        }
    }

    public static int calcAge(String idCard) {
        try {
            int year = Integer.parseInt(idCard.substring(6, 10));
            int month = Integer.parseInt(idCard.substring(10, 12));
            int dayOfMonth = Integer.parseInt(idCard.substring(12, 14));
            LocalDate birthday = LocalDate.of(year, month, dayOfMonth);
            return Period.between(birthday, LocalDate.now()).getYears();
        }catch (Exception e) {
            //发生任何异常快速返回一个无效值
            return -1;
        }
    }

    */
/**
     * notice:  The toString methods of this Object Should never be return a "". if do,
     * then you will get exception end;
     * @param
     * @return
     *//*

    public static  boolean isAllEmpty(Object... a){
        for (Object t : a) {
            if (!Objects.isNull(t)&&!"".equals(t.toString())&&!ObjectIfEmpty(t)){
                return false;
            }
        }

        return true;
    }

    */
/**
     * judge params If All Not Empty eg:  true=isAllNotEmpyt("sdf",Arrays.asList("sdf",s),3)
     * @param a
     * @return
     *//*

    public static  boolean isAllNotEmpty(Object... a){
        for (Object t : a) {
            if (Objects.isNull(t)||"".equals(t.toString())||ObjectIfEmpty(t)){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
    }

    private static boolean ObjectIfEmpty(Object s) {
        if (s instanceof Collection){
            return ((Collection<?>) s).isEmpty();
        }else if (s instanceof Object[]){
            return ((Object[]) s).length<=0;
        }else  if (s instanceof Map){
            return ((Map<?, ?>) s).isEmpty();
        }
        return false;
    }


    //不是全为空，如果入参有一个
    public static boolean isNotAllEmpty(Object... a){
        return !isAllEmpty(a);
    }
    */
/**
     * notice:  The toString methods of this Object Should never be return a "". if do,
     * then you will get exception end;
     * @param s
     * @return
     *//*

    public static  boolean isEmpty(Object s){
        if (!Objects.isNull(s)&&!"".equals(s.toString())&&!ObjectIfEmpty(s)){
            return false;
        }
        return true;
    }
    public static boolean isNotEmpty(Object s){
        return !isEmpty(s);
    }

    */
/**
     * notice that the param of  otherCondition shuldn't be null, if you do, you will get a null point exception
     * @param s
     * @param otherCondition 提交预期的正确解决，有一个为false返回false
     * @return
     *//*

    public static boolean isNotEmpty(Object s,Boolean... otherCondition){
        boolean ifAllTrue=!isEmpty(s);
        for (Boolean aBoolean : otherCondition) {
            if (!(ifAllTrue=aBoolean&&ifAllTrue)){
                 break;
            };
        }
        return ifAllTrue;
    }

    */
/**
     * 获取当前账号所在的townCode 和 townName
     * @return
     *//*

    public static TAddressTownEntity getCurrentUserServiceAreaEntity(){
        TAddressTownService townService = (TAddressTownService) SpringUtil.getBean("tAddressTownService");
        String towncode = ShiroUtils.getUserEntity().getTowncode();
        if (Objects.isNull(towncode)){
            return null;
        }
        TAddressTownEntity towncode1 = townService.getOne(new QueryWrapper<TAddressTownEntity>()
                .eq("towncode", towncode)
        );
        return towncode1;
    }

    */
/**
     * 给SPILT传的不是数组使用的。组装条件
     * @param wrapper
     * @param sportsRecruitingEntityClass
     * @param param
     *//*

    public static <T>void composeQueryWrapper(QueryWrapper<T> wrapper, Class<T> sportsRecruitingEntityClass, ReqParam param) {
        Map<String, Object> params = param.getParams();
        composeCondition(wrapper, sportsRecruitingEntityClass, params,null);
    }
    */
/**
     * 给SPILT传的不是数组使用的。组装特定条件
     * @param wrapper
     * @param sportsRecruitingEntityClass
     * @param param
     *//*

    public static <T> void composeQuerySpecilWrapper(QueryWrapper<T> wrapper, Class sportsRecruitingEntityClass,ReqParam<T> param,List<String> conditionList) {
        composeCondition(wrapper, sportsRecruitingEntityClass, param.getParams(),conditionList);
    }

    private static <T> void composeCondition(QueryWrapper<T> wrapper, Class sportsRecruitingEntityClass, Map<String, Object> map,List<String> conditionList) {
        for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
            String key = stringObjectEntry.getKey();
            if (!Objects.isNull(key) && key.startsWith("SPILT")&&(Objects.isNull(conditionList)||conditionList.contains(key))) {
                String[] s = key.split("_");
                String tableColumProperty = s[1];
                Object value = stringObjectEntry.getValue();
                String fieldString = CommonUtils.getFieldString(sportsRecruitingEntityClass, tableColumProperty);
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(fieldString)) {
                    continue;
                }
                wrapper.and(item -> item.apply("FIND_IN_SET({0}," + fieldString + ")", value));
            }
        }
    }

    */
/**
     *  0 市残联， 1区残联， 2街镇残联，=》 治理段    9协同段
     * @param userEntity
     * @return
     *//*

    public static String getTownLevel(SysUserEntity userEntity) {
        if (isShiCanLian(userEntity)){
            return "0";
        }else if (isXieTong(userEntity)){
            return "9";
        }else if (!Objects.isNull(userEntity.getTowncode())&&userEntity.getTowncode().length()<=6){
             return "1";
        }else  if (!Objects.isNull(userEntity.getTowncode())&&userEntity.getTowncode().length()>6){
            return "2";
        }
        return "9";
    }
}
*/
