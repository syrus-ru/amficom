/*
 * $Id: MapViewSaveCommand.java,v 1.14 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Props.MapViewPanel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;

/**
 * Класс используется для сохранения топологической схемы на сервере
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclietn_v1
 */
public class MapViewSaveCommand extends VoidCommand
{
	MapView mapView;
	ApplicationContext aContext;

	public MapViewSaveCommand(MapView mapView, ApplicationContext aContext)
	{
		this.mapView = mapView;
		this.aContext = aContext;
	}

	public void execute()
	{
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapViewProperties"), 
				true, 
				this.mapView,
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
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString("MapSaving")));

			MapSaveCommand cmd = new MapSaveCommand(this.mapView.getMap(), this.aContext);
			cmd.execute();
			if(cmd.getResult() == Command.RESULT_CANCEL)
			{
				setResult(Command.RESULT_CANCEL);
				return;
			}
			
			for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();)
			{
				Scheme scheme = (Scheme )it.next();
				scheme.mapImpl(this.mapView.getMap());
				try
				{
					SchemeStorableObjectPool.flush(true);// save scheme
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
			}
			
//			MapStorableObjectPool.putStorableObject(mapView);
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
			
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else
		{
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

}
