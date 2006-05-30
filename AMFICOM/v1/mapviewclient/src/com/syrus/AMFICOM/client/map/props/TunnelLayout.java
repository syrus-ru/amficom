/*-
 * $$Id: TunnelLayout.java,v 1.30 2006/05/30 11:27:00 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.Color;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * @version $Revision: 1.30 $, $Date: 2006/05/30 11:27:00 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class TunnelLayout implements PropertyChangeListener {
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoTabbedPane ugoTabbedPane;
	private static final int RADIUS = 15;
	private static final int SPACE = 2;
	private int m, n;
	
	PipeBlock pipeBlock;
	
	EllipseCell[][] cells;

	IntPoint activeCoordinates = null;
	
	PhysicalLinkAddEditor parent;

	public TunnelLayout(PhysicalLinkAddEditor parent) {
		this.parent = parent;

		this.internalContext.setDispatcher(new Dispatcher());

		this.ugoTabbedPane = new UgoTabbedPane(this.internalContext);
		this.ugoTabbedPane.setEditable(false);
		this.ugoTabbedPane.getGraph().setAntiAliased(true);
		this.ugoTabbedPane.getGraph().setMakeNotifications(true);

		this.internalContext.getDispatcher().addPropertyChangeListener(
				ObjectSelectedEvent.TYPE,
				this);
		
		setEnabled(false);
	}

	public UgoTabbedPane getUgoPanel() {
		return this.ugoTabbedPane;
	}

	protected void removeSelection() {
		this.ugoTabbedPane.getGraph().clearSelection();

		for(int i = 0; i < this.m; i++) {
			for(int j = 0; j < this.n; j++) {
				GraphActions.setObjectBackColor(
						this.ugoTabbedPane.getGraph(),
						this.cells[i][j],
						Color.WHITE);
			}
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent )pce;
			if(ev.isSelected(ObjectSelectedEvent.OTHER_OBJECT)) {
				Object obj = ev.getSelectedObject();

				removeSelection();

				if(obj instanceof EllipseCell) {
					EllipseCell cell = (EllipseCell )obj;
					GraphActions.setObjectBackColor(
							this.ugoTabbedPane.getGraph(),
							cell,
							Color.YELLOW);
					this.ugoTabbedPane.getGraph().setSelectionCell(cell);

					boolean found = false;
					for(int i = 0; i < this.m && !found; i++) {
						for(int j = 0; j < this.n && !found; j++)
							if(this.cells[i][j].equals(cell)) {
								this.activeCoordinates = new IntPoint(i, j);
								found = true;
								this.parent.cableBindingSelected(i, j);
							}
					}

				}
			}
			else
				if(ev.isSelected(ObjectSelectedEvent.ALL_DESELECTED)) {
					this.activeCoordinates = null;
				}
		}
	}

	public void setPipeBlock(PipeBlock pipeBlock) {
		this.pipeBlock = pipeBlock;
		if(pipeBlock == null)
			setDimension(0, 0);
		else
			setDimension(pipeBlock.getDimension().getWidth(), pipeBlock
					.getDimension().getHeight());
		updateElements();
	}

	public void setDimension(int m, int n) {
		this.m = m;
		this.n = n;

		this.ugoTabbedPane.getGraph().removeAll();

		this.cells = new EllipseCell[m][n];

		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++) {
				Rectangle bounds = new Rectangle(
						(i + 1) * SPACE + 2 * i * RADIUS,
						(j + 1) * SPACE + 2 * j * RADIUS,
						2 * RADIUS,
						2 * RADIUS);
				this.cells[i][j] = addCell(this.ugoTabbedPane.getGraph(), "", bounds); //$NON-NLS-1$
			}
	}

	public void setActiveElement(Object or) {
		removeSelection();
		this.activeCoordinates = this.pipeBlock.getBinding(or);

		if(this.activeCoordinates != null) {
			EllipseCell cell = this.cells[this.activeCoordinates.x][this.activeCoordinates.y];
			GraphActions.setObjectBackColor(
					this.ugoTabbedPane.getGraph(),
					cell,
					Color.YELLOW);
			this.ugoTabbedPane.getGraph().setSelectionCell(cell);
		}
	}
	
	public void setActiveCoordinates(IntPoint activeCoordinates) {
		removeSelection();
		this.activeCoordinates = activeCoordinates;
		this.ugoTabbedPane.getGraph().setSelectionCell(this.cells[activeCoordinates.x][activeCoordinates.y]);
	}

	public IntPoint getActiveCoordinates() {
		return this.activeCoordinates;
	}

	public void setEnabled(boolean b) {
		this.ugoTabbedPane.getGraph().setEnabled(b);
	}
	
	public void updateElements() {
		if(this.pipeBlock == null) {
			return;
		}
		int counter = 1;
		int limit = this.n * this.m;

		int istart = this.pipeBlock.isLeftToRight() ? 0 : this.m - 1;
		int jstart = this.pipeBlock.isTopToBottom() ? 0 : this.n - 1;

		int iend = this.m - 1 - istart;
		int jend = this.n - 1 - jstart;

		int iincrement = this.pipeBlock.isLeftToRight() ? 1 : -1;
		int jincrement = this.pipeBlock.isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while(counter <= limit) {
			GraphActions.setText(this.ugoTabbedPane.getGraph(),this.cells[i][j], String.valueOf(counter++));
			if(counter > limit)
				break;
			if(this.pipeBlock.isHorizontalVertical()) {
				if(i == iend) {
					i = istart;
					j += jincrement;
				}
				else
					i += iincrement;
			}
			else {
				if(j == jend) {
					j = jstart;
					i += iincrement;
				}
				else
					j += jincrement;
			}
		}
	}

	EllipseCell addCell(SchemeGraph graph, Object userObject, Rectangle bounds) {
		Map viewMap = new HashMap();
		EllipseCell cell = new EllipseCell(userObject);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, false);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] {cell}, viewMap, null, null, null);
		
		return cell;
	}
}
