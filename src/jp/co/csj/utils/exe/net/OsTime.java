package jp.co.csj.utils.exe.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class OsTime {

    public static void main(String[] args) throws MalformedURLException, IOException, ParseException,
            InterruptedException {

        System.out.println("开始访问百度服务器：http://www.baidu.com");
        URLConnection conn = new URL("http://www.baidu.com").openConnection();
        String dateStr = conn.getHeaderField("Date");
        System.out.println("获取到的服务器时间：" + dateStr);

        // 解析为北京时间：GMT+8
        DateFormat httpDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = httpDateFormat.parse(dateStr);
        System.out.println("解析成北京时间格式：" + date);

        // 解析成简洁的日期格式
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println("解析成标准时间格式：" + dateTimeFormat.format(date));

        // 取日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String currDate = dateFormat.format(date);

        // 设置Windows系统日期
        Process exec = Runtime.getRuntime().exec("cmd /c date " + currDate);
        if (exec.waitFor() == 0) {
            System.out.println("设置系统日期成功：" + currDate);
        } else {
            System.out.println("设置系统日期失败：" + currDate);
        }

        // 取时间
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String currTime = timeFormat.format(date);

        // 设置Windows系统时间
        exec = Runtime.getRuntime().exec("cmd /c time " + currTime);
        if (exec.waitFor() == 0) {
            System.out.println("设置系统时间成功：" + currTime);
        } else {
            System.out.println("设置系统时间失败：" + currTime);
        }

    }
}
