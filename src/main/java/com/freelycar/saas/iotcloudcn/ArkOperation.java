package com.freelycar.saas.iotcloudcn;

import com.freelycar.saas.iotcloudcn.util.BoxCommand;
import com.freelycar.saas.iotcloudcn.util.BoxCommandResponse;
import com.freelycar.saas.iotcloudcn.util.SafeConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author tangwei - Toby
 * @date 2019-02-27
 * @email toby911115@gmail.com
 */
public class ArkOperation {
    static final Logger logger = LoggerFactory.getLogger(ArkOperation.class);

    private static final String SERVER_BASE_URL = "https://serverapi.iotcloudcn.com";
    public static final String URL_OPEN_BOX = SERVER_BASE_URL + "/api/box/open";
    public static final String URL_QUERY_BOX = SERVER_BASE_URL + "/api/box/query";
    public static final String URL_QUERY_BOARD = SERVER_BASE_URL + "/api/board/query";

    public static BoxCommandResponse openBox(String deviceId, int boxId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, boxId);
        try {
            BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_OPEN_BOX, cmd, BoxCommandResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BoxCommandResponse queryBox(String deviceId, int boxId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, boxId);
        try {
            BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_QUERY_BOX, cmd, BoxCommandResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BoxCommandResponse queryBoard(String deviceId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, 0);
        try {
            BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_QUERY_BOARD, cmd, BoxCommandResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
