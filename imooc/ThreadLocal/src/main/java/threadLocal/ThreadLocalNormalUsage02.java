package threadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* 1000个打印日期的任务，用线程池来执行
* */
public class ThreadLocalNormalUsage02 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public String date(int seconds) {
        //参数的单位是毫秒
        Date date = new Date(seconds * 1000);
        //需要转换格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"
        );
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLocalNormalUsage02().date(finalI);
                    System.out.println(date);
                }
            });
        }
        threadPool.shutdown();
    }
}
