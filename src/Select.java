import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author 关凯宁
 * @date 2020/5/18 11:18
 * 在选择时采取的多线程线程类
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

    private people find(double a, double[] proTable) {
        int init= (int) Math.floor(a*gen.getPopulation().size());
        while (init>=0&&proTable[init]>a){
            if(init==0){
                return gen.getPopulation().getFirst();
            }
            init--;
        }
        while(init<gen.getPopulation().size()&&proTable[init]<=a){
            if (init==gen.getPopulation().size()-1){
                return gen.getPopulation().getLast();
            }
            init++;
        }
        return gen.getPopulation().get(init);
    }


    @Override
    public void run() {
        double a=ran.nextDouble();
        tem[0]=find(a,proTable);
        a=ran.nextDouble();
        tem[1]=find(a,proTable);
        try {
            cross(tem);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        count.countDown();
    }

    private void cross(people[] tem) throws CloneNotSupportedException {
        people maxOne=(tem[0].getAdaptation()>tem[1].getAdaptation())?tem[0]:tem[1];
        if(ran.nextDouble()<selfAdapt(maxOne)){
            int startPoint=ran.nextInt(gen.PEOPLE_LENGTH);
            int crossLength=ran.nextInt(gen.PEOPLE_LENGTH-startPoint+1);
            people newTem0= (people) tem[0].clone();
            System.arraycopy(tem[1].errorSeq, startPoint, newTem0.errorSeq, startPoint, crossLength);
            newTem0.hasCompute=false;
            gen.addPeopleTonewPopu(newTem0);
        }else{
            gen.addPeopleTonewPopu(maxOne);
        }

    }

    private double mutatu(people b) {
        if(b.getAdaptation()<gen.getAvgAdaptation()){
            return PM1;
        }else{
            return PM1-((PM1-PM2)*(gen.getBestPeople().getAdaptation()-b.getAdaptation())/(gen.getBestPeople().getAdaptation()-gen.getAvgAdaptation()));
        }
    }

    private double selfAdapt(people maxOne) {
        if(maxOne.getAdaptation()<gen.getAvgAdaptation()){
            return PC1;
        }else{
            return PC1-((PC1-PC2)*(maxOne.getAdaptation()-gen.getAvgAdaptation())/(gen.getBestPeople().getAdaptation()-gen.getAvgAdaptation()));
        }
    }

    public static void main(String[] args) {

    }
}
