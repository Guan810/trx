import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 */
public class Genetic {

    final static double cross_pro=0.6;
    final static double mutate_pro=0.03;

    private int numOfPopu;
    private LinkedList<people> population;
    private int gen;
    private boolean[] target;
    private int totalAdaptation;
    private SortedMap<Integer, Integer> adaMap;

    public Genetic(int numOfPopu, LinkedList<people> population, int gen, boolean[] target) {
        this.numOfPopu = numOfPopu;
        this.population = population;
        this.gen = gen;
        this.target = target;
        this.totalAdaptation=0;
        this.adaMap=new TreeMap<>();
    }

    public Genetic(LinkedList<people> population, boolean[] target) {
        this(population.size(),population,1,target);
    }

    public void computeAdapation(ExecutorService es){
        for(people p:population){
            if(!p.hasCompute){
                es.execute(new BMCompute(this,p));
            }
        }
    }

    public void selection(){
        for (int i = 0; i < numOfPopu; i++) {
            
        }
    }

    public synchronized void insertToMap(int key,int value){
        if(adaMap.containsKey(key)){
            adaMap.put(key,adaMap.get(key)+1);
        }else{
            adaMap.put(key,1);
        }
    }
    public synchronized void setTotalAdaptationToZero() {
        this.totalAdaptation = 0;
    }

    public void addToTotalAdaptation(int totalAdaptation) {
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
}
