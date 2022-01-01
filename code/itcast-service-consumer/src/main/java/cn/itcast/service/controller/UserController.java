package cn.itcast.service.controller;


import cn.itcast.service.pojo.TbUser;
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
public class UserController {
    @Autowired
    private RestTemplate restTemplate;



    @Autowired
    private DiscoveryClient discoveryClient; //包含了拉取的所有服务信息




    @GetMapping("/user")
    @ResponseBody
    public TbUser queryUserById(@RequestParam("id") Long id) {
        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
        ServiceInstance serviceInstance = instances.get(0);
        TbUser user = this.restTemplate.getForObject(
                "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/getuser/" + id, TbUser.class);
        /*反序列回TbUser.class*/
        return user;
    }
}
