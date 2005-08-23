/**
 * $Id: NodeTypeController.java,v 1.42 2005/08/23 09:41:16 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * контроллер типа сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.42 $, $Date: 2005/08/23 09:41:16 $
 * @module mapviewclient
 */
public class NodeTypeController extends AbstractNodeController {

	private static final String DEFAULT_IMAGE_CODENAME = "defaultimageresource";

	private static final String DEFAULT_IMAGE_FILENAME = "images/defaultsite.gif";

	/**
	 * Instance.
	 */
	private static NodeTypeController instance = null;

	/** Хэш-таблица имен пиктограмм для предустановленных типов узлов. */
	private static java.util.Map<String, String> imageFileNames = new HashMap<String, String>();

	private static Identifier defaultImageId = null;

	static {
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
	protected NodeTypeController() {
		super(null);
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if (instance == null) {
			instance = new NodeTypeController();
		}
		return instance;
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	@Override
	public boolean isElementVisible(final MapElement me, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	@Override
	public void paint(final MapElement me, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	@Override
	public boolean isMouseOnElement(final MapElement me, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить имя пиктограммы по кодовому имени для предустановленного типа
	 * сетевого узла.
	 * 
	 * @param codename кодовое имя
	 * @return имя пиктограммы
	 */
	public static String getImageFileName(final String codename) {
		return imageFileNames.get(codename);
	}

	public static Image getImage(final SiteNodeType type) {
		return MapPropertiesManager.getImage(type.getImageId());
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
	public static Identifier getImageId(final Identifier userId, final String codename, final String filename)
			throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(String.valueOf(ImageResourceSort._FILE),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGERESOURCE_CODE,
				ImageResourceWrapper.COLUMN_SORT);
		final Set<AbstractBitmapImageResource> bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

		for (final Iterator<AbstractBitmapImageResource> it = bitMaps.iterator(); it.hasNext();) {
			final FileImageResource ir = (FileImageResource) it.next();
			if (ir.getCodename().equals(codename)) {
				return ir.getId();
			}
		}
		final FileImageResource ir = FileImageResource.createInstance(userId, codename, filename);
		StorableObjectPool.flush(ir, userId, true);
		return ir.getId();
	}

	/**
	 * Получить тип сетевого узла по кодовому имени.
	 * 
	 * @param codename
	 *        кодовое имя
	 * @return тип сетевого узла
	 * @throws ApplicationException
	 */
	public static SiteNodeType getSiteNodeType(final String codename) throws ApplicationException {
		return getSiteNodeType(codename, false);
	}

	static SiteNodeType getSiteNodeType(final String codename, final boolean useLoader) throws ApplicationException {
		final StorableObjectCondition pTypeCondition = new TypicalCondition(codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		final Set<SiteNodeType> pTypes = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, useLoader);
		if (pTypes.size() == 1) {
			return pTypes.iterator().next();
		}
		return null;
	}

	/**
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого типа
	 * нет, создается новый.
	 * 
	 * @param userId
	 *        пользователь
	 * @param codename
	 *        кодовое имя
	 * @param isTopological
	 *        наносится пользователем
	 * @return тип сетевого узла
	 * @throws ApplicationException
	 * @throws CreateObjectException
	 */
	static SiteNodeType getSiteNodeType(final MapLibrary mapLibrary,
			final Identifier userId,
			final SiteNodeTypeSort sort,
			final String codename,
			final boolean isTopological) throws ApplicationException {
		SiteNodeType type = getSiteNodeType(codename, true);
		if (type == null) {
			type = SiteNodeType.createInstance(userId,
					sort,
					codename,
					LangModelMap.getString(codename),
					"",
					NodeTypeController.getImageId(userId, codename, NodeTypeController.getImageFileName(codename)),
					isTopological,
					mapLibrary.getId());

			StorableObjectPool.flush(type, userId, true);
		}
		return type;
	}

	/**
	 * Получить список всех типов сетевых узлов.
	 * 
	 * @return список типов сетевых узлов &lt;{@link SiteNodeType}&gt;
	 */
	public static Collection getTopologicalNodeTypes() {
		Set<SiteNodeType> objects = Collections.emptySet();
		final StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.SITENODE_TYPE_CODE);

		// todo getTopologicalNodeTypes should get only included libraries
		// Set<Identifier> libIds = new HashSet<Identifier>();
		// for(Iterator iter = map.getMapLibraries().iterator(); iter.hasNext();) {
		// MapLibrary library = (MapLibrary )iter.next();
		// libIds.add(library.getId());
		// }
		//		
		// StorableObjectCondition pTypeCondition = new LinkedIdsCondition(libIds,
		// ObjectEntities.SITENODE_TYPE_CODE);

		try {
			objects = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, false);

			objects.remove(getUnboundNodeType());

			for (final Iterator<SiteNodeType> it = objects.iterator(); it.hasNext();) {
				final SiteNodeType mnpe = it.next();
				if (!mnpe.isTopological()) {
					it.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Получить тип непривязанного сетевого узла ({@link SiteNodeType#DEFAULT_UNBOUND}).
	 * @return тип сетевого узла
	 * @throws ApplicationException 
	 */
	public static SiteNodeType getUnboundNodeType() throws ApplicationException {
		return NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_UNBOUND, false);
	}

	public static Identifier getDefaultImageId() {
		return NodeTypeController.defaultImageId;
	}

	public static void createDefaults(final Identifier creatorId) throws ApplicationException {
		NodeTypeController.defaultImageId = getImageId(
				creatorId,
				DEFAULT_IMAGE_CODENAME, 
				DEFAULT_IMAGE_FILENAME);
	}

}
