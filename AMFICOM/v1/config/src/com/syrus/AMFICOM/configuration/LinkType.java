/*
 * $Id: LinkType.java,v 1.21 2005/01/17 11:49:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
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
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.21 $, $Date: 2005/01/17 11:49:37 $
 * @author $Author: stas $
 * @module config_v1
 */

public class LinkType extends AbstractLinkType implements Characterized {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257007652839372857L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_SORT = "sort";
	public static final String COLUMN_MANUFACTURER = "manufacturer";
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturerCode";
	public static final String COLUMN_IMAGE_ID = "imageId";
	public static final String COLUMN_CHARACTERISTICS = "characteristics";
	private static Object[][] exportColumns = null;

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
		}
		catch (IllegalDataException ide) {
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
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(ltt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
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
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					codename,
					description);
		this.name = name;
		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
		this.characteristics = new ArrayList();

		super.currentVersion = super.getNextVersion();

		this.linkTypeDatabase = ConfigurationDatabaseContext.linkTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static LinkType createInstance(Identifier creatorId,
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
			return new LinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINKTYPE_ENTITY_CODE), creatorId, codename, description, name, sort.value(), manufacturer, manufacturerCode, imageId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("LinkType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.linkTypeDatabase != null)
				this.linkTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
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

	protected synchronized void setAttributes(Date created,
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
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							codename,
							description);
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

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	protected void setCharacteristics0(final List characteristics) {
		if (characteristics != null)
			this.characteristics.clear();
		else
			this.characteristics = new LinkedList();
		this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public Object[][] exportColumns() {
		if (exportColumns == null) {
			exportColumns = new Object[8][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_SORT;
			exportColumns[4][0] = COLUMN_MANUFACTURER;
			exportColumns[5][0] = COLUMN_MANUFACTURER_CODE;
			exportColumns[6][0] = COLUMN_IMAGE_ID;
			exportColumns[7][0] = COLUMN_CHARACTERISTICS;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = String.valueOf(getSort().value());
		exportColumns[4][1] = getManufacturer();
		exportColumns[5][1] = getManufacturerCode();
		exportColumns[6][1] = getImageId();
		List characteristics = new ArrayList(getCharacteristics().size());
		for (Iterator it = getCharacteristics().iterator(); it.hasNext(); ) {
			Characteristic ch = (Characteristic)it.next();
			characteristics.add(ch.exportColumns());
		}
		exportColumns[7][1] = characteristics;

		return exportColumns;
	}
}

