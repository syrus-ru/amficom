/*
 * $Id: LRUMapSaver.java,v 1.1 2004/11/11 08:01:29 max Exp $
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

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/11 08:01:29 $
 * @author $Author: max $
 * @module module_name
 */
public class LRUMapSaver {
    
    private File file;
        
    public LRUMapSaver() {
    	        
    }
       
    public void saveLRUMap(LRUMap lruMap, File outputFile) throws FileNotFoundException, IOException {
        this.file = outputFile;
        save(lruMap);
    }
    
    public void saveLRUMap(LRUMap lruMap, String pathName) throws FileNotFoundException, IOException {
        this.file = new File(pathName);
        save(lruMap);
    }
    
    private void save(LRUMap lruMap) {
    	
        File tempFile = null;
        
        try {
            tempFile = new File(this.file.getPath() + ".swp");
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(tempFile));
            String objectEntity = null;
            for (Iterator it = lruMap.iterator(); it.hasNext();) {
    			StorableObject e = (StorableObject) it.next();
                objectEntity = e.getId().getObjectEntity();
                break;
    		}
            Log.debugMessage("Trying to save LRUMap with " + objectEntity + " | LRUMapSaver.save ", Log.DEBUGLEVEL10);       
            out.writeObject(objectEntity);
            out.writeObject(lruMap);
            out.close();
            System.out.println(this.file.getPath());
            System.out.println(tempFile.getPath());
            tempFile.renameTo(this.file);
        } catch (FileNotFoundException fnfe) {
            Log.errorMessage("LRUMapSaver.save | " + fnfe.getMessage());        	
        } catch (IOException ioe) {
        	Log.errorMessage("LRUMapSaver.save | " + ioe.getMessage());
        } finally {
        	tempFile.delete();
        }
        
        
    }
    
    public LRUMap loadLRUMap(File outputFile) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(outputFile));
        String objectEntity = (String) in.readObject();
        Log.debugMessage("Trying to load LRUMap with " + objectEntity + " | LRUMapSaver.load ", Log.DEBUGLEVEL10);
        LRUMap lruMap = (LRUMap) in.readObject();
        return lruMap;
    }
}
