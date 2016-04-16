package com.fanwe.seallibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-05
 * Time: 16:30
 * 功能:
 */
public class SearchHistory {
    public List<String> datas;
    public SearchHistory(List<String> datas){
        this.datas = datas;
    }
    public SearchHistory(){
        this.datas = new ArrayList<>();
    }
}
