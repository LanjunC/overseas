package cn.codingcrea.overseas.controller;

import cn.codincrea.overseas.model.Comment;
import cn.codingcrea.overseas.service.CommentService;
import cn.codingcrea.overseas.utils.LoginUserIdContext;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/api/comment")
public class CommentController implements Project4Constant {

    @Autowired
    private LoginUserIdContext loginUserIdContext;

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addDiscussPost(@RequestBody Comment comment) {
        Integer id = loginUserIdContext.getUserId();
        comment.setUserId(id);
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.saveComment(comment);
        return Project4Utils.getJSONString(CODE_SUCCESS, "评论成功！", null);
    }
}
