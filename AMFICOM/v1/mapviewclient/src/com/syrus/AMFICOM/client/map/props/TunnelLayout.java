package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkBinding;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.util.*;

import java.awt.*;

import com.jgraph.graph.*;
import com.jgraph.pad.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import javax.swing.JPanel;

public class TunnelLayout implements OperationListener
{
	private ApplicationContext internalContext = new ApplicationContext();
//	private SchemePanelNoEdition panel;
	private UgoPanel panel;
	private static final int RADIUS = 20;
	private static final int SPACE = 2;
	private int m, n;
	
	MapPhysicalLinkBinding binding;
	
	EllipseCell[][] cells;

	Point activeCoordinates = null;

	public TunnelLayout(MapPhysicalLinkBinding binding)
	{
		this();
		setBinding(binding);
	}

	public TunnelLayout()
	{
		internalContext.setDispatcher(new Dispatcher());

		panel = new UgoPanel(internalContext);
		panel.getGraph().setEditable(false);
//		panel = new SchemePanelNoEdition(internalContext);
//		panel.getGraph().setActualSize(new Dimension(0, 0));

		internalContext.getDispatcher().register(this, SchemeNavigateEvent.type);
	}

	public UgoPanel getPanel()
	{
		return panel;
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent )oe;
			if (ev.OTHER_OBJECT_SELECTED)
			{
				Object[] objs = (Object[])ev.getSource();
				panel.getGraph().removeSelectionCells();

				for (int k = 0; k < objs.length; k++)
				{
					if (objs[k] instanceof EllipseCell)
					{
						EllipseCell cell = (EllipseCell )objs[k];
						
						for (int i = 0; i < m; i++) 
						{
							for (int j = 0; j < n; j++) 
							{
								if(cells[i][j].equals(cell))
								{
									activeCoordinates = new Point(i, j);
									panel.getGraph().setSelectionCell(cell);
									return;
								}
							}
						}
						
					}
				}
			}
			else
			if(ev.SCHEME_ALL_DESELECTED)
			{
				activeCoordinates = null;
			}
		}
	}

	public void setBinding(MapPhysicalLinkBinding binding)
	{
		this.binding = binding;
		if(binding == null)
			setDimension(0, 0);
		else
			setDimension(binding.getDimension().width, binding.getDimension().height);
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
//						(3 * i + 1) * radius, 
//						(3 * j + 1) * radius, 
//						2 * radius, 
//						2 * radius);
				cells[i][j] = addCell(panel.getGraph(), "", bounds);
			}
	}

	public void setActiveElement(ObjectResource or)
	{
		activeCoordinates = binding.getBinding(or);
		panel.getGraph().setSelectionCell(cells[activeCoordinates.x][activeCoordinates.y]);
	}
	
	public ObjectResource getActiveElement()
	{
		return binding.getBound(activeCoordinates.x, activeCoordinates.y);
	}

	public void setActiveCoordinates(Point activeCoordinates)
	{
		this.activeCoordinates = activeCoordinates;
		panel.getGraph().setSelectionCell(cells[activeCoordinates.x][activeCoordinates.y]);
	}
	
	public Point getActiveCoordinates()
	{
		return activeCoordinates;
	}

	public void updateElements()
	{
		for (int i = 0; i < m; i++) 
		{
			for (int j = 0; j < n; j++) 
			{
				ObjectResource or = binding.getBound(i, j);
				if(or == null)
					cells[i][j].setUserObject("");
				else
					cells[i][j].setUserObject(or.getName());
			}
		}
	}

	EllipseCell addCell(SchemeGraph graph, Object userObject, Rectangle bounds)
	{
		Map viewMap = new HashMap();
		EllipseCell cell = new EllipseCell(userObject);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] {cell}, viewMap, null, null, null);
		
		return cell;
	}
}