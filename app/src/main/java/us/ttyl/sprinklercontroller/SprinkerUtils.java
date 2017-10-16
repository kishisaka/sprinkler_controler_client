package us.ttyl.sprinklercontroller;

/**
 * Created by test on 12/18/2016.
 */

public class SprinkerUtils {
    public static String getDayOfWeekNumber(String day) {
        switch (day) {
            case "Sun":
                return "6";
            case "Mon":
                return "0";
            case "Tue":
                return "1";
            case "Wed":
                return "2";
            case "Thr":
                return "3";
            case "Fri":
                return "4";
            case "Sat":
                return "5";
            default:
                return "-1";
        }
    }

    public static  String getDayFullNameFromDayNumber(String day) {
        switch (day) {
            case "6":
                return "Sun";
            case "0":
                return "Mon";
            case "1":
                return "Tue";
            case "2":
                return "Wed";
            case "3":
                return "Thr";
            case "4":
                return "Fri";
            case "5":
                return "Sat";
            default:
                return "-1";
        }
    }
}
