/*-
* $Id: ManagerGraphModelListener.java,v 1.8 2005/12/08 16:07:58 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphModelEvent.GraphModelChange;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.8 $, $Date: 2005/12/08 16:07:58 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModelListener implements GraphModelListener {
	
	private final ManagerMainFrame	managerMainFrame;
	
	private CharacteristicType			xType;
	private CharacteristicType			yType;
	private CharacteristicType			nonStorableObjectNameType;

	public ManagerGraphModelListener(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	public void graphChanged(GraphModelEvent e) {
		try {
			if (this.managerMainFrame.arranging) {
				return;
			}
			final GraphModelChange change = e.getChange();
			
			final GraphModel model = this.managerMainFrame.graph.getModel();
			
			final Object[] inserted = change.getInserted();
			final Object[] changed = change.getChanged();
			final Object[] removed = change.getRemoved();

			final PerspectiveTreeModel treeModel = this.managerMainFrame.getTreeModel();
			if (inserted != null) {
				for(final Object insertedObject : inserted) {
					assert Log.debugMessage("insertedObject:" + insertedObject , Log.DEBUGLEVEL10);
					if (model.isPort(insertedObject)) {
						final TreeNode[] pathToRoot = treeModel.getPathToRoot((TreeNode) insertedObject);
						if (pathToRoot != null) {
							this.managerMainFrame.tree.scrollPathToVisible(new TreePath(pathToRoot));
						}
					} else {
						treeModel.reload();
						final Perspective perspective = this.managerMainFrame.getPerspective();
						perspective.firePropertyChangeEvent(
							new PropertyChangeEvent(this, "layoutBeans", null, null));
					}
				}
			}
			
			if (changed != null && removed == null) {
				for(Object changedObject : changed) {
					Log.debugMessage("changedObject " + changedObject + '[' + changedObject.getClass().getName() + ']', Log.DEBUGLEVEL10);
					if (model.isPort(changedObject)) {
						TreeNode[] pathToRoot = treeModel.getPathToRoot((TreeNode) changedObject);
						if (pathToRoot != null) {
							this.managerMainFrame.tree.scrollPathToVisible(new TreePath(pathToRoot));
						}
					} else  if (model.isEdge(changedObject)) {
						Edge edge = (Edge) changedObject;
						final MPort source = (MPort) edge.getSource();
						final MPort target = (MPort) edge.getTarget();
						source.updateCache();
						target.updateCache();
						
						assert Log.debugMessage(source  +" -> " + target,
							Log.DEBUGLEVEL10);
						final AbstractBean bean = source.getUserObject();
						
						
						final ConnectionSet connectionSet = change.getConnectionSet();							
						final MPort source2 = (MPort) connectionSet.getPort(edge, true);
						final MPort target2 = (MPort) connectionSet.getPort(edge, false);

						Log.debugMessage(source2  +" -> " + target2, Log.DEBUGLEVEL10);
						
						if (source2 != null) {
							source2.updateCache();
						}
						
						if (target2 == null) {
							final AbstractBean bean2 = source2.getBean();
							Log.debugMessage(bean2, Log.DEBUGLEVEL10);
							bean2.applyTargetPort(null, null);
							LayoutItem layoutItem = this.getLayoutItem(bean2.getId());
							Log.debugMessage(
								"set layoutItem:" 
								+ layoutItem.getName() 
								+ ", parentId:" + Identifier.VOID_IDENTIFIER,
								Log.DEBUGLEVEL10);
							layoutItem.setParentId(Identifier.VOID_IDENTIFIER); 
						} else {
							target2.updateCache();
						}
						bean.applyTargetPort(target2, target);
						
						
						if (!this.managerMainFrame.arranging) {
							
							assert Log.debugMessage(
								target,
								Log.DEBUGLEVEL10);

							assert Log.debugMessage(
								target.getBean(),
								Log.DEBUGLEVEL10);

							assert Log.debugMessage(
								target.getBean().getId(),
								Log.DEBUGLEVEL10);
							
							String codeName = bean.getId();
							LayoutItem sourceItem = this.getLayoutItem(codeName);
							Identifier targetItemId = 
								target != null ? 
										this.getLayoutItem(target.getBean().getId()).getId() :
										Identifier.VOID_IDENTIFIER;
							sourceItem.setParentId(targetItemId);
						}
						
					} else {
						DefaultGraphCell cell = (DefaultGraphCell)changedObject;
						MPort port = (MPort) cell.getChildAt(0);
						AbstractBean bean = port.getBean();
						if (bean != null) {
							bean.setName((String) cell.getUserObject());
						}
						
						AttributeMap attributes = cell.getAttributes();
						Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
						String title = cell.getUserObject().toString();

						if (!this.managerMainFrame.arranging) {								
							String codeName = bean.getId();
							LayoutItem item = this.getLayoutItem(codeName);
							if (item == null) {
								item = LayoutItem.createInstance(LoginManager.getUserId(),
									Identifier.VOID_IDENTIFIER,
									this.managerMainFrame.perspective.getCodename(),
									codeName);
								
								Characteristic.createInstance(LoginManager.getUserId(),
									this.getXType(),
									"x",
									"x",
									Integer.toString((int) rectangle2D.getX()),
									item,
									true,
									true);
								
								Characteristic.createInstance(LoginManager.getUserId(),
									this.getYType(),
									"y",
									"y",
									Integer.toString((int) rectangle2D.getY()),
									item,
									true,
									true);
								
								assert Log.debugMessage("create characteristics" 
										+ " for "
										+ item.getName() 
										+ '@'
										+ item.getLayoutName(), 
									Log.DEBUGLEVEL10);
								
								if (bean.getIdentifier().isVoid()) {
									Characteristic.createInstance(LoginManager.getUserId(),
										this.getNonStorableObjectNameType(),
										"title",
										"title",
										title,
										item,
										true,
										true);
								}
								
								if (this.managerMainFrame.perspective.isSupported(bean)) {
									this.managerMainFrame.perspective.addLayoutBean(bean);
								}
								
							} 
							else {
								boolean xFound = false;
								boolean yFound = false;
								boolean nameFound = false;
								assert Log.debugMessage(item.getName()
										+ " characteristics "
										+ item.getCharacteristics(false), 
									Log.DEBUGLEVEL10);

								for(final Characteristic characteristic : item.getCharacteristics(false)) {
									final String codename = characteristic.getType().getCodename();
									if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
										xFound = true;
									} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
										yFound = true;
									} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
										nameFound = true;
									}
									
									if (xFound && yFound && nameFound) {
										break;
									}
								}
								
								if (!xFound) {
									Characteristic.createInstance(LoginManager.getUserId(),
										this.getXType(),
										"x",
										"x",
										Integer.toString((int) rectangle2D.getX()),
										item,
										true,
										true);
									assert Log.debugMessage("create x characteristic" 
										+ " for "
										+ item.getName() 
										+ '@'
										+ item.getLayoutName(), 
									Log.DEBUGLEVEL10);
								}
								
								if (!yFound) {
									Characteristic.createInstance(LoginManager.getUserId(),
										this.getYType(),
										"y",
										"y",
										Integer.toString((int) rectangle2D.getY()),
										item,
										true,
										true);
									
									assert Log.debugMessage("create y characteristic" 
										+ " for "
										+ item.getName() 
										+ '@'
										+ item.getLayoutName(), 
									Log.DEBUGLEVEL10);
								}
								
								if (!nameFound) {
									Characteristic.createInstance(LoginManager.getUserId(),
										this.getNonStorableObjectNameType(),
										"title",
										"title",
										title,
										item,
										true,
										true);
								}
								
								for(Characteristic characteristic : item.getCharacteristics(false)) {
									String codename = characteristic.getType().getCodename();
									if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
										characteristic.setValue(Integer.toString((int) rectangle2D.getX()));
									} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
										characteristic.setValue(Integer.toString((int) rectangle2D.getY()));
									} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
										characteristic.setValue(title);
									}
								}

							} 
						}
					
					}
				}
			} 
			if (removed != null) {
				if (changed != null) {
					for(final Object changedObject : changed) {
//						assert Log.debugMessage("changedObject " 
//							+ changedObject 
//							+ '[' 
//							+ changedObject.getClass().getName() 
//							+ ']', 
//						Log.DEBUGLEVEL03);						
						this.disposePort(changedObject, model);
					}
				}
				for(final Object removedObject : removed) {
					assert Log.debugMessage("removedObject " 
							+ removedObject 
							+ '[' 
							+ removedObject.getClass().getName() 
							+ ']', 
						Log.DEBUGLEVEL03);
					// First of all remove all edges
					 if (model.isEdge(removedObject)) {
						Edge edge = (Edge) removedObject;
						ConnectionSet connectionSet = change.getConnectionSet();							
						MPort source = (MPort) connectionSet.getPort(edge, true);
						MPort target = (MPort) connectionSet.getPort(edge, false);

						AbstractBean bean = source.getUserObject();
						bean.applyTargetPort(target, null);
						assert Log.debugMessage(bean.getId(), Log.DEBUGLEVEL03);
//						final LayoutItem layoutItem = this.getLayoutItem(bean.getId());
//						assert Log.debugMessage("removedObject | layoutItem:" 
//								+ layoutItem.getName() 
//								+ ", layoutName:" 
//								+ layoutItem.getLayoutName(),
//							Log.DEBUGLEVEL10);
//						layoutItem.setParentId(Identifier.VOID_IDENTIFIER);
					 } 
				}
				
				for(final Object removedObject : removed) {
					this.disposePort(removedObject, model);
				}
				
				treeModel.reload();
				final Perspective perspective = this.managerMainFrame.getPerspective();
				perspective.firePropertyChangeEvent(
					new PropertyChangeEvent(this, "layoutBeans", null, null));
			}				
		} catch (final ApplicationException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.managerMainFrame.graph, exception
					.getMessage(), I18N.getString("Manager.Error"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void disposePort(final Object disposedObject,
			final GraphModel model) 
	throws ApplicationException {
		final boolean port = model.isPort(disposedObject);
		// First of all remove all edges
		assert Log.debugMessage("disposedObject " 
				+ disposedObject 
				+ '[' 
				+ disposedObject.getClass().getName() 
				+ ']'
				+ (port ? " is a port" : " is not a port"), 
			Log.DEBUGLEVEL03);
		
		if (port) {
			final MPort source = (MPort) disposedObject;
			final AbstractBean bean = source.getUserObject();
			bean.dispose();
			this.managerMainFrame.perspective.removeLayoutBean(bean);
		}
	}
	
	private LayoutItem getLayoutItem(final String codename) throws ApplicationException {
		
		final String perspectiveName = this.managerMainFrame.perspective.getCodename();
		assert Log.debugMessage("perspectiveName:"
			+ perspectiveName, Log.DEBUGLEVEL10);
		
		assert Log.debugMessage("codename:" + codename , Log.DEBUGLEVEL10);
		
		final CompoundCondition compoundCondition = 
			new CompoundCondition(new TypicalCondition(
				perspectiveName, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				LayoutItemWrapper.COLUMN_LAYOUT_NAME),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(
					LoginManager.getUserId(),
					ObjectEntities.LAYOUT_ITEM_CODE)
//	XXX uncommented when using javac				
//					{
//				
//					@Override
//					public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
//						return storableObjects.isEmpty();
//					}
//				}
	);
		
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
	
	private final CharacteristicType getNonStorableObjectNameType() 
	throws ApplicationException {
		if (this.nonStorableObjectNameType == null) {
			final Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(
					LayoutItem.CHARACTERISCTIC_TYPE_NAME, 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME), true);
			
			this.nonStorableObjectNameType = storableObjectsByCondition.iterator().next();
		}
		return this.nonStorableObjectNameType;
	}

	
	private final CharacteristicType getXType() 
	throws ApplicationException {
		if (this.xType == null) {
			final Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME), true);
			
			this.xType = storableObjectsByCondition.iterator().next();		
		}
		return this.xType;
	}

	
	private final CharacteristicType getYType() throws ApplicationException {
		if (this.yType == null) {
			final Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(
					LayoutItem.CHARACTERISCTIC_TYPE_Y, 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME), true);
			
			this.yType = storableObjectsByCondition.iterator().next();
		}
		return this.yType;
	}
}

