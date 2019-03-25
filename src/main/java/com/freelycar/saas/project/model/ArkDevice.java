package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArkDevice {
    private String deviceId;

    private String boardId;

    private Integer boxId;

    private Boolean online;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"boardId\":\"")
                .append(boardId).append('\"');
        sb.append(",\"boxId\":")
                .append(boxId);
        sb.append(",\"online\":")
                .append(online);
        sb.append('}');
        return sb.toString();
    }
}
