package com.frank_ghost.hotfix.web;


import com.frank_ghost.hotfix.Replace;

/**
 * Created by admin on 2017/10/3.
 */

public class Caclutor {
    @Replace(clazz = "com.frank_ghost.fixframework.Caclutor", method = "caculator")
    public int caculator() {
//		1
        //10/0
        int i = 1;
        int j = 10;
//模拟异常修复
        return j / i;
    }
}
