package com.fanwe.seallibrary.comm;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-17
 * Time: 11:09
 * FIXME
 */
public class Constants {
    public static final String OSS_FILE_PATH = "/temp/";
    public static final String TOKEN = "token";
    public static final String PAY_TYPE_BALANCE="balancePay";
    public static final String PAY_TYPE_CASH_ON_DELIVERY="cashOnDelivery";

    public static final String LOGIN_TOKEN = "logintoken";
    public static final String USER_ID="userId";
    public static final String DEVICE_TYPE = "android";
    public static final String PREFERENCE_CONFIG = "config";
    public static final String KEY = "key";
    public static final String MAP_POINT = "mapPoint";

    public static final String ACTION_NAME = "com.yizan.community.activity.OrderDetailActivity";
    /**Action name message **/
    public static final String ACTION_MSG = "com.yizan.community.msg";
    //消息已读
    public static final int MSG_READ=1;
    //消息未读
    public static final int MSG_UNREAD=0;

    /** 分页，查询所有 **/
    public static final int PAGE_SIZE = 20;
    public static final String DATA = "data";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_ADDR = "addr";
    public static final String EXTRA_SELLER = "seller";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ADDRESS = "address";

    public static final String EXTRA_BACK_ABLE="backable";
    public static final String EXTRA_INDEX="index";
    public static final String EXTRA_POINT="point";
    public static final String EXTRA_PAY = "pay";
    public static final String EXTRA_AUTO = "auto";

    //1商品   2服务
    public static final int TYPE_GOODS=1;
    public static final int TYPE_SERVICE=2;

    public static final int HEADER = 0;
    public static final int ITEM = 1;
    public static final int ITEM1 = 1;
    public static final int ITEM2 = 2;
    //商家休息
    public static final int DELIVERY_CLOSE=0;

    public static final String PREFERENCE_SERVICE_TEL = "service_tel";

    public static final String PATTERN_Y_M = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_Y_M_H_M = "yyyy年MM月dd日 HH:mm";
    public static final String PATTERN_H_M = "HH:mm";

    public static final String YMD = "yyyy-MM-dd";
    public static final String MMSS = "mm:ss";

    /**
     * 订单ID
     */
    public static final String ORDERID="orderId";

    /**
     * 月份
     */
    public static final String MONTH="month";
    /**评价百分比**/
    public static final String PATTERN_PERCENT_EVALUATION="0.0%";

    public static final String WEEKS="weeks";
    public static final String ALLWEEKS="allweeks";
    public static final String WEEKS_TIME="weekstime";


    public interface SERVERS_TYPE {
        String WASH = "wash";
        String CLEAN = "clean";
        String MAINTAIN = "maintain";
    }

    // 微信支付
    public static final String ACTION_PAY_RESULT = "com.yizan.community.life.WXPAY_RECEIVED";

    public static final String PAY_TYPE_WEICHAT = "weixin";
    public static final String PAY_TYPE_ALIPAY = "alipay";
    public static final String PAY_TYPE_UNION = "unionapp";
}
