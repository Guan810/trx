import java.util.concurrent.*;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 */
public class Main {

    public static void run(boolean[] target) throws InterruptedException {
        ThreadPoolExecutor thpool=new ThreadPoolExecutor(100,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>());
        Genetic gen=new Genetic(initGen.init(100,target.length),target, 20);
        gen.computeAdapation(thpool);
        System.out.println(gen.toSimplyString());
//        long begin,end;
        while(gen.getGen()<30){
//            begin=System.nanoTime();
            gen.selection(thpool);
//            end=System.nanoTime();
//            System.out.println(end-begin);
//            begin=System.nanoTime();
            gen.computeAdapation(thpool);
//            end=System.nanoTime();
//            System.out.println(end-begin);
            System.out.println(gen.toSimplyString());
        }
    }

    public static void main(String[] args) {
        byte[] a={0,0,0,0,1,1,1,0,1,0};
        try {
            run(BMCompute.change(a));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
