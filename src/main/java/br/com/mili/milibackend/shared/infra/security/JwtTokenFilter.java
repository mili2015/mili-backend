package br.com.mili.milibackend.shared.infra.security;

import br.com.mili.milibackend.shared.infra.security.model.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final UserDetailsService userDetailsService;

    public JwtTokenFilter(JwtUtils jwtUtils, HandlerExceptionResolver handlerExceptionResolver, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = header.replace("Bearer ", "");

            if (jwtUtils.validarToken(token)) {
                String username = jwtUtils.extractUsername(token);
                var roles = jwtUtils.extractRoles(token);

                UserDetails userfound = userDetailsService.loadUserByUsername(username);

                if (userfound == null)
                    return;

                List<GrantedAuthority> grantedAuthorities = userfound.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userfound,
                        null,
                        grantedAuthorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
