package com.freelycar.saas.iotcloudcn;

import java.util.Arrays;

public class BoxCommandResponse {
    public int code;
    public String msg;
    public String deviceId;
    public byte boardId;
    public byte boxId;
    public boolean is_open;
    public boolean[] open_array;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"msg\":\"")
                .append(msg).append('\"');
        sb.append(",\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"boardId\":")
                .append(boardId);
        sb.append(",\"boxId\":")
                .append(boxId);
        sb.append(",\"is_open\":")
                .append(is_open);
        sb.append(",\"open_array\":")
                .append(Arrays.toString(open_array));
        sb.append('}');
        return sb.toString();
    }
}
