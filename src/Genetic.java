import java.util.*;
import java.util.concurrent.*;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 *
 * 该Genetic类是种群类，本算法中的一切行为均是基于该类进行。
 */
public class Genetic {
    /**
     * 种群中people的编号长度，即errorSeq的长度
     */
    public final int PEOPLE_LENGTH;
    private LinkedList<people> population;
    private int gen;
    private boolean[] target;
    private int totalAdaptation;
    private double avgAdaptation;
    private people bestPeople;
    private LinkedList<people> newPopu;

    /**
     * 添加一个TreeSet来维护适应度较大的个体，同时便于输出
     */
    private final GoodPeople goodPeople;

    public Genetic(int peopleLength,  LinkedList<people> population, int gen, boolean[] target, int goodPeopleLast) {
        PEOPLE_LENGTH = peopleLength;
        this.population = population;
        this.gen = gen;
        this.target = target;
        this.totalAdaptation=0;
        this.bestPeople=null;
        goodPeople=new GoodPeople(goodPeopleLast);
    }

    public Genetic(LinkedList<people> population, boolean[] target, int goodPeopleLast) {
        this(target.length, population,1,target, goodPeopleLast);
    }

    public void computeAdapation(ExecutorService es) throws InterruptedException {
        CountDownLatch count=new CountDownLatch(population.size());
        for(people p:population){
            es.execute(new BMCompute(this,p,count));
        }
        count.await();
        avgAdaptation=(double)totalAdaptation/population.size();
    }

    public void selection(ExecutorService es) throws InterruptedException {
        setTotalAdaptationToZero();

        double[] proTable=new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            people temp=population.get(0);
            if(i==0){
                proTable[0]=(double) temp.getAdaptation()/totalAdaptation;
            }else{
                proTable[i]=proTable[i-1]+(double) temp.getAdaptation()/totalAdaptation;
            }
        }
        newPopu = new LinkedList<>();
        CountDownLatch count=new CountDownLatch(population.size()-goodPeople.size());
        Random ran=new Random(System.currentTimeMillis());
        for (int i = 0; i < population.size()-goodPeople.size(); i++) {
            es.execute(new Select(proTable,count, ran, this));
        }
        count.await();
        population=newPopu;
        newPopu=null;
        population.addAll(goodPeople);
        gen++;
    }

    @Override
    public String toString(){
        StringBuilder res=new StringBuilder("第"+gen+"代：\n");
        res.append("全部People: \n");
        for(people p:population){
            res.append(p.toString()).append("\n");
        }
        res.append("good people: \n");
        for(people p:goodPeople){
            res.append(p.toString()).append("\n");
        }
        return res.toString();
    }

    public String toSimplyString(){
        StringBuilder res=new StringBuilder("第"+gen+"代：\n");
        res.append("good people: \n");
        for(people p:goodPeople){
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

    public void setTotalAdaptation(int totalAdaptation) {
        this.totalAdaptation = totalAdaptation;
    }

    public double getAvgAdaptation() {
        return avgAdaptation;
    }

    public void setAvgAdaptation(double avgAdaptation) {
        this.avgAdaptation = avgAdaptation;
    }

    public people getBestPeople() {
        return bestPeople;
    }

    public LinkedList<people> getNewPopu() {
        return newPopu;
    }

    public void setNewPopu(LinkedList<people> newPopu) {
        this.newPopu = newPopu;
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
    public synchronized void setMaxAdaptation(people bestPeople) {
        if(bestPeople.getAdaptation()>this.bestPeople.getAdaptation()){
            this.bestPeople = bestPeople;
        }
    }

    public GoodPeople getGoodPeople() {
        return goodPeople;
    }

    public static void main(String[] args) throws InterruptedException {
        byte[] b={0,0,1,0,0,0,1,0,0,1};
        ThreadPoolExecutor thpool=new ThreadPoolExecutor(100,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>());
        Genetic a=new Genetic(initGen.init(100,b.length),BMCompute.change(b), 2);
        a.computeAdapation(thpool);
        System.out.println(a.toString());
        for (int i = 0; i < 20; i++) {
            a.selection(thpool);
            a.computeAdapation(thpool);
            System.out.println(a.toString());
        }
    }
}
