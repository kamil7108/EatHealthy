package km.EH;

import km.EH.JWTUtill.JWTUtill;
import km.EH.Security.UserLogInService;
import km.EH.modles.AuthenticationRequest;
import km.EH.modles.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloServlet {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLogInService userLogInService;
    @Autowired
    private JWTUtill jwtUtill;

    @RequestMapping("/hello")
    String sayHello(){
        return new String("Siemka");
    }

    @PostMapping("/authentication")
    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
        }catch (AuthenticationException authenticationException){
            throw new Exception("Incorrect password or username");
        }
        final UserDetails userDetails = userLogInService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtill.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
