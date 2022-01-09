package threadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 两个线程打印日期
* */
public class ThreadLocalNormalUsage00 {
    public String date(int seconds) {
        //参数的单位是毫秒
        Date date = new Date(seconds * 1000);
        //需要转换格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"
        );
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String date = new ThreadLocalNormalUsage00().date(10);
                System.out.println(date);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String date = new ThreadLocalNormalUsage00().date(104707);
                System.out.println(date);
            }
        }).start();
    }
}
