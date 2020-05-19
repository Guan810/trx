import java.util.concurrent.*;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 * 主测试函数
 */
public class Main {

    public static void run(boolean[] target) throws InterruptedException {
        ThreadPoolExecutor thpool=new ThreadPoolExecutor(100,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        Genetic gen=new Genetic(initGen.init(10000,target.length),target, 10);
        gen.computeAdapation(thpool);
        System.out.println(gen.toSimplyString());
        while(gen.getGen()<300){
            gen.selection(thpool);
            gen.mutatu(thpool);
            gen.computeAdapation(thpool);
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
