/**
 * $Id: MapEditorApplicationModel.java,v 1.7 2005/08/22 11:37:44 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;


/**
 * �������� ������ �������������� ���������, ��������� ������������ 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/08/22 11:37:44 $
 * @module mapviewclient
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModel extends ApplicationModel 
{
	public static final String ITEM_VIEW_PROPERTIES = "mapActionViewProperties";
	public static final String ITEM_EDIT_PROPERTIES = "mapActionEditProperties";

	public static final String ITEM_MAP = "menuMap";
	public static final String ITEM_MAP_NEW = "menuMapNew";
	public static final String ITEM_MAP_OPEN = "menuMapOpen";
	public static final String ITEM_MAP_CLOSE = "menuMapClose";
	public static final String ITEM_MAP_SAVE = "menuMapSave";
	public static final String ITEM_MAP_SAVE_AS = "menuMapSaveAs";
	public static final String ITEM_MAP_ADD_MAP = "menuMapAddMap";
	public static final String ITEM_MAP_REMOVE_MAP = "menuMapRemoveMap";
	public static final String ITEM_MAP_ADD_EXTERNAL = "menuMapAddExternal";
	public static final String ITEM_MAP_EXPORT = "menuMapExport";
	public static final String ITEM_MAP_IMPORT = "menuMapImport";

	public static final String ITEM_MAP_VIEW = "menuMapView";
	public static final String ITEM_MAP_VIEW_NEW = "menuMapViewNew";
	public static final String ITEM_MAP_VIEW_OPEN = "menuMapViewOpen";
	public static final String ITEM_MAP_VIEW_CLOSE = "menuMapViewClose";
	public static final String ITEM_MAP_VIEW_SAVE = "menuMapViewSave";
	public static final String ITEM_MAP_VIEW_SAVE_AS = "menuMapViewSaveAs";
	public static final String ITEM_MAP_VIEW_ADD_SCHEME = "menuMapViewAddScheme";
	public static final String ITEM_MAP_VIEW_REMOVE_SCHEME = "menuMapViewRemoveScheme";

	public static final String ITEM_MAP_LIBRARY = "menuMapLibrary";
	public static final String ITEM_MAP_LIBRARY_NEW = "menuMapLibraryNew";
	public static final String ITEM_MAP_LIBRARY_OPEN = "menuMapLibraryOpen";
	public static final String ITEM_MAP_LIBRARY_SAVE = "menuMapLibrarySave";
	public static final String ITEM_MAP_LIBRARY_SAVE_AS = "menuMapLibrarySaveAs";
	public static final String ITEM_MAP_LIBRARY_REMOVE = "menuMapLibraryRemove";
	public static final String ITEM_MAP_LIBRARY_IMPORT = "menuMapLibraryImport";
	public static final String ITEM_MAP_LIBRARY_EXPORT = "menuMapLibraryExport";
	public static final String ITEM_MAP_LIBRARY_NEW_SITE_TYPE = "menuMapLibraryNewSiteType";
	public static final String ITEM_MAP_LIBRARY_NEW_LINK_TYPE = "menuMapLibraryNewLinkType";
	public static final String ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE = "menuMapLibraryRemoveSiteType";
	public static final String ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE = "menuMapLibraryRemoveLinkType";

	public static final String ITEM_VIEW = "menuView";
	public static final String ITEM_VIEW_GENERAL = "menuViewGeneral";
	public static final String ITEM_VIEW_ADDITIONAL = "menuViewAdditional";
	public static final String ITEM_VIEW_CHARACTERISTICS = "menuView�haracteristics";
	public static final String ITEM_VIEW_MAP_CHOOSER = "menuViewMapChooser";
	public static final String ITEM_VIEW_MAP_LAYERS = "menuViewMapLayers";
	public static final String ITEM_VIEW_MAP = "menuViewMap";
	public static final String ITEM_VIEW_NAVIGATOR = "menuViewNavigator";
	public static final String ITEM_VIEW_ALL = "menuViewAll";

	public static final String ITEM_REPORT = "menuReport";
	public static final String ITEM_REPORT_CREATE = "menuReportCreate";

	public MapEditorApplicationModel()
	{
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
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE);
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE);

		add(MapEditorApplicationModel.ITEM_VIEW);
		add(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
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
