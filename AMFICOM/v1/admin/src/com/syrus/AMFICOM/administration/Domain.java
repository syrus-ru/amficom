/*
 * $Id: Domain.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;

public class Domain extends DomainMember implements Characterized {
	private static final long serialVersionUID = 6401785674412391641L;

	private String name;
	private String description;
	private List characteristics;

	private StorableObjectDatabase domainDatabase;

	public Domain(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);	
		
		this.characteristics = new LinkedList();
		this.domainDatabase = AdministrationDatabaseContext.domainDatabase;
		try {
			this.domainDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Domain(Domain_Transferable dt) throws CreateObjectException {
		super(dt.header,
			  (dt.domain_id.identifier_string.length() != 0) ? (new Identifier(dt.domain_id)) : null);
		this.name = new String(dt.name);
		this.description = new String(dt.description);

		try {
			this.characteristics = new ArrayList(dt.characteristic_ids.length);
			for (int i = 0; i < dt.characteristic_ids.length; i++)
				this.characteristics.add(AdministrationStorableObjectPool.getStorableObject(new Identifier(dt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.domainDatabase = AdministrationDatabaseContext.domainDatabase;
	}

	protected Domain(Identifier id,
								 Identifier creatorId,
								 Identifier domainId,
								 String name,
								 String description) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;

		this.characteristics = new ArrayList();

		super.currentVersion = super.getNextVersion();

		this.domainDatabase = AdministrationDatabaseContext.domainDatabase;
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Domain_Transferable(super.getHeaderTransferable(),
									   (super.domainId != null) ? (Identifier_Transferable)super.domainId.getTransferable() : (new Identifier_Transferable("")),
									   new String(this.name),
									   new String(this.description),
									   charIds);
	}	
	

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
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
		return this.characteristics;
	}

	protected void setCharacteristics0(List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
	     	this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(List characteristics) {
		this.setCharacteristics0(characteristics);
	    super.currentVersion = super.getNextVersion();
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
			return new Domain(IdentifierPool.getGeneratedIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE),
						creatorId,
						domainId,
						name,
						description);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Domain.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.domainDatabase != null)
				this.domainDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
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
	
	public List getDependencies() {
		return new LinkedList(this.characteristics);
	}
}
