/*
 * $Id: LinkedIdsCondition.java,v 1.7 2004/12/07 10:47:23 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/07 10:47:23 $
 * @author $Author: bass $
 * @module measurement_v1
 */
public class LinkedIdsCondition implements StorableObjectCondition {

	protected Domain				domain;

	protected static final Short		CHARACTERISTIC_SHORT		= new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);

	protected Short				entityCode;

	protected List				linkedIds;
	protected Identifier			identifier;	

	public LinkedIdsCondition(LinkedIdsCondition_Transferable transferable) throws DatabaseException,
			CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool
				.getStorableObject(new Identifier(transferable.domain_id), true);
		if (transferable.linked_ids.length == 1) {
			this.identifier = new Identifier(transferable.linked_ids[0]);
		} else {
			this.linkedIds = new ArrayList(transferable.linked_ids.length);
			for (int i = 0; i < transferable.linked_ids.length; i++) {
				this.linkedIds.add(new Identifier(transferable.linked_ids[i]));
			}
		}

		setEntityCode(transferable.entity_code);
	}

	public LinkedIdsCondition(List linkedIds, short entityCode) {
		this(linkedIds, new Short(entityCode));
	}

	public LinkedIdsCondition(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	public LinkedIdsCondition(Identifier identifier, Short entityCode) {
		this.identifier = identifier;
		this.entityCode = entityCode;
	}

	public LinkedIdsCondition(Identifier identifier, short entityCode) {
		this(identifier, new Short(entityCode));
	}

	public Domain getDomain() {
		return this.domain;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode}is {@link Characteristic}for all
	 *         characteristics for StorableObject identifier which can have characteristics in identifier;</li>
	 *         </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				if (object instanceof Characteristic) {
					Characteristic characteristic = (Characteristic) object;
					Identifier id = characteristic.getCharacterizedId();
					if (this.linkedIds == null) {
						Identifier characterizedId = this.identifier;
						if (characterizedId.equals(id)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier characterizedId = (Identifier) it.next();
							if (characterizedId.equals(id)) {
								condition = true;
								break;
							}
						}
					}
				}
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown for this condition");
		}

		return condition;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setEntityCode(short entityCode) {
		switch (entityCode) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				this.entityCode = CHARACTERISTIC_SHORT;
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown for this condition");
		}

	}

	public void setEntityCode(Short entityCode) {
		this.setEntityCode(entityCode.shortValue());
	}

	public void setLinkedIds(List linkedIds) {
		this.linkedIds = linkedIds;
		this.identifier = null;
	}

	public Object getTransferable() {

		if (this.entityCode == null) { throw new UnsupportedOperationException("entityCode doesn't set"); }

		short s = this.entityCode.shortValue();
		switch (s) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown");
		}

		LinkedIdsCondition_Transferable transferable = new LinkedIdsCondition_Transferable();
		Identifier_Transferable[] linkedIdTransferable;
		if (this.linkedIds != null) {
			linkedIdTransferable = new Identifier_Transferable[this.linkedIds.size()];
			int i = 0;

			for (Iterator it = this.linkedIds.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				linkedIdTransferable[i] = (Identifier_Transferable) id.getTransferable();
			}
		} else {
			linkedIdTransferable = new Identifier_Transferable[1];
			linkedIdTransferable[0] = (Identifier_Transferable) this.identifier.getTransferable();
		}

		transferable.domain_id = (Identifier_Transferable) this.domain.getId().getTransferable();
		transferable.linked_ids = linkedIdTransferable;
		transferable.entity_code = s;

		return transferable;
	}
	
	public boolean isNeedMore(List list) {		
		return true;
	}	

	public List getLinkedIds() {
		return this.linkedIds;
	}
	
	public Identifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
		this.linkedIds = null;
	}
}
