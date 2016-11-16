package com.util;

import java.math.BigDecimal;

/**
 * Created by lizhen on 2015/4/17.
 */
public class StringUtil {

    public static BigDecimal toBigDecimal(String s) {
        return toBigDecimal(s, null);
    }

    public static BigDecimal toBigDecimal(String s, BigDecimal defaultValue) {
        if (s == null || "".equals(s.trim()) || "Infinity".equals(s.trim()) || "NaN".equals(s.trim())) {
            return defaultValue;
        }

        try {
            return new BigDecimal(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double toDouble(String s) {
        return toDouble(s, null);
    }

    public static Double toDouble(String s, Double defaultValue) {
        if (s == null || "".equals(s.trim()) || "Infinity".equals(s.trim()) || "NaN".equals(s.trim()))
            return defaultValue;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer toInteger(String s) {
        return toInteger(s, null);
    }

    public static Integer toInteger(String s, Integer defaultValue) {
        if (s == null || "".equals(s.trim()) || !s.matches("^[-+]?[0-9]+$"))
            return defaultValue;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Long toLong(String s) {
        return toLong(s, null);
    }

    public static Long toLong(String s, Long defaultValue) {
        if (s == null || "".equals(s.trim()))
            return defaultValue;
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        String tempStr = str.trim();
        if (tempStr.length() == 0)
            return true;
        if (tempStr.toLowerCase().equals("null"))
            return true;
        return false;
    }

    public static long ipToLong(String strIP) {
        try {
            if (strIP == null || strIP.length() == 0) {
                return 0L;
            }
            long[] ip = new long[4];
            int position1 = strIP.indexOf(".");
            int position2 = strIP.indexOf(".", position1 + 1);
            int position3 = strIP.indexOf(".", position2 + 1);
            ip[0] = Long.parseLong(strIP.substring(0, position1));
            ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(strIP.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        } catch (Exception ex) {
            return 0L;
        }
    }

    public static String longToip(long ipLong) {
        long mask[] = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };
        long num = 0;
        StringBuffer ipInfo = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            num = (ipLong & mask[i]) >> (i * 8);
            if (i > 0)
                ipInfo.insert(0, ".");
            ipInfo.insert(0, Long.toString(num, 10));
        }
        return ipInfo.toString();
    }

    public static String byteToString(byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String tempStr = Integer.toHexString(digest[i] & 0xff);
            if (tempStr.length() == 1) {
                buf.append("0").append(tempStr);
            } else {
                buf.append(tempStr);
            }
        }
        return buf.toString().toLowerCase();
    }

    /**
     * 修改值
     * @param value 当前值
     * @param bit 第几位
     * @param flag 0/1
     * @return
     */
    public static int setBinaryIndex(int value,int bit,int flag){
        if(getBinaryIndex(value, bit) == 1){
            if(flag == 0){
                value = value - (1<<bit-1);
            }
        }else{
            if(flag == 1){
                value = value|(1<<bit-1);
            }
        }
        return value;
    }
    /**
     * 返回第几位0/1值
     * @param value 当前值
     * @param bit 第几位
     *
     * @return
     */
    public static int getBinaryIndex(int value,int bit){
        int remainder = 0;
        for(int i=0; i<bit;i++){
            int factor = value/2;
            remainder = value%2;
            if(factor == 0){
                if(i<bit-1){
                    remainder = 0;
                }
                break;
            }
            value = factor;
        }
        return remainder;
    }

    public static void main(String[] args){
    }
}
