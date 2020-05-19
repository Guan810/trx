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

    private final CountDownLatch count;
    private final Random ran;
    private final people[] tem;
    private final Genetic gen;

    public Select(CountDownLatch count, Genetic gen,Random ran) {
        this.count = count;
        this.ran = ran;
        this.gen = gen;
        this.tem=new people[2];
    }

    private people find(double a) {
        double count=0;
        for (people p:gen.getGoodPeople()){
            if(count>a){
                return p;
            }
            count+=p.adaptation;
        }
        return gen.getGoodPeople().last();
    }


    @Override
    public void run() {
        //轮盘赌取出个体
        double a=ran.nextDouble()*(double) gen.getTotalAdaptation();
        tem[0]=find(a);
        a=ran.nextDouble()*(double)gen.getTotalAdaptation();
        tem[1]=find(a);
        //交叉
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
            gen.addPeopleToPopu(newTem0);
        }else{
            gen.addPeopleToPopu(maxOne);
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
        Random ran=new Random(System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            System.out.println(ran.nextDouble());
        }
    }

}
