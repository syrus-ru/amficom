/**
 * $Id: MapLinkElement.java,v 1.7 2004/09/21 14:56:16 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import java.util.HashMap;

/**
 * �������� �������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/09/21 14:56:16 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapLinkElement extends StubResource implements MapElement 
{
	protected String id = "";
	protected String name = "";
	protected String description = "";
	protected String mapId = "";

	protected String startNodeId;
	protected String endNodeId;

	protected Map map;
	
	/** ��������� ���� */
	protected MapNodeElement startNode;
	/** �������� ���� */
	protected MapNodeElement endNode;

	/** �������� ����������� */
	public java.util.Map attributes = new HashMap();

	/** ���� ��������� */
	protected boolean selected = false;

	/** ���� ����, ��� ������ ������ */
	protected boolean removed = false;

	/** ���� ������� ������� ������� */
	protected boolean alarmState = false;
	
	/**
	 * �������� ���� �������� ��������
	 */
	public boolean isRemoved()
	{
		return removed;
	}
	
	/**
	 * ���������� ���� �������� ��������
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public MapNodeElement getStartNode()
	{
		return this.startNode;
	}
	
	public void setStartNode(MapNodeElement startNode)
	{
		this.startNode = startNode;
	}
	
	public MapNodeElement getEndNode()
	{
		return this.endNode;
	}
	
	public void setEndNode(MapNodeElement endNode)
	{
		this.endNode = endNode;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDomainId()
	{
		return getMap().getDomainId();
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
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
			MapNodeElement smne = startNode;
			s2 =  ":\n" + "   " + LangModelMap.getString("From") + " " + smne.getName() + " [" + LangModel.getString("node" + smne.getTyp()) + "]";
			MapNodeElement emne = endNode;
			s3 = "\n" + "   " + LangModelMap.getString("To") + " " + emne.getName() + " [" + LangModel.getString("node" + emne.getTyp()) + "]";
		}
		catch(Exception e)
		{
		}
		return s1 + s2 + s3;
	}

	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(
				(startNode.getAnchor().getX() + endNode.getAnchor().getX()) / 2,
				(startNode.getAnchor().getY() + endNode.getAnchor().getY()) / 2);
	}

	/**
	 * �������� ������ ����� �� ��������� NodeElement
	 */
	public MapNodeElement getOtherNode(MapNodeElement node)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getOtherNode(" + node + ")");
		

		if ( getEndNode() == node )
			return getStartNode();
		if ( getStartNode() == node )
			return getEndNode();
		return null;
	}

	/**
	 * �������� ������� ���������
	 */
	public MapElementState getState()
	{
		return new MapLinkElementState(this);
	}

	/**
	 * ������������ ���������
	 */
	public void revert(MapElementState state)
	{
		MapLinkElementState mles = (MapLinkElementState )state;
		setName(mles.name);
		setDescription(mles.description);
		attributes = new HashMap(mles.attributes);
		setStartNode(mles.startNode);
		setEndNode(mles.endNode);
	}

//////////////////////////////////////////////////////////////////////////
// ��������� ���������

	/**
	 * ��������������� �������
	 */
//    private boolean toBoolean(String bool) 
//	{
//		return ((bool != null) && bool.equalsIgnoreCase("true"));
//    }
	
	/**
	 * ���������� ������� ����� ��� ���������
	 */
/*
	public void setSelectedLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("selected_thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"selected_thickness");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"selected_thickness");
			attributes.put("selected_thickness", ea);
		}
		ea.value = String.valueOf(size);
	}
*/
	/**
	 * �������� ������� ����� ��� ���������
	 */
/*
	public int getSelectedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("selected_thickness");
		if(ea == null)
			return DEF.selected_thickness;
		return Integer.parseInt(ea.value);
	}
*/
	/**
	 * ���������� ������� �����
	 */
	public void setLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"thickness");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"thickness");
			attributes.put("thickness", ea);
		}
		ea.value = String.valueOf(size);
	}

	/**
	 * �������� ������� �����
	 */
	public int getLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.value);
	}

	/**
	 * ���������� �����
	 */
/*
	public void setFont (Font font)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("font");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"font");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					font.getFontName(),
					"font");
			attributes.put("font", ea);
		}
		ea.value = font.getFontName();
	}
*/
	/**
	 * �������� �����
	 */
/*
	public Font getFont ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("font");
		if(ea == null)
			return MapPropertiesManager.getFont();
		return new Font(ea.value, 1, 12);
	}
*/
	/**
	 * ���������� ��� �����
	 */
	public void setStyle (String style)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"style");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					style,
					"style");
			attributes.put("style", ea);
		}
		ea.value = style;
	}

	/**
	 * �������� ��� �����
	 */
	public String getStyle ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.value;
	}

	/**
	 * �������� ����� �����
	 */
	public Stroke getStroke ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.value);

	}

	/**
	 * ���������� �������
	 */
/*
	public void setMetric (String metric)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("metric");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"metric");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					metric,
					"metric");
			attributes.put("metric", ea);
		}
		ea.value = metric;
	}
*/
	/**
	 * �������� �������
	 */
/*
	public String getMetric()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("metric");
		if(ea == null)
			return MapPropertiesManager.getMetric();
		return ea.value;
	}
*/
	/**
	 * ���������� ����
	 */
	public void setColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"color");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"color");
			attributes.put("color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
	}

	/**
	 * �������� ����
	 */
	public Color getColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.value));
	}

	/**
	 * ���������� ������� ������� �������
	 */
	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	/**
	 * �������� ������� ������� �������
	 */
	public boolean getAlarmState()
	{
		return alarmState;
	}

	/**
	 * ���������� ���� ��� ������� ������� �������
	 */
	public void setAlarmedColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"alarmed_color");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"alarmed_color");
			attributes.put("alarmed_color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
	}

	/**
	 * �������� ���� ��� ������� ������� �������
	 */
	public Color getAlarmedColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.value));
	}

	/**
	 * ���������� ������� ����� ��� ������ ������� �������
	 */
	public void setAlarmedLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"alarmed_thickness");
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"alarmed_thickness");
			attributes.put("alarmed_thickness", ea);
		}
		ea.value = String.valueOf(size);
	}

	/**
	 * �������� ������� ����� ��� ������ ������� �������
	 */
	public int getAlarmedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.value);
	}
	
}
