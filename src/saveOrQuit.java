import java.util.Random;

/**
 * @author 关凯宁
 * @date 2020/5/17 16:03
 */
public class saveOrQuit implements Runnable{
    private final people p;
    private final double avg;

    public saveOrQuit(people p, double gen) {
        this.p = p;
        this.avg = gen;
    }

    @Override
    public void run() {
        Random ran=new Random(12344);
        double pro=(double) p.adaptation-avg;

    }
}
