/*
 * $Id: Domain.java,v 1.22 2004/12/07 10:58:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.22 $, $Date: 2004/12/07 10:58:36 $
 * @author $Author: bass $
 * @module configuration_v1
 */

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;

public class Domain extends DomainMember implements Characterized {
	static final long serialVersionUID = 6401785674412391641L;

	private String name;
	private String description;
	private List characteristics;

	private StorableObjectDatabase domainDatabase;

	public Domain(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);	
		
		this.characteristics = new LinkedList();
		this.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
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
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(dt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
		
		this.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
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
	 */
	public static Domain createInstance(Identifier creatorId,
										Identifier domainId,
										String name,
										String description) {
		if (creatorId == null || name == null || 
				description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new Domain(IdentifierPool.generateId(ObjectEntities.DOMAIN_ENTITY_CODE),
					creatorId,
					domainId,
					name,
					description);
	}
	
	public static Domain getInstance(Domain_Transferable dt) throws CreateObjectException {
		Domain domain = new Domain(dt);
		
		domain.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
		try {
			if (domain.domainDatabase != null)
				domain.domainDatabase.insert(domain);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return domain;
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
	public boolean isChild(Domain domain){
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
