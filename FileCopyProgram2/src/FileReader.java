
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
public class FileReader extends Thread {

    RandomAccessFile f;
    byte[] block;
    int size;
    InputBuffer inBuff;

    public FileReader(InputBuffer i,RandomAccessFile f, int size) {
        this.f = f;
        this.size = size;
        block = new byte[size];
        inBuff = i;
        
    }

    @Override
    public void run() {
       // System.out.println("Starting FileReader");
        try {
            f.seek(0L);
            
            while (successful()) {
                inBuff.readBlockFromFile(block);
            }
          //  System.out.println("FileReader thread done.");
            MainDriver.noMoreBlock=true;
        } catch (IOException ex) {
        }
    }

    private boolean successful() throws IOException {
        return f.read(block) != -1;
    }

    public InputBuffer getInBuff() {
        return inBuff;
    }
    @Override
    public void finalize(){
        try {
            this.f.close();
        } catch (IOException ex) {
        }
    }
}
