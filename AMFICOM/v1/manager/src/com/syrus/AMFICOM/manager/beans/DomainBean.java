/*-
 * $Id: DomainBean.java,v 1.3 2005/12/08 16:05:17 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_NAME;

import java.beans.PropertyChangeEvent;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/08 16:05:17 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainBean extends Bean {
	
	private Domain domain;
	
	@Override
	protected void setIdentifier(Identifier storableObject) throws ApplicationException {
		super.setIdentifier(storableObject);
		this.domain = StorableObjectPool.getStorableObject(this.identifier, true);
	}
	
	public final String getDescription() {
		return this.domain.getDescription();
	}

	@Override
	public final String getName() {
		return this.domain.getName();
	}
	
	public final void setDescription(String description) {
		String description2 = this.domain.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.domain.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final void setName(final String name) {
		String name2 = this.domain.getName().intern();
		if (name2 != name.intern() ) {
			assert Log.debugMessage("was:" + name2 + ", now:" + name, Log.DEBUGLEVEL09);
			this.domain.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		Identifier parentId = Identifier.VOID_IDENTIFIER;
		if (newPort != null) {
			parentId = ((DomainBean) newPort.getUserObject()).getIdentifier();
		}		
		assert Log.debugMessage(this.domain.getId() 
				+ ", set parent " 
				+ parentId,
			Log.DEBUGLEVEL10); 
		this.domain.setDomainId(parentId);
	}	

	@Override
	public void dispose() throws ApplicationException {
		assert Log.debugMessage("DomainBean.dispose | " + this.identifier, Log.DEBUGLEVEL03);
		
		final CompoundCondition compoundCondition = 
			new CompoundCondition(new TypicalCondition(
				this.getId(), 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				LayoutItemWrapper.COLUMN_LAYOUT_NAME),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(
					LoginManager.getUserId(),
					ObjectEntities.LAYOUT_ITEM_CODE) {
					@Override
					public boolean isNeedMore(Set< ? extends Identifiable> storableObjects) {
						return storableObjects.isEmpty();
					}
				});
		
		final Set<LayoutItem> layoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);	
		
		for(final LayoutItem layoutItem : layoutItems) {
			StorableObjectPool.delete(layoutItem.getCharacteristics(false));
		}
		
		StorableObjectPool.delete(layoutItems);
		
		this.disposeDomainMember(ObjectEntities.KIS_CODE);
		this.disposeDomainMember(ObjectEntities.SERVER_CODE);
		this.disposeDomainMember(ObjectEntities.MCM_CODE);		
		this.disposeUsers();
		
		StorableObjectPool.delete(this.identifier);		
		
		super.disposeLayoutItem();
	}
	
	@SuppressWarnings("unchecked")
	private void disposeDomainMember(final short domainMemberCode) 
	throws ApplicationException {
		final Set<? extends DomainMember> kiss = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.identifier, domainMemberCode), 
				true);
		for (final DomainMember member : kiss) {
			if (member.getDomainId().equals(this.identifier)) {
				StorableObjectPool.delete(member.getId());
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void disposeUsers() throws ApplicationException {
		final Set<PermissionAttributes> permissionAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.identifier, ObjectEntities.PERMATTR_CODE), 
				true);
		for (final PermissionAttributes attributes : permissionAttributes) {
			if (attributes.getDomainId().equals(this.identifier)) {
				StorableObjectPool.delete(attributes.getParentId());
				StorableObjectPool.delete(attributes.getId());
			}
		}
		
	}
	
	@Override
	public final String getCodename() {
		return ObjectEntities.DOMAIN;
	}
	
	public final Domain getDomain() {
		return this.domain;
	}
}
