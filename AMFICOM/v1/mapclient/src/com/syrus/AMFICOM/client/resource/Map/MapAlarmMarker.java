package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Configure.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.*;

//A0A
public class MapAlarmMarker extends MapMarker
{
	static final public String typ = "mapalarmmarker";
	public Object descriptor;

	public String link_id;

	public boolean showAlarmState = true;

	protected Image icon2 = null;// used to display alarmed marker while blinking

	public MapAlarmMarker(
			String myID, 
			MapContext myMapContext,
			Rectangle myBounds, 
			String myImageID,
			double mylen, 
			MapTransmissionPathElement path,
			String link_id)
	{
		super(myID, myMapContext, myBounds, myImageID, mylen, path);

		setImageID("images/alarm_bell_red.gif");
		this.name = "Сигнал тревоги";
		this.link_id = link_id;

		ImageIcon myImageIcon = new ImageIcon("images/alarm_bell_green.gif");
		icon2 = myImageIcon.getImage().getScaledInstance(
				(int)getBounds().getWidth(),
				(int)getBounds().getHeight(),
				Image.SCALE_SMOOTH);
	}

	public boolean isMovable()
	{
		return false;
	}

	public String getToolTipText()
	{
		String s1 = "Сигнал тревоги " + getName() + 
				" (путь " + transmissionPath.getName() + 
				") дистанция - " + getModel().getColumnValue("Length_1");

		return s1;
	}

	//рисуем маркер
	public void paint (Graphics g)
	{
		LogicalNetLayer lnl = getLogicalNetLayer();
	
		Point p = lnl.convertMapToScreen( getAnchor());

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

		if ( this.getShowAlarmed() )
		{
			pg.drawImage(
					icon,
					p.x - icon.getWidth(lnl) / 2,
					p.y - icon.getHeight(lnl) / 2,
					lnl);
		}
		else
		{
			pg.drawImage(
					icon2,
					p.x - icon.getWidth(lnl) / 2,
					p.y - icon.getHeight(lnl) / 2,
					lnl);
		}

		//Если выбрано то рисуем прямоугольник
		if (isSelected())
		{
			pg.setColor( Color.red);
			pg.drawRect( 
				p.x - icon.getWidth(lnl) / 2,
				p.y - icon.getHeight(lnl) / 2,
				icon.getWidth(lnl),
				icon.getHeight(lnl));
		}
	}

	public void setShowAlarmed(boolean bool)
	{
		showAlarmState = bool;
	}

	public boolean getShowAlarmed()
	{
		return showAlarmState;
	}

	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		int mode = getLogicalNetLayer().getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
//A0A
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();

		}//SwingUtilities.isLeftMouseButton(me)
		return strategy;
	}

	public String getTyp()
	{
		return typ;
	}

}