package com.yizan.community.action;

import android.content.Context;

import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.ArticleInfo;
import com.fanwe.seallibrary.model.DistrictAuthInfo;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.fanwe.seallibrary.model.PropertyCompany;
import com.fanwe.seallibrary.model.Repair;
import com.fanwe.seallibrary.model.RepairType;
import com.fanwe.seallibrary.model.result.ArticleResult;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.DistrictAuthResult;
import com.yizan.community.action.result.DistrictListResult;
import com.yizan.community.action.result.DistrictResult;
import com.yizan.community.action.result.PropertyCompanyResult;
import com.yizan.community.action.result.RepairListResult;
import com.yizan.community.action.result.RepairResult;
import com.yizan.community.action.result.RepairTypeListResult;
import com.yizan.community.helper.ApiCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:53
 * 功能: 物业接口
 */
public class PropertyAction extends BaseAction {
    public PropertyAction(Context context) {
        super(context);
    }

    /**
     * 我的物业
     *
     * @param districtId
     * @param callback
     */
    public void getDistrict(int districtId, final ApiCallback<DistrictAuthInfo> callback) {
        Map<String, Object> data = new HashMap<>();
        if (districtId >= 0) {
            data.put("districtId", districtId);
        } else {
            data = null;
        }
        call(URLConstants.WY_GET_DISTRICT, data, DistrictAuthResult.class, callback);
    }

    /**
     * 我的物业
     *
     * @param districtId
     * @param callback
     */
    public void districtDetail(int districtId, final ApiCallback<DistrictInfo> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);

        call(URLConstants.WY_DISTRICT_DETAIL, data, DistrictResult.class, callback);
    }

    /**
     * 加入我的小区
     *
     * @param districtId
     * @param callback
     */
    public void addDistrict(int districtId, ApiCallback<Void> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);

        call(URLConstants.WY_ADD_DISTRICT, data, BaseResult.class, callback);
    }

    /**
     * 删除小区
     *
     * @param districtId
     * @param callback
     */
    public void delDistrict(int districtId, ApiCallback<Void> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);

        call(URLConstants.WY_DEL_DISTRICT, data, BaseResult.class, callback);
    }

    /**
     * 我的小区列表
     *
     * @param callback
     */
    public void districtList(ApiCallback<List<DistrictInfo>> callback) {
        call(URLConstants.WY_DISTRICT_LIST, null, DistrictListResult.class, callback);
    }

    /**
     * 物业详情
     *
     * @param districtId
     * @param callback
     */
    public void propertyDetail(int districtId, ApiCallback<PropertyCompany> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);
        call(URLConstants.WY_PROPERTY_DETAIL, data, PropertyCompanyResult.class, callback);
    }

    /**
     * 报修列表
     *
     * @param districtId
     * @param page
     * @param callback
     */
    public void reapirList(int districtId, int page, ApiCallback<List<Repair>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);
        data.put("page", page);
        call(URLConstants.WY_REPAIR_LIST, data, RepairListResult.class, callback);
    }

    /**
     * 报修详情
     *
     * @param callback
     */
    public void repairDetail(int districtId, int id, ApiCallback<Repair> callback) {
        call(URLConstants.WY_REPAIR_GET, null, RepairResult.class, callback);
    }

    /**
     * 报修类型列表
     */
    public void repairTypeList(ApiCallback<List<RepairType>> callback) {

        call(URLConstants.WY_REPAIR_TYPE_LIST, null, RepairTypeListResult.class, callback);
    }


    /**
     * 添加报修
     *
     * @param districtId
     * @param typeId
     * @param images
     * @param content
     * @param callback
     */
    public void createRepair(int districtId, int typeId, List<String> images, String content, ApiCallback<Void> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);
        data.put("typeId", typeId);
        data.put("images", images);
        data.put("content", content);

        call(URLConstants.WY_CREATE_REPAIR, data, BaseResult.class, callback);
    }

    /**
     * 公告列表
     *
     * @param selelrId
     * @param callback
     */
    public void articleList(int sellerId, ApiCallback<List<ArticleInfo>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("sellerId", sellerId);

        call(URLConstants.SELLER_ARTICLE_LIST, data, ArticleResult.class, callback);
    }

    /**
     * 公告阅读
     *
     * @param id
     * @param callback
     */
    public void articleRead(int id, ApiCallback<Void> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        call(URLConstants.WY_ARTICLE_READ, data, BaseResult.class, callback);
    }


    /**
     * 申请门禁
     * @param districtId
     * @param callback
     */
    public void openDoorApply(int districtId, ApiCallback<DistrictAuthInfo> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("districtId", districtId);

        call(URLConstants.USER_OPEN_DOOR_APPLY, data, DistrictAuthResult.class, callback);
    }
}
