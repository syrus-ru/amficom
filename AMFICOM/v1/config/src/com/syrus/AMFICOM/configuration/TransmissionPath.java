/*
 * $Id: TransmissionPath.java,v 1.43 2005/01/26 15:09:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
/**
 * @version $Revision: 1.43 $, $Date: 2005/01/26 15:09:22 $
 * @author $Author: bob $
 * @module config_v1
 */

public class TransmissionPath extends MonitoredDomainMember implements Characterized, TypedObject {

	private static final long serialVersionUID = 8129503678304843903L;

	private TransmissionPathType type;
	private String name;
	private String description;
	private Identifier startPortId;
	private Identifier finishPortId;

	private List characteristics;

	private StorableObjectDatabase transmissionPathDatabase;

	public TransmissionPath(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new LinkedList();
		this.monitoredElementIds = new LinkedList();
		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPath(TransmissionPath_Transferable tpt) throws CreateObjectException {
		super(tpt.header,
				new Identifier(tpt.domain_id));
		super.monitoredElementIds = new ArrayList(tpt.monitored_element_ids.length);
		for (int i = 0; i < tpt.monitored_element_ids.length; i++)
			super.monitoredElementIds.add(new Identifier(tpt.monitored_element_ids[i]));

		this.name = new String(tpt.name);
		this.description = new String(tpt.description);
		this.startPortId = new Identifier(tpt.start_port_id);
		this.finishPortId = new Identifier(tpt.finish_port_id);

		try {
			this.characteristics = new ArrayList(tpt.characteristic_ids.length);
			for (int i = 0; i < tpt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(tpt.characteristic_ids[i]), true));

			this.type = (TransmissionPathType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(tpt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
	}

	protected TransmissionPath(Identifier id,
								Identifier creatorId,
								Identifier domainId,
								String name,
								String description,
								TransmissionPathType type,
								Identifier startPortId,
								Identifier finishPortId){
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;

		this.characteristics = new LinkedList();
		super.monitoredElementIds = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param startPortId
	 * @param finishPortId
	 * @throws CreateObjectException
	 */
	public static TransmissionPath createInstance(Identifier creatorId,
																		Identifier domainId,
																		String name,
																		String description,
																		TransmissionPathType type,
																		Identifier startPortId,
																		Identifier finishPortId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null ||
				type == null || startPortId == null || finishPortId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new TransmissionPath(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATH_ENTITY_CODE),
											creatorId,
											domainId,
											name,
											description,
											type,
											startPortId,
											finishPortId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TransmissionPath.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.transmissionPathDatabase != null)
				this.transmissionPathDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();


		return new TransmissionPath_Transferable(super.getHeaderTransferable(),
												 (Identifier_Transferable)this.getDomainId().getTransferable(),
												 meIds,
												 new String(this.name),
												 new String(this.description),
												 (Identifier_Transferable)this.type.getId().getTransferable(),
												 (Identifier_Transferable)this.startPortId.getTransferable(),
												 (Identifier_Transferable)this.finishPortId.getTransferable(),
												 charIds);
	}

	public String getName() {
		return this.name;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getFinishPortId() {
		return this.finishPortId;
	}

	public Identifier getStartPortId() {
		return this.startPortId;
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												Identifier domainId,
												String name,
												String description,
												TransmissionPathType type,
												Identifier startPortId,
												Identifier finishPortId) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.startPortId);
		dependencies.add(this.finishPortId);
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
	
	/**
	 * @param finishPortId The finishPortId to set.
	 */
	public void setFinishPortId(Identifier finishPortId) {
		this.finishPortId = finishPortId;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param startPortId The startPortId to set.
	 */
	public void setStartPortId(Identifier startPortId) {
		this.startPortId = startPortId;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(TransmissionPathType type) {
		this.type = type;
		super.currentVersion = super.getNextVersion();
	}
}
