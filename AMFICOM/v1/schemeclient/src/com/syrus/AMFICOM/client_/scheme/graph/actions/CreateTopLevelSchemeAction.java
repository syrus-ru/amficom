/*
 * $Id: CreateTopLevelSchemeAction.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class CreateTopLevelSchemeAction extends AbstractAction {
	UgoTabbedPane sourcePane;
	UgoTabbedPane targetPane;
	ApplicationContext aContext;

	public CreateTopLevelSchemeAction(UgoTabbedPane sourcePane, UgoTabbedPane targetPane, ApplicationContext aContext) {
		super(Constants.createTopLevelElementKey);
		this.sourcePane = sourcePane;
		this.targetPane = targetPane;
		this.aContext = aContext;
	}

	public void actionPerformed(ActionEvent e) {
		Scheme scheme = sourcePane.getCurrentPanel().getSchemeResource().getScheme();
		if (scheme == null) {
			Log.debugMessage("Can't create top level for null scheme", Log.SEVERE);
			return;
		}
		
		SchemeGraph graph = sourcePane.getGraph();
		Object[] cells = graph.getAll();
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);

		if (groups.length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					"Не найдено ни одного компонента", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		ArrayList blockports_in = new ArrayList();
		ArrayList blockports_out = new ArrayList();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++) {
			if (bpcs[i].isCablePort()) {
				SchemeCablePort port = bpcs[i].getSchemeCablePort();
				if (port.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._IN))
					blockports_in.add(bpcs[i]);
				else
					blockports_out.add(bpcs[i]);
			} else {
				SchemePort port = bpcs[i].getSchemePort();
				if (port.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._IN))
					blockports_in.add(bpcs[i]);
				else
					blockports_out.add(bpcs[i]);
			}
		}
		if (blockports_in.size() == 0 && blockports_out.size() == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Не найдено ни одного иерархического порта", "Ошибка",
					JOptionPane.OK_OPTION);
			return;
		}

		Identifier userId = new Identifier(((RISDSessionInfo)this.aContext.getSessionInterface()).getAccessIdentifier().user_id);
		SchemeElement schemeElement = targetPane.getCurrentPanel().getSchemeResource().getSchemeElement();
		if (schemeElement == null) {
			try {
				schemeElement = SchemeElement.createInstance(userId);
			} 
			catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
		}
		SchemeDevice device;
		if (schemeElement.getSchemeDevices().isEmpty()) {
			try {
				device = SchemeDevice.createInstance(userId);
			} 
			catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
		}
		else {
			device = (SchemeDevice)schemeElement.getSchemeDevices().iterator().next();
		}
		schemeElement.setName(scheme.getName());
		schemeElement.setDescription(scheme.getDescription());
		schemeElement.setSymbol(scheme.getSymbol());
		schemeElement.setScheme(scheme);
		targetPane.getCurrentPanel().getSchemeResource().setSchemeElement(schemeElement);

//		Rectangle oldrect = graph.getCellBounds(cells);
		ImageIcon icon = null;
		BitmapImageResource ir = scheme.getSymbol();
		if (ir != null) {
			icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}
		
		CreateUgo.create(targetPane.getGraph(), icon, scheme.getLabel(), device, blockports_in, blockports_out);
		CreateGroup action = new CreateGroup(targetPane);
		action.actionPerformed(null);

		schemeElement.setSchemeElements(action.getChildSchemeElements());
		schemeElement.setSchemeDevices(action.getChildSchemeDevices());
		schemeElement.setSchemeLinks(action.getChildSchemeLinks());
		device.setSchemePorts(action.getChildSchemePorts());
		device.setSchemeCablePorts(action.getChildSchemeCablePorts());
	}
}
