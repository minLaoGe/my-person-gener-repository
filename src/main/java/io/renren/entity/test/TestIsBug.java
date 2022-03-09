package io.renren.entity.test;

public class TestIsBug {
    public Boolean isSuccess;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "TestIsBug{" +
                "isSuccess=" + isSuccess +
                '}';
    }
}
