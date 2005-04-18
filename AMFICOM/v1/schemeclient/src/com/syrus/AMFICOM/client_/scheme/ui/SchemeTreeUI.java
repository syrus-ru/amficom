/*-
 * $Id: SchemeTreeUI.java,v 1.2 2005/04/18 10:45:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.Iterator;

import javax.swing.event.*;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.general.ui_.tree_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.logic.*;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 10:45:18 $
 * @module schemeclient_v1
 */

public class SchemeTreeUI extends IconedTreeUI implements OperationListener {
	ApplicationContext aContext;
	
	public SchemeTreeUI(Item rootItem, ApplicationContext aContext) {
		super(rootItem);
		super.treeUI.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
		setContext(aContext);
	}
	
	void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().unregister(this, ObjectSelectedEvent.TYPE);
			this.aContext.getDispatcher().unregister(this, SchemeEvent.TYPE);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().register(this, ObjectSelectedEvent.TYPE);
		this.aContext.getDispatcher().register(this, SchemeEvent.TYPE);
	}
	
	public void operationPerformed(OperationEvent e) {
		if (e.getActionCommand().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)e;
			Object selected = ev.getSelectedObject();
			if (selected != null) {
				ItemTreeModel model = this.treeUI.getTreeModel();
				Item node = findNode((Item)model.getRoot(), selected, true);
				if (node != null)
					this.treeUI.getTree().setSelectionPath(new TreePath(model.getPathToRoot(node)));
			}
		}
		else if (e.getActionCommand().equals(SchemeEvent.TYPE)) {
			SchemeEvent ev = (SchemeEvent)e;
			if (ev.isType(SchemeEvent.CREATE_OBJECT) || ev.isType(SchemeEvent.DELETE_OBJECT)) {
				ItemTreeModel model = this.treeUI.getTreeModel();
				updateRecursively((Item)model.getRoot());
			}
			if (ev.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object obj = ev.getObject();
				ItemTreeModel model = this.treeUI.getTreeModel();
				Item node = findNode((Item)model.getRoot(), obj, false);
				if (node != null) {
					model.setObjectNameChanged(node, null, null);
//					this.treeUI.getTree().updateUI();
				}
			}
		}
	}
	
	void updateRecursively(Item item) {
		if (item instanceof Populatable) {
			Populatable populatable = (Populatable)item;
			if (populatable.isPopulated())
				populatable.populate();
			for (Iterator it = item.getChildren().iterator(); it.hasNext();)
				updateRecursively((Item)it.next());
		}
	}

	void tree_valueChanged(TreeSelectionEvent e) {
		Item node = (Item)e.getPath().getLastPathComponent();
		if (node == null)
			return;
		VisualManager manager = null;
		if (node instanceof Visualizable)
			manager = ((Visualizable)node).getVisualManager();
		
		Object object = node.getObject();
		
		long type;
		if (object instanceof PortType)
			type = ObjectSelectedEvent.PORT_TYPE;
		else if (object instanceof MeasurementPortType)
			type = ObjectSelectedEvent.MEASUREMENTPORT_TYPE;
		else if (object instanceof MeasurementType)
			type = ObjectSelectedEvent.MEASUREMENT_TYPE;
		else if (object instanceof LinkType)
			type = ObjectSelectedEvent.LINK_TYPE; 
		else if (object instanceof CableLinkType)
			type = ObjectSelectedEvent.CABLELINK_TYPE;
		else if (object instanceof EquipmentType)
			type = ObjectSelectedEvent.EQUIPMENT_TYPE;
		else if (object instanceof String) {
			type = ObjectSelectedEvent.OTHER_OBJECT;
			if (manager != null)
				object = null; 
		}
		else {
			Log.debugMessage("Unsupported tree object type " + object, Log.FINEST);
			System.err.println("Unsupported tree object type " + object);
			return;
		}
		ObjectSelectedEvent ev = new ObjectSelectedEvent(e.getSource(), object, manager, type);
		aContext.getDispatcher().notify(ev);
	}
}
