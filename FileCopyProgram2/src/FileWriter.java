
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
public class FileWriter extends Thread {

    byte[] block;
    RandomAccessFile f;
    int BlockSize;
    OutputBuffer outBuff;

    public FileWriter(OutputBuffer o,RandomAccessFile f, int size) {
        this.f = f;
        this.BlockSize = size;
        this.outBuff = o;
    }

    public void run() {
        //System.out.println("Starting Thread FileWriter");
        try {
            while (true) {

                outBuff.writeToFile();
//                if (MainDriver.endOfFile()) {
//                    break;
//                }
                if (MainDriver.noMoreBlock && MainDriver.inFile.getFilePointer() <= MainDriver.outFile.getFilePointer()) {
                    break;
                }
            }
        } catch (IOException ex) {
        }
      //  System.out.println("FileWriter Thread Done");

    }

    public OutputBuffer getOutBuff() {
        return outBuff;
    }
    public void finalize(){
        try {
            outBuff.writeAll();
        } catch (IOException ex) {
        }
    }
}
