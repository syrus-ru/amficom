/*
 * $Id: KISType.java,v 1.7 2004/11/15 13:50:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.KISType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;

/**
 * @version $Revision: 1.7 $, $Date: 2004/11/15 13:50:27 $
 * @author $Author: bob $
 * @module module_name
 */
public class KISType extends StorableObjectType {    

	static final long serialVersionUID = 2648925418432386875L;

	private String name;
	
    private StorableObjectDatabase kisTypeDatabase;
    
    public KISType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
    	super(id);
        this.kisTypeDatabase = ConfigurationDatabaseContext.kisTypeDatabase;
        try {
            this.kisTypeDatabase.retrieve(this);
        }
        catch (IllegalDataException ide) {
            throw new RetrieveObjectException(ide.getMessage(), ide);
        }
    }
    public KISType(KISType_Transferable ktt) throws CreateObjectException {
    	super(ktt.header,
			  new String(ktt.codename),
			  new String(ktt.description));
    	this.name = ktt.name;
    }
    protected KISType(Identifier id,
             Identifier creatorId,
             String codename,
             String description,
			 String name){
        super(id,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                creatorId,
                creatorId,
                codename,
                description);
        this.name = name;
        
        this.kisTypeDatabase = ConfigurationDatabaseContext.kisTypeDatabase;
    }
    /**
     * create new instance for client 
     * @param id
     * @param creatorId
     * @param codename
     * @param description
     * @return
     */
    public static KISType createInstance(Identifier id,
    		Identifier creatorId,
			String codename,
			String description,
			String name){
        return new KISType(id,
        		creatorId,
				codename,
				description,
				name);
    }
    
    public static KISType getInstance(KISType_Transferable ktt) throws CreateObjectException {
        KISType kisType = new KISType(ktt);
        
        kisType.kisTypeDatabase = ConfigurationDatabaseContext.kisTypeDatabase;
        try {
            if (kisType.kisTypeDatabase != null)
                kisType.kisTypeDatabase.insert(kisType);
        }
        catch (IllegalDataException ide) {
            throw new CreateObjectException(ide.getMessage(), ide);
        }
        
        return kisType;
    }
    
    public Object getTransferable() {
        return new KISType_Transferable(super.getHeaderTransferable(),
				new String(super.codename),
				(super.description != null) ? (new String(super.description)) : "",
				(this.name != null) ? (new String(this.name)) : "");
    }
    protected synchronized void setAttributes(Date created,
            Date modified,
            Identifier creatorId,
            Identifier modifierId,
            String codename,
            String description,
			String name) {
        super.setAttributes(created,
                modified,
                creatorId,
                modifierId,
                codename,
                description);
        this.name = name;
    }    
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}
	
	public List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
