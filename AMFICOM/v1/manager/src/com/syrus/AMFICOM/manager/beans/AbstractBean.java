/*-
 * $Id: AbstractBean.java,v 1.5.2.1 2006/05/18 17:47:01 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;

/**
 * @version $Revision: 1.5.2.1 $, $Date: 2006/05/18 17:47:01 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBean {

	protected Identifier	identifier;

	protected String		id;
	
	protected ManagerMainFrame	managerMainFrame;
	
	protected AbstractBean() {
		// nothing
	}

	protected AbstractBean(final Identifier id) {
		this.identifier = id;
	}

	public final Identifier getIdentifier() {
		return this.identifier;
	}

	public abstract void applyTargetPort(final MPort oldPort,
	                                     final MPort newPort) 
	throws ApplicationException;
	
	public abstract void dispose() throws ApplicationException;
	
	public final void disposeLayoutItem() throws ApplicationException {
		final CompoundCondition compoundCondition = 
			new CompoundCondition(new TypicalCondition(
				this.getId(), 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				StorableObjectWrapper.COLUMN_NAME),
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
			StorableObjectPool.delete(layoutItem.getCharacteristics());
		}
		
		StorableObjectPool.delete(layoutItems);
	}

	@SuppressWarnings("unused")
	protected void setIdentifier(final Identifier id) 
	throws ApplicationException {
		this.identifier = id;
	}

	public final String getId() {
		return this.id;
	}
	
	public final void setId(final String codeName) {
		this.id = codeName;
	}
	
	public abstract String getName();
	
	public abstract String getCodename();
	
	public abstract boolean isEditable();
	
	public abstract boolean isDeletable();
	
//	public boolean isEditable() {
//		return true;
//	}
//	
//	public  boolean isDeletable() {
//		return true;
//	}
	
	public abstract void setName(final String name);

	@Override
	public String toString() {
		return this.getClass().getName() + " is " + this.id + '/' + this.getName() + '/';
	}
	
	public final void setManagerMainFrame(final ManagerMainFrame graphText) {
		this.managerMainFrame = graphText;
	}
	
	protected final LayoutItem getLayoutItem(final String layoutName,
	                                         final String codename) throws ApplicationException {
		final CompoundCondition compoundCondition = 
			new CompoundCondition(new TypicalCondition(
				layoutName, 
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
		
		compoundCondition.addCondition(new TypicalCondition(
			codename, 
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.LAYOUT_ITEM_CODE,
			StorableObjectWrapper.COLUMN_NAME));
		
		final Set<LayoutItem> layoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		
		if (layoutItems.isEmpty()) {
			return null;
		}
		return layoutItems.iterator().next();
	}

}
