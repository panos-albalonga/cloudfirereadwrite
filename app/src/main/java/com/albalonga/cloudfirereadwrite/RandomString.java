package com.albalonga.cloudfirereadwrite;

import java.nio.charset.Charset;
import java.util.Random;

public class RandomString {
    private String randomStr;

    public RandomString(){
//        byte[] array = new byte[7]; // length is bounded by 7
//        new Random().nextBytes(array);
//        randomStr = new String(array, Charset.forName("UTF-8"));

        int leftLimit = 65; // letter 'a'=97     Z=90
        int rightLimit = 122; // letter 'z'=122
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        randomStr = buffer.toString();
    }
    public String getRandomStr(){
        return randomStr;
    }
}
