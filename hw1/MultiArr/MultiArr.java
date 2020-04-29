import java.util.Arrays;

/** Multidimensional array
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        //TODO: Your code here!
        int rowlen = arr.length;
        int[] collen = new int[1];
        for(int i = 0; i<arr.length;i++){
            if(collen[0] < arr[i].length){
                collen[0] = arr[i].length;
            }


        }
        System.out.println("Rows:"+ rowlen);
        System.out.println("Columns:" + collen[0]);


    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        //TODO: Your code here!
        int[] arrr_ele = new int[1];
        for(int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[i].length; j++){
                if (arrr_ele[0] < arr[i][j]){
                    arrr_ele[0] = arr[i][j];
                }
            }
        }
        return arrr_ele[0];
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        //TODO: Your code here!!
        int[] result = new int[arr.length];
        for(int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[i].length; j ++){
                result[i] += arr[i][j];
            }
        }
        return result;
    }
    public static void main(String... args) {
        int[][] test = new int[][]{{1,3,4},{1},{5,6,7,8},{7,9}};
        MultiArr.printRowAndCol(test);
    }

}