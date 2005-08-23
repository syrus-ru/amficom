/**
 * $Id: TopologyTreeModel.java,v 1.1 2005/08/23 09:40:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.map.TopologyConditionWrapper;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;

public class TopologyTreeModel implements ChildrenFactory {

	public static final String TOPOLOGY_BRANCH = "topology";

	static final int IMG_SIZE = 16;

	static ImageIcon layerIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/layers.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	NetMapViewer netMapViewer = null;
	
	public TopologyTreeModel() {
		// empty
	}
	
	public void populate(Item node) {
		if(this.netMapViewer == null) {
			return;
		}

		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(TopologyTreeModel.TOPOLOGY_BRANCH)) {
				populateTopologyNode((PopulatableIconedNode )node);
			}
		}
		else if(node.getObject() instanceof SpatialLayer) {
			populateLayerNode((PopulatableIconedNode )node);
		}
	}

	void populateTopologyNode(PopulatableIconedNode node) {

		try {
			MapImageLoader mapImageLoader = this.netMapViewer.getRenderer().getLoader();
			List<SpatialLayer> layers = mapImageLoader.getMapConnection()
					.getLayers();

			java.util.Map nodePresense = new HashMap();

			List toRemove = new LinkedList();

			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode) iter
						.next();
				SpatialLayer layer = (SpatialLayer) childNode.getObject();
				if(layers.contains(layer)) {
					if(childNode.isPopulated())
						childNode.populate();
					nodePresense.put(layer, childNode);
				}
				else
					toRemove.add(childNode);
			}
			for(Iterator it = toRemove.iterator(); it.hasNext();) {
				Item childItem = (Item) it.next();
				childItem.setParent(null);
			}

			for(SpatialLayer layer : layers) {
				Item childNode = (Item) nodePresense.get(layer);
				if(childNode == null) {
					FiltrableIconedNode newItem = new FiltrableIconedNode();
					newItem.setObject(layer);
					newItem.setIcon(layerIcon);
					newItem.setName(layer.getName());
					newItem.setChildrenFactory(this);
					newItem.setCanHaveChildren(true);
					newItem.setDefaultCondition(null);
					newItem.setFilter(new Filter(new TopologyConditionWrapper(), null));
					//				newItem.populate();
					node.addChild(newItem);
				}
			}
		} catch(Exception e) {
			Log.debugException(e, Level.SEVERE);
		}
	}

	void populateLayerNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		SpatialLayer spatialLayer = (SpatialLayer )parentNode.getObject();

		try {
			List<SpatialObject> objects;
			MapImageLoader mapImageLoader = this.netMapViewer.getRenderer().getLoader();

			try {
				StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
				if(condition != null) {
					TypicalCondition typicalCondition = (TypicalCondition)condition;
					objects = mapImageLoader.findSpatialObjects(spatialLayer, (String)typicalCondition.getValue());
				}
				else {
					Rectangle2D.Double bounds = this.netMapViewer.getVisibleBounds();
					objects = mapImageLoader.findSpatialObjects(spatialLayer, bounds);
				}
			} catch (Exception e) {
				Log.debugException(e, Level.SEVERE);
				return;
			}

			java.util.Map nodePresense = new HashMap();

			List toRemove = new LinkedList();

			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				IconedNode childNode = (IconedNode) iter
						.next();
				SpatialObject spatialObject = (SpatialObject) childNode.getObject();
				if(objects.contains(spatialObject)) {
					nodePresense.put(spatialObject, childNode);
				}
				else
					toRemove.add(childNode);
			}
			for(Iterator it = toRemove.iterator(); it.hasNext();) {
				Item childItem = (Item) it.next();
				childItem.setParent(null);
			}

			for(SpatialObject spatialObject : objects) {
				Item childNode = (Item) nodePresense.get(spatialObject);
				if(childNode == null) {
					IconedNode newItem = new IconedNode(
							spatialObject,
							spatialObject.getLabel(),
							false);
					node.addChild(newItem);
				}
			}
		} catch(Exception e) {
			Log.debugException(e, Level.SEVERE);
		}
	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

}
