/*
 * $Id: MCM.java,v 1.17 2005/04/08 12:02:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.17 $, $Date: 2005/04/08 12:02:07 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class MCM extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 4622885259080741046L;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;
	private Identifier serverId;

	private Set characteristics;

	public MCM(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MCM(MCM_Transferable mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}		
	}

	protected MCM(Identifier id,
					Identifier creatorId,
					long version,
					Identifier domainId,
					String name,
					String description,
					String hostname,
					Identifier userId,
					Identifier serverId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
		this.serverId = serverId;

		this.characteristics = new HashSet();
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MCM_Transferable mt = (MCM_Transferable)transferable;
		super.fromTransferable(mt.header, new Identifier(mt.domain_id));
		this.name = mt.name;
		this.description = mt.description;
		this.hostname = mt.hostname;
		this.userId = new Identifier(mt.user_id);
		this.serverId = new Identifier(mt.server_id);

		Set characteristicIds = Identifier.fromTransferables(mt.characteristic_ids);
		this.characteristics = GeneralStorableObjectPool.getStorableObjects(characteristicIds, true);
	}
	
	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator it = this.characteristics.iterator(); it.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)it.next()).getId().getTransferable();

		return new MCM_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.domainId.getTransferable(),
									this.name,
									this.description,
									this.hostname,
									(Identifier_Transferable)this.userId.getTransferable(),
									(Identifier_Transferable)this.serverId.getTransferable(),
									charIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getHostName() {
		return this.hostname;
	}

	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getServerId() {
		return this.serverId;
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

	public static MCM createInstance(Identifier creatorId,
									 Identifier domainId,
									 String name,
									 String description,
									 String hostname,
									 Identifier userId,
									 Identifier serverId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || hostname == null || 
				userId == null || serverId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			MCM mcm = new MCM(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MCM_ENTITY_CODE),
								creatorId,
								0L,
								domainId,
								name,
								description,
								hostname,
								userId,
								serverId);
			mcm.changed = true;
			return mcm;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MCM.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  Identifier domainId,
											  String name,
											  String description,
											  String hostname,
											  Identifier userId,
											  Identifier serverId) {
		super.setAttributes(created,												
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
		this.serverId = serverId;
	}
	
	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.userId);
		dependencies.add(this.serverId);
		return dependencies;
	}
	
	public void setHostName(String hostname) {
		this.hostname = hostname;
		super.changed = true;
	}
	
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	
	public void setServerId(Identifier serverId) {
		this.serverId = serverId;
		super.changed = true;
	}
	
	public void setUserId(Identifier userId) {
		this.userId = userId;
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_MCM;
	}
}
