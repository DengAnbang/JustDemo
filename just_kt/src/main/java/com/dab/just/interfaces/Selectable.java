package com.dab.just.interfaces;

/**
 * Created by Wendell on 2018/1/30 11:01
 */

public interface Selectable {
//    private Boolean[] mBooleans = new Boolean[]{false};

     Boolean[] getDefaultSelect();
    default Boolean select() {
        getDefaultSelect()[0] = !getDefaultSelect()[0];
        return getDefaultSelect()[0];
    }
    default Boolean isSelect() {
        return getDefaultSelect()[0];
    }

}
