package com.meowing.loud.arms.utils;

public class XMWeakPassword {

    private static String[] XM_WEAK_PASSWORDS = {
            "password",
            "user",
            "admin",
            "pass",
            "name",
            "root",
            "iloveyou",
    };

    public static String matchWeakPassword(String passStr) {

        if ( null != passStr ) {
            passStr = passStr.toLowerCase();

            for ( String weekPassword : XM_WEAK_PASSWORDS ) {
                if ( passStr.contains(weekPassword) ) {
                    return weekPassword;
                }
            }
        }

        return null;
    }
}

