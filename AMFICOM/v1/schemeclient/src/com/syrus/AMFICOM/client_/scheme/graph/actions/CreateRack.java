/*-
 * $Id: CreateRack.java,v 1.9 2006/04/07 13:53:02 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.RACK;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.jgraph.graph.ParentMap;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
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
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
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
		
		Rack oldRack = null;
		
		for (Object cell : graph.getSelectionCells()) {
			if (cell instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup)cell;
				if (group.getType() == DeviceGroup.SCHEME) {
					Log.debugMessage(LangModelGraph.getString("Error.rack.scheme"), Level.WARNING); //$NON-NLS-1$
					JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
							LangModelGraph.getString("Error.group.scheme"), //$NON-NLS-1$
							LangModelGraph.getString("error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return;
				}
			} 
			else if (cell instanceof Rack) {
				oldRack = (Rack)cell;
//				Log.debugMessage(LangModelGraph.getString("Error.rack.rack"), Level.WARNING); //$NON-NLS-1$
//				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
//						LangModelGraph.getString("Error.group.rack"), //$NON-NLS-1$
//						LangModelGraph.getString("error"), //$NON-NLS-1$
//						JOptionPane.OK_OPTION);
//				return;
			}
		}
		
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;

		if (oldRack != null) {
			
		}
		
		UgoPanel p = this.pane.getCurrentPanel();
		if (p instanceof ElementsPanel) {
			SchemeResource res = ((ElementsPanel)p).getSchemeResource();
			try {
				SchemeElement element = SchemeObjectsFactory.createSchemeElement(res.getScheme());
				element.setName(LangModelGraph.getString("rack") + counter); //$NON-NLS-1$

				final EquipmentType rackEquipmentType = EquipmentType.valueOf(RACK);

				ProtoEquipment rackProto = null;
				final LinkedIdsCondition condition = new LinkedIdsCondition(rackEquipmentType, ObjectEntities.PROTOEQUIPMENT_CODE);
				try {
					Set<ProtoEquipment> rackProtos = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (!rackProtos.isEmpty()) {
						rackProto = rackProtos.iterator().next();
					}
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
				if (rackProto == null) {
					rackProto = SchemeObjectsFactory.createProtoEquipment(LangModelGraph.getString("rack"), rackEquipmentType);
				}
				element.setProtoEquipment(rackProto);
				counter++;
				createRack(graph, cells, element);
				// create ugo
				ApplicationContext internalContext =  new ApplicationContext();
				internalContext.setDispatcher(new Dispatcher());
				SchemeGraph invisibleGraph = new UgoTabbedPane(internalContext).getGraph();
				invisibleGraph.setMakeNotifications(false);
				CreateUgo.createRackUgo(element, invisibleGraph);
				
				SchemeImageResource imres = element.getUgoCell();
				if (imres == null) {
					imres = SchemeObjectsFactory.createSchemeImageResource();
					element.setUgoCell(imres);
				}
				imres.setData((List)invisibleGraph.getArchiveableState());
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
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
			Log.errorMessage(e);
		}
		return rack;
	}
	
	static Rack createGroup(SchemeGraph graph, Object[] cells, Identifier id) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		Rack group = Rack.createInstance(null, viewMap, id);
		
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
