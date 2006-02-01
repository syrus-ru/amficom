/*-
 * $Id: SchemeCableLinkLayout.java,v 1.19 2006/02/01 14:24:30 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.ThreadCell;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.19 $, $Date: 2006/02/01 14:24:30 $
 * @module schemeclient
 */

public class SchemeCableLinkLayout extends DefaultStorableObjectEditor implements PropertyChangeListener {
	protected SchemeCableLink link;
	ApplicationContext aContext;
	
	private static final int FIBER_RADIUS = 8;
	private static final int GAP = 10;
	private int radius = 20;
	
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoTabbedPane panel;
//	private Map mapping = new HashMap();
	protected JScrollPane scrollPane;
	
	CharacteristicType colorType = null;

	public SchemeCableLinkLayout() {
		this.internalContext.setDispatcher(new Dispatcher());
		this.internalContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);

		this.panel = new UgoTabbedPane(this.internalContext);
		this.panel.getGraph().setGraphEditable(false);
		this.panel.getGraph().setAntiAliased(true);
		this.scrollPane = new JScrollPane(this.panel.getGraph());
		
		try {
			TypicalCondition condition = new TypicalCondition(CharacteristicTypeCodenames.COMMON_COLOUR, OperationSort.OPERATION_EQUALS, ObjectEntities.CHARACTERISTIC_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (!characteristicTypes.isEmpty()) {
				this.colorType = characteristicTypes.iterator().next();
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	public void setObject(Object or) {
		this.link = (SchemeCableLink) or;
//		this.mapping.clear();
		GraphActions.clearGraph(this.panel.getGraph());

		if (this.link != null) {
			try {
				List<SchemeCableThread> scts = ClientUtils.getSortedCableThreads(this.link);
				// в модуле всегда по 4 волокна
				// TODO ввести цвет модуля
				int nModules = (int)((scts.size() - .5) / 4.0) + 1;
				
				int tmp = (int) (2 * FIBER_RADIUS * Math.sqrt(Math.round((double) 
						scts.size() / (double) nModules + 0.499)));
				this.radius = 20;
				if (tmp > this.radius)
					this.radius = tmp;

				this.panel.getGraph().removeAll();
				createModules(nModules);
				createFibers(nModules, scts);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}
	
	public Object getObject() {
		return this.link;
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	private void createFibers(int nModules, final Collection fibers) {
		int nFibers = fibers.size();
		int moduleFibers = nFibers / nModules;
		int additionalFibers = nFibers - (nModules * moduleFibers);

		double angle = 2 * Math.PI / nModules;
		double inner_angle = 2 * Math.PI
				/ (moduleFibers + (additionalFibers == 0 ? 0 : 1));
		int r1 = this.radius;
		int r2 = (int) ((this.radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		Iterator it = fibers.iterator();
		for (int i = 0; i < nModules; i++) {
			int module_center_x = GAP + this.radius + (int) (r2 * (1 + Math.cos(i * angle)));
			int module_center_y = GAP + this.radius + (int) (r2 * (1 + Math.sin(i * angle)));
			for (int j = 0; j < (i < additionalFibers ? moduleFibers + 1 : moduleFibers); j++) {
				SchemeCableThread sct = (SchemeCableThread)it.next();
				
				int x = module_center_x
						+ (int) (this.radius / 2 * (Math.cos(j * inner_angle))) - FIBER_RADIUS;
				int y = module_center_y
						+ (int) (this.radius / 2 * (Math.sin(j * inner_angle))) - FIBER_RADIUS;
				Rectangle bounds = new Rectangle(x, y, 2 * FIBER_RADIUS,
						2 * FIBER_RADIUS);
				
				Color color = Color.WHITE;
				try {
					Characteristic ch = ClientUtils.getCharacteristic(sct.getCharacteristics(false), CharacteristicTypeCodenames.COMMON_COLOUR);
					if (ch != null) {
						color = new Color(Integer.parseInt(ch.getValue()));
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
				addThreadCell(this.panel.getGraph(), sct, bounds,	color);
			}
		}
	}
	
	private void createModules(int nModules) {
		double angle = 2 * Math.PI / nModules;
		int r1 = this.radius;
		int r2 = (int) ((this.radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		addCell(this.panel.getGraph(), "", new Rectangle(GAP - 8, GAP - 8, //$NON-NLS-1$
				16 + 2 * (r2 + r1), 16 + 2 * (r2 + r1)), Color.LIGHT_GRAY);
		addCell(this.panel.getGraph(), "", new Rectangle(GAP - 1, GAP - 1, //$NON-NLS-1$
				2 + 2 * (r2 + r1), 2 + 2 * (r2 + r1)), Color.WHITE);
		addCell(this.panel.getGraph(), "", new Rectangle(GAP //$NON-NLS-1$
				+ (int) Math.round(this.radius * 1.915) + 2, GAP
				+ (int) Math.round(this.radius * 1.915) + 2, 2 * (r2 - r1), 2 * (r2 - r1)),
				Color.GRAY);
		for (int i = 0; i < nModules; i++) {
			int x = GAP + (int) (r2 * (1 + Math.cos(i * angle)));
			int y = GAP + (int) (r2 * (1 + Math.sin(i * angle)));
			Rectangle bounds = new Rectangle(x, y, 2 * this.radius, 2 * this.radius);
			addCell(this.panel.getGraph(), "", bounds, Color.WHITE); //$NON-NLS-1$
		}
		this.panel.getCurrentPanel().setGraphSize(new Dimension(2 * GAP + 2 * (r2 + r1), 2 * GAP + 2 * (r2 + r1)));
	}

	public JComponent getGUI() {
		return this.scrollPane;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)evt;
			if (ev.isSelected(ObjectSelectedEvent.OTHER_OBJECT)) {
				Object obj = ev.getSelectedObject();
				if (obj instanceof ThreadCell) {
					ThreadCell cell = (ThreadCell)obj;
					SchemeCableThread sct = cell.getSchemeCableThread();
					
					Color color = Color.WHITE;
					try {
						Characteristic ch = ClientUtils.getCharacteristic(sct.getCharacteristics(false), CharacteristicTypeCodenames.COMMON_COLOUR);
						if (ch != null) {
							color = new Color(Integer.parseInt(ch.getValue()));
						}
					} catch (ApplicationException e) {
						Log.errorMessage(e);
					}
					
					this.panel.getGraph().setSelectionCell(cell);
					GraphActions.setObjectBackColor(this.panel.getGraph(), cell, color);
				} else if (obj instanceof EllipseCell) {
					this.panel.getGraph().clearSelection();
				}
			}
		}
	}

	private void addThreadCell(SchemeGraph graph, SchemeCableThread schemeCableThread,
			Rectangle bounds, Color color) {
		String name = ClientUtils.parseNumberedName(schemeCableThread.getName());

		Map viewMap = new HashMap();
		ThreadCell cell = ThreadCell.createInstance(name, bounds, color, viewMap, schemeCableThread);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
//		mapping.put(threadType, cell);
	}
	
//	public ThreadTypeCell getCell(CableThreadType ctt) {
//		return (ThreadTypeCell)mapping.get(ctt);
//	}

	private void addCell(SchemeGraph graph, Object userObject, Rectangle bounds,
			Color color) {
		Map viewMap = new HashMap();
		EllipseCell cell = new EllipseCell(userObject);
		Map map = GraphConstants.createMap();
		map.put(Constants.SELECTABLE, new Boolean(false));
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
	}
}
