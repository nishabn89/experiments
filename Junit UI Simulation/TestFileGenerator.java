package org.eclipse.photran.ui.swttests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * 
 * @author srikanth
 */
public class TestFileGenerator
{
     
    public static void generateTestData(int fileSize) throws IOException
    {
        String declarationBlock = "PROGRAM MAIN\n" + "INTEGER I, I_START, I_END, I_INC\n"
            + "REAL A(100)\n";
        String loopBody = "A(I) = 0.0E0\n";
        String endBlock = "END\n";
        // lines in start and end blocks
        int numLines = 4;
        String location = "/home/srikanth/SWTBotTesting/Scalability/TestFile.f90";
        File file = new File(location);
        file.delete();
        file.createNewFile();
        BufferedWriter f = new BufferedWriter(new FileWriter(location));

        f.write(declarationBlock);
        for (int i = numLines; i < fileSize; i++)
            f.write(loopBody);
        f.write(endBlock);
        f.close();
    }
}
