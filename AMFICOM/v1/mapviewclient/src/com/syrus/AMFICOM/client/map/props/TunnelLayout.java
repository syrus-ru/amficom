package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.GraphActions;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

public class TunnelLayout implements OperationListener
{
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoPanel panel;
	private static final int RADIUS = 20;
	private static final int SPACE = 2;
	private int m, n;
	
	PhysicalLinkBinding binding;
	
	EllipseCell[][] cells;

	IntPoint activeCoordinates = null;
	
	MapLinkBindPanel parent;

	public TunnelLayout(MapLinkBindPanel parent)
	{
		this.parent = parent;

		this.internalContext.setDispatcher(new Dispatcher());

		this.panel = new UgoPanel(this.internalContext);
		this.panel.getGraph().setGraphEditable(false);

		this.internalContext.getDispatcher().register(this, SchemeNavigateEvent.type);
	}

	public UgoPanel getPanel()
	{
		return this.panel;
	}

	protected void removeSelection()
	{
		this.panel.getGraph().removeSelectionCells();

		for (int i = 0; i < this.m; i++) 
		{
			for (int j = 0; j < this.n; j++) 
			{
				GraphActions.setObjectBackColor(this.panel.getGraph(), this.cells[i][j], Color.WHITE);
			}
		}
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent )oe;
			if (ev.OTHER_OBJECT_SELECTED)
			{
				Object[] objs = (Object[])ev.getSource();
				removeSelection();

				for (int k = 0; k < objs.length; k++)
				{
					if (objs[k] instanceof EllipseCell)
					{
						EllipseCell cell = (EllipseCell )objs[k];
						GraphActions.setObjectBackColor(this.panel.getGraph(), cell, Color.YELLOW);
						this.panel.getGraph().setSelectionCell(cell);
						
						boolean found = false;
						for (int i = 0; i < this.m && !found; i++) 
						{
							for (int j = 0; j < this.n && !found; j++) 
								if(this.cells[i][j].equals(cell))
								{
									this.activeCoordinates = new IntPoint(i, j);
									found = true;
									this.parent.cableBindingSelected(i, j);
								}
						}
						
					}
				}
				this.panel.getGraph().setGraphChanged(true);
			}
			else
			if(ev.SCHEME_ALL_DESELECTED)
			{
				this.activeCoordinates = null;
			}
		}
	}

	public void setBinding(PhysicalLinkBinding binding)
	{
		this.binding = binding;
		if(binding == null)
			setDimension(0, 0);
		else
			setDimension(binding.getDimension().getWidth(), binding.getDimension().getHeight());
		updateElements();
	}

	private void setDimension(int m, int n)
	{
		this.m = m;
		this.n = n;

		this.panel.getGraph().removeAll();
		
		this.cells = new EllipseCell[m][n];

		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
			{
				Rectangle bounds = new Rectangle(
						(i + 1) * SPACE + 2 * i * RADIUS,
						(j + 1) * SPACE + 2 * j * RADIUS,
						2 * RADIUS,
						2 * RADIUS);
				this.cells[i][j] = addCell(this.panel.getGraph(), "", bounds);
			}
	}

	public void setActiveElement(Object or)
	{
		removeSelection();
		this.activeCoordinates = this.binding.getBinding(or);
		if(this.activeCoordinates != null)
			this.panel.getGraph().setSelectionCell(this.cells[this.activeCoordinates.x][this.activeCoordinates.y]);
	}
	
	public void setActiveCoordinates(IntPoint activeCoordinates)
	{
		removeSelection();
		this.activeCoordinates = activeCoordinates;
		this.panel.getGraph().setSelectionCell(this.cells[activeCoordinates.x][activeCoordinates.y]);
	}
	
	public IntPoint getActiveCoordinates()
	{
		return this.activeCoordinates;
	}

	public void updateElements()
	{
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

		while(true)
		{
			GraphActions.setText(this.panel.getGraph(),this.cells[i][j], String.valueOf(counter++));
			if(counter > limit)
				break;
			if(this.binding.isHorizontalVertical())
			{
				if(i == iend)
				{
					i = istart;
					j += jincrement;
				}
				else
					i += iincrement;
			}
			else
			{
				if(j == jend)
				{
					j = jstart;
					i += iincrement;
				}
				else
					j += jincrement;
			}
		}
	}

	EllipseCell addCell(SchemeGraph graph, Object userObject, Rectangle bounds)
	{
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
