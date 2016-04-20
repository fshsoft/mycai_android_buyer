package com.fiftyonemycai365.buyer.helper;

import com.fanwe.seallibrary.model.DoorKeysInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fiftyonemycai365.buyer.YizanApp;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-28
 * Time: 14:11
 * 功能:
 */
public class DoorMgr {
    protected DoorMgr() {

    }

    public static DoorMgr getInstance() {
        return new DoorMgr();
    }

    public List<DoorKeysInfo> getAllOpenDoors() {
        if (!O2OUtils.isLogin(YizanApp.getInstance())) {
            return new ArrayList<>();
        }
        UserDoors userDoors = PreferenceUtils.getObject(YizanApp.getInstance(), UserDoors.class);
        if (userDoors == null || userDoors.userId != getUserId() || ArraysUtils.isEmpty(userDoors.list)) {
            return new ArrayList<>();
        }

        ArrayList<DoorKeysInfo> doors = new ArrayList<>();
        for (DistrictDoor districtDoor : userDoors.list) {
            if (!ArraysUtils.isEmpty(districtDoor.list) && districtDoor.openStatus == 0) {
                doors.addAll(districtDoor.list);
            }
        }
        return doors;
    }

    protected int getUserId() {
        UserInfo userInfo = PreferenceUtils.getObject(YizanApp.getInstance(), UserInfo.class);
        if (userInfo == null) {
            return -1;
        }
        return userInfo.id;
    }

    public DistrictDoor getDistrictDoor(int districtId) {
        if (!O2OUtils.isLogin(YizanApp.getInstance())) {
            return new DistrictDoor();
        }
        int userId = getUserId();
        UserDoors userDoors = PreferenceUtils.getObject(YizanApp.getInstance(), UserDoors.class);
        if (userDoors == null || userDoors.userId != userId || ArraysUtils.isEmpty(userDoors.list)) {
            return new DistrictDoor(districtId);
        }

        for (DistrictDoor districtDoor : userDoors.list) {
            if (districtDoor.districtId != districtId) {
                continue;
            }
            return districtDoor;
        }
        return new DistrictDoor(districtId);
    }

    public void setDistrictDoors(DistrictDoor districtDoor) {
        if (!O2OUtils.isLogin(YizanApp.getInstance())) {
            return;
        }
        int userId = getUserId();
        UserDoors userDoors = PreferenceUtils.getObject(YizanApp.getInstance(), UserDoors.class);
        if (userDoors == null || userDoors.userId != userId) {
            userDoors = new UserDoors();
            userDoors.userId = userId;
        }

        if (ArraysUtils.isEmpty(userDoors.list)) {
            userDoors.list = new ArrayList<>();
        }

        boolean isExist = false;
        for (DistrictDoor door : userDoors.list) {
            if (door.districtId == districtDoor.districtId) {
                door.list = districtDoor.list;
                door.openStatus = districtDoor.openStatus;
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            userDoors.list.add(districtDoor);
        }
        PreferenceUtils.setObject(YizanApp.getInstance(), userDoors);

    }


    public int getDoorCount() {
        List<DoorKeysInfo> list = getAllOpenDoors();
        if (ArraysUtils.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    public static class UserDoors {
        public int userId;
        public List<DistrictDoor> list;
    }

    public static class DistrictDoor {
        public int districtId;
        public int openStatus; // 0: 自动开，1: 手动开
        public List<DoorKeysInfo> list;

        public DistrictDoor(){

        }
        public DistrictDoor(int districtId){
            this.districtId = districtId;
        }
        public boolean isAutoOpen() {
            return openStatus == 0;
        }

        public void setAutoOpen(boolean bOpen) {
            this.openStatus = bOpen ? 0 : 1;
            DoorMgr.getInstance().setDistrictDoors(this);
        }

        public void setDoorList(List<DoorKeysInfo> list){
            this.list = list;
            DoorMgr.getInstance().setDistrictDoors(this);
        }
    }
}
