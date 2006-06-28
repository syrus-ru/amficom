/*-
 * $Id: SchemeTreeSelectionListener.java,v 1.23 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.ALL_DESELECTED;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.Visualizable;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.23 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class SchemeTreeSelectionListener implements TreeSelectionListener, PropertyChangeListener {
	IconedTreeUI treeUI;
	ApplicationContext aContext;
	private boolean doNotify = true; 
	
	public SchemeTreeSelectionListener(final IconedTreeUI treeUI, final ApplicationContext aContext) {
		this.treeUI = treeUI;
		this.treeUI.getTree().addTreeSelectionListener(this);
		this.treeUI.getTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath selectedPath1 = treeUI.getTree().getClosestPathForLocation(e.getX(), e.getY());
					if (selectedPath1 != null) {
						Item item = (Item)selectedPath1.getLastPathComponent();
						Object object = item.getObject();
						if (object instanceof Scheme) {
							Scheme scheme = (Scheme)object;
							treeUI.getTree().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));
							treeUI.getTree().setCursor(Cursor.getDefaultCursor());
						} else if (object instanceof SchemeElement) {
							SchemeElement schemeElement = (SchemeElement)object;
							treeUI.getTree().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, schemeElement.getId(), SchemeEvent.OPEN_SCHEMEELEMENT));
							treeUI.getTree().setCursor(Cursor.getDefaultCursor());
						} else if (object instanceof Measurement) {
							ObjectSelectedEvent ev = new ObjectSelectedEvent(this, (Measurement)object, null, ObjectSelectedEvent.MEASUREMENT);
							treeUI.getTree().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							aContext.getDispatcher().firePropertyChange(ev, false);
							treeUI.getTree().setCursor(Cursor.getDefaultCursor());
						}
					}
				}
			}
		});

		setContext(aContext);
	}
	
	void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		if (!this.doNotify) {
			return;
		}
			
		TreePath[] paths = this.treeUI.getTree().getSelectionPaths();
		ObjectSelectedEvent ev;
		if (paths == null || paths.length == 0) {
			ev = new ObjectSelectedEvent(this, Collections.<Identifiable>emptySet(), null, ALL_DESELECTED);
		} else {
			Set<Identifiable> objects = new HashSet<Identifiable>();
			Visualizable visualizableNode = null;

			for (TreePath path : paths) {
				Item node = (Item)path.getLastPathComponent();
				if (visualizableNode == null && node instanceof Visualizable) {
					visualizableNode = (Visualizable)node;
				}

				final Object object = node.getObject();
				if (object instanceof Identifiable) {
					objects.add((Identifiable)object);
				}
			}

			VisualManager manager = null;
			long type = 0;
			if (objects.isEmpty()) {
				type = ALL_DESELECTED;
			} else if (objects.size() == 1 &&  visualizableNode != null) {
				manager = visualizableNode.getVisualManager();
				type = ClientUtils.getEventType(objects.iterator().next());
			} else {
				boolean hasEqualType = ClientUtils.hasEqualType(objects);
				type = ClientUtils.getEventType(objects.iterator().next()) + ObjectSelectedEvent.MULTIPLE;
				if (hasEqualType) {
					if (type == ObjectSelectedEvent.SCHEME_PORT) {
						manager = SchemePortPropertiesManager.getInstance(this.aContext);
					} else {
						manager = MultipleSelectionPropertiesManager.getInstance(this.aContext);
					}
				} else {
					manager = MultipleSelectionPropertiesManager.getInstance(this.aContext);
				}
			}
			if (this.treeUI.isLinkObjects()) {
				type += ObjectSelectedEvent.INSURE_VISIBLE;
			}
			ev = new ObjectSelectedEvent(this, objects, manager, type);
		}
		this.aContext.getDispatcher().firePropertyChange(ev, false);
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)e;
			if (this.treeUI.isLinkObjects()) {
				Set<TreePath> paths = new HashSet<TreePath>();
				for (Identifiable selected : ev.getIdentifiables()) {
					ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
					Item node = this.treeUI.findNode((Item)model.getRoot(), selected, false);
					
					// XXX commented by Stas as in observer in might result in open all measurements
//					if (node == null) {
//						node = this.treeUI.findNode((Item)model.getRoot(), selected, true);
//					} 
					if (node != null) {
						this.doNotify = false;
						paths.add(new TreePath(model.getPathToRoot(node)));
					}
				}
				this.treeUI.getTree().setSelectionPaths(paths.toArray(new TreePath[paths.size()]));
				this.doNotify = true;
			}
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent ev = (SchemeEvent)e;
			if ((ev.isType(SchemeEvent.CREATE_OBJECT) || ev.isType(SchemeEvent.DELETE_OBJECT)) && this.treeUI.isLinkObjects()) {
				ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
				this.treeUI.updateRecursively((Item)model.getRoot());
			}
			if (ev.isType(SchemeEvent.UPDATE_OBJECT)) {
				try {
					Object obj = ev.getStorableObject();
					ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
					Item node = this.treeUI.findNode((Item)model.getRoot(), obj, false);
					if (node != null) {
						model.setObjectNameChanged(node, null, null);
						this.treeUI.getTree().updateUI();
					}
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		}
	}
}
