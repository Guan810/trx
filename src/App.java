public class App {
  public static void main(String[] args) {
    int[][] array1 = new int[10][4];
    create(array1);
    for(int i=0;i<10;i++){
      for(int j=0;j<4;j++) {
        System.out.print(array1[i][j]);
      }
      System.out.print("\n");
    }
  }
  public static void create(int[][] array){
    int i,j;
    for(i=0;i<3;i++){
      for(j=0;j<4;j++){
        array[i][j]=(int)(Math.random()*2);
      }
      while(equals(array, i)){// 重新生成
        for (j = 0; j < 4; j++) {
          array[i][j] = (int) (Math.random() * 2);
        }
      }
    }
  }

  public static boolean equals(int[][] array, int k) {
    int i,j;
    int bool;
    if(k==0) {
      return false;
    }
    for(i=0;i<k;i++){
      bool=1;
      for (j=0;j<4;j++){
        if(array[i][j]!=array[k][j]){
          bool= 0;
          break;
        }
      }
      if (bool==1){//i和k完全相等
        System.out.print(1);
        return true;
      }
    }
    return false;
  }
}
