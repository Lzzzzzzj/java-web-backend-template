package com.example.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "token:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt)) {
                log.debug("收到Token: {}", jwt.substring(0, Math.min(20, jwt.length())) + "...");
                
                if (jwtTokenProvider.validateToken(jwt)) {
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    String tokenKey = TOKEN_PREFIX + userId;
                    
                    log.debug("从Redis查询Token，key: {}", tokenKey);
                    
                    try {
                        Object cachedObj = redisTemplate.opsForValue().get(tokenKey);
                        String cachedToken = cachedObj != null ? cachedObj.toString() : null;
                        
                        log.debug("Redis中的Token: {}", cachedToken != null ? "存在" : "不存在");
                        
                        if (cachedToken != null && cachedToken.equals(jwt)) {
                            String username = jwtTokenProvider.getUsernameFromToken(jwt);
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            
                            UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.info("用户认证成功: {}, userId: {}", username, userId);
                        } else {
                            log.warn("Token验证失败 - Token不匹配或已过期，userId: {}", userId);
                        }
                    } catch (Exception e) {
                        log.error("Redis操作异常: {}", e.getMessage());
                    }
                } else {
                    log.warn("Token验证失败 - JWT无效");
                }
            }
        } catch (Exception ex) {
            log.error("无法设置用户认证信息", ex);
        }
        
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
