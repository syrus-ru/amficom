/*
 * $Id: DomainCondition.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.List;

import com.syrus.AMFICOM.administration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
 * @author $Author: arseniy $
 * @module administration_v1
 */
public class DomainCondition implements StorableObjectCondition {
	private Domain domain;
	private Short entityCode;

	public DomainCondition(DomainCondition_Transferable dct) throws DatabaseException, CommunicationException {
		this.domain = (Domain) AdministrationStorableObjectPool.getStorableObject(new Identifier(dct.domain_id), true);
		this.entityCode = new Short(dct.entity_code);
	}

	public DomainCondition(Domain domain, Short entityCode) {
		this.domain = domain;
		this.entityCode = entityCode; 
	}

	public DomainCondition(Domain domain, short entityCode) {
		this.domain = domain;
		this.entityCode = new Short(entityCode);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if ((this.domain != null) && (object instanceof StorableObject)) {
			StorableObject storableObject = (StorableObject)object;
			switch (this.entityCode.shortValue()) {
				case ObjectEntities.USER_ENTITY_CODE:
					{
						User user = (User)storableObject;
						condition = true;
					}
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					Domain domain2 = (Domain)storableObject;
					if (domain2.isChild(this.domain))
						condition = true;
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					{
						Server server = (Server) storableObject;
						Domain serverDomain = (Domain)AdministrationStorableObjectPool.getStorableObject(server.getDomainId(), true);
						if (serverDomain.isChild(this.domain))
							condition = true;
					}
					break;
				case ObjectEntities.MCM_ENTITY_CODE:	
					{
						MCM mcm = (MCM)storableObject;
						Domain mcmDomain = (Domain)AdministrationStorableObjectPool.getStorableObject(mcm.getDomainId(), true);
						if (mcmDomain.isChild(this.domain))
							condition = true;
					}
					break;
//				case ObjectEntities.PERMATTR_ENTITY_CODE:
//					condition = true;
//					break;
			default:                        
				condition = true;
			}
		}

		return condition;
	}
	
	
	public boolean isNeedMore(List list) {
		return true;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public Domain getDomain() {
		return this.domain;
	}
	
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;
	}
	
	public Object getTransferable() {
		return new DomainCondition_Transferable(this.entityCode
						.shortValue(), (Identifier_Transferable) this.domain.getId().getTransferable());
	}

}
