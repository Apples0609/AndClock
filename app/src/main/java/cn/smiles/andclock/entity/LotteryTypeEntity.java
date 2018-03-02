package cn.smiles.andclock.entity;

import java.util.List;

/**
 * 彩票种类
 *
 * @author kaifang
 * @date 2018/3/2 12:17
 */
public class LotteryTypeEntity {

    /**
     * msg : success
     * result : ["双色球","大乐透","3D","排列3","排列5","七星彩","七乐彩","胜负彩","任选九","六场半全场","四场进球"]
     * retCode : 200
     */

    private String msg;
    private String retCode;
    private List<String> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LotteryTypeEntity{" +
                "msg='" + msg + '\'' +
                ", retCode='" + retCode + '\'' +
                ", result=" + result +
                '}';
    }
}
