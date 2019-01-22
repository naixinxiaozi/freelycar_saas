package com.freelycar.saas.wsutils;

import com.alibaba.fastjson.JSON;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class WSClient {
    public static final String RESULT_SUCCESS = "suc";
    public static final String RESULT_ERROR = "err";
    public static final String DOOR_STATE_CLOSE = "0";
    public static final String DOOR_STATE_OPEN = "1";
    private static final Logger log = LogManager.getLogger(WSClient.class);
    private static final String USER_NAME = "xiaoyi";
    private static final String USER_PASSWORD = "159357123";
    private static String handle = null;
    private static String outdate = "0";
    private static Client client;

    static {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        client = dcf.createClient("http://47.93.207.49:8080/IoTWebService/platformws?wsdl");
        client = dcf.createClient("http://yungui.njlanniu.com:8080/IoTWebService/platformws?wsdl");
        Object[] a = {USER_NAME, USER_PASSWORD};
        Object[] c = null;
        try {
            c = client.invoke("getPermission", a);
        } catch (Exception e) {
            log.error("调用方法getPermission(获取句柄)失败！", e);
            e.printStackTrace();
        }

        ResultBean res = JSON.parseObject((String) c[0], ResultBean.class);
        //TODO 暂时不存session，改为每次执行都调用
        handle = res.getValue();
    }

    /**
     * invoke getPermission()
     * 获取句柄
     * 检测登陆的用户是否合法，必须先调用该接口才能调用其他的接口函数
     * 返回一个默认生存周期为一小时、类型为字符串的句柄，该句柄是以后调用其他接口的唯一依据。
     * handle失效时重新获取
     *
     * @return string
     */
    private static String getHandle() {
        Object[] a = {USER_NAME, USER_PASSWORD};
        Object[] b = null;
        try {
            b = client.invoke("getPermission", a);
        } catch (Exception e) {
            log.error("调用方法getPermission(获取句柄)失败！", e);
            e.printStackTrace();
        }
        ResultBean res = JSON.parseObject((String) b[0], ResultBean.class);
        handle = res.getValue();
        return handle;
    }

    /**
     * 对设备的反向控制
     * 相当于对柜子门的开和关
     *
     * @param deviceID 设备编号
     * @param command  控制指令：0表示关，1表示开
     * @return string
     */
    public static String controlDevices(String deviceID, String command) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(command)) {
            log.error("参数command为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, command};
        Object[] res = null;
        try {
            res = client.invoke("controlDevices", t);
        } catch (Exception e) {
            log.error("调用方法controlDevices(对设备的反向控制)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 查询所有设备当前状态信息
     *
     * @return string
     */
    @Deprecated
    public static String getAllDevicesState() {
        handle = getHandle();

        Object[] t = {handle};
        Object[] res = null;
        try {
            res = client.invoke("getAllDevicesState", t);
        } catch (Exception e) {
            log.error("调用方法getAllDevicesState(查询所有设备当前状态信息)失败！", e);
            e.printStackTrace();
        }
        if (null == res) {
            return null;
        }
        return (String) res[0];
    }

    /**
     * 根据IMEI号查询设备下所以柜门的状态
     *
     * @param gwNum 设备IMEI号
     * @return
     */
    public static String getAllDeviceStateByGWNum(String gwNum) {
        if (StringUtils.isEmpty(gwNum)) {
            log.error("参数gwNum为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, gwNum};
        Object[] res = null;
        try {
            res = client.invoke("getAllDeviceStateByGWNum", t);
        } catch (Exception e) {
            log.error("调用方法getAllDeviceStateByGWNum(查询当前智能柜所有柜门状态信息)失败！", e);
            e.printStackTrace();
        }
        if (null == res) {
            return null;
        }
        return (String) res[0];
    }


    /**
     * 查询某个指定设备当前状态信息
     *
     * @param deviceID 设备编号
     * @return string
     */
    public static String getDeviceStateByID(String deviceID) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID};
        Object[] res = null;
        try {
            // 原：getDeviceStateByID 现：getSingleDeviceStateByID
            res = client.invoke("getSingleDeviceStateByID", t);
        } catch (Exception e) {
            log.error("调用方法getDeviceStateByID(查询某个指定设备当前状态信息)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 查询某个指定设备的打开密码
     * 若某一字段为空，则返回结果中没有这个字段
     *
     * @param deviceID 设备编号
     * @return string
     */
    public static String queryPasswordByID(String deviceID) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID};
        Object[] res = null;
        try {
            res = client.invoke("queryPasswordByID", t);
        } catch (Exception e) {
            log.error("调用方法queryPasswordByID(查询某个指定设备的打开密码)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 查询某个指定设备的打开密码（含用户类型）
     * customerType=0时，与queryPasswordByID接口返回值一致，所有密码初始化为0000
     *
     * @param deviceID     设备编号
     * @param customerType 客户类型(0:普通用户，1，2:其他类型用户)
     * @return string
     */
    public static String querySpecPasswordByID(String deviceID, String customerType) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(customerType)) {
            log.error("参数customerType为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, customerType};
        Object[] res = null;
        try {
            res = client.invoke("querySpecPasswordByID", t);
        } catch (Exception e) {
            log.error("调用方法querySpecPasswordByID(查询某个指定设备的打开密码)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 设置某个指定设备的打开密码
     *
     * @param deviceID     设备编号
     * @param customerType 客户类型(0:普通用户，1，2:其他类型用户)
     * @param passWord     要设置的密码
     * @return string
     */
    public static String updateSpecPasswordByID(String deviceID, String customerType, String passWord) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(customerType)) {
            log.error("参数customerType为空！");
            return "";
        }
        if (StringUtils.isEmpty(passWord)) {
            log.error("参数passWord为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, customerType, passWord};
        Object[] res = null;
        try {
            res = client.invoke("updateSpecPasswordByID", t);
        } catch (Exception e) {
            log.error("调用方法updateSpecPasswordByID(设置某个指定设备的打开密码)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 设置是否开启密码的自动更新
     * 密码自动更新只针对customerType为0的用户密码，默认开启自动更新
     *
     * @param deviceID 设备编号
     * @param tag      0表示关闭，1表示开启
     * @return string
     */
    public static String updateResetTag(String deviceID, String tag) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(tag)) {
            log.error("参数tag为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, tag};
        Object[] res = null;
        try {
            res = client.invoke("updateResetTag", t);
        } catch (Exception e) {
            log.error("调用方法updateResetTag(设置是否开启密码的自动更新)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 查询某个指定设备所绑定的卡号
     *
     * @param deviceID 设备编号
     * @return string
     */
    public static String queryCardByID(String deviceID) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID};
        Object[] res = null;
        try {
            res = client.invoke("queryCardByID", t);
        } catch (Exception e) {
            log.error("调用方法queryCardByID(查询某个指定设备所绑定的卡号)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 为某个指定设备添加指定卡号
     *
     * @param deviceID 设备编号
     * @param cardNum  要设置的卡号
     * @return string
     */
    public static String addCardByID(String deviceID, String cardNum) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(cardNum)) {
            log.error("参数cardNum为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, cardNum};
        Object[] res = null;
        try {
            res = client.invoke("addCardByID", t);
        } catch (Exception e) {
            log.error("调用方法addCardByID(为某个指定设备添加指定卡号)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 删除某个设备的指定卡号
     *
     * @param deviceID 设备编号
     * @param cardNum  要删除的卡号
     * @return string
     */
    public static String deleteCardByID(String deviceID, String cardNum) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        if (StringUtils.isEmpty(cardNum)) {
            log.error("参数cardNum为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID, cardNum};
        Object[] res = null;
        try {
            res = client.invoke("deleteCardByID", t);
        } catch (Exception e) {
            log.error("调用方法deleteCardByID(删除某个设备的指定卡号)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 对某个指定设备进行补货
     * 确保传入的deviceID有效，若调用接口时，goods本来就为1，接口也将返回success
     *
     * @param deviceID 设备编号，如：801001-1
     * @return string
     */
    public static String replenishByDeviceID(String deviceID) {
        if (StringUtils.isEmpty(deviceID)) {
            log.error("参数deviceID为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle, deviceID};
        Object[] res = null;
        try {
            res = client.invoke("replenishByDeviceID", t);
        } catch (Exception e) {
            log.error("调用方法replenishByDeviceID(对某个指定设备进行补货)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 对某个指定网关下所有设备节点进行补货
     * 确保传入的gwNum有效，若调用接口时，goods本来就为1，接口也将返回success
     *
     * @param gwNum 网关号，如：801001
     * @return string
     */
    public static String oneKeyReplenish(String gwNum) {
        if (StringUtils.isEmpty(gwNum)) {
            log.error("参数gwNum为空！");
            return "";
        }
        handle = getHandle();

        Object[] t = {handle};
        Object[] res = null;
        try {
            res = client.invoke("oneKeyReplenish", t);
        } catch (Exception e) {
            log.error("调用方法oneKeyReplenish(对某个指定网关下所有设备节点进行补货)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }

    /**
     * 注销用户的句柄
     *
     * @return string
     */
    public static String logout() {
        if (StringUtils.isEmpty(handle)) {
            return null;
        }

        Object[] t = {handle};
        Object[] res = null;
        try {
            res = client.invoke("logout", t);
        } catch (Exception e) {
            log.error("调用方法logout(注销用户的句柄)失败！", e);
            e.printStackTrace();
        }
        return (String) res[0];
    }
}
