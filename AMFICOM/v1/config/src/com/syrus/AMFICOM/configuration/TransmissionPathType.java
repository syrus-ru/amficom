/*
 * $Id: TransmissionPathType.java,v 1.25 2005/01/25 12:13:08 bob Exp $
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

import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.25 $, $Date: 2005/01/25 12:13:08 $
 * @author $Author: bob $
 * @module config_v1
 */

public class TransmissionPathType extends StorableObjectType implements Characterized {

	private static final long serialVersionUID = 5311725679846973948L;

	public static final String COLUMN_NAME = "name";

	private String name;

	private List characteristics;

	private StorableObjectDatabase transmissionPathTypeDatabase;

	public TransmissionPathType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new LinkedList();
		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		try {
			this.transmissionPathTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPathType(TransmissionPathType_Transferable tptt) throws CreateObjectException {
		super(tptt.header,
				new String(tptt.codename),
				new String(tptt.description));
		this.name = tptt.name;
		try {
			this.characteristics = new ArrayList(tptt.characteristic_ids.length);
			for (int i = 0; i < tptt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(tptt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
	}

	protected TransmissionPathType(Identifier id,
								Identifier creatorId,
								String codename,
								String description,
								String name) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					codename,
					description);
		this.name = name;
		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.transmissionPathTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static TransmissionPathType createInstance(Identifier creatorId,
																				String codename,
																				String description,
																				String name) throws CreateObjectException {
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new TransmissionPathType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE),
											creatorId,
											codename,
											description,
											name);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TransmissionPathType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.transmissionPathTypeDatabase != null)
				this.transmissionPathTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
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

		return new TransmissionPathType_Transferable(super.getHeaderTransferable(),
										new String(super.codename),
										(super.description != null) ? (new String(super.description)) : "",
										(this.name != null) ? (new String(this.name)) : "",
										charIds);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
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
		return this.characteristics;
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
}
