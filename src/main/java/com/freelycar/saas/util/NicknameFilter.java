package com.freelycar.saas.util;

import org.apache.commons.codec.binary.Base64;

public class NicknameFilter {
    private static final String LAST_3_BYTE_UTF_CHAR = "\uFFFF";
    private static final String REPLACEMENT_CHAR = "\uFFFD";

    public static String filter4BytesUTF8(String s) {
        final int length = s.length();
        StringBuilder b = new StringBuilder(length);
        for (int offset = 0; offset < length; ) {
            final int codepoint = s.codePointAt(offset);
            // do something with the codepoint
            if (codepoint > NicknameFilter.LAST_3_BYTE_UTF_CHAR.codePointAt(0)) {
                b.append(NicknameFilter.REPLACEMENT_CHAR);
            } else {
                if (Character.isValidCodePoint(codepoint)) {
                    b.appendCodePoint(codepoint);
                } else {
                    b.append(NicknameFilter.REPLACEMENT_CHAR);
                }
            }
            offset += Character.charCount(codepoint);
        }
        return b.toString();
    }

    public static String encodeNickname(String nickname) {
        return new String(Base64.encodeBase64(nickname.getBytes()));
    }

    public static String decodeNikename(String nickname) {
        return new String(Base64.decodeBase64(nickname));
    }

}
