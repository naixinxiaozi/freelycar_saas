package com.freelycar.saas.util;

import com.freelycar.saas.iotcloudcn.BoxCommand;
import com.freelycar.saas.iotcloudcn.BoxCommandResponse;
import com.freelycar.saas.iotcloudcn.SafeConnection;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

class TestApi {

    //public static final String SERVER_BASE_URL = "http://localhost:8089";
    public static final String SERVER_BASE_URL = "https://serverapi.iotcloudcn.com";
    public static final String URL_OPEN_BOX = SERVER_BASE_URL + "/api/box/open";
    public static final String URL_QUERY_BOX = SERVER_BASE_URL + "/api/box/query";
    public static final String URL_QUERY_BOARD = SERVER_BASE_URL + "/api/board/query";
    static final Logger logger = LoggerFactory.getLogger(TestApi.class);

    @Test
    void testOpenBox() throws IOException {
        BoxCommand cmd = new BoxCommand("100000000000001", 1, 1);
        BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_OPEN_BOX, cmd, BoxCommandResponse.class);

        assertTrue(response.code == 0);
        assertTrue(response.deviceId.equals(cmd.deviceId));
        assertTrue(response.boardId == cmd.boardId);
        assertTrue(response.boxId == cmd.boxId);
        assertTrue(response.is_open);

    }


    @Test
    void testQueryBox() throws IOException {
        BoxCommand cmd = new BoxCommand("100000000000001", 1, 1);
        BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_QUERY_BOX, cmd, BoxCommandResponse.class);

        assertTrue(response.code == 0);
        assertTrue(response.deviceId.equals(cmd.deviceId));
        assertTrue(response.boardId == cmd.boardId);
        assertTrue(response.boxId == cmd.boxId);
        assertTrue(response.is_open);

    }


    @Test
    void testQueryBoard() throws IOException {
        //For Query Board, always use 0 as boxId
        BoxCommand cmd = new BoxCommand("100000000000001", 1, 0);
        BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_QUERY_BOARD, cmd, BoxCommandResponse.class);

        assertTrue(response.code == 0);
        assertTrue(response.deviceId.equals(cmd.deviceId));
        assertTrue(response.boardId == cmd.boardId);
    }


}
