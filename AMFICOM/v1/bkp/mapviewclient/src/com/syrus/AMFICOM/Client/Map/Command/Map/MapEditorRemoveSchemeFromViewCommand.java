/**
 * $Id: MapEditorRemoveSchemeFromViewCommand.java,v 1.3 2004/10/19 10:41:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

import javax.swing.JOptionPane;

/**
 * убрать из вида выбранную схему 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorRemoveSchemeFromViewCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;
	Scheme sch;

	public MapEditorRemoveSchemeFromViewCommand()
	{
	}

	public MapEditorRemoveSchemeFromViewCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}
	
	public void setParameter(String key, Object val)
	{
		if(key.equals("scheme"))
			sch = (Scheme )val;
		if(key.equals("mapFrame"))
			this.mapFrame = (MapFrame)val;
	}

	public Object clone()
	{
		return new MapEditorRemoveSchemeFromViewCommand(mapFrame, aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		if(sch == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));


		if(JOptionPane.showConfirmDialog(
				Environment.getActiveWindow(),
				"Убрать схему \'" + sch.getName() + "\' из вида?",
				"",
				JOptionPane.NO_OPTION | JOptionPane.YES_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		mapFrame.getMapView().removeScheme(sch);
		mapFrame.getContext().getDispatcher().notify(new MapEvent(
				mapFrame.getMapView(),
				MapEvent.MAP_VIEW_CHANGED));

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
