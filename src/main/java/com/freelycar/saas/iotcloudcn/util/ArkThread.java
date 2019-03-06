package com.freelycar.saas.iotcloudcn.util;

import com.freelycar.saas.iotcloudcn.ArkOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * @author 唐炜
 */
public class ArkThread extends Thread {

    private final static long TIME_INTERVAL = 5000;
    private final static long TIMEOUT = 50000;
    private static Logger log = LogManager.getLogger(ArkThread.class);
    private String deviceId = null;
    private int boxId = 0;
    private BoxCommandResponse boxCommandResponse = null;
    private boolean stopMe = true;
    private long startTime = System.currentTimeMillis();
    private String endStatus = "timeout";

    public ArkThread(String deviceId, int boxId) {
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(Runnable target, String deviceId, int boxId) {
        super(target);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(ThreadGroup group, Runnable target, String deviceId, int boxId) {
        super(group, target);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(String name, String deviceId, int boxId) {
        super(name);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(ThreadGroup group, String name, String deviceId, int boxId) {
        super(group, name);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(Runnable target, String name, String deviceId, int boxId) {
        super(target, name);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(ThreadGroup group, Runnable target, String name, String deviceId, int boxId) {
        super(group, target, name);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    public ArkThread(ThreadGroup group, Runnable target, String name, long stackSize, String deviceId, int boxId) {
        super(group, target, name, stackSize);
        this.deviceId = deviceId;
        this.boxId = boxId;
    }

    @Override
    public void run() {
        while (stopMe) {
            synchronized (ArkThread.class) {
                try {
                    Thread.sleep(TIME_INTERVAL);
                    //是否已经超时，超时则直接退出进程
                    if ((System.currentTimeMillis() - startTime) > TIMEOUT) {
                        log.error(deviceId + " 柜门未关，已超时。线程终止。");
                        this.stopMe();
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(deviceId)) {
                    log.error("deviceId为空值，线程终止。deviceId：" + deviceId);
                    this.stopMe();
                    break;
                }
                if (0 == boxId) {
                    log.error("boxId为空值，线程终止。boxId：" + boxId);
                    this.stopMe();
                    break;
                }
                boxCommandResponse = ArkOperation.queryBox(deviceId, boxId);

                int code = boxCommandResponse.code;
                boolean isOpen = boxCommandResponse.is_open;
                if (ArkOperation.SUCCESS_CODE == code) {
                    if (!isOpen) {
                        endStatus = "success";
                        log.info(deviceId + " 柜门关闭。正常结束进程");
                        this.stopMe();
                        break;
                    }
                }
            }
            // 让出CPU，给其他线程执行
            Thread.yield();
        }
    }

    private void stopMe() {
        this.stopMe = false;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public BoxCommandResponse getBoxCommandResponse() {
        return boxCommandResponse;
    }

    public String getEndStatus() {
        return endStatus;
    }
}
