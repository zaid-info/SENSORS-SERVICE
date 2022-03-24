package sia.sensorsservice.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH");
        response.addHeader("Access-Control-Allow-Headers","Origin,Accept,x-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,authorization,observe");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers,Access-Control-Allow-Credentials,authorization");

        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(response.SC_OK);
        } else {
            String jwt =request.getHeader(SecurityParams.JWT_HEADER_NAME);
            if(jwt==null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }

            JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            DecodedJWT decodedJWT=jwtVerifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length()));
            String username=decodedJWT.getSubject();
            List<String> roles=decodedJWT.getClaim("roles").asList(String.class);
            List<GrantedAuthority>authorities=new ArrayList<>();
            roles.forEach(s -> {
                authorities.add(new SimpleGrantedAuthority(s));
            });

            UsernamePasswordAuthenticationToken user=new UsernamePasswordAuthenticationToken(username,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(request,response);

        }



    }
}
