/*
 * $Id: CableLinkType.java,v 1.1 2004/12/10 16:49:52 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/10 16:49:52 $
 * @author $Author: max $
 * @module config_v1
 */
public class CableLinkType extends AbstractLinkType implements Characterized {
    
    private static final long   serialVersionUID    = 3257007652839372857L;
    private String name;
    private int                     sort;
    private String                  manufacturer;
    private String                  manufacturerCode;
    private Identifier              imageId;
    private List                    characteristics;
    private List                    cableThreadTypes;
    private StorableObjectDatabase  cableLinkTypeDatabase;

    public CableLinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
        super(id);

        this.characteristics = new LinkedList();
        this.cableThreadTypes = new LinkedList();
        this.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;
        try {
            this.cableLinkTypeDatabase.retrieve(this);
        } catch (IllegalDataException ide) {
            throw new RetrieveObjectException(ide.getMessage(), ide);
        }
    }

    public CableLinkType(CableLinkType_Transferable cltt) throws CreateObjectException {
        super(cltt.header, new String(cltt.codename), new String(cltt.description));
        this.sort = cltt.sort.value();
        this.manufacturer = cltt.manufacturer;
        this.manufacturerCode = cltt.manufacturerCode;
        this.imageId = new Identifier(cltt.image_id);
        this.name = cltt.name;
        try {
            this.characteristics = new ArrayList(cltt.characteristic_ids.length);
            List characteristicIds = new ArrayList(cltt.characteristic_ids.length);
            for (int i = 0; i < cltt.characteristic_ids.length; i++)
                characteristicIds.add(new Identifier(cltt.characteristic_ids[i]));
            this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
            
            this.cableThreadTypes = new ArrayList(cltt.cableThreadTypeIds.length);
            List cableThreadTypeIds = new ArrayList(cltt.cableThreadTypeIds.length);
            for (int i = 0; i < cltt.cableThreadTypeIds.length; i++)
                cableThreadTypeIds.add(new Identifier(cltt.cableThreadTypeIds[i]));
            this.cableThreadTypes.addAll(ConfigurationStorableObjectPool.getStorableObjects(cableThreadTypeIds, true));			
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
    }

    protected CableLinkType(Identifier id,
            Identifier creatorId,
            String codename,
            String description,
            String name,
            int sort,
            String manufacturer,
            String manufacturerCode,
            Identifier imageId) {
        super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
                codename, description);
        this.name = name;
        this.sort = sort;
        this.manufacturer = manufacturer;
        this.manufacturerCode = manufacturerCode;
        this.imageId = imageId;
        this.characteristics = new LinkedList();
        this.cableThreadTypes = new LinkedList();
        this.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;

    }

    /**
     * create new instance for client
     * @throws CreateObjectException
     */
    public static CableLinkType createInstance(  Identifier creatorId,
                                            String codename,
                                            String description,
                                            String name,
                                            LinkTypeSort sort,
                                            String manufacturer,
                                            String manufacturerCode,
                                            Identifier imageId) throws CreateObjectException {
        if (creatorId == null || codename == null || description == null || name == null ||
                sort == null || manufacturer == null || manufacturerCode == null || imageId == null)
            throw new IllegalArgumentException("Argument is 'null'");
        

        try {
            return new CableLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLE_LINKTYPE_ENTITY_CODE), creatorId, codename, description, name, sort.value(), manufacturer, manufacturerCode, imageId);
        } catch (IllegalObjectEntityException e) {
            throw new CreateObjectException("CableLinkType.createInstance | cannot generate identifier ", e);
        }
    }

    public static CableLinkType getInstance(CableLinkType_Transferable ltt) throws CreateObjectException {
        CableLinkType cableLinkType = new CableLinkType(ltt);

        cableLinkType.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;
        try {
            if (cableLinkType.cableLinkTypeDatabase != null)
                cableLinkType.cableLinkTypeDatabase.insert(cableLinkType);
        } catch (IllegalDataException ide) {
            throw new CreateObjectException(ide.getMessage(), ide);
        }

        return cableLinkType;
    }

    public Object getTransferable() {
        int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        i = 0;
        Identifier_Transferable[] cableThreadTypeIds = new Identifier_Transferable[this.cableThreadTypes.size()];
        for (Iterator iterator = this.cableThreadTypes.iterator(); iterator.hasNext();)
            cableThreadTypeIds[i++] = (Identifier_Transferable)((CableThreadType)iterator.next()).getId().getTransferable();
        
        return new CableLinkType_Transferable(super.getHeaderTransferable(),
                                         new String(super.codename),
                                         (super.description != null) ? (new String(super.description)) : "",
                                         (this.name != null) ? (new String(this.name)) : "",
                                         LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
                                         (Identifier_Transferable) this.imageId.getTransferable(),
                                         charIds,
                                         cableThreadTypeIds);
    }

    protected synchronized void setAttributes(  Date created,
                                                Date modified,
                                                Identifier creatorId,
                                                Identifier modifierId,
                                                String codename,
                                                String description,
                                                String name,
                                                int sort,
                                                String manufacturer,
                                                String manufacturerCode,
                                                Identifier imageId) {
        super.setAttributes(created, modified, creatorId, modifierId, codename, description);
        this.name = name;
        this.sort = sort;
        this.manufacturer = manufacturer;
        this.manufacturerCode = manufacturerCode;
        this.imageId = imageId;
    }
    
    public void insert() throws CreateObjectException {
        try {
            if (this.cableLinkTypeDatabase != null)
                this.cableLinkTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae.getMessage(), ae);
        }
    }
    
    public Identifier getImageId() {
        return this.imageId;
    }
    
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    public String getManufacturerCode() {
        return this.manufacturerCode;
    }
    
    public LinkTypeSort getSort() {
        return LinkTypeSort.from_int(this.sort);
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.currentVersion = super.getNextVersion();
        this.name = name;
    }

    public List getDependencies() {        
        return this.characteristics;
    }
    
    public List getCharacteristics() {
        return Collections.unmodifiableList(this.characteristics);
    }
    
    protected void setCharacteristics0(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
    }
    
    public void setCharacteristics(final List characteristics) {
        this.setCharacteristics0(characteristics);
        super.currentVersion = super.getNextVersion();
    }
	public List getCableThreadTypes() {
		return this.cableThreadTypes;
	}
	public void setCableThreadTypes(List cableThreadTypes) {
		this.cableThreadTypes = cableThreadTypes;
	}
}
