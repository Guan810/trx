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
    /**目标序列*/
    private final boolean[] target;
    /**
     * 添加一个TreeSet来维护适应度较大的个体，同时便于输出
     */
    private TreeSet<people> goodPeople;
    /**
     * 当前种群的所有个体集合
     */
    private LinkedList<people> population;
    /**
     * 当前种群的代数
     */
    private int gen;
    private final int showNum;
    private int totalAdaptation;
    private double avgAdaptation;
    private people bestPeople;

    public Genetic(int peopleLength,  LinkedList<people> population, int gen, boolean[] target, int showNum){
        PEOPLE_LENGTH = peopleLength;
        this.population = population;
        this.gen = gen;
        this.showNum=showNum;
        this.target = target;
        this.totalAdaptation=0;
        this.bestPeople=null;
        goodPeople= new TreeSet<>();
        boolean[] standran=new boolean[PEOPLE_LENGTH];
        Arrays.fill(standran,false);
        bestPeople=new people(standran);
        CountDownLatch count=new CountDownLatch(1);
        new Thread(new BMCompute(this,bestPeople,count)).start();
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Genetic(LinkedList<people> population, boolean[] target, int showNum) {
        this(target.length, population,1,target, showNum);
    }


    /**
     * 一个种群整体进行BM算法的方法，采用多线程
     */
    public void computeAdapation(ExecutorService es) throws InterruptedException {
        goodPeople.clear();
        setTotalAdaptationToZero();
        CountDownLatch count=new CountDownLatch(population.size());
        for(people p:population){
            es.execute(new BMCompute(this,p,count));
        }
        count.await();
        goodPeople.addAll(population);
        avgAdaptation=(double)totalAdaptation/population.size();
    }

    /**
     * 选择与交叉函数
     */
    public void selection(ExecutorService es) throws InterruptedException {
        population.clear();
        CountDownLatch count=new CountDownLatch(goodPeople.size());
        Random ran=new Random(System.currentTimeMillis());
        for (int i = 0; i < goodPeople.size(); i++) {
            es.execute(new Select(count, this,ran));
        }
        count.await();
//        for (people p:goodPeople){
//            population.add(p);
//            if(population.size()==goodPeople.size()){
//                break;
//            }
//        }
    }

    public void mutatu(ExecutorService es) throws InterruptedException {
        CountDownLatch count=new CountDownLatch(population.size());
        for (people i:population) {
            es.execute(new Mutatu(this,i,count));
        }
        count.await();
        gen++;
    }

    @Override
    public String toString(){
        StringBuilder res=new StringBuilder("第"+gen+"代：  共有"+goodPeople.size()+"\n");
        res.append("最好的一个：\n");
        if(this.bestPeople==null){
            res.append("Threre is no best now. This gen has just been built!\n");
        }else{
            res.append(this.bestPeople.toString()).append("\n");
        }
        res.append("全部: \n");
        for(people p:goodPeople){
            res.append(p.toString()).append("\n");
        }
        return res.toString();
    }

    public String toSimplyString(){
        StringBuilder res=new StringBuilder("第"+gen+"代：  共有"+goodPeople.size()+"  总适应度为"+totalAdaptation+"\n");
        res.append("最好的一个：\n");
        if(this.bestPeople==null){
            res.append("Threre is no best now. This gen has just been built!\n");
        }else{
            res.append(this.bestPeople.toString()).append("\n");
        }
        res.append("good people: \n");
        int i=0;
        for(people p:goodPeople){
            res.append(p.toString()).append("\n");
            if(i++>=showNum){
                break;
            }
        }
        return res.toString();
    }

    public TreeSet<people> getGoodPeople() {
        return goodPeople;
    }

    public void setTotalAdaptationToZero() {
        this.totalAdaptation = 0;
    }

    public synchronized void addPeopleToPopu(people p){
        this.population.add(p);
    }

    public synchronized void addToTotalAdaptation(int totalAdaptation) {
        this.totalAdaptation += totalAdaptation;
    }

    public int getTotalAdaptation() {
        return totalAdaptation;
    }

    public void setTotalAdaptation(int totalAdaptation) {
        this.totalAdaptation = totalAdaptation;
    }

    public boolean[] getTarget() {
        return target;
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

    public synchronized void setBestPeople(people bestPeople){
        if(this.bestPeople==null||bestPeople.getAdaptation()>this.bestPeople.getAdaptation()){
            try {
                this.bestPeople = (people) bestPeople.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public TreeSet<people> getTreeSet() {
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
