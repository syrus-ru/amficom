/*
 * $Id: PhysicalLinkType.java,v 1.5 2004/12/03 13:26:34 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/12/03 13:26:34 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkType extends StorableObjectType implements Characterized {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3690191057812271924L;

	private List					characteristics;

	private Identifier				domainId;

	private String					name;

	private StorableObjectDatabase	physicalLinkTypeDatabase;
	
	public PhysicalLinkType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			this.physicalLinkTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLinkType(PhysicalLinkType_Transferable pltt) throws CreateObjectException {
		super(pltt.header, pltt.codename, pltt.description);
		this.name = pltt.name;
		this.domainId = new Identifier(pltt.domain_id);

		try {
			this.characteristics = new ArrayList(pltt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(pltt.characteristicIds.length);
			for (int i = 0; i < pltt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(pltt.characteristicIds[i]));

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PhysicalLinkType(final Identifier id,
						   final Identifier creatorId,
						   final String codename,
						   final String name,
						   final String description,
						   final Identifier domainId) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		this.name = name;
		this.description = description;
		this.domainId = domainId;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
	}

	public static PhysicalLinkType getInstance(PhysicalLinkType_Transferable pntt) throws CreateObjectException {
		PhysicalLinkType physicalLinkType = new PhysicalLinkType(pntt);

		physicalLinkType.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			if (physicalLinkType.physicalLinkTypeDatabase != null)
				physicalLinkType.physicalLinkTypeDatabase.insert(physicalLinkType);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return physicalLinkType;
	}

	public List getCharacteristics() {
		return  Collections.unmodifiableList(this.characteristics);
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.domainId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public String getDescription() {
		return this.description;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public String getName() {
		return this.name;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		
		return new PhysicalLinkType_Transferable(super.getHeaderTransferable(), 
				(Identifier_Transferable)this.domainId.getTransferable(),
				this.codename,
				this.name,
				this.description,
				charIds);
	}

	public void setCharacteristics(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public void setDescription(final String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public void setDomainId(final Identifier domainId) {
		this.domainId = domainId;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(final String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  String codename,
											  Identifier domainId,
											  String name,
											  String description) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					codename,
					description);
			this.name = name;
			this.domainId = domainId;
		}
}
