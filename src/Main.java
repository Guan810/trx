import java.util.concurrent.*;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 */
public class Main {

    public static void run(){
        ThreadPoolExecutor thpool=new ThreadPoolExecutor(100,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        boolean[] target=new boolean[1000];
        Genetic gen=new Genetic(initGen.init(1<<10),target);
        gen.computeAdapation(thpool);
    }

    public static void main(String[] args) {
        long a=(long)2*Integer.MAX_VALUE;
        System.out.println(a);
    }
}
