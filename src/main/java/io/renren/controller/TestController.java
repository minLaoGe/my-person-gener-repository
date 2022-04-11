package io.renren.controller;

import io.renren.entity.test.TestIsBug;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test")
public class TestController {

    @PostMapping("/hello")
    /*
    * 这是一个方例子
    * */
    public String getTest(@RequestBody TestIsBug testIsBug){
        System.out.println(testIsBug);
        return "success";
    }

    public static void main(String[] args) {
        Boolean aBoolean=null;
        if (aBoolean){
            System.out.println(2342);
        }else {
            System.out.println(4334);
        }
    }
}
