/*
 *       BuildingPictureCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.common.JspSnippets;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;

/**
 *
 * @author jaroslav_b
 */
public class BuildingPictureCmd extends Command {
    
    public BuildingPictureCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        JspSnippets.writeBinaryData(
                getCompany().getPictureContentType(), 
                getCompany().getPictureFilename(), 
                getCompany().getPictureData(), 
                getRequest(), 
                getResponse());
    }
}
