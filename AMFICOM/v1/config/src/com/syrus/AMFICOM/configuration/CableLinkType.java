/*
 * $Id: CableLinkType.java,v 1.22 2005/03/04 13:32:12 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/03/04 13:32:12 $
 * @author $Author: bass $
 * @module config_v1
 */
public class CableLinkType extends AbstractLinkType implements Characterizable {

	private static final long   serialVersionUID    = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	private List cableThreadTypes;

	private List characteristics;

	private StorableObjectDatabase cableLinkTypeDatabase;

	public CableLinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new ArrayList();
		this.cableThreadTypes = new ArrayList();

		this.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;
		try {
			this.cableLinkTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
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
			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));

			this.cableThreadTypes = new ArrayList(cltt.cableThreadTypeIds.length);
			List cableThreadTypeIds = new ArrayList(cltt.cableThreadTypeIds.length);
			for (int i = 0; i < cltt.cableThreadTypeIds.length; i++)
				cableThreadTypeIds.add(new Identifier(cltt.cableThreadTypeIds[i]));
			this.cableThreadTypes.addAll(ConfigurationStorableObjectPool.getStorableObjects(cableThreadTypeIds, true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;
	}

	protected CableLinkType(Identifier id,
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
		this.characteristics = new ArrayList();
		this.cableThreadTypes = new ArrayList();

		this.cableLinkTypeDatabase = ConfigurationDatabaseContext.cableLinkTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableLinkType createInstance(Identifier creatorId,
			String codename,
			String description,
			String name,
			LinkTypeSort sort,
			String manufacturer,
			String manufacturerCode,
			Identifier imageId) throws CreateObjectException {
		if (creatorId == null
				|| codename == null
				|| description == null
				|| name == null
				|| sort == null
				|| manufacturer == null
				|| manufacturerCode == null
				|| imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			CableLinkType cableLinkType = new CableLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLELINKTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					name,
					sort.value(),
					manufacturer,
					manufacturerCode,
					imageId);
			cableLinkType.changed = true;
			return cableLinkType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableLinkType.createInstance | cannot generate identifier ", e);
		}
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
			super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
			this.name = name;
			this.sort = sort;
			this.manufacturer = manufacturer;
			this.manufacturerCode = manufacturerCode;
			this.imageId = imageId;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		super.changed = true;
	}

	
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		super.changed = true;
		this.manufacturer = manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		super.changed = true;
		this.manufacturerCode = manufacturerCode;
	}

	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}
	
	public void setSort(LinkTypeSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name){
		super.changed = true;
		this.name = name;
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}

	public List getCableThreadTypes() {
		return Collections.unmodifiableList(this.cableThreadTypes);
	}

	protected void setCableThreadTypes0(final Collection cableThreadTypes) {
		this.cableThreadTypes.clear();
		if (cableThreadTypes != null)
			this.cableThreadTypes.addAll(cableThreadTypes);
	}

	public void setCableThreadTypes(Collection cableThreadTypes) {
		this.setCableThreadTypes0(cableThreadTypes);
		super.changed = true;
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
		super.changed = true;
	}
	
}
