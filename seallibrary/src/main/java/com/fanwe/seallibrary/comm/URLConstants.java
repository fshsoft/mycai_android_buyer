package com.fanwe.seallibrary.comm;


import com.fanwe.seallibrary.BuildConfig;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-17
 * Time: 11:10
 * FIXME
 */
public class URLConstants {
    public static enum URLEnum {

        /**
         * 加密
         */
        Encryption,
        /**
         * 未加密
         */
        NoEncryption,
    }

    public static final URLEnum ENUM = URLEnum.Encryption;
    //
//    public static final String URL = "192.168.1.199";//BuildConfig.DOMAIN_WWW;
//    public static final String DOMAIN = "http://" + URL + "/buyer/v1/";
    public static final String URL = BuildConfig.DOMAIN_WWW;
    public static final String DOMAIN = "http://" + BuildConfig.DOMAIN_API + URL + "/buyer/v1/";


    private static final String DOMAIN_WAP = "http://wap." + URL;

    public static final String URL_GOOD_DETAIL = DOMAIN_WAP + "/Goods/detail?goodsId=";
    // 免责声明
    public static final String URL_DISCLAIMER = DOMAIN_WAP + "/More/detail?code=1";
    // 关于我们
    public static final String URL_ABOUT = DOMAIN_WAP + "/More/staffaboutus";
    // 使用帮助
    public static final String URL_HELP = DOMAIN_WAP + "/More/detail?code=2";
    //退款详情
    public static final String URL_REFUND = DOMAIN + "order.viewrefund?";
    /**
     * 服务商圈设置 Web地址
     */
    public static final String DISTRICT_RANGE = DOMAIN_WAP + "/district/index?token=%s&staffId=%d";
    /**
     * POI webservice方式搜索
     */
    public static final String POI_SUGGESTION = "http://apis.map.qq.com/ws/place/v1/suggestion/";
    public static final String POI_SEARCH = "http://apis.map.qq.com/ws/place/v1/search";
    /**
     * geocoder webservice方式搜索
     */
    public static final String MAP_GEOCODER = "http://apis.map.qq.com/ws/geocoder/v1";

    public static final String SHOP_RANGE = DOMAIN + "seller.mappos?token=%s&userId=%d&mapPoint=%s";
    public static final String SYS_MESSAGE = DOMAIN + "msg.message?token=%s&userId=%d";//DOMAIN + "/seller.mappos?token=%s&userId=%d";
    public static final String INIT = DOMAIN + "app.init";
    public static final String INENTIFYING = DOMAIN + "user.mobileverify";// 发验证码
    public static final String REGISTER = DOMAIN + "user.reg"; // 注册
    //找回密码
    public static final String USER_RESET = DOMAIN + "user.repwd";
    public static final String LOGIN = DOMAIN + "user.login"; // 登录
    //http://api.shnow.cn/buyer/v1/user.ping
    public static final String USER_PING=DOMAIN+"user.ping";
    public static final String QUICK_LOGIN = DOMAIN + "user.verifylogin"; // 会员快捷登陆（验证码登录）
    public static final String LOGOUT = DOMAIN + "user.logout"; // 登出
    public static final String MESGREAD = DOMAIN + "msg.read"; // 登出
    public static final String ORDER_GETSHARE = DOMAIN + "activity.getshare"; // 分享活动
    public static final String ORDER_NOTSHOW = DOMAIN + "order.notshow"; // 不显示分享活动那个框框


    /**
     * 商家
     */
    public static final String SELLERCATELISTS = DOMAIN + "seller.catelists";
    public static final String SELLERLISTS = DOMAIN + "seller.lists";
    public static final String SELLERDETAIL = DOMAIN + "seller.detail";


    public static final String GOODS_DETAIL = DOMAIN + "goods.detail";

    public static final String SELLER_HOTLISTS = DOMAIN + "seller.hotlists";


    /**
     * 首页配置
     */
    public static final String HOME_CONFIG = DOMAIN + "config.index";

    /**
     * 订单列表
     */
    public static final String ORDERLISTS = DOMAIN + "order.lists";
    /**
     * 催单
     */
    public static final String ORDER_REMINDER = DOMAIN + "order.urge";

    /**
     * 购物车列表
     */
    public static final String CART_LISTS = DOMAIN + "shopping.lists";

    // 消息状态
    public static final String MSG_STATUS = DOMAIN + "msg.status";
    // 余额查询
    public static final String BALANCE_QUERY = DOMAIN + "user.balance";
    // 余额查询列表
    public static final String BALANCE_LIST = DOMAIN + "user.getbalance";

    /**
     * 获取商品列表
     */
    public static final String GOODS_LISTS = DOMAIN + "goods.lists";


    /**
     * 保存购物车
     */
    public static final String SHOPPING_SAVE = DOMAIN + "shopping.save";

    /**
     * 删除购物车
     */
    public static final String SHOPPING_DELETE = DOMAIN + "shopping.delete";

    /***
     * 商家页广告位
     */
    public static final String SHOPPING_ADV = DOMAIN + "config.seller";

    /***
     * 服务列表
     */
    public static final String SERVICE_LIST = DOMAIN + "service.lists";


    /***
     * 创建定单
     */
    public static final String ORDER_CREATE = DOMAIN + "order.create";
    public static final String ORDER_DETAIL = DOMAIN + "order.detail";
    public static final String ORDER_CANCEL = DOMAIN + "order.cancel";
    public static final String ORDER_CONFIRM = DOMAIN + "order.confirm";
    public static final String ORDER_REFUND = DOMAIN + "order.refund";
    public static final String ORDER_DELETE = DOMAIN + "order.delete";
    public static final String ORDER_STATUS = DOMAIN + "order.status";
    public static final String ORDER_PAY = DOMAIN + "order.pay";
    public static final String USER_RECHARGE = DOMAIN + "user.charge";


    public static final String COLLECT_CREATE = DOMAIN + "collect.create";


    /**
     * 评价统计
     */
    public static final String SELLER_RATE_STATISTICS = DOMAIN + "rate.order.statistics";

    /**
     * 评价列表
     */
    public static final String SELLER_RATE_LIST = DOMAIN + "rate.order.lists";

    /**
     * 添加评价
     */
    public static final String SELLER_RATE_CREATE = DOMAIN + "rate.order.create";

    /**
     * 公告列表
     */
    public static final String SELLER_ARTICLE_LIST = DOMAIN + "article.lists";

    /**
     * 订单详情
     */
    public static final String ORDERDETAIL = DOMAIN + "order.details";
    public static final String ORDER_DEL = DOMAIN + "order.delete";
    public static final String ORDERDESTAFFLOG = DOMAIN + "order.stafflog";
    /**
     * 我的收藏
     */
    public static final String COLLECTIONLISTS = DOMAIN + "collect.lists";
    public static final String COLLECTIONCREATE = DOMAIN + "collect.create";
    public static final String COLLECTIONDELETE = DOMAIN + "collect.delete";

    /**
     * 收货地址
     */
    public static final String USERADDRESSLISTS = DOMAIN + "user.address.lists";
    public static final String USERADDRESSCREATE = DOMAIN + "user.address.create";
    public static final String USERADDRESSSETDEFAULT = DOMAIN + "user.address.setdefault";
    public static final String USERADDRESSDELETE = DOMAIN + "user.address.delete";
    /**
     * 订单更新状态
     */
    public static final String ORDERUPDATESTATUS = DOMAIN + "order.updatestatus";

    /**
     * 开始服务
     */
    public static final String ORDER_STARTSERVICE = DOMAIN + "order.startservice";

    /**
     * 接受订单
     */
    public static final String ORDER_RECEIVESERVICE = DOMAIN + "order.receiveservice";

    /**
     * 拒绝订单
     */
    public static final String ORDER_REFUSESERVICE = DOMAIN + "order.refuseservice";

    /**
     * 评价回复
     */
    public static final String ORDER_STAFF_REPLY = DOMAIN + "rate.staff.reply";
    /**
     * 正在服务的订单
     */
    public static final String ORDERFIRST = DOMAIN + "order.first";

    /**
     * 统计明细
     */
    public static final String STATISTICSDETAIL = DOMAIN + "statistics.detail";
    /**
     * 按月份来统计
     */
    public static final String STATISTICSMONTH = DOMAIN + "statistics.month";

    //员工余额
    public static final String STAFFBALANCE = DOMAIN + "staff.get";


    // 评价列表
    public static final String EVALUATION_LIST = DOMAIN + "rate.staff.lists";
    // 评价统计
    public static final String EVALUATION_STATISTICS = DOMAIN + "staff.detail";

    // 日程列表
    public static final String SCHEDULE_LIST = DOMAIN + "schedule.lists";


    // 日程列表
    public static final String SCHEDULE_UPDATE = DOMAIN + "schedule.update";
    // 消息列表
    public static final String MSG_LIST = DOMAIN + "msg.lists";
    // 消息阅读
    public static final String MSG_READ = DOMAIN + "msg.read";
    // 消息删除
    public static final String MSG_DEL = DOMAIN + "msg.delete";
    //是否有未读消息
    public static final String MSG_GET = DOMAIN + "msg.get";

    /**
     * 意见反馈
     */
    public static final String FEEDBACK_CREATE = DOMAIN + "feedback.create";


    /**
     * 添加请假
     */
    public static final String STAFFLEAVE_CREATE = DOMAIN + "staffleave.create";

    /**
     * 请假列表
     */
    public static final String STAFFLEAVE_LISTS = DOMAIN + "staffleave.lists";
    /**
     * 删除请假记录
     */
    public static final String STAFFLEAVE_DELETE = DOMAIN + "staffleave.delete";

    // 员工信息更新
    public static final String STAFF_UPDATE = DOMAIN + "user.info.update";
    // 服务地址更新
    public static final String STAFF_ADDRESS_ADD = DOMAIN + "staff.address";
    /**
     * 服务时间列表
     */
    public static final String STAFFSTIME_LISTS = DOMAIN + "staffstime.lists";

    /**
     * 员工服务时间详情
     */
    public static final String STAFFSTIME_EDIT = DOMAIN + "staffstime.edit";

    /**
     * 员工服务时间添加
     */
    public static final String STAFFSTIME_ADD = DOMAIN + "staffstime.add";

    /**
     * 员工服务时间删除
     */
    public static final String STAFFSTIME_DELETE = DOMAIN + "staffstime.delete";

    /**
     * 员工服务时间更新
     */
    public static final String STAFFSTIME_UPDATE = DOMAIN + "staffstime.update";

    /**
     * 开始洗车
     */
    public static final String ORDER_START_CAR = DOMAIN + "order.startcar";

    /**
     * 结束洗车
     */
    public static final String ORDER_FINISH_CAR = DOMAIN + "order.finishcar";

    /**
     * 公告
     */
    public static final String ANNOUNCEMENT_MSG = DOMAIN + "announce.get";
    public static final String ANNOUNCEMENT_READ = DOMAIN + "announce.read";
    public static final String ANNOUNCEMENT = DOMAIN + "announce.lists";

    public static final String STAFF_REPLY = DOMAIN + "rate.staff.reply";
    /**
     * 消息
     */

    public static final String MESSAGE_LIST = DOMAIN + "msg.lists";
    public static final String MESSAGE_GETDATA = DOMAIN + "msg.getdata";
    public static final String MESSAGE_READ = DOMAIN + "msg.read";
    public static final String MESSAGE_DELETE = DOMAIN + "msg.delete";
    /**
     * 未完成定单日程
     */
    public static final String ORDER_SCHEDULE = DOMAIN + "order.schedule";

    /**
     * 历史定单日程
     */
    public static final String ORDER_HISTORY_SCHEDULE = DOMAIN + "order.wapsorderlists";


    /**
     * 家政开始服务
     */
    public static final String ORDER_HOUSE_KEEPING_START = DOMAIN + "order.startservice";


    /**
     * 取件成功
     */
    public static final String ORDER_PICK_UP = DOMAIN + "order.pickup";

    /**
     * 更新定单状态
     */
    public static final String ORDER_UPDATE_STATUS = DOMAIN + "order.updatestatus";

    /**
     * 获取收费商品
     */
    public static final String ORDER_GOODSLISTS = DOMAIN + "order.goodslists";


    /**
     * 创建工作日志
     */
    public static final String ORDER_STAFF_LOG = DOMAIN + "order.stafflog";
    /**
     * 是否为商家
     */
    public static final String USER_IS_SELLER = DOMAIN + "seller.check";

    public static final String USER_SELLER_REG = DOMAIN + "seller.reg";


    /**
     * 门禁相关
     */
    //获取开门钥匙信息
    public static final String USER_DOOR_KEYS = DOMAIN + "user.getdoorkeys";
    //修改门禁信息
    public static final String USER_EDIT_DOOR = DOMAIN + "user.editdoorinfo";
    //获取最近小区
    public static final String DISTRICT_NEAREST = DOMAIN + "district.getnearestlist";
    //搜索小区
    public static final String DISTRICT_SEARCH = DOMAIN + "district.searchvillages";
    //获取小区楼栋号
    public static final String DISTRICT_BUILDING = DOMAIN + "district.getbuildinglist";
    //获取小区房间号
    public static final String DISTRICT_ROOMS = DOMAIN + "district.getroomlist";
    //小区身份认证
    public static final String USER_VILLAGES_AUTH = DOMAIN + "user.villagesauth";
    //小区身份认证检查
    public static final String USER_CHECK_VILLAGESAUTH = DOMAIN + "user.checkvillagesauth";
    // 小区门禁申请
    public static final String USER_OPEN_DOOR_APPLY = DOMAIN + "user.applyaccess";

    // 开门统计
    public static final String USER_OPEN_DOOR_STAT = DOMAIN + "user.opendoor";

    /**
     * 更新手机号码校验
     */
    public static final String USER_INFO_VERIFY_MOBILE = DOMAIN + "user.info.verifymobile";

    /**
     * 更新手机号码
     */
    public static final String USER_UPDATE_MOBILE = DOMAIN + "user.updatemobile";
    /**
     * 修改密码（新）
     */
    public static final String USER_INFO_CHG_PWD = DOMAIN + "user.renewpwd";


    // 订单计算
    public static final String ORDER_COMPUTE = DOMAIN + "order.compute";

    // bbs

    /**
     * 生活圈首页-论坛板块
     */
    public static final String BBS_HOME_FORUM_INDEX = DOMAIN + "forumposts.index";

    //帖子列表
    public static final String BBS_FORUM_LIST = DOMAIN + "forumposts.lists";

    //帖子搜索
    public static final String BBS_FORUM_SEARCH = DOMAIN + "forumposts.search";

    //帖子板块
    public static final String BBS_FORUM_PLATE = DOMAIN + "forumplate.lists";

    //帖子详情
    public static final String BBS_FORUM_POSTS_DETAIL = DOMAIN + "forumposts.edit";

    //创建/修改 帖子
    public static final String BBS_FORUM_POSTS_SAVE = DOMAIN + "forumposts.save";

    //论坛消息列表
    public static final String BBS_FORUM_MESSAGE_LIST = DOMAIN + "forummessage.lists";

    //论坛消息删除
    public static final String BBS_FORUM_MESSAGE_DEL = DOMAIN + "forummessage.delete";

    //论坛消息阅读
    public static final String BBS_FORUM_MESSAGE_READ = DOMAIN + "forummessage.read";

    //我的帖子
    public static final String BBS_USER_FORUM_LIST = DOMAIN + "forumposts.userlists";

    //帖子回复
    public static final String BBS_FORUM_REPLY = DOMAIN + "forumposts.reply";

    //帖子点赞
    public static final String BBS_FORUM_PRAISE = DOMAIN + "forumposts.praise";

    //帖子举报
    public static final String BBS_FORUM_COMPLAIN = DOMAIN + "forumposts.complain";

    //帖子删除
    public static final String BBS_FORUM_DELETE = DOMAIN + "forumposts.delete";

    //会员优惠券列表
    public static final String USER_PROMOTION_LIST = DOMAIN + "user.promotion.lists";

    //优惠券兑换 / 领取
    public static final String USER_PROMOTION_EXCHANGE = DOMAIN + "user.promotion.exchange";
    //会员可用的第一个优惠券信息
    public static final String USER_PROMOTION_FIRST = DOMAIN + "user.promotion.first";
    //获取优惠券信息
    public static final String USER_PROMOTION_INFO = DOMAIN + "user.promotion.get";


    // 物业

    // 获取我的物业
    public static final String WY_GET_DISTRICT = DOMAIN + "district.getdistrict";

    // 加入我的小区
    public static final String WY_ADD_DISTRICT = DOMAIN + "district.create";

    // 删除小区
    public static final String WY_DEL_DISTRICT = DOMAIN + "district.delete";

    // 获取小区详情
    public static final String WY_DISTRICT_DETAIL = DOMAIN + "district.get";

    // 物业详情
    public static final String WY_PROPERTY_DETAIL = DOMAIN + "property.detail";

    // 我的小区列表
    public static final String WY_DISTRICT_LIST = DOMAIN + "district.lists";

    // 报修列表
    public static final String WY_REPAIR_LIST = DOMAIN + "property.repairlists";

    // 报修详情
    public static final String WY_REPAIR_GET = DOMAIN + "property.repairget";

    // 报修类型列表
    public static final String WY_REPAIR_TYPE_LIST = DOMAIN + "property.typelists";

    // 添加报修
    public static final String WY_CREATE_REPAIR = DOMAIN + "property.createrepair";

    // 社区公告已读
    public static final String WY_ARTICLE_READ = DOMAIN + "article.read";

    //分享
    public static final String WY_SHARE = DOMAIN + "useractive.share";


}
