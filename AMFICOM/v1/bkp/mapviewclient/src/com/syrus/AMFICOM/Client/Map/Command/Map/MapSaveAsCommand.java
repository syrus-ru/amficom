/*
 * $Id: MapSaveAsCommand.java,v 1.12 2005/01/13 15:16:24 krupenn Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/01/13 15:16:24 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapSaveAsCommand extends VoidCommand
{
	Map map;
	Map newMap;
    ApplicationContext aContext;

	/**
	 * 
	 * @param paramName comments
	 * @exception Exception comments
	 */
	public MapSaveAsCommand(
			Map map, 
			ApplicationContext aContext)
	{
		this.map = map;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		Identifier userId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		Identifier domainId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().domain_id);

		try
		{
			newMap = Map.createInstance(userId, domainId, map.getName() + "(Copy)", "");
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return;
		}

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapSaving")));

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapProperties"), 
				true, 
				newMap,
				MapPanel.getInstance());

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
//			try
//			{
//				newMap = (Map )map.clone();
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
				MapStorableObjectPool.putStorableObject(newMap);
			}
			catch (IllegalObjectEntityException e)
			{
				e.printStackTrace();
			}
			try
			{
				MapStorableObjectPool.flush(true);// save newMap
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

	public Map getMap()
	{
		return map;
	}

	public Map getNewMap()
	{
		return newMap;
	}

}
