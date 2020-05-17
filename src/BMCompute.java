import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:39
 */
public class BMCompute implements Runnable {
    private final Genetic gen;
    private final people p;

    public BMCompute(Genetic gen,people p) {
        this.gen = gen;
        this.p=p;
    }

    public static void compute(boolean[] s, people p) {
        //最终表达式
        ArrayList<Boolean> expression = new ArrayList<>();
        expression.add(true);
        //复杂度，即表达式最高次数的值
        ArrayList<Integer> l = new ArrayList<>();
        l.add(0);

        boolean d;
        Boolean[] pre = {true};
        //用在错误情况下的第二种情况，指示相差的次数
        int num = 0;
        Boolean[] curr = {true};


        //将错误序列加入目标序列
        for (int i = 0; i < s.length; i++) {
            s[i]^=p.errorSeq[i];
        }

        // 开始循环
        for (int i = 0; i < s.length; i++) {
            d = false;
            for (int j = 0; j < expression.size(); j++) {
                d ^= expression.get(j) & s[i-expression.size()+1+j];
            }

            // compute f
            if (d) {
                if (l.get(i) == 0) {
                    while (expression.size() <= i) {
                        expression.add(false);
                    }
                    expression.add(true);
                    l.add(i + 1);
                } else {
                    curr = expression.toArray(new Boolean[1]);
                    while (expression.size() < num) {
                        expression.add(false);
                    }
                    for (int j = 0; j < pre.length; j++) {
                        if (j + num < expression.size()) {
                            expression.set(j + num, expression.get(j + num) ^ pre[j]);
                        } else {
                            expression.add(pre[j]);
                        }
                    }
                    int ll = Math.max(l.get(i), i + 1 - l.get(i));
                    l.add(ll);
                }

            } else {
                l.add(l.get(i));
            }
            if (l.get(i).equals(l.get(i + 1))) {
                num++;
            } else {
                num = 1;
                pre = curr;
            }

            //一次循环结束
        }
        boolean[] res=new boolean[expression.size()];
        for (int i = 0; i < res.length; i++) {
            res[i]=expression.get(i);
        }
        p.expression=res;
        p.complex=l.get(l.size()-1);
        p.adaptation=people.MAX_BOUND-p.complex-p.error;
    }

    public static boolean[] change(byte[] a){
        boolean[] res=new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i]=(a[i]==1);
        }
        return res;
    }

    public static void main(String[] args) {
        byte[] a={0,0,1,1,0,1};
        boolean[] b=new boolean[6];
        Arrays.fill(b,false);
        people bb=new people(b);
        compute(change(a),bb);
        System.out.println(bb.toString());
    }

    @Override
    public void run() {
        compute(gen.getTarget(),p);
        p.hasCompute=true;
        gen.addToTotalAdaptation(p.getAdaptation());
    }
}

