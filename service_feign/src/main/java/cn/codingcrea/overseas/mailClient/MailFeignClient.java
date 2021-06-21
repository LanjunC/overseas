package cn.codingcrea.overseas.mailClient;

import cn.codincrea.overseas.dto.MailSendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FeignClient(value = "service-mail")
@Repository//不加的话调用者那边一直报错提示找不到bean很烦
public interface MailFeignClient {

    @PostMapping("/inner/mail/send")
    public String sendMail(MailSendDto mailSendDto);
}
