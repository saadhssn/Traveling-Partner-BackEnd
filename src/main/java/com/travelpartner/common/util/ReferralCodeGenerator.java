package com.travelpartner.common.util;

import java.util.UUID;

public class ReferralCodeGenerator {

    public static String generateCode(String name) {

        String prefix = name.replaceAll("\\s+", "")
                .substring(0, Math.min(3, name.length()))
                .toUpperCase();

        String random = UUID.randomUUID().toString()
                .substring(0, 5)
                .toUpperCase();

        return prefix + random;
    }
}