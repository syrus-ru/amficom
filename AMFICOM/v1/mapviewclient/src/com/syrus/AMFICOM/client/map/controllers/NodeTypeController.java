/**
 * $Id: NodeTypeController.java,v 1.18 2005/05/18 14:59:46 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * контроллер типа сетевого узла.
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/05/18 14:59:46 $
 * @module mapviewclient_v1
 */
public class NodeTypeController extends AbstractNodeController
{

	/**
	 * Instance.
	 */
	private static NodeTypeController instance = null;

	/** Хэш-таблица имен пиктограмм для предустановленных типов узлов. */
	private static java.util.Map imageFileNames = new HashMap();
	
	static
	{
		imageFileNames.put(SiteNodeType.DEFAULT_UNBOUND, "images/unbound.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_ATS, "images/ats.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_BUILDING, "images/building.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_PIQUET, "images/piquet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_WELL, "images/well.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_CABLE_INLET, "images/cableinlet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_TOWER, "images/tower.gif");
	}

	/**
	 * Private constructor.
	 */
	protected NodeTypeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new NodeTypeController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since SiteNodeType is not really a Map Element
	 */
//	public boolean isSelectionVisible(MapElement me)
//	{
//		throw new UnsupportedOperationException();
//	}

	/**
	 * {@inheritDoc}
	 * Suppress since SiteNodeType is not really a Map Element
	 */
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since SiteNodeType is not really a Map Element
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since SiteNodeType is not really a Map Element
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить имя пиктограммы по кодовому имени для предустановленного типа 
	 * сетевого узла.
	 * @param codename кодовое имя
	 * @return имя пиктограммы
	 */
	public static String getImageFileName(String codename)
	{
		return (String )imageFileNames.get(codename);
	}

	public static Image getImage(SiteNodeType type)
	{
		return MapPropertiesManager.getImage(type.getImageId());
	}

	/**
	 * Получить пиктограмму по кодовому имени для предустановленного типа 
	 * сетевого узла. Если пиктограмма не существует, она создается.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @param filename файл пиктограммы
	 * @return Идентификатор пиктограммы ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 */
	public static Identifier getImageId(
			Identifier userId,
			String codename, 
			String filename)
	{
		try 
		{
			StorableObjectCondition condition = new TypicalCondition(
				String.valueOf(ImageResourceSort._FILE),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
				ImageResourceWrapper.COLUMN_SORT);
			Collection bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//			List bitMaps = Collections.EMPTY_LIST;

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) 
			{
				FileImageResource ir = (FileImageResource )it.next();
				if(ir.getCodename().equals(codename))
					return ir.getId();
				
			}
		}
		catch (ApplicationException ex) 
//		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		try
		{
			FileImageResource ir = FileImageResource.createInstance(
				userId,
				filename);
			StorableObjectPool.putStorableObject(ir);
			ResourceStorableObjectPool.flush(true);
			return ir.getId();
		} catch (CreateObjectException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalObjectEntityException e) {
			e.printStackTrace();
			return null;
		} catch(ApplicationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Получить тип сетевого узла по кодовому имени.
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 */
	public static SiteNodeType getSiteNodeType(
			String codename)
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		try
		{
			Collection pTypes =
				StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				SiteNodeType type = (SiteNodeType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого 
	 * типа нет, создается новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 */
	private static SiteNodeType getSiteNodeType(
			Identifier userId,
			SiteNodeTypeSort sort,
			String codename)
	{
		SiteNodeType type = getSiteNodeType(codename);
		if(type == null)
		try
		{
			type = SiteNodeType.createInstance(
				userId,
				sort,
				codename,
				LangModelMap.getString(codename),
				"",
				NodeTypeController.getImageId(
						userId, 
						codename, 
						NodeTypeController.getImageFileName(codename)),
				true);
				
			StorableObjectPool.putStorableObject(type);
			MapStorableObjectPool.flush(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			type = null;
		}
		return type;
	}

	public static void createDefaults(Identifier creatorId)
	{
		// make sure SiteNodeType.ATS is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.ATS, SiteNodeType.DEFAULT_ATS);
		// make sure SiteNodeType.BUILDING is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.BUILDING, SiteNodeType.DEFAULT_BUILDING);
		// make sure SiteNodeType.PIQUET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.PIQUET, SiteNodeType.DEFAULT_PIQUET);
		// make sure SiteNodeType.WELL is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.WELL, SiteNodeType.DEFAULT_WELL);
		// make sure SiteNodeType.CABLE_INLET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.CABLE_INLET, SiteNodeType.DEFAULT_CABLE_INLET);
		// make sure SiteNodeType.UNBOUND is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.UNBOUND, SiteNodeType.DEFAULT_UNBOUND);
		// make sure SiteNodeType.CABLE_INLET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.TOWER, SiteNodeType.DEFAULT_TOWER);
	}

	/**
	 * Получить список всех типов сетевых узлов.
	 * @return список типов сетевых узлов &lt;{@link SiteNodeType}&gt;
	 */
	public static Collection getTopologicalNodeTypes() {
		Collection list = Collections.EMPTY_LIST;
		StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		try {
			list =
				StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			list.remove(getUnboundNodeType());

			for(Iterator it = list.iterator(); it.hasNext();) {
				SiteNodeType mnpe = (SiteNodeType )it.next();
				if(!mnpe.isTopological())
					it.remove();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Получить тип непривязанного сетевого узла ({@link SiteNodeType#DEFAULT_UNBOUND}).
	 * @return тип сетевого узла
	 */
	public static SiteNodeType getUnboundNodeType()
	{
		return NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_UNBOUND);
	}


}
