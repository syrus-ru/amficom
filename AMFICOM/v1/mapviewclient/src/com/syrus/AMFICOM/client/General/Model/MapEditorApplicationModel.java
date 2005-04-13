/**
 * $Id: MapEditorApplicationModel.java,v 1.12 2005/04/13 15:45:37 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

/**
 * �������� ������ �������������� ���������, ��������� ������������ 
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/04/13 15:45:37 $
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
		add("menuMapAddMap");
		add("menuMapRemoveMap");
		add("menuMapAddExternal");
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
