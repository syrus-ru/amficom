/**
 * $Id: LinkTypeController.java,v 1.1 2004/12/24 15:42:12 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import java.util.Iterator;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractLinkController;

/**
 * �������� �������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/24 15:42:12 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class LinkTypeController extends AbstractLinkController
{
	private static LinkTypeController instance = null;
	
	private LinkTypeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new LinkTypeController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isSelectionVisible(MapElement me)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ����� ��������� �� ���������, ���� ��� ��������� � ������ ����� ���������
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		throw new UnsupportedOperationException();
	}

	private static java.util.Map lineColors = new HashMap();
	
	static
	{
		lineColors.put(PhysicalLinkType.COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.UNBOUND, Color.RED);
	}

	public static Color getLineColor(String codename)
	{
		return (Color )lineColors.get(codename);
	}

	private static java.util.Map lineThickness = new HashMap();
	
	static
	{
		lineThickness.put(PhysicalLinkType.COLLECTOR, new Integer(3));
		lineThickness.put(PhysicalLinkType.TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.UNBOUND, new Integer(1));
	}

	public static int getLineThickness(String codename)
	{
		return ((Integer )lineThickness.get(codename)).intValue();
	}
	

	private static java.util.Map bindDimensions = new HashMap();
	
	static
	{
		bindDimensions.put(PhysicalLinkType.COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.UNBOUND, new IntDimension(0, 0));
	}

	public static IntDimension getBindDimension(String codename)
	{
		return (IntDimension )bindDimensions.get(codename);
	}
	
	public static Characteristic getCharacteristic(
			PhysicalLinkType me, 
			CharacteristicType cType)
	{
		for(Iterator it = me.getCharacteristics().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
				return ch;
		}
		return null;
	}

	/**
	 * ���������� ������� �����
	 */
	public void setLineSize (PhysicalLinkType link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� �����
	 */
	public int getLineSize (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * ���������� ��� �����
	 */
	public void setStyle (PhysicalLinkType link, String style)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(style);
	}

	/**
	 * �������� ��� �����
	 */
	public String getStyle (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * �������� ����� �����
	 */
	public Stroke getStroke (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * ���������� ����
	 */
	public void setColor (PhysicalLinkType link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * �������� ����
	 */
	public Color getColor(PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * ���������� ���� ��� ������� ������� �������
	 */
	public void setAlarmedColor (PhysicalLinkType link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * �������� ���� ��� ������� ������� �������
	 */
	public Color getAlarmedColor(PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * ���������� ������� ����� ��� ������ ������� �������
	 */
	public void setAlarmedLineSize (PhysicalLinkType link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� ����� ��� ������ ������� �������
	 */
	public int getAlarmedLineSize (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}
}
