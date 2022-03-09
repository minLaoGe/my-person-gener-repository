package io.renren.entity.test;

public class TestA
{
    private TestB testB;

    public TestA(TestB testB) {
        this.testB = testB;
    }

    public void a(){
        testB.b();
    }
    public TestB getTestB(){
        return testB;
    }
    public void setTestB(TestB testB){
        this.testB=testB;
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
