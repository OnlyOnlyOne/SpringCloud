package cn.itcast.service.controller;

import cn.itcast.service.pojo.TbUser;
import cn.itcast.service.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;







import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getuser/{id}")
    public TbUser queryUserById(@PathVariable("id") Long id) {
        TbUser user = userService.queryUserById(id);
        return user;
    }

    @PostMapping("/getuser")
    public TbUser queryUser(@RequestParam() Long id) {
        TbUser user = userService.queryUserById(id);
        return user;




    }
}
