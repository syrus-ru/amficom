/*-
 * $Id: SchemeTreeSelectionListener.java,v 1.17 2005/11/30 08:14:05 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.17 $, $Date: 2005/11/30 08:14:05 $
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
					TreePath selectedPath1 = treeUI.getTree().getSelectionModel().getSelectionPath();
					if (selectedPath1 != null) {
						Item item = (Item)selectedPath1.getLastPathComponent();
						Object object = item.getObject();
						if (object instanceof Scheme) {
							Scheme scheme = (Scheme)object;
							treeUI.getTree().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));
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
		if (!this.doNotify || !e.isAddedPath()) {
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
		else if (object instanceof ProtoEquipment)
			type = ObjectSelectedEvent.PROTO_EQUIPMENT;
		else if (object instanceof SchemeProtoGroup)
			type = ObjectSelectedEvent.SCHEME_PROTOGROUP;
		else if (object instanceof SchemeProtoElement)
			type = ObjectSelectedEvent.SCHEME_PROTOELEMENT;
		else if (object instanceof Scheme)
			type = ObjectSelectedEvent.SCHEME;
		else if (object instanceof SchemeElement)
			type = ObjectSelectedEvent.SCHEME_ELEMENT;
		else if (object instanceof SchemeLink)
			type = ObjectSelectedEvent.SCHEME_LINK;
		else if (object instanceof SchemeCableLink)
			type = ObjectSelectedEvent.SCHEME_CABLELINK;
		else if (object instanceof SchemePath)
			type = ObjectSelectedEvent.SCHEME_PATH;
		else if (object instanceof SchemePort)
			type = ObjectSelectedEvent.SCHEME_PORT;
		else if (object instanceof SchemeCablePort)
			type = ObjectSelectedEvent.SCHEME_CABLEPORT;
		else if (object instanceof Measurement)
			type = ObjectSelectedEvent.MEASUREMENT;
		else if (object instanceof String || object instanceof IdlKind) {
			type = ObjectSelectedEvent.OTHER_OBJECT;
			if (manager != null)
				object = null; 
		} else {
			Log.debugMessage("Unsupported tree object type " + object, Level.FINER); //$NON-NLS-1$
			return;
		}
		if (this.treeUI.isLinkObjects()) {
			type += ObjectSelectedEvent.INSURE_VISIBLE;
		}
		ObjectSelectedEvent ev = new ObjectSelectedEvent(this, object, manager, type);
		this.aContext.getDispatcher().firePropertyChange(ev, false);
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
