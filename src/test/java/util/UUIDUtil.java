package util;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

import java.util.UUID;

/**
 * @author toby9
 * 2018/9/21
 */
public class UUIDUtil extends UUIDHexGenerator implements IdentifierGenerator {

    public UUIDUtil() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String generate() {
        return getUUID();
    }
}
