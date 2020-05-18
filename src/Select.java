import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author 关凯宁
 * @date 2020/5/18 11:18
 */
public class Select implements Runnable{
    private final static double PC1 =0.9;
    private final static double PC2 =0.6;
    private final static double PM1 =0.1;
    private final static double PM2 =0.01;

    private final double[] proTable;
    private final CountDownLatch count;
    private final Random ran;
    private final people[] tem;
    private final Genetic gen;

    public Select(double[] proTable, CountDownLatch count, Random ran, Genetic gen) {
        this.proTable = proTable;
        this.count = count;
        this.ran = ran;
        this.gen = gen;
        this.tem=new people[2];
    }

    public void invoke() {
        double a=ran.nextDouble();
        tem[0]=find(a,proTable);
        a=ran.nextDouble();
        tem[1]=find(a,proTable);
    }

    private people find(double a, double[] proTable) {
        int init= (int) Math.floor(a*gen.getNumOfPopu());
        while (init>=0&&proTable[init]>a){
            if(init==0){
                return gen.getPopulation().getFirst();
            }
            init--;
        }
        while(init<gen.getNumOfPopu()&&proTable[init]<=a){
            if (init==gen.getNumOfPopu()-1){
                return gen.getPopulation().getLast();
            }
            init++;
        }
        return gen.getPopulation().get(init);
    }

    @Override
    public void run() {
        this.invoke();
        gen.addPeopleTonewPopu(cross(tem));
        count.countDown();
    }

    private people cross(people[] tem) {
        people maxOne=(tem[0].getAdaptation()>tem[1].getAdaptation())?tem[0]:tem[1];
        people res;
        if(ran.nextDouble()<selfAdapt(maxOne)){
            int startPoint=ran.nextInt(gen.PEOPLE_LENGTH);
            int crossLength=ran.nextInt(gen.PEOPLE_LENGTH-startPoint+1);
            boolean[] newErrorSeq = tem[0].errorSeq.clone();
            if (crossLength - startPoint > 0) {
                System.arraycopy(tem[1].errorSeq, startPoint, newErrorSeq, startPoint, crossLength - startPoint);
            }
            res=new people(newErrorSeq);
        }else{
            res=maxOne;
        }
        double mutatuPro=mutatu(res);
        Random ra=new Random(ran.nextLong());
        for (int i = 0; i< gen.PEOPLE_LENGTH; i++) {
            if(ra.nextDouble()<mutatuPro){
                res.errorSeq[i]=!res.errorSeq[i];
            }
        }
        return res;
    }

    private double mutatu(people b) {
        if(b.getAdaptation()<gen.getAvgAdaptation()){
            return PM1;
        }else{
            return PM1-((PM1-PM2)*(gen.getMaxAdaptation()-b.getAdaptation())/(gen.getMaxAdaptation()-gen.getAvgAdaptation()));
        }
    }

    private double selfAdapt(people maxOne) {
        if(maxOne.getAdaptation()<gen.getAvgAdaptation()){
            return PC1;
        }else{
            return PC1-((PC1-PC2)*(maxOne.getAdaptation()-gen.getAvgAdaptation())/(gen.getMaxAdaptation()-gen.getAvgAdaptation()));
        }
    }

    public static void main(String[] args) {
        Random ran=new Random();
        int i=0;
        while(i<10){
            i++;
            System.out.println(ran.nextInt(2));
        }
    }
}
