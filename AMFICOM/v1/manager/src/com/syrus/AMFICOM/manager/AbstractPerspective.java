/*-
* $Id: AbstractPerspective.java,v 1.3 2005/11/09 15:09:48 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/11/09 15:09:48 $
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

	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory) 
	throws ApplicationException {
		return this.createAction(factory, null);
	}
	
	protected final AbstractAction createGetTheSameOrCreateNewAction(final AbstractBeanFactory<?> factory,
				final Chechable chechable,
				final DefaultGraphCell parentCell) 
	throws ApplicationException {
   		final String name = factory.getName();
   		final BeanUI beanUI = this.getBeanUI(factory.getCodename());
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
	
	protected final AbstractAction createGetTheSameOrCreateNewAction(final AbstractBeanFactory<?> factory,
 				final Chechable chechable,
 				final DefaultGraphCell parentCell,
 				final String codename) 
 	throws ApplicationException {
    		final String name = factory.getName();
    		final BeanUI beanUI = this.getBeanUI(factory.getCodename());
    		Icon icon = beanUI.getIcon(factory);
    		FACTORY_MAP.put(factory.getCodename(), factory);
    		
    		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
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
    					
    					final AbstractBean bean = factory.createBean(codename);
    					
    					graphRoutines.createChild(parentCell, 
    						bean.getName(), 
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
			final DefaultGraphCell parentCell) 
	throws ApplicationException {
		final String name = factory.getName();
		final BeanUI beanUI = this.getBeanUI(factory.getCodename());
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
	
	protected final String getIdentifierString(final Identifier memberId) {
		return memberId.getIdentifierString();
	}
	
	protected final LayoutItem getLayoutItem(final Identifier memberId,
		final Identifier parentLayoutItemId,
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
			parentLayoutItemId, 
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

	private final String getNamePrefix(final String codename) {
		final int index = codename.indexOf('_');
		return index >= 0 ? codename.substring(0, index) : codename;
	}
	
	protected final PerspectiveData getPerspectiveData(){
		final ManagerHandler managerHandler = this.managerMainFrame.getManagerHandler();
		return managerHandler.getPerspectiveData(this.getNamePrefix(this.getCodename()));
	}
	
	public AbstractBean createBean(final String codename) 
	throws ApplicationException {
		assert Log.debugMessage("codename is " + codename , Log.DEBUGLEVEL10);
		PerspectiveData perspectiveData = this.getPerspectiveData();
		final String namePrefix = this.getNamePrefix(codename);
		assert Log.debugMessage("namePrefix:" + namePrefix , Log.DEBUGLEVEL10);
		final AbstractBeanFactory beanFactory = perspectiveData.getBeanFactory(namePrefix);
		if (beanFactory != null) {
			return beanFactory.createBean(codename);
		}
		throw new IllegalArgumentException(codename + " is not support by perspective " + codename);
	}
	
	public BeanUI getBeanUI(final String codename) throws ApplicationException {		
		assert Log.debugMessage("codename is " + codename , Log.DEBUGLEVEL10);
		PerspectiveData perspectiveData = this.getPerspectiveData();
		final BeanUI beanUI = perspectiveData.getBeanUI(this.getNamePrefix(codename));
		if (beanUI != null) {
			return beanUI;
		}
		throw new IllegalArgumentException(codename + " is not support by perspective " + codename);		
	}
	
	public boolean isDeletable(final AbstractBean abstractBean) {
		final PerspectiveData perspectiveData = this.getPerspectiveData();
		final String codename = abstractBean.getCodename();
		return !perspectiveData.isUndeletable(codename);
	}
	
	protected interface Chechable {
		boolean isNeedIn(final AbstractBean bean);
	}
	
}

