/*
 * $Id: LRUMapSaver.java,v 1.3 2004/11/12 07:41:26 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/12 07:41:26 $
 * @author $Author: max $
 * @module module_name
 */
public class LRUMapSaver {
    
    private static String pathNameOfSaveDir;
    private static File saveDir;
    
    static {
        pathNameOfSaveDir = ApplicationProperties.getString("lrumapsavedir", "SerializedLRUMaps");
        saveDir = new File(pathNameOfSaveDir);
        saveDir.mkdir(); 
    }
    
    private LRUMapSaver() {
        
    }
       
    public static void save(LRUMap lruMap, short objectEntityCode) {
    	
        File tempFile = null;
        try {               
            String objectEntityName =  ObjectEntities.codeToString(objectEntityCode);
            File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
            tempFile = new File(saveFile.getPath() + ".swp");        
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(tempFile));                      
            Log.debugMessage("LRUMapSaver.save | Trying to save LRUMap with " + objectEntityName +  
                    " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10);       
            out.writeObject(new Short(objectEntityCode));
            out.writeObject(lruMap);
            out.close();
            tempFile.renameTo(saveFile);
        } catch (FileNotFoundException fnfe) {
            Log.errorMessage("LRUMapSaver.save | Error: " + fnfe.getMessage());        	
        } catch (IOException ioe) {
        	Log.errorMessage("LRUMapSaver.save | Error: " + ioe.getMessage());
        } finally {
        	if(tempFile != null)
                tempFile.delete();
        }
        
        
    }
    
    public static LRUMap load(short objectEntityCode) {
        
        try {
            
            if (!saveDir.isDirectory()) {
            	Log.errorMessage("LRUMapSaver.load | Could not find save dir. Loading failed");
                return null;
            }            
            String objectEntityName =  ObjectEntities.codeToString(objectEntityCode);
            Log.debugMessage("Trying to load LRUMap with " + objectEntityName + " | LRUMapSaver.load ", Log.DEBUGLEVEL10);
            File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(saveFile));
            Short keyObjectEntityCode = (Short) in.readObject();
            if (keyObjectEntityCode.shortValue() != objectEntityCode) {
            	Log.errorMessage("LRUMapSaver.load | Wrong input file "+ saveFile.getAbsolutePath() + ". Loading failed");
                return null;
            }            
            LRUMap lruMap = (LRUMap) in.readObject();
            return lruMap;
        } catch (FileNotFoundException fnfe) {
            Log.errorMessage("LRUMapSaver.save | Error: " + fnfe.getMessage());
            return null;
        } catch (ClassNotFoundException cnfe) {
            Log.errorMessage("LRUMapSaver.save | Error: " + cnfe.getMessage());
            return null;
        } catch (IOException ioe) {
            Log.errorMessage("LRUMapSaver.save | Error: " + ioe.getMessage());
            return null;
        }
    }
}
