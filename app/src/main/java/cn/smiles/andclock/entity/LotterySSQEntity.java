package cn.smiles.andclock.entity;

import java.util.List;

/**
 * 双色球
 *
 * @author kaifang
 * @date 2018/3/2 12:16
 */
public class LotterySSQEntity {

    /**
     * msg : success
     * result : {"awardDateTime":"2018-02-27 21:15","lotteryDetails":[{"awardNumber":6,"awardPrice":8201615,"awards":"一等奖"},{"awardNumber":111,"awardPrice":216325,"awards":"二等奖"},{"awardNumber":988,"awardPrice":3000,"awards":"三等奖"},{"awardNumber":56391,"awardPrice":200,"awards":"四等奖"},{"awardNumber":1137669,"awardPrice":10,"awards":"五等奖"},{"awardNumber":8361005,"awardPrice":5,"awards":"六等奖"}],"lotteryNumber":["07","14","19","21","22","23","03"],"name":"双色球","period":"2018022","pool":371039376,"sales":333617132}
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
         * awardDateTime : 2018-02-27 21:15
         * lotteryDetails : [{"awardNumber":6,"awardPrice":8201615,"awards":"一等奖"},{"awardNumber":111,"awardPrice":216325,"awards":"二等奖"},{"awardNumber":988,"awardPrice":3000,"awards":"三等奖"},{"awardNumber":56391,"awardPrice":200,"awards":"四等奖"},{"awardNumber":1137669,"awardPrice":10,"awards":"五等奖"},{"awardNumber":8361005,"awardPrice":5,"awards":"六等奖"}]
         * lotteryNumber : ["07","14","19","21","22","23","03"]
         * name : 双色球
         * period : 2018022
         * pool : 371039376
         * sales : 333617132
         */

        private String awardDateTime;
        private String name;
        private String period;
        private int pool;
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

        public int getPool() {
            return pool;
        }

        public void setPool(int pool) {
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
             * awardNumber : 6
             * awardPrice : 8201615
             * awards : 一等奖
             */

            private int awardNumber;
            private int awardPrice;
            private String awards;

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
        }
    }
}
