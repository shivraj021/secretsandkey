package com.advanceauth.config;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.time.Instant;

public class MfaCodeGenerator {

    public static String generateCurrentCode(String secret) throws Exception {
        Base32 base32 = new Base32();
        byte[] secretBytes = base32.decode(secret);

        long timeWindow = Instant.now().getEpochSecond() / 30;
        byte[] timeBytes = ByteBuffer.allocate(8).putLong(timeWindow).array();

        SecretKeySpec signKey = new SecretKeySpec(secretBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(timeBytes);

        int offset = hash[hash.length - 1] & 0xF;
        int binary =
                ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);

        int otp = binary % 1000000;
        return String.format("%06d", otp);
    }

    public static void main(String[] args) throws Exception {
        String secret = "JBSWY3DPEHPK3PXP"; // base32 secret
        String code = generateCurrentCode(secret);
        System.out.println("Current TOTP Code: " + code);
    }
}


///api/auth/register
//{
//	  "username": "shivraj@gmail.com",
//	  "password": "test123"
//	}
//GET /oauth2/authorization/google
//POST /api/auth/get-mfa?username=shivraj@gmail.com
//POST /api/student/create
//{
//	  "name": "Ravi",
//	  "email": "ravi@student.com",
//	  "age": "21",
//	  "course": "Java",
//	  "phone": "9998887776"
//	}
//
//Step	Endpoint	Action
//1	POST /api/auth/register	Register user (optional)
//2	GET /oauth2/authorization/google	Google login (opens Google page)
//3	OAuth2LoginSuccessHandler	JWT token response
//4	POST /api/auth/get-mfa	Get QR URL
//5	POST /api/auth/verify-mfa	Submit TOTP and get new JWT
//6	POST /api/student/create	Create student with JWT
