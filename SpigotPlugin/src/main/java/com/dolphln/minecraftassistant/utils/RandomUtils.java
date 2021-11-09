package com.dolphln.minecraftassistant.utils;

import java.security.SecureRandom;

public class RandomUtils {

    public static int getRandomInt(int min, int max) {
        return new SecureRandom().nextInt(max - min + 1) + min;
    }

}
