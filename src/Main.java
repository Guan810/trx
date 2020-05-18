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
        Genetic gen=new Genetic(initGen.init(10,target.length),target);
        gen.computeAdapation(thpool);
        System.out.println(gen.toString());
        while(gen.getGen()<10){
            gen.selection(thpool);
            gen.computeAdapation(thpool);
            System.out.println(gen.toString());
        }
    }

    public static void main(String[] args) {
        byte[] a={1,0,0,0,1,1,1,0,1,0};
        try {
            run(BMCompute.change(a));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
