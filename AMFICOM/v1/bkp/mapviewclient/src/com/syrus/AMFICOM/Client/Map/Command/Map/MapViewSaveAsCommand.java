/*
 * $Id: MapViewSaveAsCommand.java,v 1.10 2005/01/30 15:38:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapViewPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Класс $RCSfile: MapViewSaveAsCommand.java,v $ используется для сохранения топологической схемы с новым
 * именем
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/01/30 15:38:18 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewSaveAsCommand extends VoidCommand
{
	MapView mapView;
	MapView newMapView;
    ApplicationContext aContext;

	/**
	 * 
	 * @param paramName comments
	 * @exception Exception comments
	 */
	public MapViewSaveAsCommand(MapView mapView, ApplicationContext aContext)
	{
		this.mapView = mapView;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		AccessIdentifier_Transferable ait = 
			aContext.getSessionInterface().getAccessIdentifier();
		Identifier creatorId = new Identifier(ait.user_id);
		Identifier domainId = new Identifier(ait.domain_id);

		try
		{
			newMapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					creatorId,
					domainId,
					LangModelMap.getString("New"),
					"",
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					mapView.getMap());

			MapViewStorableObjectPool.putStorableObject(newMapView);

			newMapView.setName(mapView.getName() + "(Copy)");
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		}

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapViewProperties"), 
				true, 
				newMapView,
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
//			try
//			{
//				newMapView = (MapView )mapView.clone();
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
				MapViewStorableObjectPool.flush(true);// save mapview
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

	public MapView getMapView()
	{
		return mapView;
	}

	public MapView getNewMapView()
	{
		return newMapView;
	}

}
