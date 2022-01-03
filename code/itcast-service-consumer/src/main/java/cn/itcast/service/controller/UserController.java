package cn.itcast.service.controller;


import cn.itcast.service.pojo.TbUser;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/consumer")
@DefaultProperties(defaultFallback = "fallbackMethod") //定义全局的熔断方法
public class UserController {
    @Autowired
    private RestTemplate restTemplate;



//    @Autowired
//    private DiscoveryClient discoveryClient; //包含了拉取的所有服务信息




    @GetMapping("/user")
    @ResponseBody
    @HystrixCommand
    public String queryUserById(@RequestParam("id") Long id) {
//        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
//        ServiceInstance serviceInstance = instances.get(0);
        return this.restTemplate.getForObject(
                "http://service-provider/user/getuser/" + id, String.class);
        /*反序列回TbUser.class*/

    }

    //定义一个熔断方法
    public String fallbackMethod() {
        TbUser tbUser = new TbUser();
        return "服务器正忙，请稍后再试";
    }
}
