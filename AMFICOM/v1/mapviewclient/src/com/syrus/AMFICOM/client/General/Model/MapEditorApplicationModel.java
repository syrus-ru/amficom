/**
 * $Id: MapEditorApplicationModel.java,v 1.2 2004/09/18 13:57:52 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyMapViewDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

import com.syrus.AMFICOM.Client.Resource.EmptyMapDataSource;

/**
 * содержит список функциональных элементов, доступных пользователю 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/18 13:57:52 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorApplicationModel extends ApplicationModel 
{
	public MapEditorApplicationModel()
	{
		super();
		
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

		add("menuScheme");
		add("menuSchemeAddToView");
		add("menuSchemeRemoveFromView");

		add("menuMapView");
		add("menuMapViewNew");
		add("menuMapViewOpen");
		add("menuMapViewClose");
		add("menuMapViewSave");
		add("menuMapViewSaveAs");

		add("menuView");
		add("menuViewProto");
		add("menuViewAttributes");
		add("menuViewElements");
		add("menuViewSetup");
		add("menuViewMap");
		add("menuViewMapScheme");
		add("menuViewAll");

		add("menuReport");
		add("menuReportOpen");

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
	
	private static DataSourceInterface dataSource = null;
	
	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equalsIgnoreCase(Environment.CONNECTION_RISD))
		{
			if(dataSource == null)
				dataSource = new RISDMapDataSource(si);
			else
				dataSource.setSession(si);
		}
		else
		if(connection.equalsIgnoreCase(Environment.CONNECTION_EMPTY))
		{
			if(dataSource == null)
				dataSource = new EmptyMapViewDataSource(si);
			else
				dataSource.setSession(si);
		}
		return dataSource;
	}
}
