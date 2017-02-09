package io.github.marktony.espresso.entity;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/2/9.
 */

public class CompanyAuto {

    private String comCode;
    private String num;
    private List<Auto> auto;

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Auto> getAuto() {
        return auto;
    }

    public void setAuto(List<Auto> auto) {
        this.auto = auto;
    }

    public class Auto {

        private String comCode;
        private String id;
        private int noCount;
        private String noPre;
        private String startTime;

        public String getComCode() {
            return comCode;
        }

        public void setComCode(String comCode) {
            this.comCode = comCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNoCount() {
            return noCount;
        }

        public void setNoCount(int noCount) {
            this.noCount = noCount;
        }

        public String getNoPre() {
            return noPre;
        }

        public void setNoPre(String noPre) {
            this.noPre = noPre;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }

}
