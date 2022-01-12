package threadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* 利用threadLocal，给每个线程分配自己的dateFormat对象，保证了线程安全。
* */
public class ThreadLocalNormalUsage05 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public String date(int seconds) {
        //参数的单位是毫秒
        Date date = new Date(seconds * 1000);
        //需要转换格式
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"
//        );
        SimpleDateFormat simpleDateFormat = ThreadSafeFormatter.dateFormatThreadLocal.get();
        return simpleDateFormat.format(date);
    }



    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLocalNormalUsage05().date(finalI);
                    System.out.println(date);
                }
            });
        }
        threadPool.shutdown();
    }
}

class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = new
            ThreadLocal<SimpleDateFormat>(){
                @Override
                protected SimpleDateFormat initialValue() {

                    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                }
            };

    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal2 = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

}
