/*
 * $Id: CableLinkType.java,v 1.34 2005/04/15 19:22:12 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.34 $, $Date: 2005/04/15 19:22:12 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public final class CableLinkType extends AbstractLinkType implements Characterizable {

	private static final long   serialVersionUID    = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	private Set characteristics;

	public CableLinkType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableLinkType(CableLinkType_Transferable cltt) throws CreateObjectException {
		try {
			this.fromTransferable(cltt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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

		this.characteristics = new HashSet();
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
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		CableLinkType_Transferable cltt = (CableLinkType_Transferable) transferable;
		super.fromTransferable(cltt.header, cltt.codename, cltt.description);
		this.sort = cltt.sort.value();
		this.manufacturer = cltt.manufacturer;
		this.manufacturerCode = cltt.manufacturerCode;
		this.imageId = new Identifier(cltt.image_id);
		this.name = cltt.name;

		Set characteristicIds = Identifier.fromTransferables(cltt.characteristic_ids);
		this.characteristics = new HashSet(cltt.characteristic_ids.length);
		this.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
	}
	
	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new CableLinkType_Transferable(super.getHeaderTransferable(),
																		 super.codename,
																		 super.description != null ? super.description : "",
																		 this.name != null ? this.name : "",
																		 LinkTypeSort.from_int(this.sort),
																		 this.manufacturer,
																		 this.manufacturerCode,
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

	public void setName(String name) {
		super.changed = true;
		this.name = name;
	}

	public Set getCableThreadTypes() {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);
		Set cableThreadTypes;
		try {
			cableThreadTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(lic, true);
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
			cableThreadTypes = Collections.EMPTY_SET;
		}
		return cableThreadTypes;
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
		return CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE;
	}
}
