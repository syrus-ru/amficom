/**
 * $Id: NodeTypeController.java,v 1.5 2005/01/13 15:15:53 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;

import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.*;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/13 15:15:53 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class NodeTypeController extends AbstractNodeController
{
	private static NodeTypeController instance = null;
	
	protected NodeTypeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new NodeTypeController();
		return instance;
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
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		throw new UnsupportedOperationException();
	}

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

	public static String getImageFileName(String codename)
	{
		return (String )imageFileNames.get(codename);
	}

	public static Identifier getImageId(
			Identifier userId,
			String codename, 
			String filename)
	{
		try 
		{
			StringFieldCondition condition = new StringFieldCondition(
				String.valueOf(ImageResourceSort._FILE),
				ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
				StringFieldSort.STRINGSORT_INTEGER);
			List bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);
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
			return ir.getId();
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static SiteNodeType getSiteNodeType(
			Identifier userId,
			String codename)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
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
			return snt;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	static List topologicalProtos = new LinkedList();

	public static List getTopologicalProtos(ApplicationContext aContext)
	{
		SiteNodeType mnpe = null;

		topologicalProtos.clear();
		
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		mnpe = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.ATS);
		mnpe = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.BUILDING);
		mnpe = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.PIQUET);
		mnpe = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.WELL);
		mnpe = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.CABLE_INLET);

		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			"",
			ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List list2 =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			for(Iterator it = list2.iterator(); it.hasNext();)
			{
				mnpe = (SiteNodeType )it.next();
				if(mnpe.isTopological())
					topologicalProtos.add(mnpe);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		topologicalProtos.remove(getDefaultUnboundProto(creatorId));
		return topologicalProtos;
	}

	public static SiteNodeType getDefaultUnboundProto(Identifier creatorId)
	{
		return NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.UNBOUND);
	}


}
