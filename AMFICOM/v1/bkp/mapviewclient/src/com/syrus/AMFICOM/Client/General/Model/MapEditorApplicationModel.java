/**
 * $Id: MapEditorApplicationModel.java,v 1.10 2005/02/07 16:09:25 krupenn Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModel extends ApplicationModel 
{
	public MapEditorApplicationModel()
	{
		add("mapActionViewProperties");
		add("mapActionEditProperties");

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuExit");

		add("menuMap");
		add("menuMapNew");
		add("menuMapOpen");
		add("menuMapClose");
		add("menuMapSave");
		add("menuMapSaveAs");
		add("menuMapExport");
		add("menuMapImport");

		add("menuScheme");
		add("menuSchemeAddToView");
		add("menuSchemeRemoveFromView");

		add("menuMapView");
		add("menuMapViewNew");
		add("menuMapViewOpen");
		add("menuMapViewClose");
		add("menuMapViewSave");
		add("menuMapViewSaveAs");
		add("menuMapViewAddScheme");
		add("menuMapViewRemoveScheme");

		add("menuView");
		add("menuViewProto");
		add("menuViewAttributes");
		add("menuViewElements");
		add("menuViewSetup");
		add("menuViewMap");
		add("menuViewMapScheme");
		add("menuViewAll");

		add("menuReport");
		add("menuReportCreate");

		add("menuHelp");
		add("menuHelpContents");
		add("menuHelpFind");
		add("menuHelpTips");
		add("menuHelpStart");
		add("menuHelpCourse");
		add("menuHelpHelp");
		add("menuHelpSupport");
		add("menuHelpLicense");
		add("menuHelpAbout");
	}
}
