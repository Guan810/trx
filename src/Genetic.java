import java.util.*;
import java.util.concurrent.*;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 */
public class Genetic {
    public static final int MAX_LENGTH = 10;

    private int numOfPopu;
    private LinkedList<people> population;
    private int gen;
    private boolean[] target;
    private int totalAdaptation;
    private double avgAdaptation;
    private int maxAdaptation;
    private LinkedList<people> newPopu;



    public Genetic(int numOfPopu, LinkedList<people> population, int gen, boolean[] target) {
        this.numOfPopu = numOfPopu;
        this.population = population;
        this.gen = gen;
        this.target = target;
        this.totalAdaptation=0;
        this.maxAdaptation=0;
    }

    public Genetic(LinkedList<people> population, boolean[] target) {
        this(population.size(),population,1,target);
    }

    public void computeAdapation(ExecutorService es) throws InterruptedException {
        CountDownLatch count=new CountDownLatch(numOfPopu);
        for(people p:population){
            es.execute(new BMCompute(this,p,count));
        }
        count.await();
        avgAdaptation=(double)totalAdaptation/numOfPopu;
    }

    public void selection(ExecutorService es) throws InterruptedException {
        double[] proTable=new double[numOfPopu];
        for (int i = 0; i < numOfPopu; i++) {
            people temp=population.get(0);
            if(i==0){
                proTable[0]=(double) temp.getAdaptation()/totalAdaptation;
            }else{
                proTable[i]=proTable[i-1]+(double) temp.getAdaptation()/totalAdaptation;
            }
        }
        newPopu = new LinkedList<>();
        CountDownLatch count=new CountDownLatch(numOfPopu);
        Random ran=new Random(12345);
        for (int i = 0; i < numOfPopu; i++) {
            es.execute(new Select(proTable,count, ran));
        }
        count.await();
        population=newPopu;
        newPopu=null;

        gen++;
    }

    @Override
    public String toString(){
        StringBuilder res=new StringBuilder("第"+gen+"代：\n");
        for(people p:population){
            res.append(p.toString()).append("\n");
        }
        return res.toString();
    }

    public synchronized void addPeopleTonewPopu(people p){
        newPopu.add(p);
    }

    public void setTotalAdaptationToZero() {
        this.totalAdaptation = 0;
    }

    public synchronized void addToTotalAdaptation(int totalAdaptation) {
        this.totalAdaptation += totalAdaptation;
    }

    public int getTotalAdaptation() {
        return totalAdaptation;
    }

    public boolean[] getTarget() {
        return target;
    }

    public void setTarget(boolean[] target) {
        this.target = target;
    }

    public int getNumOfPopu() {
        return numOfPopu;
    }

    public void setNumOfPopu(int numOfPopu) {
        this.numOfPopu = numOfPopu;
    }

    public LinkedList<people> getPopulation() {
        return population;
    }

    public void setPopulation(LinkedList<people> population) {
        this.population = population;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }
    public synchronized void setMaxAdaptation(int maxAdaptation) {
        if(maxAdaptation>this.maxAdaptation){
            this.maxAdaptation = maxAdaptation;
        }
    }

    private class Select implements Runnable{
        private final static double PC1 =0.9;
        private final static double PC2 =0.6;
        private final static double PM1 =0.1;
        private final static double PM2 =0.01;

        private final double[] proTable;
        private final CountDownLatch count;
        private final Random ran;
        private final people[] tem;

        public Select(double[] proTable, CountDownLatch count, Random ran) {
            this.proTable = proTable;
            this.count = count;
            this.ran = ran;
            this.tem=new people[2];
        }

        public void invoke(int i) {
            double a=ran.nextDouble();
            tem[i]=find(a,proTable);
        }

        private people find(double a, double[] proTable) {
            int init= (int) Math.floor(a*numOfPopu);
            while (init>=0&&proTable[init]>a){
                if(init==0){
                    return population.getFirst();
                }
                init--;
            }
            while(init<numOfPopu&&proTable[init]<=a){
                if (init==numOfPopu-1){
                    return population.getLast();
                }
                init++;
            }
            return population.get(init);
        }

        @Override
        public void run() {
            this.invoke(0);
            this.invoke(1);
            addPeopleTonewPopu(cross(tem));
            count.countDown();
        }

        private people cross(people[] tem) {
            people maxOne=(tem[0].getAdaptation()>tem[1].getAdaptation())?tem[0]:tem[1];
            people res;
            if(ran.nextDouble()<selfAdapt(maxOne)){
                int startPoint=ran.nextInt(MAX_LENGTH);
                int crossLength=ran.nextInt(MAX_LENGTH-startPoint-1)+1;
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
            for (int i=0;i<MAX_LENGTH;i++) {
                if(ra.nextDouble()<mutatuPro){
                    res.errorSeq[i]=!res.errorSeq[i];
                }
            }
            return res;
        }

        private double mutatu(people b) {
            if(b.getAdaptation()<avgAdaptation){
                return PM1;
            }else{
                return PM1-((PM1-PM2)*(maxAdaptation-b.getAdaptation())/(maxAdaptation-avgAdaptation));
            }
        }

        private double selfAdapt(people maxOne) {
            if(maxOne.getAdaptation()<avgAdaptation){
                return PC1;
            }else{
                return PC1-((PC1-PC2)*(maxOne.getAdaptation()-avgAdaptation)/(maxAdaptation-avgAdaptation));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        byte[] b={0,0,1,0,0,0,1,0,0,1};
        ThreadPoolExecutor thpool=new ThreadPoolExecutor(100,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        Genetic a=new Genetic(initGen.init(10),BMCompute.change(b));
        a.computeAdapation(thpool);
        System.out.println(a.toString());
        a.selection(thpool);
        a.computeAdapation(thpool);
        System.out.println(a.toString());
    }
}
