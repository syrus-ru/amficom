/*
 * $Id: MapSaveAsCommand.java,v 1.10 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.Map.Props.MapPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.map.MapStorableObjectPool;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Класс $RCSfile: MapSaveAsCommand.java,v $ используется для сохранения 
 * топологической схемы с новым именем
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2004/12/22 16:38:40 $
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

		Identifier userId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		Map mc2;

		try
		{
			mc2 = Map.createInstance(userId, mc.getName() + "(Copy)", "");
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return;
		}

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
//			try
//			{
//				mc2 = (Map )mc.clone(dataSource);
//			}
//			catch(CloneNotSupportedException e)
//			{
//				return;
//			}
/*
			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Pool.removeHash(MapPropertiesManager.MAP_CLONED_IDS);
			}
*/
			try
			{
				MapStorableObjectPool.putStorableObject(mc2);
			}
			catch (IllegalObjectEntityException e)
			{
				e.printStackTrace();
			}
			try
			{
				MapStorableObjectPool.flush(true);// save mc2
			}
			catch (VersionCollisionException e)
			{
				e.printStackTrace();
			}
			catch (IllegalDataException e)
			{
				e.printStackTrace();
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}

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
