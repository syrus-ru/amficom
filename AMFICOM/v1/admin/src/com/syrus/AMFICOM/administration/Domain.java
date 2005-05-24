/*
 * $Id: Domain.java,v 1.29 2005/05/24 13:24:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

/**
 * @version $Revision: 1.29 $, $Date: 2005/05/24 13:24:57 $
 * @author $Author: bass $
 * @module administration_v1
 */

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

public class Domain extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 6401785674412391641L;

	private String name;
	private String description;

	private Set characteristics;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Domain(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);	

		this.characteristics = new HashSet();

		DomainDatabase database = (DomainDatabase) DatabaseContext.getDatabase(ObjectEntities.DOMAIN_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Domain(Domain_Transferable dt) throws CreateObjectException {
		try {
			this.fromTransferable(dt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Domain(Identifier id,
					 Identifier creatorId,
					 long version,
					 Identifier domainId,
					 String name,
					 String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;

		this.characteristics = new HashSet();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Domain_Transferable dt = (Domain_Transferable)transferable;
		super.fromTransferable(dt.header, (dt.domain_id.identifier_string.length() != 0) ? (new Identifier(dt.domain_id)) : null);
		this.name = dt.name;
		this.description = dt.description;

		Set characteristicIds = Identifier.fromTransferables(dt.characteristic_ids);
		this.characteristics = new HashSet(dt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid(): ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new Domain_Transferable(super.getHeaderTransferable(),
				(super.domainId != null) ? (Identifier_Transferable) super.domainId.getTransferable() : (new Identifier_Transferable("")),
				this.name,
				this.description,
				Identifier.createTransferables(this.characteristics));
	}	
	
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.description != null
				&& this.characteristics != null
				&& this.characteristics != Collections.EMPTY_SET;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	
	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}
	
	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public void setCharacteristics0(Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @throws CreateObjectException
	 */
	public static Domain createInstance(Identifier creatorId, Identifier domainId, String name, String description)
			throws CreateObjectException {
		try {
			Domain domain = new Domain(IdentifierPool.getGeneratedIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description);

			assert domain.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			domain.changed = true;
			return domain;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												Identifier domainId,
												String name,
												String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
	}
	
	/**
	 *
	 * @param domain
	 * @return true if this is child of domain, false otherwise
	 */
	public boolean isChild(Domain domain) {
		/**
		 * calculate parent tree
		 */
		return this.id.equals(domain.getId());
	}	
	
	public Identifier getParentDomainId(){
		return super.domainId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN;
	}
}
