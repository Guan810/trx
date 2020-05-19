import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 *
 * 个体类
 */
public class people implements Comparable<people> , Cloneable{

    final static int MAX_BOUND=1513;

    /**表达式按照次数从小到大排列*/
    boolean[] errorSeq;
    /**通过BM算法计算出的表达式*/
    boolean[] expression;
    /**通过BM算法计算出的表达式的复杂度*/
    int complex;
    /**当前表达式的错误个数，需要通过AdaptationTest中相关方法得出*/
    int error;
    /**指示是否经过了BM算法计算，创建初是false*/
    boolean hasCompute;
    int adaptation;

    public people(boolean[] errorSeq) {
        this.errorSeq=errorSeq;
        int count=0;
        for(boolean i:errorSeq){
            if(i){
                count++;
            }
        }
        this.error=count;
        this.hasCompute=false;
    }

    @Override
    public String toString(){
        StringBuilder res;
        if(hasCompute){
            res = new StringBuilder("Complex = " + complex + "\t Error = " + error + "\t Adaptation = "+adaptation);
        }else{
            res = new StringBuilder("Complex = " + complex + "\t");
        }
        res.append("\t errorSeq: ");
        for (boolean i:this.errorSeq){
            if(i){
                res.append(1).append(" ");
            }else{
                res.append(0).append(" ");
            }
        }
        if(hasCompute){
            res.append("\t expression: ");
            for(boolean i:this.expression){
                if(i){
                    res.append(1).append(" ");
                }else{
                    res.append(0).append(" ");
                }
            }
        }else{
            res.append("\tThis people has not been computed!");
        }

        return res.toString();
    }

    public String toSimplyString(){
        StringBuilder res;
        if(hasCompute){
            res = new StringBuilder("Complex = " + complex + "\t Error = " + error + "\t Adaptation = "+adaptation);
        }else{
            res = new StringBuilder("Complex = " + complex + "\t");
        }
        return res.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        people people = (people) o;
        return adaptation == people.adaptation &&
                Arrays.equals(errorSeq, people.errorSeq);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(adaptation);
        result = 31 * result + Arrays.hashCode(errorSeq);
        return result;
    }

    public int getAdaptation(){
        assert hasCompute;
        return adaptation;
    }

    @Override
    public int compareTo(@NotNull people o) {
        int res=o.adaptation-this.adaptation;
        int num= (res==0)?o.error-this.error:res;
        return (num==0)?1:num;
    }

    public static void main(String[] args) {
        boolean[] o={};
        boolean[] a={true};
        boolean[] b={true,true};
        boolean[] c={true,true,true};
        people a1=new people(o);
        people a2=new people(b);
        people a3=new people(a);
        people a4=new people(o);
        a1.adaptation=1508;
        a2.adaptation=1505;
        a3.adaptation=1507;
        a4.adaptation=1508;
        a1.hasCompute=true;
        a2.hasCompute=true;
        a3.hasCompute=true;
        a4.hasCompute=true;
        TreeSet<people> tree=new TreeSet<>();
        tree.add(a3);
        tree.add(a1);
        tree.add(a4);
        tree.add(a2);
        for(people p: tree){
            System.out.println(p.toSimplyString());
        }
    }

}
