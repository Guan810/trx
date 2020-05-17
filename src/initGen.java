import java.util.LinkedList;
import java.util.Random;

/**
 * @author 关凯宁
 * @date 2020/5/17 14:29
 */
public class initGen {

    static LinkedList<people> init(int n){
        LinkedList<people> res=new LinkedList<>();
        for (int i = 0; i < n; i++) {
            res.add(randomPeople());
        }
        return res;
    }

    private static people randomPeople() {
        Random ran=new Random(12345);
        boolean[] res=new boolean[1000];
        for (int i = 0; i < 1000; i++) {
            res[i]=(ran.nextInt(2)==1);
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
