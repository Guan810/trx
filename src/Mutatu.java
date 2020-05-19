import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author 关凯宁
 * @date 2020/5/19 8:39
 *
 * 变异线程
 */
public class Mutatu implements Runnable{
    private final static double PM1 =0.2;
    private final static double PM2 =0.02;

    private final Genetic gen;
    private final people p;
    private final CountDownLatch count;
    private final Random ran;

    public Mutatu(Genetic gen, people p, CountDownLatch count) {
        this.gen = gen;
        this.p = p;
        this.count = count;
        this.ran=new Random(System.currentTimeMillis());
    }

    private double mutatu(people b) {
        if(b.getAdaptation()<gen.getAvgAdaptation()){
            return PM1;
        }else{
            return PM1-((PM1-PM2)*(gen.getBestPeople().getAdaptation()-b.getAdaptation())/(gen.getBestPeople().getAdaptation()-gen.getAvgAdaptation()));
        }
    }

    @Override
    public void run() {
        double pro=mutatu(p);
        for (int i = 0; i < gen.PEOPLE_LENGTH; i++) {
            if(ran.nextDouble()<pro){
                p.errorSeq[i]=!p.errorSeq[i];
                p.hasCompute=false;
            }
        }
        count.countDown();
    }
}
