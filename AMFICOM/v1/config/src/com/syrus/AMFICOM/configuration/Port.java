/*
 * $Id: Port.java,v 1.37 2005/01/25 12:13:08 bob Exp $
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

import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.37 $, $Date: 2005/01/25 12:13:08 $
 * @author $Author: bob $
 * @module config_v1
 */
public class Port extends StorableObject implements Characterized, TypedObject {
	private static final long serialVersionUID = -5139393638116159453L;

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_SORT = "sort";

	private PortType type;
	private String description;
	private Identifier equipmentId;
	private int sort;

	private List characteristics;

	private StorableObjectDatabase portDatabase;

	public Port(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new LinkedList();
		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
		try {
			this.portDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Port(Port_Transferable pt) throws CreateObjectException {
		super(pt.header);

		try {
			this.type = (PortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(pt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.description = new String(pt.description);
		this.equipmentId = new Identifier(pt.equipment_id);

		this.sort = pt.sort.value();

		try {
			this.characteristics = new ArrayList(pt.characteristic_ids.length);
			for (int i = 0; i < pt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(pt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
	}

	protected Port(Identifier id,
							 Identifier creatorId,
							 PortType type,
							 String description,
							 Identifier equipmentId,
							 int sort) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
		this.sort = sort;

		this.characteristics = new ArrayList();

		super.currentVersion = super.getNextVersion();

		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param equipmentId
	 * @param sort
	 * @throws CreateObjectException
	 */
	public static Port createInstance(Identifier creatorId,
										PortType type,
										String description,
										Identifier equipmentId,
										PortSort sort) throws CreateObjectException {
		if (creatorId == null || type == null || description == null ||
				type == null || equipmentId == null || sort == null )
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Port(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORT_ENTITY_CODE),
						creatorId,
						type,
						description,
						equipmentId,
						sort.value());
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Port.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.portDatabase != null)
				this.portDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
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

		return new Port_Transferable(super.getHeaderTransferable(),
									 (Identifier_Transferable)this.type.getId().getTransferable(),
									 new String(this.description),
									 (Identifier_Transferable)this.equipmentId.getTransferable(),
									 PortSort.from_int(this.sort),
									 charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public int getSort() {
		return this.sort;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
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
		super.currentVersion = super.getNextVersion();
	}

	protected synchronized void setAttributes(Date created,
																Date modified,
																Identifier creatorId,
																Identifier modifierId,
																PortType type,
																String description,
																Identifier equipmentId,
																int sort) {
		super.setAttributes(created,
						modified,
						creatorId,
						modifierId);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
		this.sort = sort;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.equipmentId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}	
}
