/*
 * $Id: PortType.java,v 1.53 2005/05/24 13:25:05 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.53 $, $Date: 2005/05/24 13:25:05 $
 * @author $Author: bass $
 * @module config_v1
 */

public class PortType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = -115251480084275101L;

	private String name;
	private int sort;

	private Set characteristics;

	public PortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		PortTypeDatabase database = (PortTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PORTTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public PortType(PortType_Transferable ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PortType(Identifier id,
						 Identifier creatorId,
						 long version,
						 String codename,
						 String description,
						 String name,
						 int sort) {
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
		this.characteristics = new HashSet();
	}


	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static PortType createInstance(Identifier creatorId,
											String codename,
											String description,
											String name,
											PortTypeSort sort) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null ||
				sort == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PortType portType = new PortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORTTYPE_ENTITY_CODE),
								creatorId,
								0L,
								codename,
								description,
								name,
								sort.value());
			portType.changed = true;
			return portType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		PortType_Transferable ptt = (PortType_Transferable) transferable;
		super.fromTransferable(ptt.header, ptt.codename, ptt.description);
		this.name = ptt.name;
		this.sort = ptt.sort.value();

		Set characteristicIds = Identifier.fromTransferables(ptt.characteristic_ids);
		this.characteristics = StorableObjectPool.getStorableObjects(characteristicIds, true);
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
				charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new PortType_Transferable(super.getHeaderTransferable(),
									 super.codename,
									 super.description != null ? super.description : "",
									 this.name != null ? this.name : "",
									 PortTypeSort.from_int(this.sort),
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
												int sort) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.name = name;
				this.sort = sort;
	}

	public String getName(){
		return this.name;
	}

	public PortTypeSort getSort() {
			return PortTypeSort.from_int(this.sort);
	}

	public void setSort(PortTypeSort sort) {
		this.sort = sort.value();
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
		return CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE;
	}
}
