
package memoryallocation;

import java.util.Random;

/**
 * @author HaiAu Bui
 * CSD 415, Professor Abbott
 * For First-Fit, I will implement the algorithm that search the entire array to find a hole
 * which is large enough for the block, the loop will request(s) and release(i)
 * then keep continuing if the condition is satisfy
 */
public class Best_Fit {
    static Random rand = new Random();
    final static int SIZE = 32;
    private static double curentMMU = 0;
    private static double accumAvgMMU = 0;
    private static double examHoleCount = 0; 
    private static double accumHoleCount = 0; 
    private static int occupiedSlot = 0;
    
    public static void main(String[] args) {   
        MMU mmu = new MMU(SIZE);
        mmu.initMMU();
        int index = 0;

            while( true ){

                //THIS IS FOR REQUEST(S)
                /**for best-fit algorithm, search the entire list to find the index that has 
                 * a has the smallest hole large enough => abs.value = randomRequestSize
                 */     
                
                int randomRequestSize;
                while( mmu.occupiedSlot < SIZE ){
                    //System.out.println("Occupied slot = " + mmu.occupiedSlot);
                    randomRequestSize =  normalDistribution();
                    //FIND MIN of whole array which MIN >= randomRequestSize
                    if((SIZE-mmu.occupiedSlot) >= randomRequestSize){
                        index = findTheSmallestHole( mmu.memoryArray, randomRequestSize);
                        mmu.request(index, randomRequestSize);
                        System.out.println("\n" + MMU.ANSI_GREEN + "Request block size of (" + randomRequestSize + ")" + MMU.ANSI_RESET);
                        mmu.printBlockList();                    
                        mmu.printCurrent();
                        System.out.println("");
                        curentMMU = mmu.getAvgMMU();
                        accumAvgMMU += curentMMU;
                        examHoleCount++;
                        occupiedSlot += randomRequestSize;
                    }   
                    
                    if ( (SIZE-occupiedSlot) == 0 ){
                        System.out.println(MMU.ANSI_CYAN + "Memory is full, start release randomly..." + MMU.ANSI_RESET);
                        break;
                    }
                    
                    System.out.println(MMU.ANSI_CYAN + "\nFree slot = " + (SIZE-occupiedSlot)
                            + ", block requested, size = " + randomRequestSize + MMU.ANSI_RESET );
                    System.out.println("!!! No hole with that size found !!!");
                    System.out.println(MMU.ANSI_CYAN  + "Continue searching...." + MMU.ANSI_RESET );  
                    accumHoleCount++;
                
                }   
                
                //Search times = numbers of holes examinated to satisfy a request
                System.out.println(MMU.ANSI_BLUE + "\n\nOccupied Slots = " + occupiedSlot + MMU.ANSI_RESET);
                System.out.println(MMU.ANSI_BLUE + "Current memory utilizattion = " + accumAvgMMU*1.0/examHoleCount);
                System.out.println(MMU.ANSI_BLUE + "Average MMU = " + accumAvgMMU/examHoleCount);   
                System.out.println(MMU.ANSI_BLUE + "Average search times = " + accumHoleCount * 1.0/examHoleCount );
               

                
                //THIS IS FOR RELEASE(i), block i choose randomly
                int blockToRelease;
                if( mmu.blockSize() == 0){
                    System.out.println(MMU.ANSI_RED + "There is no block to release" + MMU.ANSI_RESET);
                }
                else{
                    blockToRelease = rand.nextInt(mmu.blockSize()) ;  
                    occupiedSlot -= mmu.blockSizeAtIndex(blockToRelease);
                    mmu.release(blockToRelease);
                    System.out.println("\n" + MMU.ANSI_PURPLE + "Release block(" + blockToRelease + ")" + MMU.ANSI_RESET);
                    mmu.printCurrent();
                    System.out.println("");
                    mmu.printBlockList();
                }

                
            }
            
    }
    
    
    /**
     * findTheSmallestHole() method will choose the smallest size that match the request size
     * if cannot find the exact, increase the request's size + 1, until found
     * @param array: memory array 
     * @param value: integer request size
     * @return : smallest integer that close to integer value
     */
    private static int findTheSmallestHole( int[] array, int value ){        
        int i = 0;
        int index = 0;
        boolean found = false;
        while( !found )
        {
            for( i =0; i<array.length; i++ ){
                if( array[i] == value * (-1) ){
                    index = i;
                    found = true;
                }
            }
            value++;
        }
        
        return index;
    }
    
    /**
     * normalDistribution() method will generate the random request size by normal distribution
     * @return 
     */
    private static int normalDistribution(){
        int value = 0;
        
        while( value < 1 || value > SIZE - 1){
            value = (int)Math.round( rand.nextGaussian()*SIZE + 1);
        }
        
        return value;
    }
    
    
    //===========================  DATA ANALYSIS - HAIAU BUI   ================================   
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////      PLOT FAMILIES OF CURVES - BEST_FIT    ///////////////////////////
    //
    //   current MMU        = 0.80, 0.83, 0.87, 0.88, 0.89, 0.90, 0.91, 0.92, 0.92, 0.93
    //   avg, MMU           = 0.80, 0.83, 0.87, 0.88, 0.89, 0.90, 0.91, 0.92, 0.92, 0.93   
    //   avg, search time   = 6.5 , 8.71, 11.7, 11.3, 11.8, 12.2, 12.5, 12.5, 11.8, 10.4
    //  
    //      avg, current MMU = 0.89  -------    avg, search time = 10.94
    
    //  In Best_Fit algorithm, the current size of occupied space divided by memory size is avg of 0.89
    //  which means that about 90%f of the memory slot have used, therefore its efficent whereas first-fit 
    //  ( first-fit has current MMU avg = 0.5 )
    //  however, the avg search time is very high, about 10.94, which means exam about 11 holes to find 
    //  a hole that match the request, compare to first-fit, avg serach time is 1.85 which means it took
    //  only ablout 2 times to exam if a hole is matching for the request
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////      PLOT FAMILIES OF CURVES - FIRST_FIT    ///////////////////////////
    //
    //   current MMU        = 0.5, 0.93, 0.38, 0.19, 0.75, 0.59, 0.90, 0.65, 0.34, 0.53
    //   avg, MMU           = 0.5, 0.51, 0.47, 0.42, 0.47, 0.49, 0.53, 0.52, 0.53, 0.55    
    //   avg, search time   = 2.0, 1.66, 1.75, 2.0 , 1.83, 1.87, 1.7 , 2.11, 1.80, 1.84
    //
    //      avg, current MMU = 0.5  -------    avg, search time = 1.85
    //
    
    
}
