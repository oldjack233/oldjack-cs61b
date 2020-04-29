


class hw0 {
    public static int max(int[] array) {
        int m = array[0];
        for (int i = 0; i < array.length; i += 1) {
            if (array[i] > m)
                m = array[i];

        }
        return m;
    }

    public static boolean threeSum(int[] array) {
        if (array.length < 3) return false;
        for (int i = 0; i < array.length; i += 1) {
            for (int j = 0; j < array.length; j += 1) {
                for (int k = 0; k < array.length; k += 1) {
                    if (array[i] + array[j] + array[k] == 0)
                        return true;

                }
            }
        }
        return false;
    }

    public static boolean threeSumDistinct(int[] array) {
        if (array.length < 3)
            return false;
        for( int i=0; i<array.length; i+=1) {
            for( int j=i+1; j<array.length; j+=1) {
                for( int k=j+1; k<array.length; k+=1) {
                    System.out.println(array[i]);
                    System.out.println(array[j]);
                    System.out.println(array[k]);
                    if (array[i]+array[j]+array[k] == 0)
                        return true;
                }
            }
        }
            return false; }



    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3, 4};
        int[] b = new int[]{-1, -2, 3, 4};
        int[] c = new int[]{-1, -2, 3};
        System.out.println(threeSumDistinct(a));
        System.out.println(threeSumDistinct(b));
        System.out.println(threeSumDistinct(c));
    }
}