/**
 * @author 关凯宁
 * @date 2020/5/17 10:12
 */
public class people {
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
            res = new StringBuilder("Complex = " + complex + "\tError = " + error + "\t");
        }else{
            res = new StringBuilder("Complex = " + complex + "\t");
        }
        res.append("errorSeq: \t");
        for (boolean i:this.errorSeq){
            if(i){
                res.append(1).append(" ");
            }else{
                res.append(0).append(" ");
            }
        }
        if(hasCompute){
            res.append("\texpression: \t");
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

        return res.append("\n").toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getAdaptation(){
        assert hasCompute;
        return adaptation;
    }

}
