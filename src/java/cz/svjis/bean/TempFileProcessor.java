/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author berk
 */
public class TempFileProcessor {
    private String directory = "gfx";
    private String companyDomain;
    private String realPath;
    private File destinationPath;
    
    public TempFileProcessor(String companyDomain, String realPath) {
        this.companyDomain = companyDomain;
        this.realPath = realPath;
        this.destinationPath= new File(this.realPath + File.separator + this.directory + File.separator + this.companyDomain);
        if (!destinationPath.exists()) {
            destinationPath.mkdir();
        }
    }

    public boolean isFileExists(String fileName) {
        File f = new File(destinationPath.getAbsolutePath() + File.separator + fileName);
        return f.exists();
    }
    
    public void deleteFile(String fileName) {
        File f = new File(destinationPath.getAbsolutePath() + File.separator + fileName);
        f.delete();
    }
    
    public void createFile(String fileName, byte[] data) throws FileNotFoundException, IOException {
        File f = new File(destinationPath.getAbsolutePath() + File.separator + fileName);
        DataOutputStream os = new DataOutputStream(new FileOutputStream(f));
        os.write(data);
        os.close();
    }
    
    public String getUrlPath(String fileName) {
        return directory + "/" + companyDomain + "/" + fileName;
    }
}
