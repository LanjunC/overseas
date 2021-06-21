package cn.codincrea.overseas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class DiscussPost {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer UserId;
    private Integer CollegeId;

    private String Title;
    private String content;

    private Integer type;
    private Integer status;

    private Date createTime;
    private Integer commentCount;
    private Double score;
}
