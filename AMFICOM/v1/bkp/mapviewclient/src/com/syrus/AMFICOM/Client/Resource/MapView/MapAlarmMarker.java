package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class MapAlarmMarker extends MapMarker
{
	static final public String typ = "mapalarmmarker";
	public Object descriptor;

	public String link_id;

	protected Image icon2 = null;// used to display alarmed marker while blinking

	public MapAlarmMarker(
			String id, 
			Map map,
			Rectangle bounds, 
			String imageID,
			double len, 
			MapPathElement path,
			String link_id)
	{
		super(id, map, bounds, imageID, len, path);

		setImageId("images/alarm_bell_red.gif");
		this.name = LangModelMap.getString("Alarm");
		this.link_id = link_id;

		ImageIcon imageIcon = new ImageIcon("images/alarm_bell_green.gif");
		icon2 = imageIcon.getImage().getScaledInstance(
				(int )getBounds().getWidth(),
				(int )getBounds().getHeight(),
				Image.SCALE_SMOOTH);
	}

	public boolean isMovable()
	{
		return false;
	}

	public String getToolTipText()
	{
		String s1 = LangModelMap.getString("Alarm") + " " + getName() + 
				" (" + LangModelMap.getString("Path_lowercase") + " " + transmissionPath.getName() + 
				") " + LangModelMap.getString("Distance_lowercase") + " - " + getModel().getColumnValue("Length_1");

		return s1;
	}

	//рисуем маркер
	public void paint (Graphics g)
	{
		MapCoordinatesConverter converter = getMap().getConverter();
	
		Point p = converter.convertMapToScreen(getAnchor());
		
		int width = (int )getBounds().width;
		int height = (int )getBounds().height;

		Graphics2D pg = (Graphics2D )g;
		pg.setStroke(MapPropertiesManager.getSelectionStroke());

		if ( MapPropertiesManager.isShowAlarmState() )
		{
			pg.drawImage(
					icon,
					p.x - width / 2,
					p.y - height / 2,
					null);
		}
		else
		{
			pg.drawImage(
					icon2,
					p.x - width / 2,
					p.y - height / 2,
					null);
		}

		//Если выбрано то рисуем прямоугольник
		if (isSelected())
		{
			pg.setColor(MapPropertiesManager.getSelectionColor());
			pg.drawRect( 
				p.x - width / 2,
				p.y - height / 2,
				width,
				height);
		}
	}

	public String getTyp()
	{
		return typ;
	}

}
