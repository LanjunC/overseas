package cn.codingcrea.overseas.UserClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-user")
@Repository
public interface UserFeignClient {
    @GetMapping("/inner/user/userInfo/{userId}")
    public String getUserInfo(@PathVariable Integer userId);
}
