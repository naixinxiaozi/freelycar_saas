package com.freelycar.saas.wxutils;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.Door;
import com.freelycar.saas.util.RoundTool;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class WechatTemplateMessage {

    private static final String PAY_SUCCESS_TEMPLATE_ID = "F5CyYJ5u9_1wRcapK1ECOYRrjxcLcL3rjB0xUg_VQn0";
    private static final String PAY_FAIL_ID = "o0TOjg7KkxoL4CtQ91j--TVVmxYDSNk-dLWqoUVd8mw";
    private static final String ORDER_CHANGED_ID = "PeRe0M0iEbm7TpN6NOThhBUjwzy_aHsi6r2E7Pa8J1A";
    private static final String ORDER_CREATE_ID = "C-m3oRNedo3vM6ugQXBa4RhOtW3qwqM7tjWNKuAgYe8";

    private static final Logger log = LogManager.getLogger(WechatTemplateMessage.class);

    private static final String PAY_ERROR_DATABASE_FAIL = "服务异常";

    private static String invokeTemplateMessage(JSONObject params) {
        //解决中文乱码问题
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        String result = HttpRequest.postCall(WechatConfig.WECHAT_TEMPLATE_MESSAGE_URL +
                        WechatConfig.getAccessTokenForInteface().getString("access_token"),
                entity, null);
        log.debug("微信模版消息结果：" + result);
        return result;
    }
/*


    //{{first.DATA}}
//类型：{{keyword1.DATA}}
//金额：{{keyword2.DATA}}
//状态：{{keyword3.DATA}}
//时间：{{keyword4.DATA}}
//备注：{{keyword5.DATA}}
//{{remark.DATA}}
    public static void paySuccess(WXPayOrder wxPayOrder) {
        log.debug("准备支付成功模版消息。。。");
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", wxPayOrder.getOpenId());
        params.put("template_id", PAY_SUCCESS_TEMPLATE_ID);
//		params.put("url", "http://www.geariot.com/fitness/class.html");
        data.put("first", keywordFactory("支付成功", "#173177"));
        data.put("keyword1", keywordFactory(wxPayOrder.getProductName(), "#173177"));
        data.put("keyword2", keywordFactory((float) (Math.round(wxPayOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword3", keywordFactory("成功", "#173177"));
        data.put("keyword4", keywordFactory(df.format(wxPayOrder.getFinishDate()), "#173177"));
        data.put("keyword5", keywordFactory(""));
        data.put("remark", keywordFactory(""));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付成功模版消息结果：" + result);
//		return result;
    }

    public static void paySuccess(ConsumOrder consumerOrder, String openId) {
        log.debug("准备支付成功模版消息。。。");
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", PAY_SUCCESS_TEMPLATE_ID);
//		params.put("url", "http://www.geariot.com/fitness/class.html");
        data.put("first", keywordFactory("支付成功", "#173177"));
        data.put("keyword1", keywordFactory(getConsumOrderProductName(consumerOrder), "#173177"));
        data.put("keyword2", keywordFactory((float) (Math.round(consumerOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword3", keywordFactory("成功", "#173177"));
        data.put("keyword4", keywordFactory(df.format(consumerOrder.getFinishTime()), "#173177"));
        data.put("keyword5", keywordFactory(""));
        data.put("remark", keywordFactory(""));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付成功模版消息结果：" + result);
//		return result;
    }


    //	{{first.DATA}}
//	支付金额：{{keyword1.DATA}}
//	商品信息：{{keyword2.DATA}}
//	失败原因：{{keyword3.DATA}}
//	{{remark.DATA}}
    public static void errorCancel(WXPayOrder wxPayOrder) {
        log.debug("支付成功，数据库更新失败！");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", wxPayOrder.getId());
        params.put("template_id", PAY_FAIL_ID);

        data.put("first", keywordFactory("支付失败", "#173177"));
        data.put("keyword1", keywordFactory((float) (Math.round(wxPayOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword2", keywordFactory(wxPayOrder.getProductName(), "#173177"));
        data.put("keyword3", keywordFactory("服务异常", "#173177"));
        data.put("remark", keywordFactory("请妥善保存单号，联系客服人员"));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付失败结果：" + result);
    }

    public static void errorWXCancel(ConsumOrder consumerOrder, String openId) {
        log.debug("支付成功，数据库更新失败！");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", PAY_FAIL_ID);
        data.put("first", keywordFactory("支付失败", "#173177"));
        data.put("keyword1", keywordFactory((float) (Math.round(consumerOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword2", keywordFactory(getConsumOrderProductName(consumerOrder), "#173177"));
        data.put("keyword3", keywordFactory("服务异常", "#173177"));
        data.put("remark", keywordFactory("请妥善保存单号，联系客服人员"));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付失败结果：" + result);
    }

    private static String getConsumOrderProductName(ConsumOrder consumerOrder) {
        String productName = "";
        for (ProjectInfo projectInfo : consumerOrder.getProjects())
            productName += projectInfo.getName();
        return productName;
    }

    private static JSONObject keywordFactory(String value) {
        JSONObject keyword = new JSONObject();
        keyword.put("value", value);
        return keyword;
    }

    private static JSONObject keywordFactory(String value, String color) {
        JSONObject keyword = keywordFactory(value);
        keyword.put("color", color);
        return keyword;
    }

    */

    /**
     * 推送通知：智能柜用户订单状态变化
     * {{first.DATA}}
     * 订单编号： {{OrderSn.DATA}}
     * 订单状态： {{OrderStatus.DATA}}
     * {{remark.DATA}}
     */
    public static void orderChanged(ConsumerOrder consumerOrder, String openId) {
        log.info("准备订单更新模版消息。。。");

        String staffName = consumerOrder.getPickCarStaffName();
        Integer state = consumerOrder.getState();
        String first;
        String stateString;
        String parkingLocation = consumerOrder.getParkingLocation();
        String remark = "";
        String remarkSuffix = "小易爱车竭诚为您服务！";
        switch (state) {
            case 1:
                stateString = "已接车";
                first = "已接到您的爱车" + consumerOrder.getLicensePlate() + "，我们将马上为您服务。";
                if (StringUtils.hasText(staffName)) {
                    remark += "服务人员：" + staffName + "\n";
                }
                break;
            case 2:
                stateString = "已完工";
                first = "您的爱车" + consumerOrder.getLicensePlate() + "已服务完成，等待您的取回。";
                if (StringUtils.hasText(staffName)) {
                    remark += "服务人员：" + staffName + "\n";
                }
                if (StringUtils.hasText(parkingLocation)) {
                    remark += "停车位置：" + parkingLocation + "\n";
                }
                break;
            case 3:
                stateString = "已交车";
                first = "您的爱车" + consumerOrder.getLicensePlate() + "已交车。期待您的再次光临。";
                break;
            default:
                stateString = "已交车";
                first = "您的爱车" + consumerOrder.getLicensePlate() + "已交车。期待您的再次光临";
        }

        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", ORDER_CHANGED_ID);
        params.put("url", WechatConfig.APP_DOMAIN + "#/shop/servicesorder?id=" + consumerOrder.getId());
        data.put("first", keywordFactory(first, "#173177"));
        data.put("OrderSn", keywordFactory(consumerOrder.getId(), "#173177"));
        data.put("OrderStatus", keywordFactory(stateString, "#173177"));
        data.put("remark", keywordFactory(remark + remarkSuffix));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.info("微信订单更新模版消息结果：" + result);
    }

    /**
     * 消息推送：订单生成时告知技师
     * <p>
     * {{first.DATA}}
     * 需求时间：{{keyword1.DATA}}
     * 需求类型：{{keyword2.DATA}}
     * 车型：{{keyword3.DATA}}
     * 报价：{{keyword4.DATA}}
     * 详情：{{keyword5.DATA}}
     * {{remark.DATA}}
     */
    public static void orderCreated(ConsumerOrder consumerOrder, String openId, Door door, Ark ark) {
        log.info("准备订单生成成功的模版消息。");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        String first = consumerOrder.getClientName() + " 刚刚预约了新的服务订单";
        String createTime = sdf.format(consumerOrder.getCreateTime());

//        String stateString;
//        String parkingLocation = consumerOrder.getParkingLocation();
//        String remark = "";
//        String remarkSuffix = "小易爱车竭诚为您服务！";


        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", ORDER_CREATE_ID);
//        params.put("url", "https://" + WechatConfig.APP_DOMAIN + "#/shop/servicesorder?id=" + consumerOrder.getId());

        //第一行信息
        data.put("first", keywordFactory(first, "#173177"));
        //需求时间
        data.put("keyword1", keywordFactory(createTime, "#173177"));
        //需求类型
        data.put("keyword2", keywordFactory("汽车服务", "#173177"));
        //车型
        data.put("keyword3", keywordFactory(consumerOrder.getCarBrand(), "#173177"));
        //报价
        data.put("keyword4", keywordFactory(String.valueOf(RoundTool.round(consumerOrder.getTotalPrice(), 2, BigDecimal.ROUND_HALF_UP)), "#173177"));
        //详情
        data.put("keyword5", keywordFactory("智能柜：" + door.getArkSn() + "-" + door.getDoorSn(), "#173177"));

        data.put("remark", keywordFactory("智能柜名称：" + ark.getName() + "\n地址：" + ark.getLocation() + "\n请前往对应的智能柜扫码查看（取车）"));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.info("微信订单更新模版消息结果：" + result);
    }

    private static JSONObject keywordFactory(String value) {
        JSONObject keyword = new JSONObject();
        keyword.put("value", value);
        return keyword;
    }

    private static JSONObject keywordFactory(String value, String color) {
        JSONObject keyword = keywordFactory(value);
        keyword.put("color", color);
        return keyword;
    }
}
