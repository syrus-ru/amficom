/*
 * $Id: LinkType.java,v 1.1 2004/10/22 13:03:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/22 13:03:43 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class LinkType extends StorableObjectType {

	private int						sort;
	private String					manufacturer;
	private String					manufacturerCode;
	private Identifier				imageId;

	private StorableObjectDatabase	linkTypeDatabase;

	public LinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			this.linkTypeDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public LinkType(LinkType_Transferable ltt) {
		super(new Identifier(ltt.id), new Date(ltt.created), new Date(ltt.modified), new Identifier(ltt.creator_id),
				new Identifier(ltt.modifier_id), new String(ltt.codename), new String(ltt.description));
		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = new Identifier(ltt.image_id);
	}

	protected LinkType(Identifier id,
			Identifier creatorId,
			String codename,
			String description,
			int sort,
			String manufacturer,
			String manufacturerCode,
			Identifier imageId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				codename, description);

		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;

		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;

	}

	/**
	 * create new instance for client
	 */
	public static LinkType createInstance(	Identifier id,
											Identifier creatorId,
											String codename,
											String description,
											LinkTypeSort sort,
											String manufacturer,
											String manufacturerCode,
											Identifier imageId) {
		return new LinkType(id, creatorId, codename, description, sort.value(), manufacturer, manufacturerCode, imageId);
	}

	public static LinkType getInstance(LinkType_Transferable ltt) throws CreateObjectException {
		LinkType equipmentType = new LinkType(ltt);

		equipmentType.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			if (equipmentType.linkTypeDatabase != null)
				equipmentType.linkTypeDatabase.insert(equipmentType);
		} catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return equipmentType;
	}

	public Object getTransferable() {
		return new LinkType_Transferable((Identifier_Transferable) super.id.getTransferable(), super.created.getTime(),
											super.modified.getTime(), (Identifier_Transferable) super.creatorId
													.getTransferable(), (Identifier_Transferable) super.modifierId
													.getTransferable(), new String(super.codename),
											(super.description != null) ? (new String(super.description)) : "",
											LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
											(Identifier_Transferable) this.imageId.getTransferable());
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description,
												int sort,
												String manufacturer,
												String manufacturerCode,
												Identifier imageId) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
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
}
