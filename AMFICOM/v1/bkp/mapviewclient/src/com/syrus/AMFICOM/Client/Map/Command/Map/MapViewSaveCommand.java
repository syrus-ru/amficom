/*
 * $Id: MapViewSaveCommand.java,v 1.6 2004/10/20 10:14:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.Map.Props.MapViewPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ����� $RCSfile: MapViewSaveCommand.java,v $ ������������ ��� ���������� �������������� ����� �� �������
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/20 10:14:39 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewSaveCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	public MapViewSaveCommand()
	{
	}

	/**
	 * 
	 * @param mapFrame comments
	 * @param aContext comments
	 */
	public MapViewSaveCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		MapSaveCommand cmd = new MapSaveCommand(mapFrame, aContext);
		cmd.execute();
		if(cmd.getResult() == Command.RESULT_CANCEL)
		{
			setResult(Command.RESULT_CANCEL);
			return;
		}

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapViewProperties"), 
				true, 
				mapFrame.getMapView(),
				MapViewPanel.getInstance());

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString("MapSaving")));

			dataSource.SaveMapView(mapFrame.getMapView().getId());

			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

}