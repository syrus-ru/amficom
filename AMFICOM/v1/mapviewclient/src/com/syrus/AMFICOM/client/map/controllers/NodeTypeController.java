/*-
 * $$Id: NodeTypeController.java,v 1.57 2006/06/15 06:35:36 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.util.Log;

/**
 * контроллер типа сетевого узла.
 * 
 * @version $Revision: 1.57 $, $Date: 2006/06/15 06:35:36 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class NodeTypeController extends AbstractNodeController {

	private static final String DEFAULT_IMAGE_CODENAME = "defaultimageresource"; //$NON-NLS-1$

	private static final String DEFAULT_IMAGE_FILENAME = "images/defaultsite.gif"; //$NON-NLS-1$

	/**
	 * Instance.
	 */
	private static NodeTypeController instance = null;

	/** ’эш-таблица имен пиктограмм дл€ предустановленных типов узлов. */
	private static java.util.Map<String, String> imageFileNames = new HashMap<String, String>();

	private static Identifier defaultImageId = null;

	static {
		imageFileNames.put(SiteNodeType.DEFAULT_UNBOUND, "images/unbound.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_ATS, "images/ats.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_BUILDING, "images/building.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_PIQUET, "images/piquet.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_WELL, "images/well.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_CABLE_INLET, "images/cableinlet.gif"); //$NON-NLS-1$
		imageFileNames.put(SiteNodeType.DEFAULT_TOWER, "images/tower.gif"); //$NON-NLS-1$
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
	 * ѕолучить им€ пиктограммы по кодовому имени дл€ предустановленного типа
	 * сетевого узла.
	 * 
	 * @param codename кодовое им€
	 * @return им€ пиктограммы
	 */
	public static String getImageFileName(final String codename) {
		return imageFileNames.get(codename);
	}

	public static Image getImage(final SiteNodeType type) {
		return MapPropertiesManager.getImage(type.getImageId());
	}

	/**
	 * ѕолучить пиктограмму по кодовому имени дл€ предустановленного типа
	 * сетевого узла. ≈сли пиктограмма не существует, она создаетс€.
	 * 
	 * @param codename кодовое им€
	 * @param filename файл пиктограммы
	 * @return »дентификатор пиктограммы ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 * @throws ApplicationException 
	 */
	public static Identifier getImageId(final String codename, final String filename)
			throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGERESOURCE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<AbstractBitmapImageResource> imageResources = StorableObjectPool.getStorableObjectsByCondition(condition, true);

		if(imageResources.size() != 0) {
			return imageResources.iterator().next().getId();
		}

		final FileImageResource ir = FileImageResource.createInstance(LoginManager.getUserId(), codename, filename);
		StorableObjectPool.flush(ir, LoginManager.getUserId(), true);
		return ir.getId();
	}

	/**
	 * ѕолучить тип сетевого узла по кодовому имени.
	 * 
	 * @param codename
	 *        кодовое им€
	 * @return тип сетевого узла
	 * @throws ApplicationException
	 */
	public static SiteNodeType getSiteNodeType(final String codename) throws ApplicationException {
		return getSiteNodeType(codename, false);
	}

	static SiteNodeType getSiteNodeType(final String codename, final boolean useLoader) throws ApplicationException {
		final StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		final Set<SiteNodeType> pTypes = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, useLoader);
		if (pTypes.size() > 0) {
			return pTypes.iterator().next();
		}
		return null;
	}

	/**
	 * ѕолучить тип сетевого узла по кодовому имени. ¬ случае, если такого типа
	 * нет, создаетс€ новый.
	 * 
	 * @param codename
	 *        кодовое им€
	 * @param isTopological
	 *        наноситс€ пользователем
	 * @return тип сетевого узла
	 * @throws ApplicationException
	 * @throws CreateObjectException
	 */
	static SiteNodeType getSiteNodeType(final MapLibrary mapLibrary,
			final SiteNodeTypeSort sort,
			final String codename,
			final boolean isTopological) throws ApplicationException {
		SiteNodeType type = getSiteNodeType(codename, true);
		if (type == null) {
			final Identifier userId = LoginManager.getUserId();
			type = SiteNodeType.createInstance(
					userId,
					sort,
					codename,
					I18N.getString(codename),
					"", //$NON-NLS-1$
					NodeTypeController.getImageId(codename, NodeTypeController.getImageFileName(codename)),
					isTopological,
					mapLibrary.getId());

			StorableObjectPool.flush(type, userId, true);
		}
		else {//TODO this is temporal
			final SiteNodeTypeSort oldSort = type.getSort();
			final String oldName = type.getName();
			if (sort != oldSort || !I18N.getString(codename).equals(oldName)) {
				type.setSort(sort);
				type.setName(I18N.getString(codename));	
			}
		}
		return type;
	}

	/**
	 * ѕолучить список всех типов сетевых узлов.
	 * 
	 * @return список типов сетевых узлов &lt;{@link SiteNodeType}&gt;
	 */
	public static Collection<SiteNodeType> getTopologicalNodeTypes(Map map) {
		Set<SiteNodeType> objects = new HashSet<SiteNodeType>();
		for(MapLibrary library : map.getMapLibraries()) {
			for(SiteNodeType siteNodeType : library.getSiteNodeTypes()) {
				if(siteNodeType.isTopological()) {
					objects.add(siteNodeType);
				}
			}
		}

		return objects;
	}

	/**
	 * ѕолучить тип неприв€занного сетевого узла ({@link SiteNodeType#DEFAULT_UNBOUND}).
	 * @return тип сетевого узла
	 * @throws ApplicationException 
	 */
	public static SiteNodeType getUnboundNodeType() throws ApplicationException {
		return NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_UNBOUND, false);
	}

	public static Identifier getDefaultImageId() {
		return NodeTypeController.defaultImageId;
	}

	public static void createDefaults() throws ApplicationException {
		NodeTypeController.defaultImageId = getImageId(
				DEFAULT_IMAGE_CODENAME, 
				DEFAULT_IMAGE_FILENAME);
	}

	public static Collection<SiteNodeType> getCableInletTypes() {
		Set<SiteNodeType> objects = Collections.emptySet();
		final StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.SITENODE_TYPE_CODE);

		try {
			objects = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, false);

			for (final Iterator<SiteNodeType> it = objects.iterator(); it.hasNext();) {
				final SiteNodeType type = it.next();
				if (type.getSort().value() != SiteNodeTypeSort._CABLE_INLET) {
					it.remove();
				}
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}
		return objects;
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	@Override
	public Rectangle getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}
}
