public class Interview {

    public static void main(String[] args){
        int[] arr = {1, 5, 10, 20, 50};
        int m = arr.length;
        int n = 0;

        System.out.println("使用动态规划求解硬币找零方案数为：" + coinChange(arr, m, n));
    }


    public static int coinChange(int[] s, int m, int n){
        int res1, res2;
        int[][] tc = new int[n+1][m];

        for(int i = 0; i < m; i++){
            tc[0][i] = 1;
        }
        for(int i = 1; i < n+1; i++){
            for(int j = 0; j < m; j++){
                res1 =  (i-s[j] >= 0)? tc[i - s[j]][j]: 0;

                res2 = (j >= 1)?tc[i][j-1]:0;
                tc[i][j] = res1 + res2;
            }
        }
        return tc[n][m-1];
    }

}
