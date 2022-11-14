import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        String s = "{cost=17.50, costType=食, currentDate=2022-01-06, costItem=午饭}";
//        List<String> myList = new ArrayList<String>(Arrays.asList(s.replace("{", "").replace("}", "").split(",")));
//        for (String s1 : myList) {
//            System.out.println(s1.split("=")[1]);
//        }

        Calendar instance = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(format.format(instance.getTime()));

        // s = {cost=445.79, costType=娱乐, currentDate=2022-10-25, costItem=14pro手机还款}
        String records = "{cost=445.79, costType=娱乐, currentDate=2022-10-25, costItem=14pro手机还款}".replace("{", "").replace("}", "");
        String cost = records.split(",")[0].replace("cost=", "").replace(" ", "");
        String costType = records.split(",")[1].replace("costType=", "").replace(" ", "");
        String currentDate = records.split(",")[2].replace("currentDate=", "").replace(" ", "");
        String costItem = records.split(",")[3].replace("costItem=", "").replace(" ", "");
        System.out.println(String.format("%5s\t%10s\t%10s\t%10s", "cost", "costType", "currentDate", "costItem"));
        System.out.println(String.format("%5s\t%10s\t%10s\t%10s", cost, costType, currentDate, costItem));

        System.out.println(records.replace("cost=", "").replace("costType=", "").replace("currentDate=", "").replace("costItem=", ""));
    }
}
