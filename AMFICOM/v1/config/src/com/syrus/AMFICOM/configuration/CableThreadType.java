/*
 * $Id: CableThreadType.java,v 1.1 2004/11/19 09:00:09 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/19 09:00:09 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class CableThreadType extends AbstractLinkType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258688818985973300L;
	private String					manufacturer;
	private String					manufacturerCode;
	private String					mark;
	private String					color;
	private Identifier				cableLinkTypeId;

	private StorableObjectDatabase	cableThreadTypeDatabase;

	public CableThreadType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			this.cableThreadTypeDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThreadType(CableThreadType_Transferable ctt) {
		super(ctt.header, new String(ctt.codename), new String(ctt.description));
		this.manufacturer = ctt.manufacturer;
		this.manufacturerCode = ctt.manufacturerCode;
		this.mark = ctt.mark;
		this.color = ctt.color;
		this.cableLinkTypeId = new Identifier(ctt.cable_link_type_id);
	}

	protected CableThreadType(Identifier id,
			Identifier creatorId,
			String codename,
			String description,
			String manufacturer,
			String manufacturerCode,
			String mark,
			String color,			
			Identifier cableLinkTypeId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				codename, description);
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.mark = mark;
		this.color = color;
		this.cableLinkTypeId = cableLinkTypeId;

		this.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;

	}

	/**
	 * create new instance for client
	 */
	public static CableThreadType createInstance(	Identifier id,
													Identifier creatorId,
													String codename,
													String description,
													String manufacturer,
													String manufacturerCode,
													String mark,
													String color,			
													Identifier cableLinkTypeId) {
		return new CableThreadType(id, creatorId, codename, description, manufacturer, manufacturerCode, mark, color, cableLinkTypeId);
	}

	public static CableThreadType getInstance(CableThreadType_Transferable ctt) throws CreateObjectException {
		CableThreadType cableThreadType = new CableThreadType(ctt);

		cableThreadType.cableThreadTypeDatabase = ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			if (cableThreadType.cableThreadTypeDatabase != null)
				cableThreadType.cableThreadTypeDatabase.insert(cableThreadType);
		} catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return cableThreadType;
	}

	public Object getTransferable() {
		return new CableThreadType_Transferable(super.getHeaderTransferable(), 
										 super.codename,
										 (super.description != null) ? super.description : "",										  
										 this.manufacturer, this.manufacturerCode,
										 (this.mark != null) ? this.mark : "",
										 (this.color != null) ? this.color : "",		
										 (Identifier_Transferable) this.cableLinkTypeId.getTransferable());
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description,
												String manufacturer,
												String manufacturerCode,
												String mark,
												String color,			
												Identifier cableLinkTypeId) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.mark = mark;
		this.color = color;
		this.cableLinkTypeId = cableLinkTypeId;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public Identifier getCableLinkTypeId() {
		return this.cableLinkTypeId;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getMark() {
		return this.mark;
	}
	

	public List getDependencies() {
		return Collections.singletonList(this.cableLinkTypeId);
	}

}
