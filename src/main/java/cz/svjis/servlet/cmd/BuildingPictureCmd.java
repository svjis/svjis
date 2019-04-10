/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.common.HttpUtils;
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
        HttpUtils.writeBinaryData(
                getCompany().getPictureContentType(), 
                getCompany().getPictureFilename(), 
                getCompany().getPictureData(), 
                getRequest(), 
                getResponse());
    }
}
