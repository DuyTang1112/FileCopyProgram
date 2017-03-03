
import java.io.RandomAccessFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duy Anh Tang
 */
public class InputBuffer extends Thread {

    byte[] buffer;
    int currNumBlock, numBlock, bufferSize, blockSize, currR, currW;

    public InputBuffer(int blockSize, int bufferSize) {
        this.blockSize = blockSize;
        this.bufferSize = bufferSize;
        numBlock = bufferSize / blockSize;
        currR = 0;
        currW = 0;
        currNumBlock=0;
        buffer=new byte[bufferSize];
    }

    public synchronized void readBlockFromFile(byte[] block) {
        while (!MainDriver.readOk) {
            try {
                wait();
            } catch (InterruptedException ie) {
            }
        }
        System.arraycopy(block, 0, buffer, currR, blockSize);
        currR += blockSize;
        currNumBlock++;
      //  System.out.println("Read block "+currNumBlock+" CurrR: "+currR);
        if (reachEndOfBuffer()) {
          //  System.out.println("reach end of buffer");
            currR = 0;
            MainDriver.readOk = false;
        }
        if (isFull()){
     //       System.out.println("Is full");
            MainDriver.readOk=false;
        } 
        
        notify();
    }

    private boolean reachEndOfBuffer() {
        return currR >= bufferSize;
    }

    public synchronized byte[] unloadBlock() {
        if (currNumBlock == 0) {
            return null;
        }
        byte[] block = new byte[blockSize];
        System.arraycopy(buffer, currW, block, 0, blockSize);
        currW += blockSize;
        currNumBlock--;
        //System.out.print("Unload ");
        //System.out.println(currNumBlock);
        if (currW>=bufferSize) {
           // System.out.println("CurrW reach end of buffer");
            currW = 0;
            MainDriver.readOk=true;
        }
        notifyAll();
        
       // if (currNumBlock<numBlock) MainDriver.readOk=true;
        return block;
        
    }

    public boolean isEmpty() {
        return currNumBlock == 0;
    }
    public boolean isFull(){
        return currNumBlock>=numBlock;
    }
}
