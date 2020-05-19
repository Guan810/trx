import java.util.Collection;
import java.util.TreeSet;

/**
 * @author 关凯宁
 * @date 2020/5/18 16:50
 * 自定义的列表，主要实现功能是不超过固定大小
 */
public class GoodPeople extends TreeSet<people> {
    public final int size;

    public GoodPeople(int size) {
        super();
        this.size = size;
    }

    @Override
    public synchronized boolean add(people people) {
            boolean res=false;
            if(size()<size){
                res = super.add(people);
            }else if(people.adaptation>this.last().adaptation){
                res = super.add(people);
                if(this.size()>size){
                    this.pollLast();
                }
            }
            return res;
    }

    @Override
    public boolean addAll(Collection<? extends people> c) {
        boolean res= super.addAll(c);
        while(size()>=size){
            pollLast();
        }
        return res;
    }
}
