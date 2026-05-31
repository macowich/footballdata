package se.mac.footballdata.predictions;

import java.util.Map;

public class OddsInfo {
    public float over_1_5;
    public float under_1_5;
    public float over_2_5;
    public float under_2_5;
    public float over_3_5;
    public float under_3_5;
    public Float homewin;
    public Float draw;
    public Float awaywin;
    public Float over8Corners;
    public Float under8Corners;
    public Float over9Corners;
    public Float under9Corners;
    public Float over10Corners;
    public Float under10Corners;
    public Float bttsYes;
    public Float bttsNo;
    public Float ht_over_0_5;
    public Float ht_under_0_5;
    public Float ht_over_1_5;
    public Float ht_under_1_5;
    public Float homeOver_1_5;
    public Float homeUnder_1_5;
    public Float awayOver_1_5;
    public Float awayUnder_1_5;

    public OddsInfo() {
    }

    public OddsInfo(Map<String, Float> oddsInfoMap) {
        over_1_5 = oddsInfoMap.get("o1.5");
        under_1_5 = oddsInfoMap.get("u1.5");
        over_2_5 = oddsInfoMap.get("o2.5");
        under_2_5 = oddsInfoMap.get("u2.5");
        over_3_5 = oddsInfoMap.get("o3.5");
        under_3_5 = oddsInfoMap.get("u3.5");
        homewin = oddsInfoMap.get("1");
        draw = oddsInfoMap.get("x");
        awaywin = oddsInfoMap.get("2");
        bttsYes = oddsInfoMap.get("bttsYes");
        bttsNo = oddsInfoMap.get("bttsNo");
        homeOver_1_5 = oddsInfoMap.get("homeOver_1_5");
        homeUnder_1_5 = oddsInfoMap.get("homeUnder_1_5");
        awayOver_1_5 = oddsInfoMap.get("awayOver_1_5");
        awayUnder_1_5 = oddsInfoMap.get("awayUnder_1_5");
    }

    @Override
    public String toString() {
        String tmp = "[o1.5=" + over_1_5 + ", u1.5=" + under_1_5 + ", o2.5=" + over_2_5 + ", u2.5=" + under_2_5 +
                ", o3.5=" + over_3_5 + ", u3.5=" + under_3_5 + "]";
        if (homewin != null) {
            tmp += ",[1=" + homewin + ", x=" + draw + ", 2=" + awaywin + "]";
        }
        if (bttsYes != null) {
            tmp += ",[btts=" + bttsYes + ", no=" + bttsNo + "]";
        }
        if (over9Corners != null) {
            tmp += ",[o9,5=" + over9Corners + ", u9,5=" + under9Corners + ", o10,5=" + over10Corners + ", u10,5=" + under10Corners + "]";
        }
        if (homeOver_1_5 != null) {
            tmp += ",[home_o1.5=" + homeOver_1_5 + ", home_u1.5=" + homeUnder_1_5 + "]";
        }
        if (awayOver_1_5 != null) {
            tmp += ",[away_o1.5=" + awayOver_1_5 + ", away_u1.5=" + awayUnder_1_5 + "]";
        }
        return tmp;
    }
}
