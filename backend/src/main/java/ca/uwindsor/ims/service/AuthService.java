package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.config.AppProperties;
import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.dto.LoginResponse;
import ca.uwindsor.ims.security.ImsUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final int tokenExpiryHours;

    public AuthService(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                       AppProperties appProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.tokenExpiryHours = appProperties.tokenExpiryHours();
    }

    public AuthResult login(LoginRequest request) {
        log.debug("Authenticating user: {}", request.username());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        ImsUserDetails user = (ImsUserDetails) auth.getPrincipal();
        log.debug("Authenticated: user={}, role={}, studentId={}", user.getUsername(), user.getRole().authority(), user.getStudentId());
        LoginResponse resp = new LoginResponse(user.getRole().authority(), user.getStudentId(), user.getUsername());
        return new AuthResult(issueToken(user), resp);
    }

    private String issueToken(ImsUserDetails user) {
        log.debug("Issuing JWT for user={}, expiresIn={}h", user.getUsername(), tokenExpiryHours);
        Instant now = Instant.now();
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(tokenExpiryHours, ChronoUnit.HOURS))
                .claim(Constants.JWT_CLAIM_ROLE, user.getRole().authority());
        if (user.getStudentId() != null) {
            claimsBuilder.claim(Constants.JWT_CLAIM_STUDENT_ID, user.getStudentId());
        }
        JwtClaimsSet claims = claimsBuilder.build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
