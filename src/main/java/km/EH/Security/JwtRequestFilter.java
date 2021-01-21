package km.EH.Security;

import km.EH.JWTUtill.JWTUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserLogInService userLogInService;

    @Autowired
    private JWTUtill jwtUtill;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    final String authorizationHeader = httpServletRequest.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
        jwt = authorizationHeader.substring(7);
        username= jwtUtill.getUsernameFromToken(jwt);
    }
    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = userLogInService.loadUserByUsername(username);
        if(jwtUtill.validateToken(jwt,userDetails)){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().
                    buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
    filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
