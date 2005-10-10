/*-
 * $Id: CreateRack.java,v 1.1 2005/10/10 12:40:27 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.ParentMap;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

public class CreateRack extends AbstractAction {
	private static final long serialVersionUID = 6635574632512492459L;
	UgoTabbedPane pane;
	private static int counter = 1;

	public CreateRack(UgoTabbedPane pane) {
		super(Constants.RACK);
		this.pane = pane;
	}
		
	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		
		for (Object cell : graph.getSelectionCells()) {
			if (cell instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup)cell;
				if (group.getType() == DeviceGroup.SCHEME) {
					Log.debugMessage(LangModelGraph.getString("Error.rack.scheme"), Level.WARNING); //$NON-NLS-1$
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							LangModelGraph.getString("Error.group.scheme"), //$NON-NLS-1$
							LangModelGraph.getString("error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return;
				}
			} else if (cell instanceof Rack) {
				Log.debugMessage(LangModelGraph.getString("Error.rack.rack"), Level.WARNING); //$NON-NLS-1$
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelGraph.getString("Error.group.rack"), //$NON-NLS-1$
						LangModelGraph.getString("error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			}
		}
		
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;

		UgoPanel p = this.pane.getCurrentPanel();
		if (p instanceof ElementsPanel) {
			SchemeResource res = ((ElementsPanel)p).getSchemeResource();
			try {
				SchemeElement element = SchemeObjectsFactory.createSchemeElement(res.getScheme());
				counter++;
				element.setName(LangModelGraph.getString("Title.rack") + counter);
				createRack(graph, cells, element);	
			} catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
		}
	}
	
	public static Rack createRack(SchemeGraph graph, Object[] cells, SchemeElement element) {
		Rack rack = createGroup(graph, cells, element.getId());
		
		// at last determine what elements it consists of
		Set<SchemeElement> childSchemeElements = new HashSet<SchemeElement>();
		Set<SchemeLink> childSchemeLinks = new HashSet<SchemeLink>();
		Set<SchemeDevice> childSchemeDevices = new HashSet<SchemeDevice>();
		
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DeviceGroup) {
				SchemeElement se = ((DeviceGroup) cells[i]).getSchemeElement();
				if (se != null) {
					childSchemeElements.add(se);
				}
			} 
			else if (cells[i] instanceof DeviceCell) {
				SchemeDevice dev = ((DeviceCell) cells[i]).getSchemeDevice();
				if (dev != null) {
					childSchemeDevices.add(dev);
				}
			} 
			else if (cells[i] instanceof DefaultLink) {
				SchemeLink link = ((DefaultLink) cells[i]).getSchemeLink();
				if (link != null) {
					childSchemeLinks.add(link);
				}
			}
		}
		
		try {
			for (SchemeDevice device : childSchemeDevices) {
				device.setParentSchemeElement(element, false);
			}
			for (SchemeLink link : childSchemeLinks) {
				link.setParentSchemeElement(element, false);
			}
			for (SchemeElement element2 : childSchemeElements) {
				element2.setParentSchemeElement(element, false);
			}
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		return rack;
	}
	
	static Rack createGroup(SchemeGraph graph, Object[] cells, Identifier id) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		Rack group = Rack.createInstance(null, viewMap, id);
		
		Map m = GraphConstants.createMap();
		GraphConstants.setSizeable(m, true);
		viewMap.put(group, m);
		
		ParentMap map = new ParentMap();
		for (int i = 0; i < cells.length; i++)
			map.addEntry(cells[i], group);
		graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null,
				map, null);
		graph.setSelectionCell(group);
		graph.selectionNotify();
		return group;
	}
}
