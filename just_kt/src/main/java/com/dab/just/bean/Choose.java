package com.dab.just.bean;

/**
 * Created by Wendell on 2018/1/30 11:01
 */

public interface Choose {
    Boolean[] isChoose = new Boolean[]{false};

    default Boolean choose(){
        isChoose[0] = !isChoose[0];
        return isChoose[0];
    }

    default Boolean isChoose() {
        return isChoose[0];
    }

}
