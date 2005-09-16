/**
 * $Id: MapEditorApplicationModel.java,v 1.15 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * содержит список функциональных элементов, доступных пользователю 
 * 
 * @version $Revision: 1.15 $, $Date: 2005/09/16 14:53:38 $
 * @module mapviewclient
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModel extends ApplicationModel {
	public static final String ITEM_VIEW_PROPERTIES = "mapActionViewProperties"; //$NON-NLS-1$
	public static final String ITEM_EDIT_PROPERTIES = "mapActionEditProperties"; //$NON-NLS-1$

	public static final String ITEM_MAP = "menuMap"; //$NON-NLS-1$
	public static final String ITEM_MAP_NEW = "menuMapNew"; //$NON-NLS-1$
	public static final String ITEM_MAP_OPEN = "menuMapOpen"; //$NON-NLS-1$
	public static final String ITEM_MAP_CLOSE = "menuMapClose"; //$NON-NLS-1$
	public static final String ITEM_MAP_SAVE = "menuMapSave"; //$NON-NLS-1$
	public static final String ITEM_MAP_SAVE_AS = "menuMapSaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_ADD_MAP = "menuMapAddMap"; //$NON-NLS-1$
	public static final String ITEM_MAP_REMOVE_MAP = "menuMapRemoveMap"; //$NON-NLS-1$
	public static final String ITEM_MAP_ADD_EXTERNAL = "menuMapAddExternal"; //$NON-NLS-1$
	public static final String ITEM_MAP_EXPORT = "menuMapExport"; //$NON-NLS-1$
	public static final String ITEM_MAP_IMPORT = "menuMapImport"; //$NON-NLS-1$

	public static final String ITEM_MAP_VIEW = "menuMapView"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_NEW = "menuMapViewNew"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_OPEN = "menuMapViewOpen"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_CLOSE = "menuMapViewClose"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_SAVE = "menuMapViewSave"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_SAVE_AS = "menuMapViewSaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_ADD_SCHEME = "menuMapViewAddScheme"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_REMOVE_SCHEME = "menuMapViewRemoveScheme"; //$NON-NLS-1$

	public static final String ITEM_MAP_LIBRARY = "menuMapLibrary"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW = "menuMapLibraryNew"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_OPEN = "menuMapLibraryOpen"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_SAVE = "menuMapLibrarySave"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_SAVE_AS = "menuMapLibrarySaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE = "menuMapLibraryRemove"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_IMPORT = "menuMapLibraryImport"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_EXPORT = "menuMapLibraryExport"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW_SITE_TYPE = "menuMapLibraryNewSiteType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW_LINK_TYPE = "menuMapLibraryNewLinkType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE = "menuMapLibraryRemoveSiteType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE = "menuMapLibraryRemoveLinkType"; //$NON-NLS-1$

	public static final String ITEM_VIEW = "menuView"; //$NON-NLS-1$
	public static final String ITEM_VIEW_GENERAL = "menuViewGeneral"; //$NON-NLS-1$
	public static final String ITEM_VIEW_ADDITIONAL = "menuViewAdditional"; //$NON-NLS-1$
	public static final String ITEM_VIEW_CHARACTERISTICS = "menuViewCharacteristics"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP_CHOOSER = "menuViewMapChooser"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP_LAYERS = "menuViewMapLayers"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP = "menuViewMap"; //$NON-NLS-1$
	public static final String ITEM_VIEW_NAVIGATOR = "menuViewNavigator"; //$NON-NLS-1$
	public static final String ITEM_VIEW_ALL = "menuViewAll"; //$NON-NLS-1$

	public static final String ITEM_REPORT = "menuReport"; //$NON-NLS-1$
	public static final String ITEM_REPORT_CREATE = "menuReportCreate"; //$NON-NLS-1$

	public MapEditorApplicationModel() {
		UIManager.put("images/main/map_mini.gif", Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")); //$NON-NLS-1$ //$NON-NLS-2$
		UIManager.put(MapEditorResourceKeys.ICON_CATALOG, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/folder.gif").getScaledInstance( //$NON-NLS-1$
						16, 16, Image.SCALE_SMOOTH)));

		add(ITEM_VIEW_PROPERTIES);
		add(ITEM_EDIT_PROPERTIES);

		add(ApplicationModel.MENU_SESSION);
		add(ApplicationModel.MENU_SESSION_NEW);
		add(ApplicationModel.MENU_SESSION_CLOSE);
//		add(ApplicationModel.MENU_SESSION_CONNECTION);
		add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
		add(ApplicationModel.MENU_SESSION_DOMAIN);
		add(ApplicationModel.MENU_EXIT);

		add(MapEditorApplicationModel.ITEM_MAP);
		add(MapEditorApplicationModel.ITEM_MAP_NEW);
		add(MapEditorApplicationModel.ITEM_MAP_OPEN);
		add(MapEditorApplicationModel.ITEM_MAP_CLOSE);
		add(MapEditorApplicationModel.ITEM_MAP_SAVE);
		add(MapEditorApplicationModel.ITEM_MAP_SAVE_AS);
		add(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		add(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		add(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		add(MapEditorApplicationModel.ITEM_MAP_EXPORT);
		add(MapEditorApplicationModel.ITEM_MAP_IMPORT);

		add(MapEditorApplicationModel.ITEM_MAP_VIEW);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		add(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);

		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE);

		add(MapEditorApplicationModel.ITEM_VIEW);
		add(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
		add(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL);
		add(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS);
		add(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER);
		add(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS);
		add(MapEditorApplicationModel.ITEM_VIEW_MAP);
		add(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR);
		add(MapEditorApplicationModel.ITEM_VIEW_ALL);

		add(MapEditorApplicationModel.ITEM_REPORT);
		add(MapEditorApplicationModel.ITEM_REPORT_CREATE);

		add(ApplicationModel.MENU_HELP);
		add(ApplicationModel.MENU_HELP_ABOUT);
	}
}
