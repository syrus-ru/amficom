/*
 * $Id: LinkType.java,v 1.38 2005/04/01 16:00:37 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.38 $, $Date: 2005/04/01 16:00:37 $
 * @author $Author: max $
 * @module config_v1
 */

public class LinkType extends AbstractLinkType implements Characterizable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	private Set characteristics;

	private StorableObjectDatabase linkTypeDatabase;

	public LinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();
		this.linkTypeDatabase = ConfigurationDatabaseContext.getLinkTypeDatabase();
		try {
			this.linkTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public LinkType(LinkType_Transferable ltt) throws CreateObjectException {
		this.linkTypeDatabase = ConfigurationDatabaseContext.getLinkTypeDatabase();
		fromTransferable(ltt);
	}

	protected LinkType(Identifier id,
						Identifier creatorId,
						long version,
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
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
		this.characteristics = new HashSet();

		this.linkTypeDatabase = ConfigurationDatabaseContext.getLinkTypeDatabase();
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
			LinkType linkType = new LinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LINKTYPE_ENTITY_CODE), 
						creatorId,
						0L,
						codename, 
						description, 
						name, 
						sort.value(), 
						manufacturer, 
						manufacturerCode, 
						imageId);
			linkType.changed = true;
			return linkType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("LinkType.createInstance | cannot generate identifier ", e);
		}
	}
	
	protected void fromTransferable(IDLEntity transferable, String codename,
			String description) throws CreateObjectException {
		LinkType_Transferable ltt = (LinkType_Transferable) transferable; 
		super.fromTransferable(ltt.header, new String(ltt.codename), new String(ltt.description));

		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = new Identifier(ltt.image_id);
		this.name = ltt.name;
		try {
			this.characteristics = new HashSet(ltt.characteristic_ids.length);
			for (int i = 0; i < ltt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(ltt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
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
												long version,
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
							version,
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
		super.changed = true;
		this.name = name;
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE;
	}

	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		super.changed = true;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(LinkTypeSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}
}
