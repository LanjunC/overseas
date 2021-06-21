package cn.codingcrea.overseas.service;

import cn.codincrea.overseas.model.DiscussPost;
import cn.codingcrea.overseas.mapper.DiscussPostMapper;
import cn.codingcrea.overseas.utils.Project4Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DiscussPostService implements Project4Constant {

    @Resource
    DiscussPostMapper discussPostMapper;

    public int saveDiscussPost(@NonNull DiscussPost post) {
        return discussPostMapper.insert(post);
    }


//    public getDiscussPosts(int userId) {
//        discussPostMapper.selectPage(, )
//    }

    public int getDiscussPostsRows(int userId) {
        if(userId == 0) {   //userId为0查询所有数量
            return discussPostMapper.selectCount(new QueryWrapper<>());
        }
        return discussPostMapper.selectCount(new QueryWrapper<DiscussPost>().eq("user_id", userId));
    }

    public Page<DiscussPost> getDiscussPosts(int userId, int target) {
        Page<DiscussPost> page = new Page<>(target, DISCUSSPOSTS_PER_PAGE);
        QueryWrapper<DiscussPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status", 2).orderByDesc("type", "create_time");
        if(userId == 0) {
            return discussPostMapper.selectPage(page, queryWrapper);
        }
        return discussPostMapper.selectPage(page, queryWrapper.eq("user_id", userId));
    }

    public DiscussPost getDiscussPostById(int discussPostId) {
        return discussPostMapper.selectById(discussPostId);
    }
}
