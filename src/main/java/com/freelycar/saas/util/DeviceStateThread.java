package com.freelycar.saas.util;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.wsutils.WSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * @author 唐炜
 */
public class DeviceStateThread extends Thread {

    private final static long TIME_INTERVAL = 5000;
    private final static long TIMEOUT = 60000;
    private static Logger log = LogManager.getLogger(DeviceStateThread.class);
    private String deviceId = "";
    private String res = "";
    private boolean stopMe = true;
    private long startTime = System.currentTimeMillis();
    private String endStatus = "timeout";

    @Override
    public void run() {
        while (stopMe) {
            synchronized (DeviceStateThread.class) {
                try {
                    Thread.sleep(TIME_INTERVAL);
                    //是否已经超时，超时则直接退出进程
                    if ((System.currentTimeMillis() - startTime) > TIMEOUT) {
                        log.info(deviceId + " 柜门未关，已超时……");
                        this.stopMe();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(deviceId)) {
                    break;
                }
                res = WSClient.getDeviceStateByID(deviceId);

                JSONObject resJSONObject = JSONObject.parseObject(res);
                if (WSClient.RESULT_SUCCESS.equalsIgnoreCase(resJSONObject.getString("res"))) {
                    JSONObject jsonObject = resJSONObject.getJSONObject("value");
                    String magne = jsonObject.getString("magne");
                    log.info(res);
                    if ("0".equals(magne)) {
                        endStatus = "success";
                        log.info(deviceId + " 柜门关闭，结束进程……");
                        this.stopMe();
                        break;
                    }
                }
            }
            // 让出CPU，给其他线程执行
            Thread.yield();
        }
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void stopMe() {
        this.stopMe = false;
    }
}
