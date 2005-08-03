/*-
 * $Id: SchemeTreeSelectionListener.java,v 1.2 2005/08/03 09:29:41 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.Visualizable;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/08/03 09:29:41 $
 * @module schemeclient_v1
 */

public class SchemeTreeSelectionListener implements TreeSelectionListener, PropertyChangeListener {
	IconedTreeUI treeUI;
	ApplicationContext aContext;
	private boolean doNotify = true; 
	
	public SchemeTreeSelectionListener(IconedTreeUI treeUI, ApplicationContext aContext) {
		this.treeUI = treeUI;
		this.treeUI.getTree().addTreeSelectionListener(this);
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
			
		Item node = (Item)e.getPath().getLastPathComponent();
		if (node == null)
			return;
		VisualManager manager = null;
		if (node instanceof Visualizable)
			manager = ((Visualizable)node).getVisualManager();
		
		Object object = node.getObject();
		
		long type;
		if (object instanceof PortType) {
			if (((PortType)object).getKind().equals(PortTypeKind.PORT_KIND_SIMPLE))
				type = ObjectSelectedEvent.PORT_TYPE;
			else
				type = ObjectSelectedEvent.CABLEPORT_TYPE;
		}
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
		else if (object instanceof SchemeProtoGroup)
			type = ObjectSelectedEvent.SCHEME_PROTOGROUP;
		else if (object instanceof SchemeProtoElement)
			type = ObjectSelectedEvent.SCHEME_PROTOELEMENT;
		else if (object instanceof Scheme)
			type = ObjectSelectedEvent.SCHEME;
		else if (object instanceof Result)
			type = ObjectSelectedEvent.RESULT;
		else if (object instanceof String || object instanceof IdlKind) {
			type = ObjectSelectedEvent.OTHER_OBJECT;
			if (manager != null)
				object = null; 
		} else {
			Log.debugMessage(this.getClass().getName() + " | Unsupported tree object type " + object, Level.FINER); //$NON-NLS-1$
			return;
		}
		ObjectSelectedEvent ev = new ObjectSelectedEvent(this, object, manager, type);
		aContext.getDispatcher().firePropertyChange(ev, false);
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)e;
			Object selected = ev.getSelectedObject();
			if (selected != null && this.treeUI.isLinkObjects()) {
				ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
				Item node = this.treeUI.findNode((Item)model.getRoot(), selected, false);
				if (node == null) {
					node = this.treeUI.findNode((Item)model.getRoot(), selected, true);
				} 
				if (node != null) {
					this.doNotify = false;
					this.treeUI.getTree().setSelectionPath(new TreePath(model.getPathToRoot(node)));
					this.doNotify = true;
				}
			}
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent ev = (SchemeEvent)e;
			if (ev.isType(SchemeEvent.CREATE_OBJECT) || ev.isType(SchemeEvent.DELETE_OBJECT)) {
				ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
				this.treeUI.updateRecursively((Item)model.getRoot());
			}
			if (ev.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object obj = ev.getObject();
				ItemTreeModel model = this.treeUI.getTreeUI().getTreeModel();
				Item node = this.treeUI.findNode((Item)model.getRoot(), obj, false);
				if (node != null) {
					model.setObjectNameChanged(node, null, null);
					this.treeUI.getTree().updateUI();
				}
			}
		}
	}
}
