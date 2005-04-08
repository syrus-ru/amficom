/*
 * $Id: Domain.java,v 1.17 2005/04/08 08:10:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

/**
 * @version $Revision: 1.17 $, $Date: 2005/04/08 08:10:41 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
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

public class Domain extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 6401785674412391641L;

	private String name;
	private String description;

	private Set characteristics;

	public Domain(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);	

		this.characteristics = new HashSet();

		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Domain(Domain_Transferable dt) throws CreateObjectException {
		this.fromTransferable(dt);
	}

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

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		Domain_Transferable dt = (Domain_Transferable)transferable;
		super.fromTransferable(dt.header, (dt.domain_id.identifier_string.length() != 0) ? (new Identifier(dt.domain_id)) : null);
		this.name = dt.name;
		this.description = dt.description;

		try {
			this.characteristics = new HashSet(dt.characteristic_ids.length);
			for (int i = 0; i < dt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(dt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

	}
	
	public IDLEntity getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Domain_Transferable(super.getHeaderTransferable(),
									   (super.domainId != null) ? (Identifier_Transferable)super.domainId.getTransferable() : (new Identifier_Transferable("")),
									   this.name,
									   this.description,
									   charIds);
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
	public static Domain createInstance(Identifier creatorId,
										Identifier domainId,
										String name,
										String description) throws CreateObjectException {
		if (creatorId == null || name == null || 
				description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			Domain domain = new Domain(IdentifierPool.getGeneratedIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE),
						creatorId,
						0L,
						domainId,
						name,
						description);
			domain.changed = true;
			return domain;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Domain.createInstance | cannot generate identifier ", e);
		}
	}

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
	
	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN;
	}
}
