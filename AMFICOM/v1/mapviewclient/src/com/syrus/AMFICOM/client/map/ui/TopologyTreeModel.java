/**
 * $Id: TopologyTreeModel.java,v 1.4 2005/08/24 14:07:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.map.TopologyConditionWrapper;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;

public class TopologyTreeModel implements ChildrenFactory {

	public static final String TOPOLOGY_BRANCH = "topology";

	private static final String NONAME_BRANCH = "noname";

	static final int IMG_SIZE = 16;

	public static final String UPDATE_STRUNG = "updating";

	static ImageIcon layerIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/layers.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	NetMapViewer netMapViewer = null;
	
	static Map<Item, SpatialLayerPopulateThread> threads = new HashMap<Item, SpatialLayerPopulateThread>(); 
	
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
		SpatialLayerPopulateThread populateThread = threads.get(node);
		if(populateThread != null) {
			if(populateThread.isRunning()) {
				populateThread.stopRunning();
			}
		}
		populateThread = new SpatialLayerPopulateThread(node);
		populateThread.start();
		threads.put(node, populateThread);
	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	class SpatialLayerPopulateThread extends Thread {

		private final PopulatableIconedNode node;

		private boolean running = false;

		private String initialName = null;
		
		public SpatialLayerPopulateThread(PopulatableIconedNode node) {
			this.node = node;
		}

		public void stopRunning() {
			System.out.println("stop populating \'" + this.node.getName() + "\'");
			this.running = false;
			finalAction();
		}
		
		public void run() {
			try {
				this.running = true;

				System.out.println("start poplating \'" + this.node.getName() + "\'");

				initialAction();

				synchronized(this.node) {
					for(Iterator iter = new LinkedList(this.node.getChildren()).iterator(); iter.hasNext();) {
						if(!this.running) {
							finalAction();
							return;
						}
						IconedNode childNode = (IconedNode) iter.next();
						childNode.setParent(null);
					}
				}

				System.out.println("children of \'" + this.initialName + "\' are removed");

				Set<SpatialObject> objects;
				SpatialLayer spatialLayer = (SpatialLayer )this.node.getObject();
				MapImageLoader mapImageLoader = TopologyTreeModel.this.netMapViewer.getRenderer().getLoader();

				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)this.node).getResultingCondition();
					if(condition != null) {
						TypicalCondition typicalCondition = (TypicalCondition)condition;
						objects = mapImageLoader.findSpatialObjects(spatialLayer, (String)typicalCondition.getValue());
					}
					else {
						if(spatialLayer.isVisible() && spatialLayer.isVisibleAtScale(TopologyTreeModel.this.netMapViewer.getMapContext().getScale())) {
							Rectangle2D.Double bounds = TopologyTreeModel.this.netMapViewer.getVisibleBounds();
							objects = mapImageLoader.findSpatialObjects(spatialLayer, bounds);
						}
						else {
							objects = Collections.emptySet();
						}
					}
				} catch (Exception e) {
					Log.debugException(e, Level.SEVERE);
					this.running = false;
					finalAction();
					return;
				}
				if(!this.running) {
					finalAction();
					return;
				}
				
				System.out.println("found " + objects.size() + " entities of \'" + this.initialName + "\'");

				for(SpatialObject spatialObject : objects) {
					if(!this.running) {
						finalAction();
						return;
					}
					String label = spatialObject.getLabel();
					if(label == null || label.length() == 0) {
						label = LangModelMap.getString(NONAME_BRANCH);
					}
					IconedNode newItem = new IconedNode(
							spatialObject,
							label,
							false);
					this.node.addChild(newItem);
				}
			} catch(Exception e) {
				Log.debugException(e, Level.SEVERE);
				System.out.println("processing \'" + this.initialName + "\' terminated by " + e.getMessage());
			}
			finally {
				finalAction();
				System.out.println("finish processing \'" + this.initialName + "\'");
				this.running = false;
			}
		}

		private void finalAction() {
			if(this.initialName != null) {
				this.node.setName(this.initialName);
				System.out.println("Set initial name \'" + this.initialName + "\'");
			}
		}

		private void initialAction() {
			this.initialName = this.node.getName();
			String updatingName = this.initialName + " " + LangModelMap.getString(UPDATE_STRUNG);
			this.node.setName(updatingName);
			System.out.println("Set temporal name \'" + updatingName + "\'");
		}

		public boolean isRunning() {
			return this.running;
		}
		
	}
}

