package threadLocal;

import jdk.internal.org.objectweb.asm.Opcodes;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 30个线程打印日期
* */
public class ThreadLocalNormalUsage01 {
    public String date(int seconds) {
        //参数的单位是毫秒
        Date date = new Date(seconds * 1000);
        //需要转换格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"
        );
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        for(int i = 0;i<30;i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLocalNormalUsage01().date(finalI);
                    System.out.println(date);
                }
            }).start();
        }
    }
}
