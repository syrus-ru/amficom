/*
 * $Id: MapMapOpenCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Config.NewMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.MapConfigureApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Editor.MapMDIMain;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapMapOpenCommand.java,v $ используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapMapOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	public MapMapOpenCommand()
	{
	}

	/**
	 * 
	 * @param myMapFrame окно карты - deprecated
	 * @deprecated
	 */
	public MapMapOpenCommand(JDesktopPane desktop, MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this(desktop, aContext);
	}

	/**
	 * 
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapMapOpenCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapOpenCommand(desktop, aContext);
	}

	public void execute()
	{
		ApplicationModelFactory factory = new MapConfigureApplicationModelFactory();
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(aContext.getDispatcher());

		MapOpenCommand moc = new MapOpenCommand(desktop, null, aC);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		moc.can_delete = true;
		moc.execute();
		if (moc.retCode == 1)
		{
			NewMapViewCommand com2 = new NewMapViewCommand(aContext.getDispatcher(), desktop, aContext, factory);
			com2.execute();
			if(com2.frame == null)
				return;

			new ViewMapPropertiesCommand(desktop, aContext).execute();
			new ViewMapElementsCommand(desktop, aContext).execute();
			new ViewMapSchemeNavigatorCommand(desktop, aContext).execute();

			com2.frame.setMapContext((MapContext )Pool.get("mapcontext", moc.retobj_id));
		}
	}

}
