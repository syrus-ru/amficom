package com.syrus.AMFICOM.Client.Map.Props;

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
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.syrus.AMFICOM.map.IntDimension;

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

		internalContext.setDispatcher(new Dispatcher());

		panel = new UgoPanel(internalContext);
		panel.getGraph().setGraphEditable(false);

		internalContext.getDispatcher().register(this, SchemeNavigateEvent.type);
	}

	public UgoPanel getPanel()
	{
		return panel;
	}

	protected void removeSelection()
	{
		panel.getGraph().removeSelectionCells();

		for (int i = 0; i < m; i++) 
		{
			for (int j = 0; j < n; j++) 
			{
				GraphActions.setObjectBackColor(panel.getGraph(), cells[i][j], Color.WHITE);
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
						GraphActions.setObjectBackColor(panel.getGraph(), cell, Color.YELLOW);
						panel.getGraph().setSelectionCell(cell);
						
						boolean found = false;
						for (int i = 0; i < m && !found; i++) 
						{
							for (int j = 0; j < n && !found; j++) 
								if(cells[i][j].equals(cell))
								{
									activeCoordinates = new IntPoint(i, j);
									found = true;
									parent.cableBindingSelected(i, j);
								}
						}
						
					}
				}
				panel.getGraph().setGraphChanged(true);
			}
			else
			if(ev.SCHEME_ALL_DESELECTED)
			{
				activeCoordinates = null;
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

		panel.getGraph().removeAll();
		
		cells = new EllipseCell[m][n];

		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
			{
				Rectangle bounds = new Rectangle(
						(i + 1) * SPACE + 2 * i * RADIUS,
						(j + 1) * SPACE + 2 * j * RADIUS,
						2 * RADIUS,
						2 * RADIUS);
				cells[i][j] = addCell(panel.getGraph(), "", bounds);
			}
	}

	public void setActiveElement(Object or)
	{
		removeSelection();
		activeCoordinates = binding.getBinding(or);
		if(activeCoordinates != null)
			panel.getGraph().setSelectionCell(cells[activeCoordinates.x][activeCoordinates.y]);
	}
	
	public void setActiveCoordinates(IntPoint activeCoordinates)
	{
		removeSelection();
		this.activeCoordinates = activeCoordinates;
		panel.getGraph().setSelectionCell(cells[activeCoordinates.x][activeCoordinates.y]);
	}
	
	public IntPoint getActiveCoordinates()
	{
		return activeCoordinates;
	}

	public void updateElements()
	{
		int counter = 1;
		int limit = n * m;

		int istart = binding.isLeftToRight() ? 0 : m - 1;
		int jstart = binding.isTopToBottom() ? 0 : n - 1;

		int iend = m - 1 - istart;
		int jend = n - 1 - jstart;

		int iincrement = binding.isLeftToRight() ? 1 : -1;
		int jincrement = binding.isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while(true)
		{
			GraphActions.setText(panel.getGraph(),cells[i][j], String.valueOf(counter++));
			if(counter > limit)
				break;
			if(binding.isHorizontalVertical())
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
