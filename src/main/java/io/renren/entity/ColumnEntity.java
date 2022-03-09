package io.renren.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.Map;

/**
 * 列的属性
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:01:45
 */
@Data
public class ColumnEntity {
	//列名
    private String columnName;
    //列名类型
    private String dataType;
    //列名备注
    private String comments;
    //label
    private String lable;

    private Long length;
    
    //属性名称(第一个字母大写)，如：user_name => UserName
    private String attrName;
    //属性名称(第一个字母小写)，如：user_name => userName
    private String attrname;
    //属性类型
    private String attrType;
    //auto_increment
    private String extra;

    //是否是选择框
    private Boolean ifSelect=false;

    //选择框的数据
    private String selectData;

    //是否需要查字典表
	private Boolean ifNeedDic=false;

	//字典表的key
	private String dicKey;

	//oracle字段对应的jdbcType
	private String jdbcType;

	//下拉框 属性的值和value的map
    private Map<Object,Object> valueMap;


    public Map<Object, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<Object, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public String getLable() {
        return lable;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Boolean getIfSelect() {
        return ifSelect;
    }

    public void setIfSelect(Boolean ifSelect) {
        this.ifSelect = ifSelect;
    }

    public String getSelectData() {
        return selectData;
    }

    public void setSelectData(String selectData) {
        this.selectData = selectData;
    }

    public Boolean getIfNeedDic() {
        return ifNeedDic;
    }

    public void setIfNeedDic(Boolean ifNeedDic) {
        this.ifNeedDic = ifNeedDic;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }
}
