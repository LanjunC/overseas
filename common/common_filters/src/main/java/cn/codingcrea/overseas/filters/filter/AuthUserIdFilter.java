package cn.codingcrea.overseas.filters.filter;

import cn.codingcrea.overseas.utils.LoginUserIdContext;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName="filter1",urlPatterns= {"/*"})
@Component
//@Order(FilterRegistrationBean.LOWEST_PRECEDENCE)
public class AuthUserIdFilter extends OncePerRequestFilter {

    @Autowired
    private LoginUserIdContext loginUserIdContext;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Login-User-Id");
        if(!StringUtils.isBlank(header)) {
            Integer loginUserId = Integer.valueOf(header);
            loginUserIdContext.setUserId(loginUserId);
        }
        chain.doFilter(request, response);

    }
}
