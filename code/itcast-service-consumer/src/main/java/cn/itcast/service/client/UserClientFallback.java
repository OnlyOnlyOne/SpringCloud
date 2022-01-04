package cn.itcast.service.client;

import cn.itcast.service.pojo.TbUser;
import org.springframework.stereotype.Component;

@Component()
public class UserClientFallback implements UserClient {
    @Override
    public TbUser queryUserById(Long id) {
        TbUser tbUser = new TbUser();
        tbUser.setUsername("服务器正忙");
        return tbUser;
    }
}
