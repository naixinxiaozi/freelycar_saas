package com.freelycar.saas.util;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;

/**
 * LeanCloud 短信认证 Java SDK 调用方式 demo
 * （2019-09会推出新SDK，暂时项目中还是采用URL调用方式，此demo不删除，作为参考）
 *
 * @author tangwei - Toby
 * @date 2019-07-12
 * @email toby911115@gmail.com
 */
public class TestAVOSCloud {
    public static void main(String[] args) {
        // 参数依次为 AppId、AppKey、MasterKey
        AVOSCloud.initialize("YPVPvcghD0yT1CtQKUOpOUGI-gzGzoHsz", "AnrwmLo01qL7RuKNbV0NwWR4", "TPP91RhAzOntQMb2elHO7nKu");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(false);

        // 下面参数中的 10 表示验证码有效时间为 10 分钟
        try {
            AVOSCloud.requestSMSCode("186xxxxxxxx", "小易爱车", "短信认证", 10);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }
}
