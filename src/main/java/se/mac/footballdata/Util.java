package se.mac.footballdata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util
{
   public static double round(double d, int decimalPlace)
   {
      BigDecimal bd = new BigDecimal(Double.toString(d));
      bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static String getLeagueName(String code) {
      return switch (code) {
         case "B1" -> "Belgium First Division";
         case "E0" -> "Premier League";
         case "SP1" -> "La Liga";
         case "SP2" -> "Secunda divison";
         case "D1" -> "Bundesliga";
         case "I1" -> "Serie A";
         case "F1" -> "Ligue 1";
         default -> code;
      };
   }

   public static String toIso(String dateStr) {
      DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      DateTimeFormatter isoFormat = DateTimeFormatter.ISO_LOCAL_DATE;

      LocalDate date = LocalDate.parse(dateStr, inputFormat);
      return date.format(isoFormat);
   }
}
