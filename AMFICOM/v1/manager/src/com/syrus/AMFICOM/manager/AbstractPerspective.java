/*-
* $Id: AbstractPerspective.java,v 1.2 2005/11/07 15:24:19 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphSelectionModel;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractPerspective implements Perspective {
	
	protected static Map<String, AbstractBeanFactory> FACTORY_MAP = new HashMap<String, AbstractBeanFactory>();
	
	protected final ManagerMainFrame managerMainFrame;
	
	public AbstractPerspective(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}

	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory) {
		return this.createAction(factory, null);
	}
	
	protected final AbstractAction createGetTheSameOrCreateNewAction(final AbstractBeanFactory<?> factory,
				final Chechable chechable,
				final DefaultGraphCell parentCell) {
   		final String name = factory.getName();
   		final BeanUI beanUI = this.managerMainFrame.getManagerHandler().getBeanUI(factory.getCodename());
   		Icon icon = beanUI.getIcon(factory);
   		FACTORY_MAP.put(factory.getCodename(), factory);
   		
   		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
   		final Perspective perspective = this;
   		final JGraph graph = this.managerMainFrame.getGraph();
   		
   		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
   			
   			public void actionPerformed(final ActionEvent e) {
   				
   				try {
   					
   					for(final AbstractBean abstractBean : graphRoutines.getLayoutBeans()) {
						if (chechable.isNeedIn(abstractBean)) {
							final GraphSelectionModel selectionModel = graph.getSelectionModel();
							selectionModel.setSelectionCell(graphRoutines.getDefaultGraphCell(abstractBean));
							return;
						}
   					}
   					
   					final AbstractBean bean = factory.createBean(perspective);
   					
   					graphRoutines.createChild(parentCell, 
   						factory.getShortName(), 
   						bean, 
   						20, 
   						20, 
   						0, 
   						0, 
   						beanUI.getImage(bean));
   				} catch (final ApplicationException ae) {
   					ae.printStackTrace();
   					JOptionPane.showMessageDialog(graph, 
   						ae.getMessage(), 
   						I18N.getString("Manager.Error"),
   						JOptionPane.ERROR_MESSAGE);
   				}				
   			}
   		};		
   	
   		action.putValue(Action.SHORT_DESCRIPTION, name);
   		return action;
   	}
	
	@SuppressWarnings("unchecked")
	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory,
			final DefaultGraphCell parentCell) {
		final String name = factory.getName();
		final BeanUI beanUI = this.managerMainFrame.getManagerHandler().getBeanUI(factory.getCodename());
		Icon icon = beanUI.getIcon(factory);
		FACTORY_MAP.put(factory.getCodename(), factory);
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			
			@SuppressWarnings("unqualified-field-access")
			public void actionPerformed(final ActionEvent e) {
				try {
					final AbstractBean bean = factory.createBean(managerMainFrame.getPerspective());
					managerMainFrame.getGraphRoutines().createChild(parentCell, 
						factory.getShortName() + "-" + factory.getCount(), 
						bean, 
						20, 
						20, 
						0, 
						0, 
						beanUI.getImage(bean));
				} catch (final ApplicationException ae) {
					ae.printStackTrace();
					JOptionPane.showMessageDialog(managerMainFrame.getGraph(), 
						ae.getMessage(), 
						I18N.getString("Manager.Error"),
						JOptionPane.ERROR_MESSAGE);
				}				
			}
		};		
	
		action.putValue(Action.SHORT_DESCRIPTION, name);
		return action;
	}
	
	protected abstract String getIdentifierString(final Identifier memberId);
	
	protected final LayoutItem getLayoutItem(final Identifier memberId,
		final Identifier networkLayoutItemId,
		final Map<Identifier, LayoutItem> existsMemberLayoutItems) 
	throws ApplicationException {
		
		if (existsMemberLayoutItems.containsKey(memberId)) {
			return existsMemberLayoutItems.get(memberId);
		}
		
		final String identifierString = this.getIdentifierString(memberId);
		
		assert Log.debugMessage("create item for " 
				+ memberId 
				+ " as " 
				+ identifierString , 
			Log.DEBUGLEVEL10);
		
		final LayoutItem layoutItem = 
		LayoutItem.createInstance(LoginManager.getUserId(), 
			networkLayoutItemId, 
			this.getCodename(), 
			identifierString);
	
		assert Log.debugMessage("created "
				+ layoutItem.getName()
				+ "@"
				+ layoutItem.getLayoutName()
				+ " -> "
				+ layoutItem.getParentId(),
				Log.DEBUGLEVEL10);
		
		existsMemberLayoutItems.put(memberId, layoutItem);
		
		return layoutItem;
	}
	
	protected final void addItems(final Set<? extends Identifiable> storableObjects,
			final Map<Identifier, LayoutItem> existsNetworkLayoutItems,
			final Set<LayoutItem> domainLayoutItems) {
		for (final Identifiable identifiable : storableObjects) {
			final Identifier id = identifiable.getId();
			final String identifierString = 
				this.getIdentifierString(id);
			for (final LayoutItem item : domainLayoutItems) {
				if (item.getName().equals(identifierString)) {
					existsNetworkLayoutItems.put(id, item);
					break;
				}
			}
		}
	}

	protected interface Chechable {
		boolean isNeedIn(final AbstractBean bean);
	}
	
}

