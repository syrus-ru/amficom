/*
 * $Id: LRUMapSaver.java,v 1.3 2004/11/17 13:07:16 max Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/17 13:07:16 $
 * @author $Author: max $
 * @module module_name
 */
public class LRUMapSaver {
    
    private static String pathNameOfSaveDir;
    private static File saveDir;   
        
    private LRUMapSaver() {
        // empty
    }
       
    public static void save(LRUMap lruMap, String objectEntityName) {
    	
        File tempFile = null;
        try {
            if (pathNameOfSaveDir == null)
            	pathNameOfSaveDir = ApplicationProperties.getString("lrumapsavedir", "SerializedLRUMaps");
            if (saveDir == null || !saveDir.exists()) {
            	saveDir = new File(pathNameOfSaveDir);
               	saveDir.mkdir(); 
            }
            
            File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
            tempFile = new File(saveFile.getPath() + ".swp");        
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(tempFile));                      
            Log.debugMessage("LRUMapSaver.save | Trying to save LRUMap with " + objectEntityName +  
                    " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL03);       
            List keys = new LinkedList();
            for (Iterator it = lruMap.keyIterator(); it.hasNext();) {
				Object key = it.next();                      
				keys.add(key);
			}
            if(keys == null || keys.isEmpty()) {
                Log.debugMessage("LRUMapSaver.save | LruMap has no elements. Nothing to save.", Log.DEBUGLEVEL03);
                return;
            }
            out.writeObject(objectEntityName);
            out.writeObject(keys);
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
    
    public static List load(String objectEntityName) {
        
        try {
            if (pathNameOfSaveDir == null)
            	pathNameOfSaveDir = ApplicationProperties.getString("lrumapsavedir", "SerializedLRUMaps");
            if (saveDir == null || !saveDir.exists()) {
                saveDir = new File(pathNameOfSaveDir);
                saveDir.mkdir(); 
            }           
            Log.debugMessage("Trying to load LRUMap with " + objectEntityName + " | LRUMapSaver.load ", Log.DEBUGLEVEL10);
            File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(saveFile));
            String keyObjectEntityName = (String) in.readObject();
            if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
            	Log.errorMessage("LRUMapSaver.load | Wrong input file "+ saveFile.getAbsolutePath() + ". Loading failed");
                return null;
            }            
            List keys = (LinkedList) in.readObject();
            return keys;
        } catch (FileNotFoundException fnfe) {
            Log.errorMessage("LRUMapSaver.save | Warning: " + fnfe.getMessage());
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
