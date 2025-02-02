package com.zufarov.tokenservice.util;

import org.springframework.context.annotation.Bean;

public class Base10ToBase64 {
    private static final String BASE64ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    public static String toBase64(long num) {
        if(num==0){return "A";}
        StringBuilder result = new StringBuilder();
        while(num>0) {
            int remainder = (int) num%64;
            result.append(BASE64ALPHABET.charAt(remainder));
            num/=64;
        }
        return result.reverse().toString();
    }

}
