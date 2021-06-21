package cn.codingcrea.overseas.service;

import cn.codincrea.overseas.model.College;
import cn.codingcrea.overseas.mapper.CollegeMapper;
import cn.codingcrea.overseas.utils.Project4Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollegeService extends Project4Utils {

    @Resource
    private CollegeMapper collegeMapper;

    @Autowired
    public CollegeService(CollegeMapper collegeMapper){
        this.collegeMapper = collegeMapper;
    }

    //查询所有院校信息
    public List<College> selectAllInfo() {
        return collegeMapper.selectList(null);
    }

    //根据ID查询
    public College selectById(int id) {
        return collegeMapper.selectById(id);
    }


    //根据QS排名分页查询
    public List<College> selectByQsRank(int qsLow, int qsHigh, int currentNum, int pageSize) {
        Page<College> page = new Page<>(currentNum,pageSize);
        Page<College> qs_rank = collegeMapper.selectPage(page, new QueryWrapper<College>().between("qs_rank", qsLow, qsHigh));
        List<College> qs_rankRecords = qs_rank.getRecords();
        return qs_rankRecords;
    }


    //根据学校名称模糊查询
    public List<College> selectByName(String collegeName,int currentNum,int pageSize) {
        Page<College> page = new Page<>(currentNum,pageSize);
        Page<College> college_name = collegeMapper.selectPage(page, new QueryWrapper<College>().like("college_name", collegeName));
        List<College> collegeNameRecords = college_name.getRecords();
        return collegeNameRecords;
    }
}
