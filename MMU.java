
package memoryallocation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author HaiAu Bui
 * CSD 415, Professor Abbott
 */
public class MMU {
  
    private int size;
    public int[] memoryArray;
    public List<Integer> allocatedBlocks;
    public List<Integer> findIndexOfBlock;
    public int blockCount = 0;
    public int occupiedSlot = 0;
    
    //for color on system.our.println
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    //constructor will pass the size of the memory array as param
    MMU( int size ){
        this.memoryArray = new int[size];
        this.size = size;
        allocatedBlocks = new ArrayList<>();
        findIndexOfBlock = new ArrayList<>();
    }
    
    
    /**
     * InitMMU() method will init the array which each index represent how many 
     * freeSlots are behind it, the array looks like [-32,-31....-2,-1]
     */
    public void initMMU(){
        System.out.print("{");
        for( int i=0; i<size; i++){            
            memoryArray[i] = - (size - i);    
            
            if(size-i == 1){
                System.out.print(memoryArray[i] + "}");
            }else{            
                System.out.print(memoryArray[i] + ",");
            }
        }
        System.out.println("\n");
    }
    
        
    /**
     * request() method will pass the start index and the request block's size as parameter
     * @param start: integer index where the block start
     * @param blockSize : integer size of the block is requested
     */
    public void request(  int start , int blockSize){
        try{
            //TODO, find startIndex-endIndex that are free to allocated blockSize
            Integer _blockSize = blockSize;
            int reIndex = start  ;
            
            if( (start + blockSize) > this.size || memoryArray[start] > 0){
                throw new ArrayIndexOutOfBoundsException();
            }
            
            allocatedBlocks.add(blockCount, _blockSize );
            findIndexOfBlock.add(blockCount, start);

            for( int i=start; i<(start+blockSize); i++){
                memoryArray[start++] = blockSize--;            
            }        

            this.blockCount++;
            this.occupiedSlot += _blockSize;
            
            //call the reArrangeAfterRequest() method to coalease the holes infront after the request is granted
            coalesceAfterRequest( reIndex );
            
        }catch( ArrayIndexOutOfBoundsException e){
            System.out.println(ANSI_RED + "Oopss! Not enought mememory for this block at the given index." + ANSI_RESET);
        }
    }
    
    
    /**
     * coalesceAfterRequest() method will coalesce slot that in front of a block is requested
     * and recalculate to show the right -k
     * @param fromIndex : integer last index of the block is releasing
     */
    private void coalesceAfterRequest( int fromIndex ){
        
        int _one = -1;
        for( int i=fromIndex-1; i>=0 ; i--){
            if( fromIndex -1 > 0  && memoryArray[i] < 0){
                memoryArray[i] = _one--;
            }
            else{
                return;
            }
        }
    }
    
    /**
     * coalesceAfterRelease() method will coalesce slots from the last index of the released block
     * to all the way backward to the index 0 to recalculate the right -k
     * @param lastIndex 
     */
    private void coalesceAfterRelease( int lastIndex ){
        //if the last index of release block has holes after it, coalesce
        int _one = -1;
        
        if( lastIndex == size -1 ){
            memoryArray[lastIndex] = -1;
        }else{
            memoryArray[lastIndex] = _one;
        }
        
        if( lastIndex+1 <size &&  memoryArray[lastIndex+1] < 0){
            _one += memoryArray[lastIndex+1] ;
        }
        
        for( int i=lastIndex  ; i>=0 ; i--){
            if( lastIndex  > 0  && memoryArray[i] < 0){
                memoryArray[i] = _one--;
            }
            else{
                return;
            }
        }
    }
    
    /**
     * release() method will release the block with number of block = int block
     * @param block: integer show what block number should be released 
     */
    public void release( int block ){
        try{
            if( block >= allocatedBlocks.size() || block < 0 ){
                throw new ArrayIndexOutOfBoundsException();
            }

            int blockSize = allocatedBlocks.get(block);
            int blockIndex = findIndexOfBlock.get(block);

            int lastIndexOfRelease = blockIndex + blockSize - 1;

            int _one = -1;
            for( int i=lastIndexOfRelease-1; i>= blockIndex; i--){
                memoryArray[i] = _one--;
            }

            allocatedBlocks.remove(block);
            findIndexOfBlock.remove(block);


            this.blockCount--;
            this.occupiedSlot -= blockSize;

            //call the coalesceAfterRelease() to coalease the entire array after release a block
            coalesceAfterRelease( lastIndexOfRelease  );
            
        }catch(ArrayIndexOutOfBoundsException e ){
            System.out.println(ANSI_RED + "There is no block at that index" + ANSI_RESET);
        }
        
    }
    
    //keep track of the blocks is currently assigned
    public int blockSize(){
        return allocatedBlocks.size();
    }
    
    //return the block's size at given index
    public int blockSizeAtIndex( int index ){
        return allocatedBlocks.get(index);
    }
    
    //keep track the list of mataining the index of each block
    public int blockIndex(){
        return findIndexOfBlock.size();
    }
    
    //will print the current how many blocks is allocated
    public void printBlockList(){
       
        for( int i=0; i<allocatedBlocks.size(); i++ ){
            System.out.println("Block: " + i + ", value = " + allocatedBlocks.get(i) 
            + ", start index = " + findIndexOfBlock.get(i));
        }
    }
    
    //return the average of memory utilization
    public double getAvgMMU(){
        return occupiedSlot * 1.0 /this.size;
    }
    
    //display current blocks allocated in the memory array
    public void printCurrent(){
        System.out.print("{");
        for(int i=0; i<memoryArray.length; i++ ){
            if( i == memoryArray.length - 1){
                if( memoryArray[i] < 0)
                    System.out.print(memoryArray[i] + "}");
                else
                    System.out.print(ANSI_GREEN + memoryArray[i] + "}" + ANSI_RESET);  
            }
            else if( memoryArray[i] < 0 ){
                System.out.print(memoryArray[i] + ",");
            }
            else{
                System.out.print(ANSI_GREEN + memoryArray[i] + "," + ANSI_RESET);                
            }
        }
    }
}
