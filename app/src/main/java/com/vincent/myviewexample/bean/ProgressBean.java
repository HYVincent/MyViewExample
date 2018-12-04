package com.vincent.myviewexample.bean;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe
 * @date 2018/11/21 17:14
 */
public class ProgressBean {

    private int status;//0 未做  1 正在做 2 做完了
    private String desc;//描述 注意 以换行符为标记拆分字符串

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
