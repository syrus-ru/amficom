/*-
* $Id: SystemUserPerpective.java,v 1.3 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserPerpective extends AbstractPerspective {

	private final UserBean userBean;
	private Object	cell;
	
	public SystemUserPerpective(final ManagerMainFrame managerMainFrame,
	                        final UserBean userBean,
	                        final Object cell) {
		super(managerMainFrame);
		this.userBean = userBean;
		this.cell = cell;
	}
	
	public final AbstractAction createAction(final AbstractBeanFactory<?> factory,
	                                  final Module module) {
		final String name = factory.getName();
		final BeanUI beanUI = BeanUI.BeanUIFactory.getBeanUI(factory.getCodename() + "BeanUI", this.managerMainFrame);
		Icon icon = beanUI.getIcon(factory);
		FACTORY_MAP.put(factory.getCodename(), factory);
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			
			public void actionPerformed(final ActionEvent e) {
				try {
					{
						CompoundCondition compoundCondition = 
							new CompoundCondition(
								new TypicalCondition(managerMainFrame.getPerspective().getCodename(),
									OperationSort.OPERATION_EQUALS,
									ObjectEntities.LAYOUT_ITEM_CODE,
									LayoutItemWrapper.COLUMN_LAYOUT_NAME), 
								CompoundConditionSort.AND, 
								new TypicalCondition(ObjectEntities.PERMATTR,
									OperationSort.OPERATION_SUBSTRING,
									ObjectEntities.LAYOUT_ITEM_CODE,
									StorableObjectWrapper.COLUMN_NAME));
						
						final Set<LayoutItem> layoutItems = 
							StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
						
						assert Log.debugMessage(".actionPerformed | " + layoutItems,
							Log.DEBUGLEVEL09);
						
						if (!layoutItems.isEmpty()) {
							for (final LayoutItem item : layoutItems) {
								final PermissionBean permissionBean = (PermissionBean) managerMainFrame.getCell(item);
								if (permissionBean.getPermissionAttributes().getModule() == module) {
									final GraphSelectionModel selectionModel = managerMainFrame.getGraph().getSelectionModel();
									selectionModel.setSelectionCell(managerMainFrame.getDefaultGraphCell(item));
									return;
								}
							}			
						}
					}
					
					final AbstractBean bean = factory.createBean(managerMainFrame.getPerspective());
					managerMainFrame.createChild(null, 
						factory.getShortName(), 
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
	
	public void addEntities(final JToolBar entityToolBar) {
		for(final Module module : Module.getValueList()) {
			if (!module.isEnable()) {
				continue;
			}
			this.managerMainFrame.addAction(this.createAction(PermissionBeanFactory.getInstance(this.managerMainFrame, module), module));
		}
	}
	
	public String getCodename() {
		return this.getUserId().getIdentifierString();
	}
	
	public String getName() {		
		return this.userBean.getName();
	}
	
	public final Identifier getDomainId() {
		return this.userBean.getId();
	}
	
	public final Identifier getUserId() {
		return this.userBean.getId();
	}
	
	public boolean isValid() {
		JGraph graph = this.managerMainFrame.getGraph();
		final GraphLayoutCache graphLayoutCache = graph.getGraphLayoutCache();
		final GraphModel model = graph.getModel();
		for(final Object root : graph.getRoots()) {
			if (!model.isPort(root) 
					&& !model.isEdge(root) 
					&& graphLayoutCache.isVisible(root)) {
				MPort port = (MPort) ((DefaultGraphCell)root).getChildAt(0);
				int visibleTarget = 0;
				for(final Port targetPort : port.getTargets()) {
					visibleTarget += graphLayoutCache.isVisible(targetPort) ? 1 : 0;
				}

				if (visibleTarget == 0 && 
						!port.getBean().getCodeName().startsWith(ObjectEntities.SYSTEMUSER)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	
	public void perspectiveApplied() {
		this.managerMainFrame.showOnlyDescendants((DefaultGraphCell) this.cell);
		
		this.managerMainFrame.showOnly(new String[] {ObjectEntities.SYSTEMUSER, 
				ObjectEntities.PERMATTR});
		
	}
	
}

