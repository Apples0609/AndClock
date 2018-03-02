package cn.smiles.andclock.entity;

import java.util.List;

/**
 * 大乐透
 *
 * @author kaifang
 * @date 2018/3/2 12:16
 */
public class LotteryDLTEntity {

    /**
     * msg : success
     * result : {"awardDateTime":"2018-02-28 20:30","lotteryDetails":[{"awardNumber":1,"awardPrice":10000000,"awards":"一等奖"},{"awardNumber":0,"awardPrice":0,"awards":"一等奖","type":"追加"},{"awardNumber":72,"awardPrice":110789,"awards":"二等奖"},{"awardNumber":17,"awardPrice":66473,"awards":"二等奖","type":"追加"},{"awardNumber":988,"awardPrice":2935,"awards":"三等奖"},{"awardNumber":364,"awardPrice":1761,"awards":"三等奖","type":"追加"},{"awardNumber":40735,"awardPrice":200,"awards":"四等奖"},{"awardNumber":14077,"awardPrice":100,"awards":"四等奖","type":"追加"},{"awardNumber":695626,"awardPrice":10,"awards":"五等奖"},{"awardNumber":233291,"awardPrice":5,"awards":"五等奖","type":"追加"},{"awardNumber":6297331,"awardPrice":5,"awards":"六等奖"}],"lotteryNumber":["12","13","18","21","29","02","08"],"name":"大乐透","period":"18023","pool":5.01645162703E9,"sales":203587803}
     * retCode : 200
     */

    private String msg;
    private ResultBean result;
    private String retCode;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public static class ResultBean {
        /**
         * awardDateTime : 2018-02-28 20:30
         * lotteryDetails : [{"awardNumber":1,"awardPrice":10000000,"awards":"一等奖"},{"awardNumber":0,"awardPrice":0,"awards":"一等奖","type":"追加"},{"awardNumber":72,"awardPrice":110789,"awards":"二等奖"},{"awardNumber":17,"awardPrice":66473,"awards":"二等奖","type":"追加"},{"awardNumber":988,"awardPrice":2935,"awards":"三等奖"},{"awardNumber":364,"awardPrice":1761,"awards":"三等奖","type":"追加"},{"awardNumber":40735,"awardPrice":200,"awards":"四等奖"},{"awardNumber":14077,"awardPrice":100,"awards":"四等奖","type":"追加"},{"awardNumber":695626,"awardPrice":10,"awards":"五等奖"},{"awardNumber":233291,"awardPrice":5,"awards":"五等奖","type":"追加"},{"awardNumber":6297331,"awardPrice":5,"awards":"六等奖"}]
         * lotteryNumber : ["12","13","18","21","29","02","08"]
         * name : 大乐透
         * period : 18023
         * pool : 5.01645162703E9
         * sales : 203587803
         */

        private String awardDateTime;
        private String name;
        private String period;
        private double pool;
        private int sales;
        private List<LotteryDetailsBean> lotteryDetails;
        private List<String> lotteryNumber;

        public String getAwardDateTime() {
            return awardDateTime;
        }

        public void setAwardDateTime(String awardDateTime) {
            this.awardDateTime = awardDateTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public double getPool() {
            return pool;
        }

        public void setPool(double pool) {
            this.pool = pool;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }

        public List<LotteryDetailsBean> getLotteryDetails() {
            return lotteryDetails;
        }

        public void setLotteryDetails(List<LotteryDetailsBean> lotteryDetails) {
            this.lotteryDetails = lotteryDetails;
        }

        public List<String> getLotteryNumber() {
            return lotteryNumber;
        }

        public void setLotteryNumber(List<String> lotteryNumber) {
            this.lotteryNumber = lotteryNumber;
        }

        public static class LotteryDetailsBean {
            /**
             * awardNumber : 1
             * awardPrice : 10000000
             * awards : 一等奖
             * type : 追加
             */

            private int awardNumber;
            private int awardPrice;
            private String awards;
            private String type;

            public int getAwardNumber() {
                return awardNumber;
            }

            public void setAwardNumber(int awardNumber) {
                this.awardNumber = awardNumber;
            }

            public int getAwardPrice() {
                return awardPrice;
            }

            public void setAwardPrice(int awardPrice) {
                this.awardPrice = awardPrice;
            }

            public String getAwards() {
                return awards;
            }

            public void setAwards(String awards) {
                this.awards = awards;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
