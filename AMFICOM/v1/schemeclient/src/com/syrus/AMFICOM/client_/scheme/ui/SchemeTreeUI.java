/*-
 * $Id: SchemeTreeUI.java,v 1.1 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.general.ui_.tree_.*;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedTreeUI;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class SchemeTreeUI extends IconedTreeUI {
	ApplicationContext aContext;
	
	public SchemeTreeUI(Item rootItem, ApplicationContext aContext) {
		super(rootItem);
		super.treeUI.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
		this.aContext = aContext;
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
		else if (object instanceof String) {
			type = ObjectSelectedEvent.OTHER_OBJECT;
			if (manager != null)
				object = null; 
		}
		else {
			Log.debugMessage("Unsupported tree object type " + object, Log.FINEST);
			return;
		}
		ObjectSelectedEvent ev = new ObjectSelectedEvent(e.getSource(), object, manager, type);
		aContext.getDispatcher().notify(ev);
	}
}
