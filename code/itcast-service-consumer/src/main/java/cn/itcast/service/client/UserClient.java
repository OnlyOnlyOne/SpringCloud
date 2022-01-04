package cn.itcast.service.client;

import cn.itcast.service.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Provider;

@FeignClient(value = "service-provider",fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("user/getuser/{id}")
    public TbUser queryUserById(@PathVariable("id") Long id);
}
