/*-
 * $Id: AbstractBean.java,v 1.16 2005/09/04 11:31:23 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultEdge;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;

/**
 * @version $Revision: 1.16 $, $Date: 2005/09/04 11:31:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBean {

	protected Identifier	id;
	
	protected Validator		validator;

	protected String		codeName;

	protected JPanel		propertyPanel;

	protected ManagerMainFrame	graphText;
	
	protected AbstractBean() {
		// nothing
	}

	protected AbstractBean(final Identifier id,
			final Validator validator,
			final JPanel propertyPanel) {
		this.id = id;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
	}

	public final Identifier getId() {
		return this.id;
	}

	public boolean isTargetValid(AbstractBean targetBean) {
		return this.validator.isValid(this, targetBean);
	}
	
	public abstract void applyTargetPort(MPort oldPort, MPort newPort); 

	public JPanel getPropertyPanel() {
		return this.propertyPanel;
	}

	public JPopupMenu getMenu(final Object cell) {
		return null;
	}

	public void updateEdgeAttributes(	DefaultEdge edge,
										MPort port) {
		// nothing yet
	}

	protected final void setPropertyPanel(JPanel propertyPanel) {
		this.propertyPanel = propertyPanel;
	}

	protected void setId(Identifier id) {
		this.id = id;
	}

	protected final void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	public final String getCodeName() {
		return this.codeName;
	}
	
	public final void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	public abstract String getName();
	
	public abstract void setName(String name);

	@Override
	public String toString() {
		return this.getClass().getName() + " is " + this.codeName + '/' + this.getName() + '/';
	}
	
	public final void setGraphText(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	protected final LayoutItem getLayoutItem(final String layoutName,
	                                         final String codename) throws ApplicationException {
		CompoundCondition compoundCondition = 
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
					public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
						return storableObjects.isEmpty();
					}
				});
		
		compoundCondition.addCondition(new TypicalCondition(
			codename, 
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.LAYOUT_ITEM_CODE,
			StorableObjectWrapper.COLUMN_NAME));
		
		Set<LayoutItem> layoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		
		if (layoutItems.isEmpty()) {
			return null;
		}
		return layoutItems.iterator().next();
	}

}
