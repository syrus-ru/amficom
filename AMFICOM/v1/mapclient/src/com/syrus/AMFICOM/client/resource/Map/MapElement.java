package com.syrus.AMFICOM.Client.Resource.Map;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;

//Интерфейс для всех элементов карты

public interface MapElement 
{
	public void paint (Graphics g);
	public void paint (Graphics g, Point myPoint);

	public boolean isSelected();
	public void select();
	public void deselect();
	public boolean isMouseOnThisObject(Point currentMousePoint);
	public String getId();
	public void setId(String myID);
	public void move (double deltaX, double deltaY);
	public MapStrategy getMapStrategy( 
		ApplicationContext aContext,
		LogicalNetLayer logicalNetLayer,
        MouseEvent me,
        Point sourcePoint);

	public JPopupMenu getContextMenu(JFrame mainFrame);
	public LogicalNetLayer getLogicalNetLayer();
	public MapContext getMapContext();
	public void setMapContext( MapContext myMapContext);
	public boolean isMovable();
	public String getToolTipText();

	public SxDoublePoint getAnchor();
	
	public Object clone(DataSourceInterface dataSource);
}

