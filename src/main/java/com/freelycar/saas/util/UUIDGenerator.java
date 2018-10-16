package com.freelycar.saas.util;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author toby9
 * 2018/9/21
 */
@Component
public class UUIDGenerator extends UUIDHexGenerator implements IdentifierGenerator {

    public UUIDGenerator() {
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String generate() {
        return getUUID();
    }
}
