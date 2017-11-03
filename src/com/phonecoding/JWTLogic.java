package com.phonecoding;

//To generate the JWT - createJWT
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;


//Decode and Verify Tokens
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

   
/**   
* @author ThiagoLeoncio ||| defensecoding.com ||| phonecoding.com
* @date 11032017
* @category AuthLogic
* @see generate token based on information provided and parse the token 
* @see Article related: http://phonecoding.com/2017/10/why-we-should-be-using-jwt.html
*/
public class JWTLogic {

/**  
* Sample method to construct a JWT     
* Provides the signature to be used for token generation
* Uses Secret key requested to sign the JWT.
* Adds expiration to the token.
* Builds and serializes the JWT
* @param id
* @param issuer
* @param subject
* @param ttlMillis 
* @return Token String
*/  
private String createJWT(String id, String issuer, String subject, long ttlMillis) {

 //The JWT signature algorithm we will be using to sign the token
 SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

 long nowMillis = System.currentTimeMillis();
 Date now = new Date(nowMillis);

 //We will sign our JWT with our ApiKey secret
 byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.getSecret());
 Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

 //Let's set the JWT Claims
 JwtBuilder builder = Jwts.builder().setId(id)
                             .setIssuedAt(now)
                             .setSubject(subject)
                             .setIssuer(issuer)
                             .signWith(signatureAlgorithm, signingKey);
                            

 //if it has been specified, let's add the expiration
 if (ttlMillis >= 0) {
 long expMillis = nowMillis + ttlMillis;
     Date exp = new Date(expMillis);
     builder.setExpiration(exp);
 }

 //Builds the JWT and serializes it to a compact, URL-safe string
 return builder.compact();
}

/**
* Sample method to validate and read the JWT
* @param jwt
* @return JWT Claims
*/  
private void parseJWT(String jwt) {

//This line will throw an exception if it is not a signed JWS (as expected)
Claims claims = Jwts.parser()         
  .setSigningKey(DatatypeConverter.parseBase64Binary(this.getSecret()))
  .parseClaimsJws(jwt).getBody();
System.out.println("ID: " + claims.getId());
System.out.println("Subject: " + claims.getSubject());
System.out.println("Issuer: " + claims.getIssuer());
System.out.println("Expiration: " + claims.getExpiration());
}

private String getSecret() {
//Secret key to be used for Token build and parse
return "ThiagoLeoncio";
}


public static void main(String[] args) {
// TODO Auto-generated method stub

JWTLogic JWTThiagoLeoncioTest = new JWTLogic();
String OfxToken=JWTThiagoLeoncioTest.createJWT("13", "JohnDoe", "JubileuTest", 2323);
System.out.print("Token Generated:"+OfxToken);


JWTThiagoLeoncioTest.parseJWT(OfxToken);

}

}
