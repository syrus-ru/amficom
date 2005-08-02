/**
 * $Id: MapLibraryController.java,v 1.1 2005/08/02 07:23:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * контроллер типа сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/08/02 07:23:37 $
 * @module mapviewclient_v1
 */
public class MapLibraryController {

	public static final String DEFAULT_LIBRARY = "defaultlibrary";

	/**
	 * Instance.
	 */
	private static MapLibraryController instance = null;

	/** Хэш-таблица имен пиктограмм для предустановленных типов узлов. */
	private static java.util.Map imageFileNames = new HashMap();
	/** Хэш-таблица цветов для предустановленных типов линии. */
	private static java.util.Map lineColors = new HashMap();
	/** Хэш-таблица толщины линии для предустановленных типов линии. */
	private static java.util.Map lineThickness = new HashMap();
	/** Хэш-таблица размерности привязки для предустановленных типов линии. */
	private static java.util.Map bindDimensions = new HashMap();

	static {
		imageFileNames.put(SiteNodeType.DEFAULT_UNBOUND, "images/unbound.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_ATS, "images/ats.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_BUILDING, "images/building.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_PIQUET, "images/piquet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_WELL, "images/well.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_CABLE_INLET, "images/cableinlet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_TOWER, "images/tower.gif");

		lineColors.put(PhysicalLinkType.DEFAULT_COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.DEFAULT_TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.DEFAULT_INDOOR, Color.GREEN);
		lineColors.put(PhysicalLinkType.DEFAULT_OVERHEAD, Color.BLUE);
		lineColors.put(PhysicalLinkType.DEFAULT_SUBMARINE, Color.MAGENTA);
		lineColors.put(PhysicalLinkType.DEFAULT_UNBOUND, Color.RED);

		lineThickness.put(PhysicalLinkType.DEFAULT_COLLECTOR, new Integer(4));
		lineThickness.put(PhysicalLinkType.DEFAULT_TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.DEFAULT_INDOOR, new Integer(1));
		lineThickness.put(PhysicalLinkType.DEFAULT_OVERHEAD, new Integer(2));
		lineThickness.put(PhysicalLinkType.DEFAULT_SUBMARINE, new Integer(3));
		lineThickness.put(PhysicalLinkType.DEFAULT_UNBOUND, new Integer(1));

		bindDimensions.put(PhysicalLinkType.DEFAULT_COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.DEFAULT_TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.DEFAULT_INDOOR, new IntDimension(1, 1));
		bindDimensions.put(PhysicalLinkType.DEFAULT_OVERHEAD, new IntDimension(10, 1));
		bindDimensions.put(PhysicalLinkType.DEFAULT_SUBMARINE, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.DEFAULT_UNBOUND, new IntDimension(0, 0));
	}

	/**
	 * Private constructor.
	 */
	protected MapLibraryController() {
		//empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapLibraryController getInstance() {
		if(instance == null)
			instance = new MapLibraryController();
		return instance;
	}

	/**
	 * Получить имя пиктограммы по кодовому имени для предустановленного типа
	 * сетевого узла.
	 * 
	 * @param codename кодовое имя
	 * @return имя пиктограммы
	 */
	private static String getImageFileName(String codename) {
		return (String )imageFileNames.get(codename);
	}

	/**
	 * Получить пиктограмму по кодовому имени для предустановленного типа
	 * сетевого узла. Если пиктограмма не существует, она создается.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @param filename файл пиктограммы
	 * @return Идентификатор пиктограммы ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 * @throws ApplicationException 
	 */
	private static Identifier getImageId(
			Identifier userId,
			String codename, 
			String filename) throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(
			String.valueOf(ImageResourceSort._FILE),
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.IMAGERESOURCE_CODE,
			ImageResourceWrapper.COLUMN_SORT);
		Collection bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

		// todo should be removed when FIR.filename and FIR.codename will be separated
		codename = filename;
		//

		for(Iterator it = bitMaps.iterator(); it.hasNext();) {
			FileImageResource ir = (FileImageResource )it.next();
			if(ir.getCodename().equals(codename))
				return ir.getId();
		}
/*
		FileImageResource ir = FileImageResource.createInstance(
				userId,
				filename);
		// todo should be used when FIR.filename and FIR.codename will be separated
//		ir.setCodename(codename);
		//
		StorableObjectPool.flush(ir.getId(), true);
*/
		FileImageResource ir = (FileImageResource )bitMaps.iterator().next();
		return ir.getId();
	}

	/**
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого типа
	 * нет, создается новый.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 * @throws ApplicationException 
	 * @throws CreateObjectException 
	 */
	private static SiteNodeType getSiteNodeType(
			Identifier userId,
			SiteNodeTypeSort sort,
			String codename) throws ApplicationException {
		SiteNodeType type = NodeTypeController.getSiteNodeType(codename);
		if(type == null) {
			type = SiteNodeType.createInstance(
				userId,
				sort,
				codename,
				LangModelMap.getString(codename),
				"",
				MapLibraryController.getImageId(
						userId, 
						codename, 
						MapLibraryController.getImageFileName(codename)),
				true);
				
			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flush(type, userId, true);
		}
		return type;
	}

	/**
	 * Получить цвет по кодовому имени для предустановленного типа линии.
	 * 
	 * @param codename кодовое имя
	 * @return цвет
	 */
	public static Color getLineColor(String codename) {
		return (Color )lineColors.get(codename);
	}

	/**
	 * Получить толщину линии по кодовому имени для предустановленного типа
	 * линии.
	 * 
	 * @param codename кодовое имя
	 * @return толщина
	 */
	public static int getLineThickness(String codename) {
		return ((Integer )lineThickness.get(codename)).intValue();
	}

	/**
	 * Получить размерность привязки по кодовому имени для предустановленного
	 * типа линии.
	 * 
	 * @param codename кодовое имя
	 * @return размерность привязки
	 */
	public static IntDimension getBindDimension(String codename) {
		return (IntDimension )bindDimensions.get(codename);
	}

	/**
	 * Получить тип линии по кодовому имени. В случае, если такого типа нет,
	 * создается новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип линии
	 * @throws CreateObjectException 
	 */
	private static PhysicalLinkType getPhysicalLinkType(
			Identifier userId,
			PhysicalLinkTypeSort sort,
			String codename) throws ApplicationException
	{
		PhysicalLinkType type = LinkTypeController.getPhysicalLinkType(codename);
		if(type == null) {
			LinkTypeController ltc = (LinkTypeController )LinkTypeController.getInstance();

			type = PhysicalLinkType.createInstance(
				userId,
				sort,
				codename,
				LangModelMap.getString(codename),
				"",
				MapLibraryController.getBindDimension(codename));

			ltc.setLineSize(userId, type, MapLibraryController.getLineThickness(codename));
			ltc.setColor(userId, type, MapLibraryController.getLineColor(codename));

			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flush(type, userId, true);
		}
		return type;
	}

	/**
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого типа
	 * нет, создается новый.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 * @throws ApplicationException 
	 * @throws CreateObjectException 
	 */
	private static MapLibrary getMapLibrary(
			Identifier userId,
			String codename) throws ApplicationException {
		MapLibrary library = getMapLibrary(codename);
		if(library == null) {
			library = new MapLibrary(
				userId,
				IdentifierPool.getGeneratedIdentifier((short )1),
				LangModelMap.getString(codename),
				null);
				
			//todo
//			StorableObjectPool.putStorableObject(library);
//			StorableObjectPool.flush(library.getId(), true);
		}
		return library;
	}

	public static MapLibrary getDefaultMapLibrary() {
		return MapLibraryController.defaultLibrary;
	}
	
	private static MapLibrary defaultLibrary = null;

	public static void createDefaults(Identifier creatorId) throws ApplicationException {
		defaultLibrary = MapLibraryController.getMapLibrary(creatorId,  DEFAULT_LIBRARY);

		// make sure SiteNodeType.ATS is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.ATS, SiteNodeType.DEFAULT_ATS);
		// make sure SiteNodeType.BUILDING is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.BUILDING, SiteNodeType.DEFAULT_BUILDING);
		// make sure SiteNodeType.PIQUET is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.PIQUET, SiteNodeType.DEFAULT_PIQUET);
		// make sure SiteNodeType.WELL is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.WELL, SiteNodeType.DEFAULT_WELL);
		// make sure SiteNodeType.CABLE_INLET is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.CABLE_INLET, SiteNodeType.DEFAULT_CABLE_INLET);
		// make sure SiteNodeType.UNBOUND is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.UNBOUND, SiteNodeType.DEFAULT_UNBOUND);
		// make sure SiteNodeType.CABLE_INLET is created
		MapLibraryController.getSiteNodeType(creatorId, SiteNodeTypeSort.TOWER, SiteNodeType.DEFAULT_TOWER);

		// make sure PhysicalLinkType.TUNNEL is created
		MapLibraryController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.TUNNEL, PhysicalLinkType.DEFAULT_TUNNEL);
		// make sure PhysicalLinkType.COLLECTOR is created
		MapLibraryController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.COLLECTOR, PhysicalLinkType.DEFAULT_COLLECTOR);
		// make sure PhysicalLinkType.UNBOUND is created
		MapLibraryController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.UNBOUND, PhysicalLinkType.DEFAULT_UNBOUND);
	}

	/**
	 * Получить тип линии по кодовому имени.
	 * @param codename кодовое имя
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public static MapLibrary getMapLibrary(
			String codename) throws ApplicationException
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				(short )1, //todo ObjectEntities.MAPLIBRARY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		Collection pTypes =
			StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
		for (Iterator it = pTypes.iterator(); it.hasNext();)
		{
			MapLibrary library = (MapLibrary )it.next();
			if (library.getCodename().equals(codename))
				return library;
		}

		return null;
	}

}
