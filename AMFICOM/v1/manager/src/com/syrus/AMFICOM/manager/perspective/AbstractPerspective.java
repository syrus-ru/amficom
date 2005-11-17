/*-
* $Id: AbstractPerspective.java,v 1.1 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
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
import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractPerspective implements Perspective {
	
	protected ManagerMainFrame managerMainFrame;

	protected PerspectiveData perspectiveData;
	
	protected Set<AbstractBean>	layoutBeans;
	
	protected Map<String, Perspective> perspectiveMap;
	
	protected Set<Perspective> perspectives;

	public final void setManagerMainFrame(ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	public final ManagerMainFrame getManagerMainFrame() {
		return this.managerMainFrame;
	}

	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory) 
	throws ApplicationException {
		return this.createAction(factory, null);
	}
	
	@SuppressWarnings("unchecked")
	protected final AbstractAction createGetTheSameOrCreateNewAction(final AbstractBeanFactory<?> factory,
				final Chechable chechable,
				final DefaultGraphCell parentCell) 
	throws ApplicationException {
   		final String name = factory.getName();
   		final String codename = factory.getCodename();
		final BeanUI beanUI = this.getBeanUI(codename);
   		Icon icon = beanUI.getIcon(factory);
   		
   		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
   		final Perspective perspective = this;
   		final JGraph graph = this.managerMainFrame.getGraph();
   		
   		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
   			
			public void actionPerformed(final ActionEvent e) {
   				
   				try {
   					
   					for(final AbstractBean abstractBean : getLayoutBeans()) {
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
	protected final AbstractAction createGetTheSameOrCreateNewAction(final AbstractBeanFactory<?> factory,
 				final Chechable chechable,
 				final DefaultGraphCell parentCell,
 				final String codename) 
 	throws ApplicationException {
    		final String name = factory.getName();
    		final BeanUI beanUI = this.getBeanUI(factory.getCodename());
    		Icon icon = beanUI.getIcon(factory);
    		
    		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
    		final JGraph graph = this.managerMainFrame.getGraph();
    		
    		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
    			
    			public void actionPerformed(final ActionEvent e) {
    				
    				try {
    					
    					for(final AbstractBean abstractBean : getLayoutBeans()) {
	    					assert Log.debugMessage(abstractBean, Log.DEBUGLEVEL03);
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
    					
    					assert Log.debugMessage("Create " + bean, Log.DEBUGLEVEL03);

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
			final Set<LayoutItem> layoutItems) {
		
		assert Log.debugMessage(storableObjects, Log.DEBUGLEVEL10);
		
		for (final Identifiable identifiable : storableObjects) {
			final Identifier id = identifiable.getId();
			final String identifierString = 
				this.getIdentifierString(id);
			for (final LayoutItem item : layoutItems) {
				assert Log.debugMessage(item.getName() + '@' + item.getLayoutName(), Log.DEBUGLEVEL10);
				if (item.getName().equals(identifierString)) {
					assert Log.debugMessage("add " + id + " as " + item , Log.DEBUGLEVEL10);
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
	
	public final void setPerspectiveData(final PerspectiveData perspectiveData) {
		this.perspectiveData = perspectiveData;
	}
	
	protected final void addSubPerspective(final Perspective perspective) 
	throws ApplicationException {
		if (this.perspectiveMap == null) {
			this.perspectiveMap = new HashMap<String, Perspective>();
			this.perspectives = new HashSet<Perspective>();
		}
		final String codename = perspective.getCodename();
		perspective.createNecessaryItems();
		assert Log.debugMessage("add "
				+ codename
				+ " to "
				+ this.getCodename(), 
				Log.DEBUGLEVEL10);
		this.perspectiveMap.put(codename, perspective);
		this.perspectives.add(perspective);
	}
	
	public final Perspective getPerspective(final String layoutName) {
		assert Log.debugMessage(this.getCodename() + ", layoutName " + layoutName, Log.DEBUGLEVEL10);
		if (this.getCodename().equals(layoutName)) {
			return this;
		}
		
		if (this.perspectiveMap == null) {
			return null;
		}
		
		final Perspective perspective = this.perspectiveMap.get(layoutName);
		if (perspective != null) {
			return perspective;
		}
		for (final String perspectiveCodename : this.perspectiveMap.keySet()) {
			assert Log.debugMessage(this.getCodename() 
					+ ", perspectiveCodename " 
					+ perspectiveCodename 
					+ ", required:"
					+ layoutName, 
				Log.DEBUGLEVEL10);
			final Perspective subPerspective = this.perspectiveMap.get(perspectiveCodename);
			
			final Perspective subSubPerspective = subPerspective.getPerspective(layoutName);
			if (subSubPerspective != null) {
				return subSubPerspective;
			}
		}
		return null;
	}
	
	public Set<Perspective> getPerspectives() {
		return this.perspectives;
	}
	
	public AbstractBean createBean(final String codename) 
	throws ApplicationException {
		assert Log.debugMessage("codename is " + codename , Log.DEBUGLEVEL10);
		final String namePrefix = this.getNamePrefix(codename);
		assert Log.debugMessage("namePrefix:" + namePrefix , Log.DEBUGLEVEL10);
		final AbstractBeanFactory beanFactory = this.perspectiveData.getBeanFactory(namePrefix);
		if (beanFactory != null) {
			return beanFactory.createBean(codename);
		}
		throw new IllegalArgumentException(codename 
			+ " is not support by perspective " 
			+ this.getCodename());
	}
	
	public BeanUI getBeanUI(final String codename) throws ApplicationException {		
		assert Log.debugMessage("codename is " + codename , Log.DEBUGLEVEL10);
		final BeanUI beanUI = this.perspectiveData.getBeanUI(this.getNamePrefix(codename));
		if (beanUI != null) {
			return beanUI;
		}
		throw new IllegalArgumentException(codename 
			+ " is not support by perspective " 
			+ this.getCodename());		
	}
	
	public AbstractItemPopupMenu getPopupMenu(final String codename) {
		final AbstractItemPopupMenu popupMenu = this.perspectiveData.getPopupMenu(codename);
		return popupMenu;
	}
	
	public boolean isDeletable(final AbstractBean abstractBean) {
		final String codename = abstractBean.getCodename();
		return !this.perspectiveData.isUndeletable(codename);
	}
	
	public Validator getValidator() {
		return this.perspectiveData.getValidator();
	}
	
	public final boolean isSupported(final AbstractBean bean) {
		final String codename = bean.getCodename();
		assert Log.debugMessage(codename, Log.DEBUGLEVEL10);
		return this.perspectiveData.isBeanUISupported(codename);
	}
	
	protected interface Chechable {
		boolean isNeedIn(final AbstractBean bean);
	}

	
	public final Set<AbstractBean> getLayoutBeans() {
		return this.layoutBeans;
	}
	
	public final void setLayoutBeans(final Set<AbstractBean> layoutBeans) {
		this.layoutBeans = layoutBeans;
	}
	
	public final void addLayoutBean(final AbstractBean bean) {
		this.layoutBeans.add(bean);
	}
	
	public final void removeLayoutBean(final AbstractBean bean) {
		this.layoutBeans.remove(bean);
	}
}

