/*
 * $Id: LinkType.java,v 1.11 2004/11/30 14:44:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.11 $, $Date: 2004/11/30 14:44:04 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class LinkType extends AbstractLinkType implements Characterized {
    
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257007652839372857L;
	private String name;
	private int						sort;
	private String					manufacturer;
	private String					manufacturerCode;
	private Identifier				imageId;
	private List                    characteristics;
	private StorableObjectDatabase	linkTypeDatabase;

	public LinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new LinkedList();
		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			this.linkTypeDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public LinkType(LinkType_Transferable ltt) throws CreateObjectException {
		super(ltt.header, new String(ltt.codename), new String(ltt.description));
		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = new Identifier(ltt.image_id);
		this.name = ltt.name;
        try {
            this.characteristics = new ArrayList(ltt.characteristic_ids.length);
            for (int i = 0; i < ltt.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(ltt.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
	}

	protected LinkType(Identifier id,
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
		this.characteristics = new ArrayList();
		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;

	}

	/**
	 * create new instance for client
	 */
	public static LinkType createInstance(	Identifier creatorId,
											String codename,
											String description,
											String name,
											LinkTypeSort sort,
											String manufacturer,
											String manufacturerCode,
											Identifier imageId) {
		return new LinkType(IdentifierPool.generateId(ObjectEntities.LINKTYPE_ENTITY_CODE), creatorId, codename, description, name, sort.value(), manufacturer, manufacturerCode, imageId);
	}

	public static LinkType getInstance(LinkType_Transferable ltt) throws CreateObjectException {
		LinkType linkType = new LinkType(ltt);

		linkType.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			if (linkType.linkTypeDatabase != null)
				linkType.linkTypeDatabase.insert(linkType);
		} catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return linkType;
	}

	public Object getTransferable() {
		int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
        return new LinkType_Transferable(super.getHeaderTransferable(),
										 new String(super.codename),
										 (super.description != null) ? (new String(super.description)) : "",
										 (this.name != null) ? (new String(this.name)) : "",
										 LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
										 (Identifier_Transferable) this.imageId.getTransferable(),
                                         charIds);
	}

	protected synchronized void setAttributes(	Date created,
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
        return this.characteristics;
    }
    
    public void setCharacteristics(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
        super.currentVersion = super.getNextVersion();
    }
}

