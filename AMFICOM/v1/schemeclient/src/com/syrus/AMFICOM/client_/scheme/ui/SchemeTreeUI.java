/*-
 * $Id: SchemeTreeUI.java,v 1.5 2005/06/22 10:16:06 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.Visualizable;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/06/22 10:16:06 $
 * @module schemeclient_v1
 */

public class SchemeTreeUI extends IconedTreeUI implements PropertyChangeListener {
	ApplicationContext aContext;
	SchemeTreeToolBar toolBar;
	boolean linkToScheme = false;
	
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
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
	}
	
	public JToolBar getToolBar() {
		if (this.toolBar == null)
			this.toolBar = new SchemeTreeToolBar();
		return this.toolBar;
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)e;
			Object selected = ev.getSelectedObject();
			if (selected != null && linkToScheme) {
				ItemTreeModel model = this.treeUI.getTreeModel();
				Item node = findNode((Item)model.getRoot(), selected, true);
				if (node != null)
					this.treeUI.getTree().setSelectionPath(new TreePath(model.getPathToRoot(node)));
			}
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent ev = (SchemeEvent)e;
			if (ev.isType(SchemeEvent.CREATE_OBJECT) || ev.isType(SchemeEvent.DELETE_OBJECT)) {
				ItemTreeModel model = this.treeUI.getTreeModel();
				toolBar.updateRecursively((Item)model.getRoot());
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
		} else {
			throw new UnsupportedOperationException("Unsupported tree object type " + object); //$NON-NLS-1$
		}
		ObjectSelectedEvent ev = new ObjectSelectedEvent(e.getSource(), object, manager, type);
		aContext.getDispatcher().firePropertyChange(ev);
	}
	
	public class SchemeTreeToolBar extends IconedTreeToolBar {
		JToggleButton		syncButton;
		
		public SchemeTreeToolBar() {
			this.syncButton = new JToggleButton();
			this.syncButton.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_SYNCHRONIZE));
			this.syncButton.setToolTipText(LangModelScheme.getString(SchemeResourceKeys.SYNCHRONIZE));
			this.syncButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			this.syncButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					linkToScheme = ((JToggleButton)e.getSource()).isSelected();
				}
			});
			this.add(this.syncButton);
		}
	}
}
