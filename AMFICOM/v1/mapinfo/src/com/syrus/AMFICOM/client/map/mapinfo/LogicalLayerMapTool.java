package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.tools.AbstractConstrainedAction;
import com.mapinfo.beans.vmapj.MapMouseEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.UI.MapKeyAdapter;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseListener;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseMotionListener;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class LogicalLayerMapTool 
	extends AbstractConstrainedAction	
	implements
		 com.mapinfo.beans.tools.MapTool, 
		 com.mapinfo.beans.vmapj.MapMouseListener,	
		 java.awt.event.KeyListener, 
		 com.mapinfo.beans.vmapj.MapPainter, 
//		 com.mapinfo.beans.tools.ToolTipTextSetter,
		 java.io.Serializable
{
	protected MouseListener ml = null;
	protected MouseMotionListener mml = null;
	protected MapKeyAdapter mka = null;

	LogicalNetLayer lnl = null;

	protected Cursor cursor = new Cursor(java.awt.Cursor.DEFAULT_CURSOR);

	protected boolean  selected = false;
	protected boolean  initialized = false;

	public LogicalLayerMapTool(LogicalNetLayer lnl)
	{
		super();
		this.lnl = lnl;
		if(ml == null)
		{
			ml = new MapMouseListener(lnl);
		}
		if(mml == null)
		{
			mml = new MapMouseMotionListener(lnl);
		}
		if(mka == null)
		{
			mka = new MapKeyAdapter(lnl);
		}
	}

	public void	setSelected(boolean	bSelected) throws PropertyVetoException
	{
	    // We want to allow	a selection	to be vetoed by VisualMapJ (or any
		// other listener)
		fireVetoableChange(
				"Selected",
				selected,
				bSelected);

		// if we got here, no veto listeners disagreed with	the	change
		selected = bSelected;

		firePropertyChange(
				"Selected", 
				new Boolean(!bSelected),
				new	Boolean(bSelected) );
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void	setCursor(Cursor cursor)
	{
		lnl.setCursor(cursor);;
	}

	public Cursor getCursor()
	{
		return lnl.getCursor();
	}

	public void mouseEntered(MapMouseEvent	e)
	{
		ml.mouseEntered(e);
	}
	public void mouseExited(MapMouseEvent e)
	{
		ml.mouseExited(e);
	}
	public void mousePressed(MapMouseEvent	e) 
	{
		ml.mousePressed(e);
	}
	public void mouseReleased(MapMouseEvent e) 
	{
		ml.mouseReleased(e);
	}
	public void mouseClicked(MapMouseEvent	e)
	{
		ml.mouseClicked(e);
	}
	public void mouseDragged(MapMouseEvent	e)
	{
		mml.mouseDragged(e);
	}
	public void mouseMoved(MapMouseEvent e)
	{
		mml.mouseMoved(e);
	}

	public void paintOnMap(Graphics	g)
	{
		lnl.paint(g);
	}

	public void	keyTyped(KeyEvent e) 
	{
		mka.keyTyped(e);
	}
	public void	keyPressed(KeyEvent	e) 
	{
		mka.keyPressed(e);
	}
	public void	keyReleased(KeyEvent e)
	{
		mka.keyReleased(e);
	}

//	public String getToolTipText(MapMouseEvent e)
//	{
//		return null;
//	}
}

