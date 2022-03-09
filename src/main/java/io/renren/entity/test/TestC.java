package io.renren.entity.test;

public class TestC {
    private  TestA testA;
    public void c() {
        testA.a();
    }

    public TestA getTestA() {
        return testA;
    }

    public TestC(TestA testA) {
        this.testA = testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }
}
