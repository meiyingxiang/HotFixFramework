package com.frank_ghost.hotfix;

/**
 * Created by admin on 2017/10/3.
 */

public class Caclutor {
    public int caculator() {
//		1
        //10/0
        int i = 0;
        int j = 10;
//模拟异常产生
        return j / i;
    }
}
