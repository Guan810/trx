import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

/**
 * @author 关凯宁
 * @date 2020/5/17 14:29
 * 用来生成随机种群，返回值是一个LinkedList类
 */
public class initGen {

    static LinkedList<people> init(int n,int len){
        Random ran=new Random(System.currentTimeMillis());
        LinkedList<people> res=new LinkedList<>();
        for (int i = 0; i < n; i++) {
            res.add(randomPeople(len,ran));
        }
        return res;
    }

    private static people randomPeople(int len,Random ra) {
        Random ran=new Random(System.currentTimeMillis()<<ra.nextInt(31));
        boolean[] res=new boolean[len];
        for (int i = 0; i < len; i++) {
            res[i]=ran.nextBoolean();
        }
        return new people(res);
    }

}
