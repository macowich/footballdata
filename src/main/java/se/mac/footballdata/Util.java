package se.mac.footballdata;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util
{
   public static double round(double d, int decimalPlace)
   {
      BigDecimal bd = new BigDecimal(Double.toString(d));
      bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }
}
