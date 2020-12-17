
package memoryallocation;

/**
 *
 * @author HaiAu Bui
 * CSD 415, Professor Abbott
 */
public class MemoryAllocation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MMU mmu = new MMU(32);
        mmu.initMMU();
        
        
        mmu.request( 3,  5);
        mmu.request( 10, 13 );
        mmu.request( 25, 5);
        
        
        mmu.printCurrent();
        System.out.println( "\nBlock array size = " + mmu.blockSize() + ", findIndexBlock size = " + mmu.blockIndex());
        mmu.printBlockList();
        System.out.println("\noccupied slots: " +  mmu.occupiedSlot + ", block count = " + mmu.blockCount );
        
        mmu.release(1);
        System.out.println( "\nBlock array size = " + mmu.blockSize() + ", findIndexBlock size = " + mmu.blockIndex());
        mmu.printBlockList();        
        mmu.printCurrent();
        System.out.println( "\n\noccupied slots: " + mmu.occupiedSlot + ", block count = " + mmu.blockCount);
    }
    
}
