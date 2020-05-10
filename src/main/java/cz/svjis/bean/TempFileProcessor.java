/*
 *       TempFileProcessor.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.bean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berk
 */
public class TempFileProcessor {
    private static final Logger LOGGER = Logger.getLogger(TempFileProcessor.class.getName());
    
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
        try {
            Files.delete(f.toPath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void createFile(String fileName, byte[] data) throws IOException {
        File f = new File(destinationPath.getAbsolutePath() + File.separator + fileName);
        try (DataOutputStream os = new DataOutputStream(new FileOutputStream(f))) {
            os.write(data);
        }
    }
    
    public String getUrlPath(String fileName) {
        return directory + "/" + companyDomain + "/" + fileName;
    }
}
