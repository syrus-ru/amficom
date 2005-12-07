/*-
 * $Id: AbstractBean.java,v 1.3 2005/12/07 15:43:50 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/12/07 15:43:50 $
 * @author $Author: bob $
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
		
//		Log.debugMessage("AbstractBean.disposeLayoutItem | delete: " + layoutItems, Log.DEBUGLEVEL10);
		
		for(final LayoutItem layoutItem : layoutItems) {
			StorableObjectPool.delete(layoutItem.getCharacteristics(false));
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
