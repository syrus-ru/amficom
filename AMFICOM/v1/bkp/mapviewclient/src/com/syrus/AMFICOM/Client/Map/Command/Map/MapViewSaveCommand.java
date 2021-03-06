/*
 * $Id: MapViewSaveCommand.java,v 1.19 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Props.MapViewVisualManager;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * ????? ???????????? ??? ?????????? ?????????????? ????? ?? ???????
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class MapViewSaveCommand extends AbstractCommand
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
		EditorDialog dialog = new EditorDialog(
				LangModelMap.getString("MapViewProperties"), 
				true, 
				this.mapView, 
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel());

		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
					this,
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString("MapViewSaving")));

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
				scheme.setMap(this.mapView.getMap());
				try
				{
//					 save scheme
					StorableObjectPool.flush(scheme.getId(), true);
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
				catch (ApplicationException e)
				{
					e.printStackTrace();
				}
			}
			
//			MapStorableObjectPool.putStorableObject(mapView);
			try
			{
//				 save mapview
				StorableObjectPool.flush(this.mapView.getId(), true);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
			
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
					this,
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelGeneral.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else
		{
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
					this,
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

}
