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
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

public class TunnelLayout implements PropertyChangeListener {
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoTabbedPane panel;
	private static final int RADIUS = 15;
	private static final int SPACE = 2;
	private int m, n;
	
	PhysicalLinkBinding binding;
	
	EllipseCell[][] cells;

	IntPoint activeCoordinates = null;
	
	PhysicalLinkAddEditor parent;

	public TunnelLayout(PhysicalLinkAddEditor parent) {
		this.parent = parent;

		this.internalContext.setDispatcher(new Dispatcher());

		this.panel = new UgoTabbedPane(this.internalContext);
		this.panel.getGraph().setGraphEditable(false);
		this.panel.getGraph().setAntiAliased(true);

		this.internalContext.getDispatcher().addPropertyChangeListener(
				ObjectSelectedEvent.TYPE,
				this);
	}

	public UgoTabbedPane getUgoPanel() {
		return this.panel;
	}

	protected void removeSelection() {
		this.panel.getGraph().clearSelection();

		for(int i = 0; i < this.m; i++) {
			for(int j = 0; j < this.n; j++) {
				GraphActions.setObjectBackColor(
						this.panel.getGraph(),
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
							this.panel.getGraph(),
							cell,
							Color.YELLOW);
					this.panel.getGraph().setSelectionCell(cell);

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
				// TODO нужна ли эта ботва?
				// this.panel.getGraph().setGraphChanged(true);
			}
			else
				if(ev.isSelected(ObjectSelectedEvent.ALL_DESELECTED)) {
					this.activeCoordinates = null;
				}
		}
	}

	public void setBinding(PhysicalLinkBinding binding) {
		this.binding = binding;
		if(binding == null)
			setDimension(0, 0);
		else
			setDimension(binding.getDimension().getWidth(), binding
					.getDimension().getHeight());
		updateElements();
	}

	private void setDimension(int m, int n) {
		this.m = m;
		this.n = n;

		this.panel.getGraph().removeAll();

		this.cells = new EllipseCell[m][n];

		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++) {
				Rectangle bounds = new Rectangle(
						(i + 1) * SPACE + 2 * i * RADIUS,
						(j + 1) * SPACE + 2 * j * RADIUS,
						2 * RADIUS,
						2 * RADIUS);
				this.cells[i][j] = addCell(this.panel.getGraph(), "", bounds);
			}
	}

	public void setActiveElement(Object or) {
		removeSelection();
		this.activeCoordinates = this.binding.getBinding(or);

		// TODO demo cheat!
//		if(n > 1 && m > 2)
//			this.activeCoordinates = new IntPoint(0, 2);
		//

		if(this.activeCoordinates != null)
			this.panel.getGraph().setSelectionCell(this.cells[this.activeCoordinates.x][this.activeCoordinates.y]);
	}
	
	public void setActiveCoordinates(IntPoint activeCoordinates) {
		removeSelection();
		this.activeCoordinates = activeCoordinates;
		this.panel.getGraph().setSelectionCell(this.cells[activeCoordinates.x][activeCoordinates.y]);
	}

	public IntPoint getActiveCoordinates() {
		return this.activeCoordinates;
	}

	public void updateElements() {
		int counter = 1;
		int limit = this.n * this.m;

		int istart = this.binding.isLeftToRight() ? 0 : this.m - 1;
		int jstart = this.binding.isTopToBottom() ? 0 : this.n - 1;

		int iend = this.m - 1 - istart;
		int jend = this.n - 1 - jstart;

		int iincrement = this.binding.isLeftToRight() ? 1 : -1;
		int jincrement = this.binding.isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while(counter <= limit) {
			GraphActions.setText(this.panel.getGraph(),this.cells[i][j], String.valueOf(counter++));
			if(counter > limit)
				break;
			if(this.binding.isHorizontalVertical()) {
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
