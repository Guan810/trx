import java.util.LinkedList;
import java.util.Random;

/**
 * @author 关凯宁
 * @date 2020/5/17 14:29
 * 用来生成随机种群，返回值是一个LinkedList类
 */
public class initGen {

    static LinkedList<people> init(int n,int len){
        LinkedList<people> res=new LinkedList<>();
        Random ran=new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            res.add(randomPeople(ran,len));
        }
        return res;
    }

    private static people randomPeople(Random ran,int len) {
        boolean[] res=new boolean[len];
        for (int i = 0; i < len; i++) {
            res[i]=ran.nextBoolean();
        }
        return new people(res);
    }

    public static void main(String[] args) {
        Random a=new Random(12345);
        for (int i = 0; i < 50; i++) {
            System.out.println(a.nextInt(2));
        }

    }
}
