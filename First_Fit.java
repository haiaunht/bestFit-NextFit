
package memoryallocation;

import java.util.Random;

/**
 * @author HaiAu Bui
 * CSD 415, Professor Abbott
 * For First-Fit, I will implement the algorithm that search the first hole
 * which is large enough for the block, the loop will request(s) and release(i)
 * then keep continuing if the condition is satisfy
 */
public class First_Fit {
    static Random rand = new Random();
    final static int SIZE = 32;
    private static double curentMMU = 0;
    private static double accumAvgMMU = 0;
    private static int examHoleCount = 0;
    private static double accumHoleCount = 0;
    private static int occupiedSlot = 0;
    
    public static void main(String[] args) {   
        
        MMU mmu = new MMU(SIZE);
        mmu.initMMU();
        
        
        while( true ){
              
            
            //THIS IS FOR REQUEST(S)
            /**for first-fit algorithm, find the first index that has 
             * a negative sign and has a abs.value = randomRequestSize
             */            
            int index = 0;
            int randomRequestSize;
            while( index < SIZE && mmu.memoryArray[index] < 0 ){
                randomRequestSize =  normalDistribution();
                if( Math.abs(mmu.memoryArray[index]) > randomRequestSize ){
                    mmu.request(index, randomRequestSize);
                    examHoleCount++;
                    System.out.println("\n" + MMU.ANSI_CYAN + "Request block size of (" + randomRequestSize + ")" + MMU.ANSI_RESET);
                    mmu.printBlockList();                    
                    mmu.printCurrent();
                    System.out.println("");
                    curentMMU = mmu.getAvgMMU();
                    accumAvgMMU += curentMMU;
                    occupiedSlot += randomRequestSize;
                }
                index += randomRequestSize ;                
                accumHoleCount++;
            }
            
            //Search times = numbers of holes examinated to satisfy a request
            System.out.println("");
            System.out.println(MMU.ANSI_BLUE + "Occupied Slots = " + occupiedSlot + MMU.ANSI_RESET);
            System.out.println(MMU.ANSI_BLUE + "Current memory utilizattion = " + occupiedSlot*1.0/SIZE);
            System.out.println(MMU.ANSI_BLUE + "Average MMU = " + accumAvgMMU*1.0/examHoleCount);   
            System.out.println(MMU.ANSI_BLUE + "Average search times = " + accumHoleCount * 1.0/examHoleCount );
            
            //THIS IS FOR RELEASE(i), block i choose randomly
            int blockToRelease;
            if( mmu.blockSize() == 0)
                System.out.println(MMU.ANSI_RED + "There is no block to release" + MMU.ANSI_RESET);
            else{
                blockToRelease = rand.nextInt(mmu.blockSize()) ;            
                //calculate the occupied slot, then release
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
    
    
    //===========================  DATA ANALYSIS ==========================================
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////      PLOT FAMILIES OF CURVES - FIRST_FIT    ///////////////////////////
    //
    //   current MMU        = 0.5, 0.93, 0.38, 0.19, 0.75, 0.59, 0.90, 0.65, 0.34, 0.53
    //   avg, MMU           = 0.5, 0.51, 0.47, 0.42, 0.47, 0.49, 0.53, 0.52, 0.53, 0.55    
    //   avg, search time   = 2.0, 1.66, 1.75, 2.0 , 1.83, 1.87, 1.7 , 2.11, 1.80, 1.84
    //
    //      avg, current MMU = 0.5  -------    avg, search time = 1.85
    //
    //  In first_fit algorithm, the current size of occupied space divided by memory size is avg of 0.5
    //  which means that about half of the memory slot have not used, therefore not efficent as best-fit 
    //  ( best-fit has current MMU avg = 0.89 )
    //  however, the avg search time is very low, about 1.85, which means exam about 1.85 holes to find 
    //  a hole that match the request, compare to best-fit, avg serach time is 10.94 which means it took
    //  almost 11 times to exam if a hole is matching for the request
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////      PLOT FAMILIES OF CURVES - BEST_FIT    ///////////////////////////
    //
    //   current MMU        = 0.80, 0.83, 0.87, 0.88, 0.89, 0.90, 0.91, 0.92, 0.92, 0.93
    //   avg, MMU           = 0.80, 0.83, 0.87, 0.88, 0.89, 0.90, 0.91, 0.92, 0.92, 0.93   
    //   avg, search time   = 6.5 , 8.71, 11.7, 11.3, 11.8, 12.2, 12.5, 12.5, 11.8, 10.4
    //  
    //      avg, current MMU = 0.89  -------    avg, search time = 10.94
    
}
