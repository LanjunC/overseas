package cn.codingcrea.overseas.controller;

import cn.codincrea.overseas.model.Comment;
import cn.codincrea.overseas.model.DiscussPost;
import cn.codincrea.overseas.model.User;
import cn.codingcrea.overseas.UserClient.UserFeignClient;
import cn.codingcrea.overseas.service.CommentService;
import cn.codingcrea.overseas.service.DiscussPostService;
import cn.codingcrea.overseas.utils.LoginUserIdContext;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/api")
public class DiscussPostController implements Project4Constant {

    @Autowired
    private LoginUserIdContext loginUserIdContext;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/discussPost/auth", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addDiscussPost(@RequestBody DiscussPost post) {
        Integer userId = loginUserIdContext.getUserId();
        System.out.println(userId);
        post.setUserId(userId);
        post.setCreateTime(new Date());
        discussPostService.saveDiscussPost(post);
        return Project4Utils.getJSONString(CODE_SUCCESS, "发帖成功！", null);
    }


    @GetMapping(value = "/discussPosts/{userId}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getDiscussPosts(@PathVariable("userId") int userId, @RequestParam("target") int target) {
        Page<DiscussPost> discussPostsPage = discussPostService.getDiscussPosts(userId, target);
        List<DiscussPost> discussPosts = discussPostsPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("total", discussPostsPage.getTotal());
        List<Map<String, Object>> postsAndUsers = new ArrayList<>();
        for(DiscussPost post : discussPosts) {
            Map<String, Object> postAndUser = new HashMap<>();
            User user = JSONObject.parseObject(userFeignClient.getUserInfo(post.getUserId()), User.class);
            postAndUser.put("discussPost", post);
            postAndUser.put("user", user);
            postsAndUsers.add(postAndUser);
        }
        map.put("discussPosts", postsAndUsers);
        return Project4Utils.getJSONString(CODE_SUCCESS, "ok!", map);
    }

    @GetMapping(value = "/discussPost/{discussPostId}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, @RequestParam("target") int target) {
        Map<String, Object> map = new HashMap<>();

        //帖子信息
        DiscussPost discussPost = discussPostService.getDiscussPostById(discussPostId);
        map.put("discussPost", discussPost);

        //对于作者信息，可以使用下面的多次查询，也可以在mybatis处进行联合查询（使用mybatis对联合查询的支持）
        User user = JSONObject.parseObject(userFeignClient.getUserInfo(discussPost.getUserId()), User.class);
        map.put("user", user);

        //评论信息

        Page<Comment> commentPage = commentService.getCommentByEntity(ENTITY_TYPE_POST, discussPost.getId(), target);
        map.put("commentCount", commentPage.getTotal());   //评论总数
        List<Comment> commentList = commentPage.getRecords();

        //评论：给帖子的评论
        //回复：给评论的评论
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null && !commentList.isEmpty()) {
            for(Comment comment : commentList) {
                //评论Vo
                Map<String, Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment",comment);
                //评论的作者
                commentVo.put("user",JSONObject.parseObject(userFeignClient.getUserInfo(comment.getUserId()), User.class));

                //回复列表
                Page<Comment> replyPage = commentService.getCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), -1);
                List<Comment> replyList = replyPage.getRecords();
                //回复Vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null && !replyList.isEmpty()) {
                    for(Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        //回复
                        replyVo.put("reply", reply);
                        //作者
                        replyVo.put("user",JSONObject.parseObject(userFeignClient.getUserInfo(reply.getUserId()), User.class));
                        //回复目标，可能是普通回复无目标，也可能有目标
                        User targetUser = reply.getTargetId() == 0 ? null :
                        JSONObject.parseObject(userFeignClient.getUserInfo(reply.getTargetId()), User.class);
                        replyVo.put("targetUser", targetUser);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                //回复数量
                commentVo.put("replyCount",replyPage.getTotal());

                commentVoList.add(commentVo);
            }
        }

        map.put("comments",commentVoList);

        return Project4Utils.getJSONString(CODE_SUCCESS, null, map);
    }
}
