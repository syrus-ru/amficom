/**
 * $Id: MapEditorApplicationModel.java,v 1.6 2004/12/29 19:05:20 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.RISDMapViewDataSource;

/**
 * содержит список функциональных элементов, доступных пользователю 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/29 19:05:20 $
 * @module
 * @author $Author: krupenn $
 * @see
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
/*
	private static DataSourceInterface dataSource = null;
	
	public DataSourceInterface getDataSource(final SessionInterface session) 
	{
		String connection = Environment.getConnectionType();
        if ((this.session == null) || (!this.session.equals(session)))
			synchronized (this) 
			{
					if ((this.session == null) || (!this.session.equals(session))) 
					{
						this.session = session;
						if(connection.equalsIgnoreCase(Environment.CONNECTION_RISD))
							this.dataSource = new RISDMapViewDataSource(this.session);
						else
						if(connection.equalsIgnoreCase(Environment.CONNECTION_EMPTY))
							this.dataSource = new EmptyMapViewDataSource(this.session);
					}
			}
        return this.dataSource;
	}
*/
}
