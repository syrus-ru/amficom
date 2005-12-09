/*-
* $Id: GraphRoutines.java,v 1.13 2005/12/09 16:15:27 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_NAME;

import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
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
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.Bean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.13 $, $Date: 2005/12/09 16:15:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class GraphRoutines {

	final JGraph	graph;
	private ManagerGraphModel	graphModel;
	private final ManagerMainFrame	managerMainFrame;
	private VerticalPortComparator	comparator;
	
	public GraphRoutines(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
		this.graph = managerMainFrame.getGraph();
		this.graphModel = managerMainFrame.graphModel;
		this.comparator = new VerticalPortComparator();
	}
	
	private void createEdgeAttributes(final Edge edge) {
		final AttributeMap attributes = edge.getAttributes();
		GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEditable(attributes, false);
		GraphConstants.setEndFill(attributes, true);
		GraphConstants.setLabelAlongEdge(attributes, true);
	}
	
	DefaultEdge createEdge(final DefaultGraphCell source, 
	                       final DefaultGraphCell target) {
		return this.createEdge(source, 
			target, 
			true);
	}

	private DefaultEdge createEdge(final DefaultGraphCell source, 
	                               final DefaultGraphCell target,
	                               final boolean addToGraph) {		
		final MPort sourcePort = (MPort) source.getChildAt(0);
		final MPort targetPort = (MPort) target.getChildAt(0);
		if (sourcePort != targetPort) {
			final boolean canConnect =
				this.graphModel.isConnectionPermitted(sourcePort, targetPort);
			assert Log.debugMessage(sourcePort + ">" + targetPort + ", "
					+ (canConnect ? "can connect" : "cannot connect"), 
				Log.DEBUGLEVEL10);
			if (canConnect) {
				DefaultEdge edge = new DefaultEdge();
				edge.setSource(sourcePort);
				edge.setTarget(targetPort);
//				 Set Arrow Style for edge
				this.createEdgeAttributes(edge);
				
				GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
				graphLayoutCache.insert(edge);		
				graphLayoutCache.setVisibleImpl(new Object[] {edge}, addToGraph);
				
				this.graph.getSelectionModel().clearSelection();
//				try {
//					final CompoundCondition compoundCondition = 
//						new CompoundCondition(new TypicalCondition(
//							this.managerMainFrame.getPerspective().getCodename(), 
//							OperationSort.OPERATION_EQUALS,
//							ObjectEntities.LAYOUT_ITEM_CODE,
//							LayoutItemWrapper.COLUMN_LAYOUT_NAME),
//							CompoundConditionSort.AND,
//							new LinkedIdsCondition(
//								LoginManager.getUserId(),
//								ObjectEntities.LAYOUT_ITEM_CODE) {
//								@Override
//								public boolean isNeedMore(
//								final Set< ? extends Identifiable> storableObjects) {
//									return storableObjects.isEmpty();
//								}
//							});
//					
//					AbstractBean targetBean = targetPort.getBean();
//					AbstractBean sourceBean = sourcePort.getBean();
//					
//					final TypicalCondition condition = new TypicalCondition(
//						sourceBean.getId(), 
//						OperationSort.OPERATION_EQUALS,
//						ObjectEntities.LAYOUT_ITEM_CODE,
//						StorableObjectWrapper.COLUMN_NAME);
//					
//					compoundCondition.addCondition(condition);
//					
//					final Set<LayoutItem> sourceItems = 
//						StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
//					
//					condition.setValue(targetBean.getId());
//
//					final Set<LayoutItem> targetItems = 
//						StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
//					
//					assert !sourceItems.isEmpty();
//					assert !targetItems.isEmpty();
//					
//					final LayoutItem sourceItem = sourceItems.iterator().next();
//					final LayoutItem targetItem = targetItems.iterator().next();
//					
//					sourceItem.setParentId(targetItem.getId());
//					
//				} catch (ApplicationException e) {
//					e.printStackTrace();
//					JOptionPane.showMessageDialog(this.graph, 
//						e.getMessage(), 
//						I18N.getString("Manager.Error"),
//						JOptionPane.ERROR_MESSAGE);
//				}
				
				return edge;
			}
		}

		return null;
	}
	
	public ManagerGraphCell createChild(final Perspective perspective,
			final DefaultGraphCell parentCell, 
            final String name, 
            final Object object, 
            final double x,
            final double y, 
            final double w, 
            final double h, 
            final Icon image) {
		final ManagerGraphCell cell = this.createVertex(perspective, name, object, x, y, w, h, image);
		final GraphLayoutCache cache = this.graph.getGraphLayoutCache();
		cache.insert(cell);	
		
 		if (parentCell != null) {
 			final DefaultEdge edge = this.createEdge(cell, parentCell);
 			assert Log.debugMessage("Create child "
 					+ cell
 					+ " for "
 					+ parentCell
 					+ " in "
 					+ perspective, 
 				Log.DEBUGLEVEL10);
 			if (object instanceof AbstractBean) {
 				final AbstractBean bean = (AbstractBean)object;
				// XXX
//				bean.updateEdgeAttributes(edge, (MPort) parentCell.getChildAt(0));
			}
 		} else {

 			assert Log.debugMessage("Create child "
					+ cell, 
				Log.DEBUGLEVEL10);
 		}
 		return cell;
	}
	
	private ManagerGraphCell createVertex(	final Perspective perspective,
          	final String name,
          	final Object object,
          	final double x,
			final double y,
			final double w,
			final double h,
			final Icon image) {

		// Create vertex with the given name
		final ManagerGraphCell cell = new ManagerGraphCell(name, perspective);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(),
			new Rectangle2D.Double(x, y, w, h));

		GraphConstants.setAutoSize(cell.getAttributes(), true);

		
		final AttributeMap attributes = cell.getAttributes();
		if (image != null) {
			GraphConstants.setIcon(attributes, image);
			attributes.remove(GraphConstants.BORDER);
			attributes.remove(GraphConstants.BORDERCOLOR);
		}

		// Add a Port
		final MPort port = new MPort(object);
		cell.add(port);
		
		if (object instanceof Bean) {
			Bean bean = (Bean)object;
			bean.addPropertyChangeListener(new PropertyChangeListener() {

				@SuppressWarnings("unqualified-field-access")
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(KEY_NAME)) {
						final AttributeMap attributeMap = cell.getAttributes();
						GraphConstants.setValue(attributeMap, evt.getNewValue());						
						final Map<GraphCell, AttributeMap> viewMap = 
							new Hashtable<GraphCell, AttributeMap>();
						viewMap.put(cell, attributeMap);
						graph.getModel().edit(viewMap, null, null, null);
					}
					
				}
			});
		}
		
		return cell;
	}
	
	public void arrangeLayoutItems() throws ApplicationException {
		this.arrangeLayoutItems(this.managerMainFrame.getPerspective());
	}
	
	public void arrangeLayoutItems(final Perspective perspective) throws ApplicationException {
		this.managerMainFrame.arranging = true;
		final String layoutName = perspective.getCodename();

		final TypicalCondition typicalCondition = new TypicalCondition(
			layoutName, 
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.LAYOUT_ITEM_CODE,
			LayoutItemWrapper.COLUMN_LAYOUT_NAME);
		
		final LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
			LoginManager.getUserId(),
			ObjectEntities.LAYOUT_ITEM_CODE) {
			@Override
			public boolean isNeedMore(Set< ? extends Identifiable> storableObjects) {
				return storableObjects.isEmpty();
			}
		};
		
		final Set<LayoutItem> layoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(
					typicalCondition,
					CompoundConditionSort.AND,
					linkedIdsCondition), 
				true);	
		
		boolean needAutoArrage = false;
		
		for(final LayoutItem layoutItem : layoutItems) {			
			// check is x,y characteristics exists
			// otherwise item will be arrange
			
			boolean xFound = false;
			boolean yFound = false;
			
			for(final Characteristic characteristic : layoutItem.getCharacteristics(false)) {
				final String codename = characteristic.getType().getCodename();
				if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
					xFound = true;
				} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
					yFound = true;
				}
				if (xFound && yFound) {
					break;
				}
			}
			
			assert Log.debugMessage(layoutItem.getName() 
					+ '@'
					+ layoutItem.getLayoutName()
					+ ", xFound:" + xFound
					+ ", yFound:" + yFound, 
				Log.DEBUGLEVEL10);
			
			if (!xFound || !yFound) {
				needAutoArrage = true;
			}
			
			this.arrangeCell(layoutItem, layoutName);
		}

		this.managerMainFrame.arranging = false;	
		
		if (needAutoArrage) {
			this.arrangeAllVisibleParents();
		}
		
	}

	@SuppressWarnings({"unchecked", "hiding"})
	public DefaultGraphCell arrangeCell(final LayoutItem item,
		final String layoutName) 
	throws NumberFormatException, ApplicationException {
		
		if (!item.getLayoutName().equals(layoutName)) {
			System.err.println("Incorrect layoutName(" 
					+ layoutName 
					+ ") for "
					+ item.getName()
					+ '@'
					+ item.getLayoutName());
			return null;
		}		
		
		assert Log.debugMessage(item, Log.DEBUGLEVEL10);
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();
		
		DefaultGraphCell parentCell = null;
		final Identifier parentId = item.getParentId();
		
		assert Log.debugMessage("parentId for " 
				+ item.getName()
				+ '@'
				+ item.getLayoutName()
				+ " is "
				+ parentId, 
			Log.DEBUGLEVEL10);
		final LayoutItem parentItem;
		if (!parentId.isVoid()) {
			parentItem = StorableObjectPool.getStorableObject(parentId, true);			
		} else {
			parentItem = null;
		}
		
		assert Log.debugMessage(item, Log.DEBUGLEVEL10);
		if (parentItem != null && parentItem.getLayoutName().equals(layoutName)) {
			parentCell = this.arrangeCell(parentItem, layoutName);
		}
				
		ManagerGraphCell itemCell = null;		
		int x = 0;
		int y = 0;
		String title = null;
		for(Characteristic characteristic : item.getCharacteristics(false)) {
			String codename = characteristic.getType().getCodename();
			if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
				x = Integer.parseInt(characteristic.getValue());
			} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
				y = Integer.parseInt(characteristic.getValue());
			} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
				title = characteristic.getValue();
			}
		}
		
		itemCell = this.getDefaultGraphCell(item);

		if (itemCell == null) {
			AbstractBean bean = this.getBean(item);
			
			final Perspective perspective = this.managerMainFrame.managerHandler.getPerspective(item.getLayoutName());
			final BeanUI beanUI = perspective.getBeanUI(bean.getCodename());
			
			itemCell = 
				this.createChild(perspective,
					parentCell, 
					title != null ? title : bean.getName(), 
					bean, 
					x, 
					y, 
					0, 
					0, 
					beanUI.getImage(bean));
			if (!graphLayoutCache.isVisible(itemCell)) {
				if (!cellBuffer.isExists(itemCell)) {
					graphLayoutCache.setVisible(itemCell, true);
				}
			}
			
//			if (parentCell != null) {
//				MPort port = (MPort) itemCell.getChildAt(0);
//				MPort parentPort = (MPort) parentCell.getChildAt(0);
//				Edge connectionEdge = null;
//				for(int j = 0; j<model.getRootCount(); j++) {
//					DefaultGraphCell edgeCell = (DefaultGraphCell) model.getRootAt(j);
//					if (model.isEdge(edgeCell)) {
//						Edge edge = (Edge) edgeCell;
//						Object target = edge.getSource();
//						Object source = edge.getTarget();
//						
//						if (target == port && source == parentPort) {
//							connectionEdge = edge;
//							break;
//						}
//					}
//				}
//				
//				if (connectionEdge != null) {
//					// make edge visible
//					if (!graphLayoutCache.isVisible(connectionEdge)) {
//						graphLayoutCache.setVisible(connectionEdge, true);
//					}
//				} else {
//					// otherwise create edge
//					this.createEdge(itemCell, parentCell);
//				}
//			}
		} else {
			
			final AttributeMap attributeMap = new AttributeMap();
			GraphConstants.setBounds(attributeMap,
				new Rectangle2D.Double(x, y, 0, 0));
			if (title != null) {
				GraphConstants.setValue(attributeMap,
					title);
			}
			Map viewMap = new Hashtable();
			viewMap.put(itemCell, attributeMap);
			model.edit(viewMap, null, null, null);
			
			
			if (parentCell != null) {
				MPort parentPort = (MPort) parentCell.getChildAt(0);
				Edge connectionEdge = null;
				for(int j = 0; j<model.getRootCount(); j++) {
					DefaultGraphCell edgeCell = (DefaultGraphCell) model.getRootAt(j);
					if (model.isEdge(edgeCell)) {
						Edge edge = (Edge) edgeCell;
						Object target = edge.getSource();
						Object source = edge.getTarget();								
						if (source == parentPort && target == itemCell.getChildAt(0)) {
							connectionEdge = edge;
							break;
						}
					}
				}
				
				
				if (connectionEdge != null) {
					// make edge visible
					if (!graphLayoutCache.isVisible(connectionEdge)) {
						graphLayoutCache.setVisible(connectionEdge, true);
					}
				} else {
					// otherwise create edge
					this.createEdge(itemCell, parentCell);
				}
			}
		}
		
		return itemCell;
	}
	
	private void arrangeAllVisibleParents(){		
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		
		final Set<Port> parentPorts = new HashSet<Port>();
		final Set<Port> childrenPorts = new HashSet<Port>();
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell) && graphLayoutCache.isVisible(cell)) {				
				final MPort port = (MPort) cell.getChildAt(0);
				final List<Port> sources = port.getSources();
				final List<Port> targets = port.getTargets();
				
				boolean child = true;
				for (final Port port2 : sources) {					
					if (graphLayoutCache.isVisible(((MPort)port2).getParent())) {
						child = false;
						break;
					}
				}
				if (child) {
					childrenPorts.add(port);
				}
				
				boolean parent = true;
				for (final Port port2 : targets) {					
					if (graphLayoutCache.isVisible(((MPort)port2).getParent())) {
						parent = false;
						break;
					}
				}
				if (parent) {
					parentPorts.add(port);
				}
			}
		}
		

		assert Log.debugMessage("childrenPorts:" + childrenPorts , Log.DEBUGLEVEL10);
		assert Log.debugMessage("parentPorts:" + parentPorts , Log.DEBUGLEVEL10);

		double width = 0;
		double height = 0;
		
		for (final Port port : parentPorts) {			
			DefaultGraphCell cell = (DefaultGraphCell) ((MPort)port).getParent();
			AttributeMap attributes = cell.getAttributes();
			Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
			width = rectangle2D.getWidth() / 2;
			height = rectangle2D.getHeight() / 2;
			this.horizontalSeparation(cell, rectangle2D.getWidth() / 2);
			this.verticalSeparation(cell, 0);
		}
		
		final Rectangle2D visibleRectangle = this.getVisibleRectangle();
		this.move(width - visibleRectangle.getX(), height - visibleRectangle.getY());
	}
	
	public void fixLayoutItemCharacteristics() {
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		final Map<GraphCell, AttributeMap> viewMap = new Hashtable<GraphCell, AttributeMap>();

		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell) && graphLayoutCache.isVisible(cell)) {				
				viewMap.put(cell, cell.getAttributes());
			}
		}
		
		model.edit(viewMap, null, null, null);
	}
	
	private void verticalSeparation(final DefaultGraphCell parentCell, final int deep) {
		
		final String spaces;
		{
			if (deep == 0) {
				spaces = "";
			} else {
				final StringBuffer buffer = new StringBuffer(deep);
				for(int i=0;i<deep; i++) {
					buffer.append("    ");
				}
				spaces = buffer.toString();
			}
		}
		
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		
		final MPort parentPort = (MPort) parentCell.getChildAt(0);
		
		final Rectangle2D parentRectangle2D = GraphConstants.getBounds(parentCell.getAttributes());

		double vMaxH = parentRectangle2D.getY();

		final List<Port> sources = parentPort.getSources();
		final List<ManagerGraphCell> sourceCells = new ArrayList<ManagerGraphCell>();
		for (final Port port : sources) {
			final ManagerGraphCell cell = (ManagerGraphCell) ((MPort)port).getParent();
			if (graphLayoutCache.isVisible(cell)) {
				sourceCells.add(cell);
			}
		}
		
		if (sourceCells.isEmpty()) {
			assert Log.debugMessage(spaces + "there is no children of " + parentCell , Log.DEBUGLEVEL10);
			return;
		}
		
		assert Log.debugMessage(spaces + parentCell , Log.DEBUGLEVEL10);
		
		Collections.sort(sourceCells, this.comparator);
		
		assert Log.debugMessage(spaces + "Children of " + parentCell + " are " + sourceCells , Log.DEBUGLEVEL10);
		
		for (final ManagerGraphCell cell : sourceCells) {			
			final AttributeMap attributes = cell.getAttributes();
			final Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
			this.verticalSeparation(cell, deep + 1);					
			
			Rectangle2D rectangle2 = this.getRectangle(cell, null);
			
			assert Log.debugMessage(spaces + cell + " in " 
					+ rectangle2.getY() 
					+ " .. "  
					+ (rectangle2.getY() + rectangle2.getHeight()),
				Log.DEBUGLEVEL10);
			double vMinH2 = rectangle2.getY();
			assert Log.debugMessage(spaces + "was(" + cell +"):"
				+ rectangle2D.getY()
				+ ", vMaxH:"
				+ vMaxH
				+ " parent(" + parentCell + "):"
				+ parentRectangle2D.getY(), 
			Log.DEBUGLEVEL10);
			
			rectangle2D.setRect(rectangle2D.getX(), 
				rectangle2D.getY() + vMaxH - vMinH2, 
				rectangle2D.getWidth(), 
				rectangle2D.getHeight());
			
			this.moveVisible(cell, 0,  vMaxH - vMinH2);
			assert Log.debugMessage(spaces + "now(" + cell +"):"
				+ rectangle2D.getY(),
			Log.DEBUGLEVEL10);

			rectangle2 = this.getRectangle(cell, null);
			assert Log.debugMessage(spaces + cell + " in " 
				+ rectangle2.getY() 
				+ " .. "  
				+ (rectangle2.getY() + rectangle2.getHeight()),
			Log.DEBUGLEVEL10);
			vMaxH = rectangle2.getY() + rectangle2.getHeight();
			assert Log.debugMessage(spaces + cell + " maxY: " + vMaxH , Log.DEBUGLEVEL10);
		}
		
		// align all children at the middle  
		final Rectangle2D rectangle2 = this.getRectangle(parentCell, null);
		this.moveVisible(parentCell, 0, 
			-(rectangle2.getY() - (parentRectangle2D.getY() + 
					(parentRectangle2D.getHeight() - rectangle2.getHeight()) / 2
					)));
		
	}
	
	private Rectangle2D getRectangle(final DefaultGraphCell parentCell,
			Rectangle2D rectangle2D) {

		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final Rectangle2D parentRectangle2D = GraphConstants.getBounds(parentCell.getAttributes());
		
		final MPort parentPort = (MPort) parentCell.getChildAt(0);
		
		if (rectangle2D == null) {
			rectangle2D = 
			new Rectangle2D.Double(0, 
				parentRectangle2D.getY(), 
				0, 
				parentRectangle2D.getHeight());
			assert Log.debugMessage("parentCell "
				+ parentCell
				+ " get " 
				+ parentRectangle2D.getY()
				+ " .. "
				+ (rectangle2D.getY() + rectangle2D.getHeight()), 
			Log.DEBUGLEVEL10);
		}

		double minY = rectangle2D.getY();
		double maxY = rectangle2D.getY() + rectangle2D.getHeight();
		
		// get child minY, maxY
		final List<Port> sources = parentPort.getSources();
		for (final Port port : sources) {			
			final DefaultGraphCell cell = (DefaultGraphCell) ((MPort)port).getParent();
			if (graphLayoutCache.isVisible(cell)) {
				final AttributeMap attributes = cell.getAttributes();
				final Rectangle2D childRectangle2D = GraphConstants.getBounds(attributes);
				double childMinY = childRectangle2D.getY();
				double childMaxY = childRectangle2D.getY() + childRectangle2D.getHeight();
				
				assert Log.debugMessage("minY:" + minY + ", childMinY:" + childMinY, Log.DEBUGLEVEL10);
				assert Log.debugMessage("maxY:" + maxY + ", childMaxY:" + childMaxY, Log.DEBUGLEVEL10);
				minY = minY < childMinY ? minY : childMinY;
				maxY = maxY > childMaxY ? maxY : childMaxY;
				assert Log.debugMessage("minY:" + minY + ", childMinY:" + childMinY, Log.DEBUGLEVEL10);
				assert Log.debugMessage("maxY:" + maxY + ", childMaxY:" + childMaxY, Log.DEBUGLEVEL10);

				
				assert Log.debugMessage("child of "
					+ parentCell
					+ " is "
					+  cell 
					+ " get " 
					+ minY
					+ " .. "
					+ maxY, 
				Log.DEBUGLEVEL10);
			}
		}		
		
		assert Log.debugMessage(parentCell 
				+ " set " 
				+ minY
				+ " .. "
				+ maxY, 
			Log.DEBUGLEVEL10);
		
		rectangle2D.setRect(0, minY, 0, maxY - minY);
		
		for (final Port port : sources) {			
			final DefaultGraphCell cell = (DefaultGraphCell) ((MPort)port).getParent();
			if (graphLayoutCache.isVisible(cell)) {
				this.getRectangle(cell, rectangle2D);
			}
		}
		
		assert Log.debugMessage(parentCell
			+ " get " 
			+ rectangle2D.getY()
			+ " , "
			+ rectangle2D.getHeight(), 
		Log.DEBUGLEVEL10);
		
		return rectangle2D;
	}
	
	private Rectangle2D getVisibleRectangle() {		
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell) && graphLayoutCache.isVisible(cell)) {
				
				final AttributeMap attributes = cell.getAttributes();
				final Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
				
				final double x = rectangle2D.getX();
				final double y = rectangle2D.getY();
				final double width = rectangle2D.getWidth();
				final double height = rectangle2D.getHeight();
				
				minX = minX < x ? minX : x;
				minY = minY < y ? minY : y;
				
				maxX = maxX > x + width ? maxX : x + width;
				maxY = maxY > y + height ? maxY : y + height;
			}
		}
		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}
	
	private void move(final double offsetX,
			final double offsetY) {
		final GraphModel model = this.graph.getModel();		
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell)) {				
				final AttributeMap attributes = cell.getAttributes();
				final Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
				rectangle2D.setRect(rectangle2D.getX() + offsetX,
					rectangle2D.getY() + offsetY,
					rectangle2D.getWidth(),
					rectangle2D.getHeight());
			}
		}
	}
	
	private void moveVisible(final DefaultGraphCell parentCell,
			final double offsetX,
			final double offsetY) {
		
		assert Log.debugMessage("move " 
				+ parentCell 
				+ " by (" 
				+ offsetX 
				+ ", " 
				+ offsetY 
				+ ")" , 
			Log.DEBUGLEVEL10);
		
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		
		final MPort parentPort = (MPort) parentCell.getChildAt(0);
		final List<Port> sources = parentPort.getSources();
		for (final Port port : sources) {			
			final DefaultGraphCell cell = (DefaultGraphCell) ((MPort)port).getParent();
			if (graphLayoutCache.isVisible(cell)) {
				final AttributeMap attributes = cell.getAttributes();
				final Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
				rectangle2D.setRect(rectangle2D.getX() + offsetX,
					rectangle2D.getY() + offsetY,
					rectangle2D.getWidth(),
					rectangle2D.getHeight());
				this.moveVisible(cell, offsetX, offsetY);
			}
		}
	}
	
	private void horizontalSeparation(final DefaultGraphCell parentCell,
			final double offset) {
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		
		final MPort parentPort = (MPort) parentCell.getChildAt(0);
		
		final Rectangle2D parentRectangle2D = GraphConstants.getBounds(parentCell.getAttributes());
		
		final List<Port> sources = parentPort.getSources();
		for (final Port port : sources) {			
			final DefaultGraphCell cell = (DefaultGraphCell) ((MPort)port).getParent();
			if (graphLayoutCache.isVisible(cell)) {
				final AttributeMap attributes = cell.getAttributes();
				final Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);

				final double x = parentRectangle2D.getX() - rectangle2D.getWidth() - offset;

				assert Log.debugMessage(port + " move to " + x , Log.DEBUGLEVEL10);
				
				rectangle2D.setRect(x,
						rectangle2D.getY(),
						rectangle2D.getWidth(),
						rectangle2D.getHeight());
				
				this.horizontalSeparation(cell, offset);
			}
			
		}
	}
	
	public ManagerGraphCell getDefaultGraphCell(final LayoutItem item) {
		return this.getDefaultGraphCell(item, false);
	}
	
	public ManagerGraphCell getDefaultGraphCell(final LayoutItem item, 
			final boolean visible) {
		final String name = item.getName();
		final String layoutName = item.getLayoutName();

		return getDefaultGraphCell(name, layoutName, visible);
	}

	public ManagerGraphCell getDefaultGraphCell(final String name,
			final String layoutName,
			final boolean visible) {
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();		
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell)) {				
				final ManagerGraphCell managerGraphCell = (ManagerGraphCell) cell;
				final ManagerGraphCell graphCell = managerGraphCell;
				final AbstractBean bean = graphCell.getAbstractBean();				
				final Perspective perspective = graphCell.getPerspective();
				final String codename = perspective.getCodename();
				final String id = bean.getId();
				if (codename.equals(layoutName) && 
						name.equals(id)) {
					if (visible && 
							!graphLayoutCache.isVisible(cell) && 
							!cellBuffer.isExists(managerGraphCell)) {
						graphLayoutCache.setVisible(cell, true);
					}
					
					return managerGraphCell;
				}
			}
		}
		
		return null;
	}
	
	
	
	public ManagerGraphCell getDefaultGraphCell(final AbstractBean bean,
			final Perspective perspective) {
		final GraphModel model = this.graph.getModel();
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell)) {
				final ManagerGraphCell managerGraphCell = (ManagerGraphCell) cell;
				final Perspective cellPerspective = managerGraphCell.getPerspective();
				if (managerGraphCell.getAbstractBean() == bean && 
						cellPerspective == perspective) {
					return managerGraphCell;
				}
			}
		}
		
		return null;
	}
	
	public Set<ManagerGraphCell> getDefaultGraphCells(final AbstractBean bean) {
		return this.getDefaultGraphCells(bean.getId());
	}
	
	public Set<ManagerGraphCell> getDefaultGraphCells(final String beanId) {
		final GraphModel model = this.graph.getModel();
		final Set<ManagerGraphCell> set = new HashSet<ManagerGraphCell>(); 
		for(int i = 0; i < model.getRootCount(); i++) {
			final DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell)) {
				final ManagerGraphCell managerGraphCell = (ManagerGraphCell) cell;
				final AbstractBean abstractBean = managerGraphCell.getAbstractBean();
				if (abstractBean.getId().equals(beanId)) {
					set.add(managerGraphCell);
				}
			}
		}
		
		return set;
	}
	
	public AbstractBean getBean(final LayoutItem layoutItem) 
	throws ApplicationException {
		final ManagerGraphCell defaultGraphCell = this.getDefaultGraphCell(layoutItem, false);

		if (defaultGraphCell != null) {
			MPort port = (MPort) defaultGraphCell.getChildAt(0);
			return port.getBean();
		}
		
		final ManagerHandler managerHandler = this.managerMainFrame.getManagerHandler();
		final Perspective perspective = managerHandler.getPerspective(layoutItem.getLayoutName());
		return perspective.createBean(layoutItem.getName());
	}	
	
	private List<AbstractBean> getLayoutBeans(final String layerName) 
	throws ApplicationException {
		final ManagerHandler managerHandler = this.managerMainFrame.getManagerHandler();
		final Perspective perspective = managerHandler.getPerspective(layerName);
		
		List<AbstractBean> layoutBeans = perspective.getLayoutBeans();
		
		if (layoutBeans == null) {
			final Set<LayoutItem> layoutItems = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(layerName, 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.LAYOUT_ITEM_CODE,
						LayoutItemWrapper.COLUMN_LAYOUT_NAME), 
					true);

			assert Log.debugMessage("layerName:" + layerName + ", " +  layoutItems, Log.DEBUGLEVEL10);
			
			layoutBeans = new ArrayList<AbstractBean>(layoutItems.size());			
			for (final LayoutItem layoutItem : layoutItems) {
				layoutBeans.add(this.getBean(layoutItem));
			}		
			perspective.setLayoutBeans(layoutBeans);

		}
		return layoutBeans;
	}
	
	
	
	public void showLayerName(final String layerName, boolean show) 
	throws ApplicationException {		
		
		final List<AbstractBean> layoutBeans = this.getLayoutBeans(layerName);
		
		if (show) {
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();
		
		for(int i = 0; i<model.getRootCount(); i++) {
			final Object rootAt = model.getRootAt(i);
			if (!model.isEdge(rootAt) && !model.isPort(rootAt)) {
				MPort port = (MPort) ((TreeNode)rootAt).getChildAt(0);
				AbstractBean bean = port.getBean();				
				boolean visible = layoutBeans.contains(bean);
				
				assert Log.debugMessage("port:" + port + (visible ? " visible" : " hide"), 
					Log.DEBUGLEVEL10);				
				final ManagerGraphCell parent = (ManagerGraphCell) port.getParent();
				if (!cellBuffer.isExists(parent)) {
					graphLayoutCache.setVisible(parent, visible);
				}
			}
		}
		
		this.graph.repaint();
		}
		this.managerMainFrame.undoManager.discardAllEdits();
		this.managerMainFrame.updateHistoryButtons();
	}
	
	private class VerticalPortComparator implements Comparator<ManagerGraphCell>{

		public int compare(	final ManagerGraphCell cell1,
				final ManagerGraphCell cell2) {
			final AttributeMap attributes1 = cell1.getAttributes();
			final Rectangle2D rectangle2D1 = GraphConstants.getBounds(attributes1);
			
			final AttributeMap attributes2 = cell2.getAttributes();
			final Rectangle2D rectangle2D2 = GraphConstants.getBounds(attributes2);

			
			return (int) (rectangle2D1.getY() - rectangle2D2.getY());
		}
	}
}

