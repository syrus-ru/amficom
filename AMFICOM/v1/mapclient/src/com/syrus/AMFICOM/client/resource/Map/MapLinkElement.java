/**
 * $Id: MapLinkElement.java,v 1.14 2004/12/07 17:02:03 krupenn Exp $
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
 * @version $Revision: 1.14 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapLinkElement extends StubResource implements MapElement 
{
	/**
	 * @deprecated
	 */
	protected String id = "";
	
	/**
	 * @deprecated
	 */
	protected String name = "";
	
	/**
	 * @deprecated
	 */
	protected String description = "";
	
	/**
	 * @deprecated
	 */
	protected String mapId = "";

	/**
	 * @deprecated
	 */
	protected String startNodeId;

	/**
	 * @deprecated
	 */
	protected String endNodeId;

	protected Map map;
	
	/** ��������� ���� */
	/**
	 * @deprecated
	 */
	protected MapNodeElement startNode;
	/** �������� ���� */
	/**
	 * @deprecated
	 */
	protected MapNodeElement endNode;

	/** �������� ����������� */
	/**
	 * @deprecated
	 */
	public java.util.Map attributes = new HashMap();

	/** ���� ��������� */
	protected boolean selected = false;

	/** ���� ����, ��� ������ ������ */
	protected boolean removed = false;

	/** ���� ������� ������� ������� */
	protected boolean alarmState = false;
	
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

	/**
	 * @deprecated
	 */
	public String getDomainId()
	{
		return this.getMap().getDomainId();
	}
	
	public String getName()
	{
		return "\"" + getStartNode().getName() + " - " + getEndNode().getName() + "\"";//name;
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
		getMap().setSelected(this, selected);
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
		if(map != null)
			this.mapId = map.getId();
	}

	/**
	 * @deprecated
	 */
	public String getToolTipText()
	{
		String s1 = name;
		String s2 = "";
		String s3 = "";
		try
		{
			MapNodeElement smne = startNode;
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getTyp()) 
				+ "]";
			MapNodeElement emne = endNode;
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getTyp()) 
				+ "]";
		}
		catch(Exception e)
		{
			Environment.log(
					Environment.LOG_LEVEL_FINER, 
					"method call", 
					getClass().getName(), 
					"getToolTipText()", e);
			
		}
		return s1 + s2 + s3;
	}

	/**
	 * @deprecated
	 */
	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(
			(startNode.getAnchor().getX() + endNode.getAnchor().getX()) / 2,
			(startNode.getAnchor().getY() + endNode.getAnchor().getY()) / 2);
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(
			(getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
			(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	/**
	 * �������� ������ ����� �� ��������� NodeElement
	 */
	public MapNodeElement getOtherNode(MapNodeElement node)
	{
		Environment.log(
			Environment.LOG_LEVEL_FINER, 
			"method call", 
			getClass().getName(), 
			"getOtherNode(" + node + ")");
		

		if ( this.getEndNode().equals(node) )
			return getStartNode();
		if ( this.getStartNode().equals(node) )
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
		this.setName(mles.name);
		this.setDescription(mles.description);
		attributes = new HashMap(mles.attributes);
		this.setStartNode(mles.startNode);
		this.setEndNode(mles.endNode);
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
	 * ���������� ������� �����
	 * @deprecated
	 */
	public void setLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"thickness");
			if(eat == null)
				return;
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
	 * @deprecated
	 */
	public int getLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.value);
	}

	/**
	 * ���������� ��� �����
	 * @deprecated
	 */
	public void setStyle (String style)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"style");
			if(eat == null)
				return;
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
	 * @deprecated
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
	 * @deprecated
	 */
	public Stroke getStroke ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.value);

	}

	/**
	 * ���������� ����
	 * @deprecated
	 */
	public void setColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"color");
			if(eat == null)
				return;
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
	 * @deprecated
	 */
	public Color getColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.value));
	}

	/**
	 * ���������� ���� ��� ������� ������� �������
	 * @deprecated
	 */
	public void setAlarmedColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"alarmed_color");
			if(eat == null)
				return;
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
	 * @deprecated
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
	 * @deprecated
	 */
	public void setAlarmedLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"alarmed_thickness");
			if(eat == null)
				return;
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
	 * @deprecated
	 */
	public int getAlarmedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.value);
	}
	
}
