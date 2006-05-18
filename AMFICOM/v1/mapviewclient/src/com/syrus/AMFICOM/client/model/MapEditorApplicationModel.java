/*-
 * $$Id: MapEditorApplicationModel.java,v 1.20 2005/10/21 14:22:28 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * содержит список функциональных элементов, доступных пользователю 
 * 
 * @version $Revision: 1.20 $, $Date: 2005/10/21 14:22:28 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorApplicationModel extends ApplicationModel {
	public static final String ITEM_VIEW_PROPERTIES = "mapActionViewProperties"; //$NON-NLS-1$
	public static final String ITEM_EDIT_PROPERTIES = "mapActionEditProperties"; //$NON-NLS-1$

	public static final String ITEM_MAP = "Menu.Map"; //$NON-NLS-1$
	public static final String ITEM_MAP_NEW = "Menu.Map.New"; //$NON-NLS-1$
	public static final String ITEM_MAP_OPEN = "Menu.Map.Open"; //$NON-NLS-1$
	public static final String ITEM_MAP_CLOSE = "Menu.Map.Close"; //$NON-NLS-1$
	public static final String ITEM_MAP_SAVE = "Menu.Map.Save"; //$NON-NLS-1$
	public static final String ITEM_MAP_SAVE_AS = "Menu.Map.SaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_ADD_MAP = "Menu.Map.AddMap"; //$NON-NLS-1$
	public static final String ITEM_MAP_REMOVE_MAP = "Menu.Map.RemoveMap"; //$NON-NLS-1$
	public static final String ITEM_MAP_ADD_EXTERNAL = "Menu.Map.AddExternal"; //$NON-NLS-1$
	public static final String ITEM_MAP_EXPORT = "Menu.Map.Export"; //$NON-NLS-1$
	public static final String ITEM_MAP_IMPORT = "Menu.Map.Import"; //$NON-NLS-1$

	public static final String ITEM_MAP_VIEW = "Menu.MapView"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_NEW = "Menu.MapView.New"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_OPEN = "Menu.MapView.Open"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_CLOSE = "Menu.MapView.Close"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_SAVE = "Menu.MapView.Save"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_SAVE_AS = "Menu.MapView.SaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_ADD_SCHEME = "Menu.MapView.AddScheme"; //$NON-NLS-1$
	public static final String ITEM_MAP_VIEW_REMOVE_SCHEME = "Menu.MapView.RemoveScheme"; //$NON-NLS-1$

	public static final String ITEM_MAP_LIBRARY = "Menu.MapLibrary"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW = "Menu.MapLibrary.New"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_OPEN = "Menu.MapLibrary.Open"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_SAVE = "Menu.MapLibrary.Save"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_SAVE_AS = "Menu.MapLibrary.SaveAs"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_CLOSE = "Menu.MapLibrary.Close"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE = "Menu.MapLibrary.Remove"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_IMPORT = "Menu.MapLibrary.Import"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_EXPORT = "Menu.MapLibrary.Export"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW_SITE_TYPE = "Menu.MapLibrary.NewSiteType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_NEW_LINK_TYPE = "Menu.MapLibrary.NewLinkType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE = "Menu.MapLibrary.RemoveSiteType"; //$NON-NLS-1$
	public static final String ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE = "Menu.MapLibrary.RemoveLinkType"; //$NON-NLS-1$

	public static final String ITEM_VIEW = "Menu.View"; //$NON-NLS-1$
	public static final String ITEM_VIEW_GENERAL = "Menu.View.General"; //$NON-NLS-1$
	public static final String ITEM_VIEW_ADDITIONAL = "Menu.View.Additional"; //$NON-NLS-1$
	public static final String ITEM_VIEW_CHARACTERISTICS = "Menu.View.Characteristics"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP_CHOOSER = "Menu.View.MapChooser"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP_LAYERS = "Menu.View.MapLayers"; //$NON-NLS-1$
	public static final String ITEM_VIEW_MAP = "Menu.View.Map"; //$NON-NLS-1$
	public static final String ITEM_VIEW_NAVIGATOR = "Menu.View.Navigator"; //$NON-NLS-1$
	public static final String ITEM_VIEW_ALL = "Menu.View.All"; //$NON-NLS-1$

	public static final String ITEM_REPORT = "Menu.Report"; //$NON-NLS-1$
	public static final String ITEM_REPORT_CREATE = "Menu.Report.Create"; //$NON-NLS-1$

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
		add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
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
		add(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE);
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

		I18N.addResourceBundle("com.syrus.AMFICOM.client.report.report");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.map");
	}
}
