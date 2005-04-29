/**
 * $Id: MapEditorApplicationModel.java,v 1.14 2005/04/29 14:02:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

/**
 * содержит список функциональных элементов, доступных пользователю 
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2005/04/29 14:02:17 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModel extends ApplicationModel 
{
	public static final String ITEM_VIEW_PROPERTIES = "mapActionViewProperties";
	public static final String ITEM_EDIT_PROPERTIES = "mapActionEditProperties";

	public static final String ITEM_SESSION = "menuSession";
	public static final String ITEM_SESSION_NEW = "menuSessionNew";
	public static final String ITEM_SESSION_CLOSE = "menuSessionClose";
	public static final String ITEM_SESSION_CONNECTION = "menuSessionConnection";
	public static final String ITEM_SESSION_CHANGE_PASSWORD = "menuSessionChangePassword";
	public static final String ITEM_SESSION_DOMAIN = "menuSessionDomain";
	public static final String ITEM_SESSION_EXIT = "menuExit";

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

	public static final String ITEM_VIEW = "menuView";
	public static final String ITEM_VIEW_GENERAL = "menuViewGeneral";
	public static final String ITEM_VIEW_ADDITIONAL = "menuViewAdditional";
	public static final String ITEM_VIEW_CHARACTERISTICS = "menuViewСharacteristics";
	public static final String ITEM_VIEW_CONTROLS = "menuViewControls";
	public static final String ITEM_VIEW_MAP = "menuViewMap";
	public static final String ITEM_VIEW_NAVIGATOR = "menuViewNavigator";
	public static final String ITEM_VIEW_ALL = "menuViewAll";

	public static final String ITEM_REPORT = "menuReport";
	public static final String ITEM_REPORT_CREATE = "menuReportCreate";

	public static final String ITEM_HELP = "menuHelp";
	public static final String ITEM_HELP_CONTENTS = "menuHelpContents";
	public static final String ITEM_HELP_FIND = "menuHelpFind";
	public static final String ITEM_HELP_TIPS = "menuHelpTips";
	public static final String ITEM_HELP_START = "menuHelpStart";
	public static final String ITEM_HELP_COURSE = "menuHelpCourse";
	public static final String ITEM_HELP_HELP = "menuHelpHelp";
	public static final String ITEM_HELP_SUPPORT = "menuHelpSupport";
	public static final String ITEM_HELP_LICENSE = "menuHelpLicense";
	public static final String ITEM_HELP_ABOUT = "menuHelpAbout";

	public MapEditorApplicationModel()
	{
		add(ITEM_VIEW_PROPERTIES);
		add(ITEM_EDIT_PROPERTIES);

		add(MapEditorApplicationModel.ITEM_SESSION);
		add(MapEditorApplicationModel.ITEM_SESSION_NEW);
		add(MapEditorApplicationModel.ITEM_SESSION_CLOSE);
		add(MapEditorApplicationModel.ITEM_SESSION_CONNECTION);
		add(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD);
		add(MapEditorApplicationModel.ITEM_SESSION_DOMAIN);
		add(MapEditorApplicationModel.ITEM_SESSION_EXIT);

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

		add(MapEditorApplicationModel.ITEM_VIEW);
		add(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
		add(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS);
		add(MapEditorApplicationModel.ITEM_VIEW_CONTROLS);
		add(MapEditorApplicationModel.ITEM_VIEW_MAP);
		add(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR);
		add(MapEditorApplicationModel.ITEM_VIEW_ALL);

		add(MapEditorApplicationModel.ITEM_REPORT);
		add(MapEditorApplicationModel.ITEM_REPORT_CREATE);

		add(MapEditorApplicationModel.ITEM_HELP);
		add(MapEditorApplicationModel.ITEM_HELP_CONTENTS);
		add(MapEditorApplicationModel.ITEM_HELP_FIND);
		add(MapEditorApplicationModel.ITEM_HELP_FIND);
		add(MapEditorApplicationModel.ITEM_HELP_START);
		add(MapEditorApplicationModel.ITEM_HELP_COURSE);
		add(MapEditorApplicationModel.ITEM_HELP_HELP);
		add(MapEditorApplicationModel.ITEM_HELP_SUPPORT);
		add(MapEditorApplicationModel.ITEM_HELP_LICENSE);
		add(MapEditorApplicationModel.ITEM_HELP_ABOUT);
	}
}
