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
    public static final int SUCCESS_CODE = 0;

    private static final String SERVER_BASE_URL = "https://serverapi.iotcloudcn.com";
    private static final Logger logger = LoggerFactory.getLogger(ArkOperation.class);
    private static final String URL_OPEN_BOX = SERVER_BASE_URL + "/api/box/open";
    private static final String URL_QUERY_BOX = SERVER_BASE_URL + "/api/box/query";
    private static final String URL_QUERY_BOARD = SERVER_BASE_URL + "/api/board/query";

    private static BoxCommandResponse getRemoteResponse(BoxCommand cmd, String url) {
        try {
            return SafeConnection.postAndGetResponse(url, cmd, BoxCommandResponse.class);
        } catch (IOException e) {
            logger.error("调用iotcloudcn远端接口出现异常", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开柜门
     *
     * @param deviceId
     * @param boxId
     * @return
     */
    public static BoxCommandResponse openBox(String deviceId, int boxId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, boxId);
        return getRemoteResponse(cmd, URL_OPEN_BOX);
    }

    /**
     * 查询某个柜门状态
     *
     * @param deviceId
     * @param boxId
     * @return
     */
    public static BoxCommandResponse queryBox(String deviceId, int boxId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, boxId);
        return getRemoteResponse(cmd, URL_QUERY_BOX);
    }

    /**
     * 查询某个柜子所有柜门状态
     * @param deviceId
     * @return
     */
    public static BoxCommandResponse queryBoard(String deviceId) {
        BoxCommand cmd = new BoxCommand(deviceId, 1, 0);
        return getRemoteResponse(cmd, URL_QUERY_BOARD);
    }
}
