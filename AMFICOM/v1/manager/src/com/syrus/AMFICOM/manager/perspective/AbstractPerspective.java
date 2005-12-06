/*-
* $Id: AbstractPerspective.java,v 1.6 2005/12/06 15:14:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.UI.ActionMutableTreeNode;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2005/12/06 15:14:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractPerspective implements Perspective {
	
	protected ManagerMainFrame managerMainFrame;

	protected PerspectiveData perspectiveData;
	
	protected List<AbstractBean>	layoutBeans;
	
	protected Map<String, Perspective> perspectiveMap;
	
	protected Set<Perspective> perspectives;
	
	protected List<AbstractAction> actions;
	
	private List<ActionMutableTreeNode> actionNodes;
	
	private List<PropertyChangeListener> listeners;	
	
	private final PropertyChangeEvent propertyChangeEvent = 
			new PropertyChangeEvent(this, "layoutBeans", null, null);

	public final void setManagerMainFrame(ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	public final ManagerMainFrame getManagerMainFrame() {
		return this.managerMainFrame;
	}
	
	public final void addPropertyChangeListener(final PropertyChangeListener listener) {
		assert Log.debugMessage(listener + this.getCodename(), Log.DEBUGLEVEL10);
		if (this.listeners == null) {
			this.listeners = new ArrayList<PropertyChangeListener>();
		}
		if (!this.listeners.contains(listener)) {
			assert Log.debugMessage("add " + listener + " to " + this.getCodename(), 
				Log.DEBUGLEVEL10);
			this.listeners.add(0, listener);
		}
	}
	
	public final void removePropertyChangeListener(final PropertyChangeListener listener) {
		if (this.listeners != null && this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}		
	}
	
	public void firePropertyChangeEvent(final PropertyChangeEvent event) {
		if (this.listeners != null && !this.listeners.isEmpty()) {
			for (final PropertyChangeListener listener : this.listeners) {
				assert Log.debugMessage(listener, Log.DEBUGLEVEL10);
				listener.propertyChange(event);
			}
		}
	}

	protected abstract void createActions() throws ApplicationException;
	
	public final List<AbstractAction> getActions() throws ApplicationException {
		if (this.actions == null) {
			this.createActions();
		}
		return this.actions;
	}
	public final List<ActionMutableTreeNode> getActionNodes() throws ApplicationException {
		if (this.actionNodes == null) {
			final List<AbstractAction> actions2 = this.getActions();
			this.actionNodes = new ArrayList<ActionMutableTreeNode>(actions2.size());
			for (final AbstractAction action : actions2) {
				this.actionNodes.add(new ActionMutableTreeNode(action, this));
			}
		}
		return this.actionNodes;
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
   			
			@SuppressWarnings("unqualified-field-access")
			public void actionPerformed(final ActionEvent e) {
   				try {
   					final Perspective currentPerspective = managerMainFrame.getPerspective();
					if (currentPerspective != perspective) {
						return;
					}
   					final List<AbstractBean> layoutBeans2 = getLayoutBeans();
					for(final AbstractBean abstractBean : layoutBeans2) {
						if (chechable.isNeedIn(abstractBean)) {
							final GraphSelectionModel selectionModel = graph.getSelectionModel();
							selectionModel.setSelectionCell(graphRoutines.getDefaultGraphCell(abstractBean, perspective));
							assert Log.debugMessage("found", Log.DEBUGLEVEL10);
							return;
						}
   					}
   					
   					final AbstractBean bean = factory.createBean(perspective);
   					
   					final Point dropLocation = managerMainFrame.getDropLocation();
					graphRoutines.createChild(perspective,
   						parentCell, 
   						factory.getShortName(), 
   						bean, 
   						dropLocation.getX(), 
   						dropLocation.getY(), 
   						0, 
   						0, 
   						beanUI.getImage(bean));
					firePropertyChangeEvent(propertyChangeEvent);
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
    		final Perspective perspective = this;
    		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
    			
    			@SuppressWarnings("unqualified-field-access")
				public void actionPerformed(final ActionEvent e) {
    				try {
    					final Perspective currentPerspective = managerMainFrame.getPerspective();
    					if (currentPerspective != perspective) {
    						return;
    					}
    					
    					final List<AbstractBean> layoutBeans2 = getLayoutBeans();
    					assert Log.debugMessage(layoutBeans2, Log.DEBUGLEVEL10);
						for(final AbstractBean abstractBean : layoutBeans2) {
	 						if (chechable.isNeedIn(abstractBean)) {
	 							assert Log.debugMessage("found:" + abstractBean, Log.DEBUGLEVEL10);
	 							final GraphSelectionModel selectionModel = graph.getSelectionModel();
	 							final ManagerGraphCell cell = 
	 								graphRoutines.getDefaultGraphCell(abstractBean, perspective);
								selectionModel.setSelectionCell(cell);
	 							return;
	 						}
    					}
    					
    					final AbstractBean bean = factory.createBean(codename);
    					
    					final Point dropLocation = managerMainFrame.getDropLocation();
    					graphRoutines.createChild(perspective,
    						parentCell, 
    						bean.getName(), 
    						bean, 
       						dropLocation.getX(), 
       						dropLocation.getY(), 
    						0, 
    						0, 
    						beanUI.getImage(bean));
    					
    					firePropertyChangeEvent(propertyChangeEvent);
    					
    					assert Log.debugMessage("Create " + bean, Log.DEBUGLEVEL10);

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
	
	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory) 
	throws ApplicationException {
		return this.createAction(factory, null, null);
	}
	
	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory,
			final DefaultGraphCell parentCell) throws ApplicationException {
		return this.createAction(factory, null, parentCell);
	}
	
	@SuppressWarnings("unchecked")
	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory,
	        final PostBeanCreationAction postBeanCreationAction,                                    
			final DefaultGraphCell parentCell) 
	throws ApplicationException {
		final String name = factory.getName();
		final BeanUI beanUI = this.getBeanUI(factory.getCodename());
		final Icon icon = beanUI.getIcon(factory);
		final Perspective perspective = this;
		final AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			
			@SuppressWarnings("unqualified-field-access")
			public void actionPerformed(final ActionEvent e) {
				try {
					final Perspective currentPerspective = managerMainFrame.getPerspective();
					if (currentPerspective != perspective) {
						return;
					}
					final AbstractBean bean = factory.createBean(perspective);
					final Point dropLocation = managerMainFrame.getDropLocation();
					managerMainFrame.getGraphRoutines().createChild(perspective,
						parentCell, 
						factory.getShortName() + "-" + factory.getCount(), 
						bean, 
						dropLocation.getX(),
   						dropLocation.getY(),
						0, 
						0, 
						beanUI.getImage(bean));
					
					if (postBeanCreationAction != null) {
						postBeanCreationAction.postActionPerform(bean);
					}
					firePropertyChangeEvent(propertyChangeEvent);
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
			this.getLayoutItem(identifierString, this.getCodename(), parentLayoutItemId);
	
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
		final int index = codename.indexOf(Identifier.SEPARATOR);
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
	
	protected final LayoutItem getLayoutItem(final String name,
	    	final String layoutName,
	    	final Identifier parentLayoutItemId) 
	throws ApplicationException {
		final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>(3);
		conditions.add(new LinkedIdsCondition(LoginManager.getUserId(), LAYOUT_ITEM_CODE));
		conditions.add(new TypicalCondition(layoutName,
					OperationSort.OPERATION_EQUALS,					
					LAYOUT_ITEM_CODE,
					LayoutItemWrapper.COLUMN_LAYOUT_NAME));
		conditions.add(new TypicalCondition(name,
			OperationSort.OPERATION_EQUALS,					
			LAYOUT_ITEM_CODE,
			StorableObjectWrapper.COLUMN_NAME));
		
		final Set<LayoutItem> items = StorableObjectPool.getStorableObjectsByCondition(
			new CompoundCondition(
				conditions,
				CompoundConditionSort.AND),
			true);
		
		assert items.size() <= 1 : "name:" + name + ",\n layoutName:" + layoutName + ",\n parentLayoutItemId:" + parentLayoutItemId + ",\n items:" + items;
		
		if (!items.isEmpty()) {
			return items.iterator().next();
		}
		
		final LayoutItem layoutItem = 
			LayoutItem.createInstance(LoginManager.getUserId(), 
				parentLayoutItemId, 
				layoutName, 
				name);
		return layoutItem;
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
	
	public Validator getValidator() {
		return this.perspectiveData.getValidator();
	}
	
	public final boolean isSupported(final AbstractBean bean) {
		final String codename = bean.getCodename();
		assert Log.debugMessage(codename, Log.DEBUGLEVEL10);
		return this.perspectiveData.isBeanUISupported(codename);
	}
	
	public final boolean isUndeletable(final AbstractBean bean) {
		final String codename = bean.getCodename();
		assert Log.debugMessage(codename, Log.DEBUGLEVEL10);
		return this.perspectiveData.isUndeletable(codename);
	}
	
	public final boolean isCuttable(final AbstractBean bean) {
		final String codename = bean.getCodename();
		assert Log.debugMessage(codename, Log.DEBUGLEVEL10);
		return this.perspectiveData.isCuttable(codename);
	}
	
	public final List<AbstractBean> getLayoutBeans() {
		return this.layoutBeans;
	}
	
	public final void setLayoutBeans(final List<AbstractBean> layoutBeans) {
		assert Log.debugMessage(layoutBeans, Log.DEBUGLEVEL10);
		this.layoutBeans = layoutBeans;
		this.firePropertyChangeEvent(this.propertyChangeEvent);
	}
	
	public final void addLayoutBean(final AbstractBean bean) {
		assert Log.debugMessage(bean, Log.DEBUGLEVEL10);
		this.layoutBeans.add(bean);
		this.firePropertyChangeEvent(this.propertyChangeEvent);
	}
	
	public final void removeLayoutBean(final AbstractBean bean) {
		assert Log.debugMessage(bean, Log.DEBUGLEVEL10);
		this.layoutBeans.remove(bean);		
		this.firePropertyChangeEvent(this.propertyChangeEvent);
	}
	
	public void putBean(AbstractBean abstractBean) {
		// nothing		
	}
	
	/////// inner classess ////
	protected interface Chechable<T extends AbstractBean> {
		boolean isNeedIn(final T bean);
	}

	protected interface PostBeanCreationAction<T extends AbstractBean> {
		void postActionPerform(final T bean) throws ApplicationException;
	}

	
}

