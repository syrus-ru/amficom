package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.DEF;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import com.syrus.AMFICOM.Client.Resource.StubResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import java.util.Hashtable;

//A0A
public abstract class MapLinkElement extends StubResource implements MapElement 
{
	public String id = "";
	public String name = "";
	public String type_id = "";
	public String description = "";
	public String owner_id = "";
	public String mapContextID = "";
	public String PhysicalLinkID = "";

	public String startNode_id;
	public String endNode_id;

	protected MapContext mapContext;
	public MapNodeElement startNode;//начало
	public MapNodeElement endNode;//конец

	public Hashtable attributes = new Hashtable();

	protected boolean selected = false;

	public boolean show_alarmed = false;

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}
	
	public void setId(String myID)
	{
		id = myID;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void select()
	{
		selected = true;
	}

	public void deselect()
	{
		selected = false;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return mapContext.getLogicalNetLayer();
	}

	public MapContext getMapContext()
	{
		return mapContext;
	}

	public void setMapContext( MapContext myMapContext)
	{
		mapContext = myMapContext;
	}

	public boolean isMovable()
	{
		return true;
	}

	public String getToolTipText()
	{
		String s1 = name;
		String s2 = "";
		String s3 = "";
		try
		{
//			MapNodeElement smne = (MapNodeElement )getMapContext().getNode(startNode_id);
			MapNodeElement smne = startNode;
			s2 =  ":\n" + "   от " + smne.getName() + " [" + LangModel.getString("node" + smne.getTyp()) + "]";
//			MapNodeElement emne = (MapNodeElement )getMapContext().getNode(endNode_id);
			MapNodeElement emne = endNode;
			s3 = "\n" + "   до " + emne.getName() + " [" + LangModel.getString("node" + emne.getTyp()) + "]";
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		return s1 + s2 + s3;
	}

	public SxDoublePoint getAnchor()
	{
		return new SxDoublePoint(
				(startNode.getAnchor().getX() + endNode.getAnchor().getX()) / 2,
				(startNode.getAnchor().getY() + endNode.getAnchor().getY()) / 2);
	}
	
    public boolean toBoolean(String name) 
	{ 
		return ((name != null) && name.equalsIgnoreCase("true"));
    }

//////////////////////////////////////////////////////////////////////////
// Обработка атрибутов

//Установить толщину линии при выделении
	public void setSelectedLineSize (int mySize)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("selected_thickness");
		if(ea == null)
			return;
		ea.value = String.valueOf(mySize);
	}

//Получить толщину линии при выделении
	public int getSelectedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("selected_thickness");
		if(ea == null)
			return DEF.selected_thickness;
		return Integer.parseInt(ea.value);
	}

//Установить толщину линии
	public void setLineSize (int mySize)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
			return;
		ea.value = String.valueOf(mySize);
	}

//Получить толщину линии
	public int getLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
			return DEF.thickness;
		return Integer.parseInt(ea.value);
	}

//Установить шрифт
	public void setFont (Font font)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("font");
		if(ea == null)
			return;
		ea.value = font.getFontName();
	}

//Получить шрифт
	public Font getFont ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("font");
		if(ea == null)
			return DEF.font;
		return new Font(ea.value, 1, 12);
	}

//Установить вид линии
	public void setStyle (String style)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return;
		ea.value = style;
	}

//Получить вид линии
	public String getStyle ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return DEF.style;
		return ea.value;
	}

	public Stroke getStroke ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return DEF.stroke;

//		return DEF.stroke;

//		LineComboBox lineComboBox = new LineComboBox();
//		lineComboBox.setSelected(ea.value);
//		return lineComboBox.
		return LineComboBox.getStrokeByType(ea.value);

	}

	public void setMetric (String myMetric)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("metric");
		if(ea == null)
			return;
		ea.value = myMetric;
	}

	public String getMetric()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("metric");
		if(ea == null)
			return DEF.metric;
		return ea.value;
	}

	public void setColor (Color myColor)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
			return;
		ea.value = String.valueOf(myColor.getRGB());
	}

	public Color getColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
			return DEF.color;
		return new Color(Integer.parseInt(ea.value));
	}

	public void setShowSizeContext(boolean bool)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("show_length");
		if(ea == null)
			return;
		ea.value = String.valueOf(bool);
	}

	public boolean getShowSizeContext()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("show_length");
		if(ea == null)
			return DEF.show_length;
		return toBoolean(ea.value);
	}

	public void setAlarmState(boolean i)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed");
		if(ea == null)
		{
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(), 
					"Сигнал тревоги", 
					"false", 
					"alarmed");
			attributes.put("alarmed", ea);
//			return;
		}
		ea.value = String.valueOf(i);
	}

	public boolean getAlarmState()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed");
		if(ea == null)
		{
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(), 
					"Сигнал тревоги", 
					"false", 
					"alarmed");
			attributes.put("alarmed", ea);
//			return false;
		}
		return toBoolean(ea.value);
	}

	public void setShowAlarmed(boolean bool)
	{
		show_alarmed = bool;
/*		
		ElementAttribute ea = (ElementAttribute )attributes.get("show_alarmed");
		if(ea == null)
			return;
		ea.value = String.valueOf(bool);
*/
	}

	public boolean getShowAlarmed()
	{
		return show_alarmed;
/*		
		ElementAttribute ea = (ElementAttribute )attributes.get("show_alarmed");
		if(ea == null)
			return DEF.showAlarmState;
		return toBoolean(ea.value);
*/
	}

	public void setAlarmedColor (Color myColor)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
			return;
		ea.value = String.valueOf(myColor.getRGB());
	}

	public Color getAlarmedColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
			return DEF.alarmed_color;
		return new Color(Integer.parseInt(ea.value));
	}

	public void setAlarmedLineSize (int mySize)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
			return;
		ea.value = String.valueOf(mySize);
	}

	public int getAlarmedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
			return DEF.alarmed_thickness;
		return Integer.parseInt(ea.value);
	}
}