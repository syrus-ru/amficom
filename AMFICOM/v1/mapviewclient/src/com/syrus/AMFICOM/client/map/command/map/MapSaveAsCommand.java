/*
 * $Id: MapSaveAsCommand.java,v 1.8 2004/11/25 13:00:49 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ����� $RCSfile: MapSaveAsCommand.java,v $ ������������ ��� ���������� 
 * �������������� ����� � ����� ������
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/11/25 13:00:49 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapSaveAsCommand extends VoidCommand
{
	MapFrame mapFrame;
    ApplicationContext aContext;

	public MapSaveAsCommand()
	{
	}

	/**
	 * 
	 * @param paramName comments
	 * @exception Exception comments
	 */
	public MapSaveAsCommand(
			MapFrame mapFrame, 
			ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		Map mc = mapFrame.getMapView().getMap();

		Map mc2 = new Map();
/*
		mc2.domainId = mc.domainId;
		mc2.userId = aContext.getSessionInterface().getUserId();
		mc2.description = mc.description;
		mc2.created_by = mc2.userId;
		mc2.modified = mc2.created;
		mc2.modified_by = mc2.userId;
*/
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapContextSaving")));

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("MapContextProperties"), 
				true, 
				mc2,
				MapPanel.getInstance());

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
			try
			{
				mc2 = (Map )mc.clone(dataSource);
			}
			catch(CloneNotSupportedException e)
			{
				return;
			}
/*
			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Pool.removeHash(MapPropertiesManager.MAP_CLONED_IDS);
			}
*/
			Pool.put( mc2.getTyp(), mc2.getId(), mc2);
			dataSource.SaveMap(mc2.getId());

			if (mapFrame != null)
			{
				mapFrame.getMapView().setMap(mc2);
				mapFrame.setTitle( LangModelMap.getString("Map") + " - "
												 + mc2.getName());
			}
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
