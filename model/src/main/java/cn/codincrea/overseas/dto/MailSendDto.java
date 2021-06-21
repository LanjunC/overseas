package cn.codincrea.overseas.dto;

import lombok.Data;

@Data
public class MailSendDto {

    private String to;
    private String subject;
    private String content;
}
