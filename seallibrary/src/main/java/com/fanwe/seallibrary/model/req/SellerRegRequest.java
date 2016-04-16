package com.fanwe.seallibrary.model.req;

/**
 * 注册商家请求实体
 * Created by atlas on 15/10/12.
 * Email:atlas.tufei@gmail.com
 */
public class SellerRegRequest {
    public int sellerType;
    public String  logo;
    public String  name;
    public String  cateIds;
    public String  address;
    public String  mobile;
//    public String  pwd;
    public String  idcardSn;
    public String  idcardPositiveImg;
    public String  idcardNegativeImg;
    public String  businessLicenceImg;
    public String introduction ;

    public int provinceId;       // 省编号
    public int cityId;          // 市编号
    public int areaId;          // 区编号
    public String mapPointStr;  // 地址经纬度字符串
    public String mapPosStr;    // 经营范围坐标字符串
    public String addressDetail; //
    public String contacts;     // 店主/法人代表(商家), 真实姓名(个人)
    public String serviceTel;   // 服务电话

    public SellerRegRequest(int sellerType,
                            String logo,
                            String name,
                            String cateIds,
                            String address,
                            String mobile,
                            String pwd,
                            String idcardSn,
                            String idcardPositiveImg,
                            String idcardNegativeImg,
                            String businessLicenceImg,
                            String introduction,
                            int provinceId,
                            int cityId,
                            int areaId,
                            String mapPointStr,
                            String mapPosStr,
                            String addressDetail,
                            String contacts,
                            String serviceTel

    ) {
        this.sellerType = sellerType;
        this.logo = logo;
        this.name = name;
        this.cateIds = cateIds;
        this.address = address;
        this.mobile = mobile;
//        this.pwd = pwd;
        this.idcardSn = idcardSn;
        this.idcardPositiveImg = idcardPositiveImg;
        this.idcardNegativeImg = idcardNegativeImg;
        this.businessLicenceImg = businessLicenceImg;
        this.introduction = introduction;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.mapPointStr = mapPointStr;
        this.mapPosStr = mapPosStr;
        this.addressDetail = addressDetail;
        this.contacts = contacts;
        this.serviceTel = serviceTel;
    }
}
