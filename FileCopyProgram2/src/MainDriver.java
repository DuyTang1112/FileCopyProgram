
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duy Anh Tang
 */
public class MainDriver {

    static boolean readOk = true;
    static boolean writeOk = false;
    static boolean noMoreBlock = false;
    static final int k = 1024;
    static final int SIZE = 16 * k * k;//48
    static RandomAccessFile inFile, outFile;
    static InputBuffer inBuff;
    static OutputBuffer outBuff;
    static int BlockSize = 64 * k;

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        inFile = new RandomAccessFile("win10.png", "rw");
        outFile = new RandomAccessFile("output.png", "rw");
//        Random random = new Random();
//        inFile.seek(0L);
//        System.out.println("Creating File...");
//        for (int i = 0; i < SIZE; i++) { //create a 4mb file
//            int j = random.nextInt(100);
//            inFile.writeInt(j);
//        }
        outFile.seek(0L);
        inFile.seek(0L);

        //   System.out.println("File creation completed.");
        inBuff = new InputBuffer(BlockSize, MainDriver.SIZE);
        outBuff = new OutputBuffer(outFile, BlockSize, MainDriver.SIZE);
        FileReader t1 = new FileReader(inBuff, inFile, BlockSize);
        FileWriter t2 = new FileWriter(outBuff, outFile, BlockSize);
        BlockUnloader b = new BlockUnloader(inBuff, outBuff, BlockSize);
        long start = System.nanoTime();
        t1.start();
        t2.start();
        b.start();
        t1.join();
        t2.join();
        b.join();
        System.out.println("Complete copying in " + (System.nanoTime() - start));
    }

}
