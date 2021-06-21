package cn.codincrea.overseas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User  {//implements UserDetails
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Date addtime;

    private Integer comment;
    private Integer type;
    private Integer status;
    private Integer sex;

    private String qq;
    private String email;
    private String phone;
    private String activationCode;
    private String avatar;

    private Double gpa;
    private Integer sat;
    private Double ielts;
    private Integer toefl;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new ArrayList<>();
//        list.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                switch (type) {
//                    case 0:
//                        return "user";
//                    case 1:
//                        return "admin";
//                    default:
//                        return "moderator";
//                }
//            }
//        });
//        return list;
//    }

//    //账号未过期
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    //账号未锁定
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    //凭证未过期
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    //账号可用
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}