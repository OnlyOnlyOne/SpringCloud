package cn.itcast.service.service;


import cn.itcast.service.mapper.TbUserMapper;
import cn.itcast.service.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    public TbUserMapper tbUserMapper;


    public TbUser queryUserById(Long id) {
        return tbUserMapper.selectUserById(id);
    }
}
