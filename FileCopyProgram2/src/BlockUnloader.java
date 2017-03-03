
import java.io.IOException;
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
public class BlockUnloader extends Thread {

    InputBuffer in;
    OutputBuffer out;
    byte[] block;
    int blockSize;

    public BlockUnloader(InputBuffer in, OutputBuffer out, int bs) {
        this.in = in;
        this.out = out;
        blockSize = bs;
        block = new byte[blockSize];
    }

    @Override
    public void run() {
       // System.out.println("Starting Thread BlockUnloader");
        while (true) {
            block = in.unloadBlock();
            // if (block==null) break;
            out.loadBlock(block);
            try {
                if (MainDriver.noMoreBlock && MainDriver.inFile.getFilePointer() <= MainDriver.outFile.getFilePointer()) {
                    break;
                }
            } catch (IOException ex) {
            }
        }
       // System.out.println("Block Unloader Thread done");
    }
}
