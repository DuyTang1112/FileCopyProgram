
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duy Anh Tang
 */
public class OutputBuffer {

    byte[] buffer;
    RandomAccessFile outFile;
    int blockSize, bufferSize;
    int currL,currNumBlock,blockCount,currW;
    boolean loadOk;

    public OutputBuffer(RandomAccessFile f, int bs, int buffs) {
        this.outFile = f;
        blockSize = bs;
        bufferSize = buffs;
        buffer = new byte[bufferSize];
        currL = 0;
        currW=0;
        currNumBlock=0;
        blockCount=bufferSize/blockSize;
    }

    public synchronized void writeToFile() throws IOException {
        while (!MainDriver.writeOk) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
       
        
        outFile.write(buffer,currW, blockSize);
        currW += blockSize;
        currNumBlock--;
        //System.out.println("Writing "+ currW);
        if (isEmpty()){
            MainDriver.writeOk=false;
        }
        if (reachEndOfBuffer()){
         //   System.out.println("Reach end of outputbuffer");
            currW=0;
        }

        notifyAll();
    }

    public boolean isFull() {
        return currNumBlock >=blockCount;
    }
    public boolean isEmpty(){
        return currNumBlock==0;
    }
     private boolean reachEndOfBuffer() {
        return currW >= bufferSize;
    }

    public synchronized void loadBlock(byte[] block) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        if (block != null) {
            System.arraycopy(block, 0, buffer, currL, blockSize);
            currL += blockSize;
            currNumBlock++;
           // System.out.println("Loading block "+currNumBlock);
            if (!isEmpty()) MainDriver.writeOk=true;
            if (currL>=bufferSize) currL=0;
           // if (isFull()) MainDriver.writeOk = true;
        }
        notifyAll();
    }
    public void writeAll() throws IOException{
        outFile.write(buffer);
    }
}
