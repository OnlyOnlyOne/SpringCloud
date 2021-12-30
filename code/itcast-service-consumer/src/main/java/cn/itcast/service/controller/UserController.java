package cn.itcast.service.controller;


import cn.itcast.service.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("consumer/user")
public class UserController {
    @Autowired
    private RestTemplate restTemplate;


    @GetMapping()
    @ResponseBody
    public TbUser queryUserById(@RequestParam("id") Long id) {
        TbUser user = this.restTemplate.getForObject(
                "http://localhost:8081/user/getuser/" + id, TbUser.class);
        /*反序列回TbUser.class*/
        return user;
    }
}
