import java.util.TreeSet;

/**
 * @author 关凯宁
 * @date 2020/5/18 16:50
 */
public class GoodPeople extends TreeSet<people> {
    public final int size;

    public GoodPeople(int size) {
        super();
        this.size = size;
    }

    @Override
    public synchronized boolean add(people people) {
        boolean res= super.add(people);
        if(this.size()>size){
            this.pollLast();
            res=false;
        }
        return res;
    }

}
