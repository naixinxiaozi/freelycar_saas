package com.freelycar.saas.iotcloudcn;

/**
 * 智能柜运维使用类
 * 不暴露为接口，在开发测试时直接操作智能柜
 *
 * @author tangwei - Toby
 * @date 2019-04-08
 * @email toby911115@gmail.com
 */
public class ArkOps {

    public static void main(String[] args) {
        String deviceId = "";
        int boxId = 1;

        int boxCount = 16;

        //打开一个柜门
//        openBox(deviceId,boxId);

        //打开所有柜门
//        openAllBox(deviceId, boxCount);

        //查询某个柜门状态
//        queryBox(deviceId, boxId);


        //查询某个柜子所有柜门
//        queryBoard(deviceId);
    }

    private static void openBox(String deviceId, int boxId) {
        System.out.println(ArkOperation.openBox(deviceId, boxId));
    }

    private static void openAllBox(String deviceId, int boxCount) {
        for (int i = 0; i < boxCount; i++) {
            openBox(deviceId, i + 1);
        }
    }

    private static void queryBox(String deviceId, int boxId) {
        System.out.println(ArkOperation.queryBox(deviceId, boxId));
    }

    private static void queryBoard(String deviceId) {
        System.out.println(ArkOperation.queryBoard(deviceId));
    }
}
