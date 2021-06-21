package cn.codingcrea.overseas.service;

import cn.codincrea.overseas.model.Comment;
import cn.codincrea.overseas.model.DiscussPost;
import cn.codingcrea.overseas.mapper.CommentMapper;
import cn.codingcrea.overseas.mapper.DiscussPostMapper;
import cn.codingcrea.overseas.utils.Project4Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CommentService implements Project4Constant {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int saveComment(@NonNull Comment comment) {

        int rows = commentMapper.insert(comment);
        //更新帖子的冗余数据
        if(comment.getEntityType() == ENTITY_TYPE_POST) {

            int count = commentMapper.selectCount(new QueryWrapper<Comment>().eq("entity_type", ENTITY_TYPE_POST).eq(
                    "entity_id", comment.getEntityId()));
            DiscussPost post = new DiscussPost();
            post.setId(comment.getEntityId());
            post.setCommentCount(count);
            discussPostMapper.updateById(post);
        }
        return rows;
    }

    public Page<Comment> getCommentByEntity(int entityType, int entityId, int target){  //target为-1表示获取列表时一次性获取所有记录,
        // 而不是10条
        Page<Comment> page;
        if(target < 0) {
            page = new Page<>(target, Integer.MAX_VALUE);
        } else {
            page = new Page<>(target, DISCUSSPOSTS_PER_PAGE);
        }
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0).eq("entity_type", entityType).eq("entity_id", entityId).orderByAsc("create_time");
        return commentMapper.selectPage(page, queryWrapper);
    }

}
