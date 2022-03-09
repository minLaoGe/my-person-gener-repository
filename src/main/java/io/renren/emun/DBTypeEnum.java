package io.renren.emun;


public enum DBTypeEnum {
    MYSQL("mysql"),ORACLE("oracle");
    private String name;

    DBTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
