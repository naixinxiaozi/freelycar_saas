package com.freelycar.saas.iotcloudcn.util;

public class BoxCommand {

    public static final int BOARD_TYPE_DEFAULT = 0x68;


    public String deviceId;
    public int boardId = 0;
    public int boxId = 0;
    public int boardType = BOARD_TYPE_DEFAULT;

    public BoxCommand(String deviceId, int boardId, int boxId) {
        this.deviceId = deviceId;
        this.boardId = boardId;
        this.boxId = boxId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"boardId\":")
                .append(boardId);
        sb.append(",\"boxId\":")
                .append(boxId);
        sb.append(",\"boardType\":")
                .append(boardType);
        sb.append('}');
        return sb.toString();
    }
}
