package com.freelycar.saas.temp;

import com.freelycar.saas.iotcloudcn.BoxCommand;
import com.freelycar.saas.iotcloudcn.BoxCommandResponse;
import com.freelycar.saas.iotcloudcn.SafeConnection;
import com.freelycar.saas.project.entity.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author tangwei - Toby
 * @date 2019-01-02
 * @email toby911115@gmail.com
 */
public class PrintTest {
    //public static final String SERVER_BASE_URL = "http://localhost:8089";
    public static final String SERVER_BASE_URL = "https://serverapi.iotcloudcn.com";
    public static final String URL_OPEN_BOX = SERVER_BASE_URL + "/api/box/open";
    public static final String URL_QUERY_BOX = SERVER_BASE_URL + "/api/box/query";
    public static final String URL_QUERY_BOARD = SERVER_BASE_URL + "/api/board/query";
    static final Logger logger = LoggerFactory.getLogger(PrintTest.class);

    public static void main(String[] args) {
        Object obj = new CouponService();
        System.out.println(obj);
        BoxCommand cmd = new BoxCommand("869696048118504", 1, 1);
        try {
            BoxCommandResponse response = SafeConnection.postAndGetResponse(URL_QUERY_BOARD, cmd, BoxCommandResponse.class);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

