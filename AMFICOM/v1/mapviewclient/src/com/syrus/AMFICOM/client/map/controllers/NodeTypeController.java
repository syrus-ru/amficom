/**
 * $Id: NodeTypeController.java,v 1.13 2005/04/06 17:41:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * контроллер типа сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/04/06 17:41:12 $
 * @module mapviewclient_v1
 */
public class NodeTypeController extends AbstractNodeController
		implements VisualManager
{
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	/**
	 * Instance.
	 */
	private static NodeTypeController instance = null;

	/** Хэш-таблица имен пиктограмм для предустановленных типов узлов. */
	private static java.util.Map imageFileNames = new HashMap();
	
	static
	{
		imageFileNames.put(SiteNodeType.UNBOUND, "images/unbound.gif");
		imageFileNames.put(SiteNodeType.ATS, "images/ats.gif");
		imageFileNames.put(SiteNodeType.BUILDING, "images/building.gif");
		imageFileNames.put(SiteNodeType.PIQUET, "images/piquet.gif");
		imageFileNames.put(SiteNodeType.WELL, "images/well.gif");
		imageFileNames.put(SiteNodeType.CABLE_INLET, "images/cableinlet.gif");
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

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	public ObjectResourceController getController() {
//		return PhysicalLinkWrapper.getInstance();
		return null;
	}
	
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
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
			Collection bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);
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
			ResourceStorableObjectPool.putStorableObject(ir);
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
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого 
	 * типа нет, создается новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 */
	public static SiteNodeType getSiteNodeType(
			Identifier userId,
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
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
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

		try
		{
			SiteNodeType snt = SiteNodeType.createInstance(
				userId,
				codename,
				LangModelMap.getString(codename),
				"",
				NodeTypeController.getImageId(
						userId, 
						codename, 
						NodeTypeController.getImageFileName(codename)),
				true);
				
			MapStorableObjectPool.putStorableObject(snt);
			MapStorableObjectPool.flush(true);
			return snt;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Получить список всех типов сетевых узлов.
	 * @param aContext контекст приложения
	 * @return список типов сетевых узлов &lt;{@link SiteNodeType}&gt;
	 */
	public static Collection getTopologicalProtos(ApplicationContext aContext)
	{
		Collection topologicalProtos = Collections.EMPTY_LIST;

		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		// make sure SiteNodeType.ATS is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.ATS);
		// make sure SiteNodeType.BUILDING is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.BUILDING);
		// make sure SiteNodeType.PIQUET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.PIQUET);
		// make sure SiteNodeType.WELL is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.WELL);
		// make sure SiteNodeType.CABLE_INLET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.CABLE_INLET);

		StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);

		try
		{
			topologicalProtos =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			topologicalProtos.remove(getDefaultUnboundProto(creatorId));

			for(Iterator it = topologicalProtos.iterator(); it.hasNext();)
			{
				SiteNodeType mnpe = (SiteNodeType )it.next();
				if(!mnpe.isTopological())
					it.remove();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return topologicalProtos;
	}

	/**
	 * Получить тип непривязанного сетевого узла ({@link SiteNodeType#UNBOUND}).
	 * @param creatorId пользователь
	 * @return тип сетевого узла
	 */
	public static SiteNodeType getDefaultUnboundProto(Identifier creatorId)
	{
		return NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.UNBOUND);
	}


}
