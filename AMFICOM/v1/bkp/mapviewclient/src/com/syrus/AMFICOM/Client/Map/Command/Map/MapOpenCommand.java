/**
 * $Id: MapOpenCommand.java,v 1.5 2004/10/19 10:41:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.Map.UI.MapController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

import java.util.List;

import javax.swing.JDesktopPane;

/**
 * открыть карту. карта открывается в новом виде
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapOpenCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	protected ObjectResource retObj;

	protected boolean canDelete = false;

	public MapOpenCommand()
	{
	}

	public MapOpenCommand(JDesktopPane desktop, MapFrame mapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapOpenCommand(desktop, mapFrame, aContext);
	}

	public void setCanDelete(boolean flag)
	{
		this.canDelete = flag;
	}

	public ObjectResource getReturnObject()
	{
		return this.retObj;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapContextOpening")));

		new MapDataSourceImage(dataSource).loadMaps();

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(MapController.getInstance(), Map.typ);

		mcd.setCanDelete(canDelete);

		List ms = Pool.getList(Map.typ);
		mcd.setContents(ms);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == mcd.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			retObj = mcd.getReturnObject();

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				setResult(Command.RESULT_NO);
			}
			else
			{
				MapViewNewCommand cmd = new MapViewNewCommand(mapFrame, aContext);
				cmd.execute();
				MapView mv = cmd.mv;
				mv.setMap((Map )mcd.getReturnObject());
				mapFrame.setMapView(mv);
				setResult(Command.RESULT_OK);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
