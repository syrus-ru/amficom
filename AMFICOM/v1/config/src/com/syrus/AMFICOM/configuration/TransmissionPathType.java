/*
 * $Id: TransmissionPathType.java,v 1.3 2004/11/04 09:05:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/04 09:05:13 $
 * @author $Author: bob $
 * @module module_name
 */

public class TransmissionPathType extends StorableObjectType {
    
    private String name;
    
    private StorableObjectDatabase transmissionPathTypeDatabase;
    
    public TransmissionPathType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
        super(id);
        this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
        try {
            this.transmissionPathTypeDatabase.retrieve(this);
        }
        catch (IllegalDataException ide) {
            throw new RetrieveObjectException(ide.getMessage(), ide);
        }
    }
    public TransmissionPathType(TransmissionPathType_Transferable tptt) throws CreateObjectException {
        super(new Identifier(tptt.id),
                new Date(tptt.created),
                new Date(tptt.modified),
                new Identifier(tptt.creator_id),
                new Identifier(tptt.modifier_id),
                new String(tptt.codename),
                new String(tptt.description));
        this.name = tptt.name;
    }
    protected TransmissionPathType(Identifier id,
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
        
        this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
    }
    /**
     * create new instance for client 
     * @param id
     * @param creatorId
     * @param codename
     * @param description
     * @return
     */
    public static TransmissionPathType createInstance(Identifier id,
            Identifier creatorId,
            String codename,
            String description,
            String name){
        return new TransmissionPathType(id,
                creatorId,
                codename,
                description,
                name);
    }
    public static TransmissionPathType getInstance(TransmissionPathType_Transferable ktt) throws CreateObjectException {
        TransmissionPathType transmissionPathType = new TransmissionPathType(ktt);
        
        transmissionPathType.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
        try {
            if (transmissionPathType.transmissionPathTypeDatabase != null)
                transmissionPathType.transmissionPathTypeDatabase.insert(transmissionPathType);
        }
        catch (IllegalDataException ide) {
            throw new CreateObjectException(ide.getMessage(), ide);
        }
        
        return transmissionPathType;
    }
    
    public Object getTransferable() {
        return new TransmissionPathType_Transferable((Identifier_Transferable)super.id.getTransferable(),
                super.created.getTime(),
                super.modified.getTime(),
                (Identifier_Transferable)super.creatorId.getTransferable(),
                (Identifier_Transferable)super.modifierId.getTransferable(),
                new String(super.codename),
                (super.description != null) ? (new String(super.description)) : "",
                (this.name != null) ? (new String(this.name)) : "");
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.currentVersion = super.getNextVersion();
        this.name = name;
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

	public List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
