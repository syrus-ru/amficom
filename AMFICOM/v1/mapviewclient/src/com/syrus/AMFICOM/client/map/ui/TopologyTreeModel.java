/*-
 * $$Id: TopologyTreeModel.java,v 1.22 2006/06/07 07:45:14 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.map.TopologyConditionWrapper;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2006/06/07 07:45:14 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class TopologyTreeModel implements ChildrenFactory {

	public static final String TOPOLOGY_BRANCH = MapEditorResourceKeys.TREE_TOPOLOGY;
	public static final String TOPOLOGY_LAYER = MapEditorResourceKeys.TOPOLOGY_LAYER + ".";

	static final int IMG_SIZE = 16;

	public static final String UPDATE_STRUNG = MapEditorResourceKeys.VALUE_UPDATING;

	static ImageIcon layerIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/layers.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	NetMapViewer netMapViewer = null;
	
	static Map<Item, SpatialLayerPopulateThread> threads = new HashMap<Item, SpatialLayerPopulateThread>(); 
	
	private PopulatableIconedNode root;

	public TopologyTreeModel() {
		// empty
	}
	
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public PopulatableIconedNode getRoot() {
		if(this.root == null) {
			this.root = new PopulatableIconedNode(
					this,
					TopologyTreeModel.TOPOLOGY_BRANCH,
					I18N.getString(TopologyTreeModel.TOPOLOGY_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);			
		}
		return this.root;
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
			List<SpatialLayer> layers = mapImageLoader.getMapConnection().getLayers();

			java.util.Map<SpatialLayer, Item> nodePresense = new HashMap<SpatialLayer, Item>();

			List<Item> toRemove = new LinkedList<Item>();

			for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode) iter.next();
				SpatialLayer layer = (SpatialLayer) childNode.getObject();
				if (layers.contains(layer)) {
					if (childNode.isPopulated()) {
						childNode.populate();
					}
					nodePresense.put(layer, childNode);
				} else {
					toRemove.add(childNode);
				}
			}
			for(Iterator it = toRemove.iterator(); it.hasNext();) {
				Item childItem = (Item) it.next();
				childItem.setParent(null);
			}

			for(SpatialLayer layer : layers) {
				Item childNode = nodePresense.get(layer);
				if(childNode == null) {
					if(this.netMapViewer.getMapContext().getMapConnection().searchIsAvailableForLayer(layer)) {
						FiltrableIconedNode filterableItem = new FiltrableIconedNode();
						filterableItem.setObject(layer);
						filterableItem.setIcon(layerIcon);
						filterableItem.setName(I18N.getString(TOPOLOGY_LAYER + layer.getName()));
						filterableItem.setChildrenFactory(this);
						filterableItem.setCanHaveChildren(true);
						filterableItem.setDefaultCondition(null);
						filterableItem.setFilter(new Filter(new TopologyConditionWrapper()));
						node.addChild(filterableItem);
					}
//					else {
//						PopulatableIconedNode populatableItem = new PopulatableIconedNode(this, layer, layer.getName(), layerIcon, true);
//						newItem = populatableItem;
//					}
					
				}
			}
		} catch(Exception e) {
			Log.errorMessage(e);
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

	class SpatialLayerPopulateThread extends Thread {

		final PopulatableIconedNode node;

		boolean running = false;

		private String initialName = null;
		
		public SpatialLayerPopulateThread(PopulatableIconedNode node) {
			this.node = node;
		}

		public void stopRunning() {
//			Log.debugMessage("stop populating \'" + this.node.getName() + "\'");
			this.running = false;
			finalAction();
		}
		
		@Override
		public void run() {
			try {
				this.running = true;

//				Log.debugMessage("start poplating \'" + this.node.getName() + "\'");

				initialAction();

				SwingUtilities.invokeAndWait( new Runnable() {
					public void run() {
						synchronized(SpatialLayerPopulateThread.this.node) {
							for(Iterator iter = new LinkedList<Item>(SpatialLayerPopulateThread.this.node.getChildren()).iterator(); iter.hasNext();) {
								if(!SpatialLayerPopulateThread.this.running) {
									finalAction();
									return;
								}
								IconedNode childNode = (IconedNode) iter.next();
								childNode.setParent(null);
							}
						}
					}
				});
				if(!this.running) {
					finalAction();
					return;
				}

//				Log.debugMessage("children of \'" + this.initialName + "\' are removed");

				List<SpatialObject> objects;
				SpatialLayer spatialLayer = (SpatialLayer )this.node.getObject();
				MapImageLoader mapImageLoader = TopologyTreeModel.this.netMapViewer.getRenderer().getLoader();

				try {
					StorableObjectCondition condition = null;
					if(this.node instanceof FiltrableIconedNode) {
						condition = ((FiltrableIconedNode)this.node).getResultingCondition();
					}
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
							objects = Collections.emptyList();
						}
					}
				} catch (Exception e) {
					Log.errorMessage(e);
					this.running = false;
					finalAction();
					return;
				}
				if(!this.running) {
					finalAction();
					return;
				}
				
//				Log.debugMessage("found " + objects.size() + " entities of \'" + this.initialName + "\'");

				for(SpatialObject spatialObject : objects) {
					if(!this.running) {
						finalAction();
						return;
					}
					String label = spatialObject.getLabel();
					if(label == null || label.length() == 0) {
						label = I18N.getString(MapEditorResourceKeys.NONAME);
					}
					IconedNode newItem = new IconedNode(
							spatialObject,
							label,
							false);
					this.node.addChild(newItem);
				}
			} catch(Exception e) {
				Log.errorMessage(e);
//				Log.debugMessage("processing \'" + this.initialName + "\' terminated by " + e.getMessage());
			}
			finally {
				finalAction();
//				Log.debugMessage("finish processing \'" + this.initialName + "\'");
				this.running = false;
			}
		}

		void finalAction() {
			if(this.initialName != null) {
				this.node.setName(this.initialName);
//				Log.debugMessage("Set initial name \'" + this.initialName + "\'");
			}
		}

		private void initialAction() {
			this.initialName = this.node.getName();
			String updatingName = this.initialName + " " + I18N.getString(UPDATE_STRUNG); //$NON-NLS-1$
			this.node.setName(updatingName);
//			Log.debugMessage("Set temporal name \'" + updatingName + "\'");
		}

		public boolean isRunning() {
			return this.running;
		}
		
	}

}

