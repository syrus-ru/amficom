/**
 * $Id: LinkTypeController.java,v 1.14 2005/04/13 11:13:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 * ���������� ���� ��������� �������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/04/13 11:13:47 $
 * @module mapviewclient_v1
 */
public final class LinkTypeController extends AbstractLinkController
		implements VisualManager
{
	/**
	 * ���-������� ������ ����� �����. ��� ����, ����� ������ {@link Color} 
	 * �� ���������� ������ ��� �� �������� ��� ������ 
	 * {@link #getColor(Identifier, PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map colorsHolder = new HashMap();
	/**
	 * ���-������� ������ ������� ������� ��� ����� �����. ��� ����, ����� 
	 * ������ {@link Color} �� ���������� ������ ��� �� �������� ��� ������ 
	 * {@link #getAlarmedColor(Identifier, PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map alarmedColorsHolder = new HashMap();

	/** ���-������� ������ ��� ����������������� ����� �����. */
	private static java.util.Map lineColors = new HashMap();
	/** ���-������� ������� ����� ��� ����������������� ����� �����. */
	private static java.util.Map lineThickness = new HashMap();
	/** ���-������� ����������� �������� ��� ����������������� ����� �����. */
	private static java.util.Map bindDimensions = new HashMap();

	/**
	 * Instance
	 */
	private static LinkTypeController instance = null;
	
	static
	{
		lineColors.put(PhysicalLinkType.COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.UNBOUND, Color.RED);

		lineThickness.put(PhysicalLinkType.COLLECTOR, new Integer(3));
		lineThickness.put(PhysicalLinkType.TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.UNBOUND, new Integer(1));

		bindDimensions.put(PhysicalLinkType.COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.UNBOUND, new IntDimension(0, 0));
	}

	/**
	 * Private constructor.
	 */
	private LinkTypeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new LinkTypeController();
		return instance;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	public ObjectResourceController getController() {
//		return PhysicalLinkTypeWrapper.getInstance();
		return null;
	}
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public String getToolTipText(MapElement mapElement)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isSelectionVisible(MapElement mapElement)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isElementVisible(MapElement mapElement, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public void paint(MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isMouseOnElement(MapElement mapElement, Point currentMousePoint)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ���� �� �������� ����� ��� ������������������ ���� �����.
	 * @param codename ������� ���
	 * @return ����
	 */
	public static Color getLineColor(String codename)
	{
		return (Color )lineColors.get(codename);
	}

	/**
	 * �������� ������� ����� �� �������� ����� ��� ������������������ 
	 * ���� �����.
	 * @param codename ������� ���
	 * @return �������
	 */
	public static int getLineThickness(String codename)
	{
		return ((Integer )lineThickness.get(codename)).intValue();
	}

	/**
	 * �������� ����������� �������� �� �������� ����� ��� ������������������ 
	 * ���� �����.
	 * @param codename ������� ���
	 * @return ����������� ��������
	 */
	public static IntDimension getBindDimension(String codename)
	{
		return (IntDimension )bindDimensions.get(codename);
	}
	
	/**
	 * ����� ������� ���� ����� �� ����.
	 * @param linkType ��� �����
	 * @param cType ��� ��������
	 * @return �������
	 */
	public static Characteristic getCharacteristic(
			PhysicalLinkType linkType, 
			CharacteristicType cType)
	{
		for(Iterator it = linkType.getCharacteristics().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
				return ch;
		}
		return null;
	}

	/**
	 * ���������� ������� �����. ������� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * � ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param size ������� �����
	 */
	public void setLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� �����. ������� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * � ������, ���� ������ �������� � �������� ���, ������� �������� 
	 * �� ��������� ({@link MapPropertiesManager#getThickness()}).
	 * @param linkType ��� �����
	 * @return ������� �����
	 */
	public int getLineSize(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * ���������� ����� ���� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param style �����
	 */
	public void setStyle(Identifier userId, PhysicalLinkType linkType, String style)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(style);
	}

	/**
	 * �������� ����� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStyle()}).
	 * @param linkType ��� �����
	 * @return �����
	 */
	public String getStyle(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * �������� ����� ���� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param linkType ��� �����
	 * @return �����
	 */
	public Stroke getStroke(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * ���������� ���� ���� �����. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param color ����
	 */
	public void setColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
		colorsHolder.put(linkType, color);
	}

	/**
	 * �������� ���� ���� �����. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getColor()}). ��� ������ ������ ������
	 * ��� ������� <code>linkType</code> ���������� ���� ���������� � 
	 * ���-������� {@link #colorsHolder} � ��� ��������� ������� ������� �� ���.
	 * @param linkType ��� �����
	 * @return ����
	 */
	public Color getColor(Identifier userId, PhysicalLinkType linkType)
	{
		Color color = (Color )colorsHolder.get(linkType);
		if(color == null)
		{
			CharacteristicType cType = getCharacteristicType(
					userId, 
					AbstractLinkController.ATTRIBUTE_COLOR);
			Characteristic ea = getCharacteristic(linkType, cType);

			if(ea == null)
				color = MapPropertiesManager.getColor();
			else
				color = new Color(Integer.parseInt(ea.getValue()));
			
			colorsHolder.put(linkType, color);
		}
		return color;
	}

	/**
	 * ���������� ���� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. 
	 * � ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param color ����
	 */
	public void setAlarmedColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
		alarmedColorsHolder.put(linkType, color);
	}

	/**
	 * �������� ���� ��� ���� ����� ��� ������� ������� �������. ���� 
	 * ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedColor()}). ��� ������ ������ ������
	 * ��� ������� <code>linkType</code> ���������� ���� ���������� � 
	 * ���-������� {@link #alarmedColorsHolder} � ��� ��������� ������� 
	 * ������� �� ���.
	 * @param linkType ��� �����
	 * @return ����
	 */
	public Color getAlarmedColor(Identifier userId, PhysicalLinkType linkType)
	{
		Color color = (Color )alarmedColorsHolder.get(linkType);
		if(color == null)
		{
			CharacteristicType cType = getCharacteristicType(
					userId, 
					AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
			Characteristic ea = getCharacteristic(linkType, cType);
			if(ea == null)
				color = MapPropertiesManager.getAlarmedColor();
			color = new Color(Integer.parseInt(ea.getValue()));

			alarmedColorsHolder.put(linkType, color);
		}
		return color;
	}

	/**
	 * ���������� ������� ����� ��� ���� ����� ��� ������ ������� �������. 
	 * ������� ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, 
	 * ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param size ������� �����
	 */
	public void setAlarmedLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� ����� ��� ���� ����� ��� ������ ������� �������. 
	 * ������� ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, 
	 * ���� ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * @param linkType ��� �����
	 * @return ������� �����
	 */
	public int getAlarmedLineSize(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * �������� ��� ����� �� �������� �����. � ������, ���� ������ ���� ���,
	 * ��������� �����.
	 * @param userId ������������
	 * @param codename ������� ���
	 * @return ��� �����
	 */
	public static PhysicalLinkType getPhysicalLinkType(
			Identifier userId,
			String codename)
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		try
		{
			Collection pTypes =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				PhysicalLinkType type = (PhysicalLinkType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching PhysicalLinkType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			LinkTypeController ltc = (LinkTypeController )LinkTypeController.getInstance();

			PhysicalLinkType pType = PhysicalLinkType.createInstance(
				userId,
				codename,
				LangModelMap.getString(codename),
				"",
				LinkTypeController.getBindDimension(codename));

			ltc.setLineSize(userId, pType, LinkTypeController.getLineThickness(codename));
			ltc.setColor(userId, pType, LinkTypeController.getLineColor(codename));

			MapStorableObjectPool.putStorableObject(pType);
			MapStorableObjectPool.flush(true);
			return pType;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �������� ������ ���� ����� �����.
	 * @param aContext �������� ����������
	 * @return ������ ����� ����� &lt;{@link PhysicalLinkType}&gt;
	 */
	public static Collection getPens(ApplicationContext aContext)
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		// make sure PhysicalLinkType.TUNNEL is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
		// make sure PhysicalLinkType.COLLECTOR is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.COLLECTOR);

		StorableObjectCondition pTypeCondition = new EquivalentCondition(
				ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);

		Collection list = null;
		try
		{
			list =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			list.remove(getDefaultUnboundPen(creatorId));
		}
		catch(Exception e)
		{
			list = Collections.EMPTY_LIST;
		}
		
		return list;
	}
	
	/**
	 * �������� ��� ����� �� ��������� ({@link PhysicalLinkType#TUNNEL}).
	 * @param creatorId ������������
	 * @return ��� �����
	 */
	public static PhysicalLinkType getDefaultPen(Identifier creatorId)
	{
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
	}

	/**
	 * �������� ��� ������������� ����� ({@link PhysicalLinkType#UNBOUND}).
	 * @param creatorId ������������
	 * @return ��� �����
	 */
	public static PhysicalLinkType getDefaultUnboundPen(Identifier creatorId)
	{
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.UNBOUND);
	}
}
