package cn.codincrea.overseas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chy
 * @since 2021-06-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "college")
@EqualsAndHashCode(callSuper = false)
public class College implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学校中文名，不重复
     */
    private String collegeName;

    /**
     * 学校英文名，不重复
     */
    private String collegeEName;

    /**
     * 国家
     */
    private String country;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * qs排名
     */
    private Integer qsRank;

    /**
     * 录取率
     */
    private Double rate;

    /**
     * 本国排名标准
     */
    private String localRankName;

    /**
     * 本国排名
     */
    private Integer localRank;

    /**
     * 图标
     */
    private String icon;

    /**
     * 学校介绍
     */
    private String introduce;

    /**
     * EA申请截止日期
     */
    private String ea;

    /**
     * RD申请截止日期
     */
    private String rd;

    /**
     * 转学申请截止日期
     */
    private String transfer;

    /**
     * 学费
     */
    private String tuitionFee;

    /**
     * 生活费
     */
    private String livingFee;


}
