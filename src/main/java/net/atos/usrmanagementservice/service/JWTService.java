//package net.atos.dms.service;
//
//
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JWTService {
//    private PrivateKey privateKey;
//
//    @Value("${jwt.private-key}")
//    private String privateKeyString;
//
//    @Value("${jwt.expiration-time}")
//    private long expirationTime;
//
//
//    public JWTService(){
////        try {
////            SecretKey sk = KeyGenerator.getInstance("HmacSHA256").generateKey();
////            key = Base64.getEncoder().encodeToString(sk.getEncoded());
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        }
//    }
//    public String generateToken(String email) {
//
//        Map<String, Object> claims = new HashMap<>();
//
//        return Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(email)
//                .issuedAt(new Date(System.currentTimeMillis()))
////              new Date(System.currentTimeMillis()+ 60 * 60 * 30)
//                .expiration(new Date(System.currentTimeMillis()+ expirationTime))
//                .and()
//                .signWith(privateKey)
//                .compact();
//
//
//    }
//    @PostConstruct
//    public void init() throws Exception {
//        privateKey = loadPrivateKey(privateKeyString);
//    }
//
//    private PrivateKey loadPrivateKey(String privateKeyString) throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePrivate(keySpec);
//    }
//
//    public String extractUserName(String token) {
//        // extract the username from jwt token
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith((SecretKey) this.privateKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//
//}
package net.atos.usrmanagementservice.service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {
//    private final String key;


    @Value("${jwt.private-key}")
    private String key;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Autowired
    private MyUserDetailsService myUserDetailsService;





    public String generateToken(String email,String nationalid) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("nationalid", nationalid);
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ expirationTime*1000))
                .signWith(getKey())
                .compact();


    }
    public SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateTokenWithUserDetails(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractNationalid(String token) {

        return extractAllClaims(token).get("nationalid", String.class);
//        return extractClaim(token,  Claims -> Claims.get("nationalid", String.class));
    }

    public boolean validateToken(String token) {
        String userName = extractUserName(token);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
        return validateTokenWithUserDetails(token, userDetails);
    }


}