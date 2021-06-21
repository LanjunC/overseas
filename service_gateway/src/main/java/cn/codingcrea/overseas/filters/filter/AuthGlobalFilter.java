package cn.codingcrea.overseas.filters.filter;

import cn.codingcrea.overseas.utils.JWTUtil;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局Filter，统一处理未登录和权限不足的服务
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered, Project4Constant {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("==="+path);

        //内部服务接口，不允许外部访问
        if(antPathMatcher.match("/inner/**", path)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response, Project4Constant.CODE_DENIED);
        }

        //api接口，异步请求，校验用户必须登录
        if(antPathMatcher.match("/**/auth/**", path)) {
            Integer userId = this.getUserId(request);
            if(StringUtils.isEmpty(userId)) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, Project4Constant.CODE_NO_LOGIN);
            }
            //将登录用户id放入请求头，注意不能直接getHeaders()然后add
            //这样后面的服务器就不需要再去用token取了
            System.out.println(userId);
            request = request.mutate().header("Login-User-Id", String.valueOf(userId)).build();
            exchange = exchange. mutate().request(request).build();

        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * api接口鉴权失败返回数据
     * @param response
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response, int code) {
        String jsonString = Project4Utils.getJSONString(code, code == CODE_DENIED ? "权限不足！" : "你还未登录！", null);
        byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取当前登录用户id
     * @param request
     * @return
     */
    private Integer getUserId(ServerHttpRequest request) {

        Integer id = null;
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if(null  != tokenList) {
            token = tokenList.get(0);
        }
        if(!StringUtils.isEmpty(token)) {
            try{
                DecodedJWT decodedToken = JWTUtil.getDecodedToken(token);
                id = Integer.parseInt(decodedToken.getClaim("id").asString());
            } catch (Exception ignore) {
                //token无效
            }
        }
        return id;
    }
}
