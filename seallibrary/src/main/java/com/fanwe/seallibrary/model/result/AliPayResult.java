package com.fanwe.seallibrary.model.result;

import android.text.TextUtils;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-28
 * Time: 15:30
 * FIXME
 */
public class AliPayResult {
    public static final int ALIPAY_FLAG = 30;
    public static final String ALI_PAY_RESULT_OK = "9000";

    //    private static final Map<String, String> sResultStatus;
    String resultStatus = null;
    String memo = null;
    String result = null;
    boolean isSignOk = false;
    private String mResult;

    public AliPayResult(String rawResult) {
        if (TextUtils.isEmpty(rawResult))
            return;

        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                resultStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                memo = gatValue(resultParam, "memo");
            }
        }
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }
//	static {
//		sResultStatus = new HashMap<String, String>();
//		sResultStatus.put("9000", "操作成功");
//		sResultStatus.put("8000", "支付结果确认中");
//		sResultStatus.put("4000", "系统异常");
//		sResultStatus.put("4001", "数据格式不正确");
//		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
//		sResultStatus.put("4004", "该用户已解除绑定");
//		sResultStatus.put("4005", "绑定失败或没有绑定");
//		sResultStatus.put("4006", "订单支付失败");
//		sResultStatus.put("4010", "重新绑定账户");
//		sResultStatus.put("6000", "支付服务正在进行升级操作");
//		sResultStatus.put("6001", "用户中途取消支付操作");
//		sResultStatus.put("7001", "网页支付失败");
//	}

    /**
     * @return the result
     */
    public String getResult() {
        return TextUtils.isEmpty(result) ? memo : result;
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }
}
