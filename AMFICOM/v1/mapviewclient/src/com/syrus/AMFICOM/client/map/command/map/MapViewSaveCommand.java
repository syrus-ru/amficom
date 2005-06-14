/*
 * $Id: MapViewSaveCommand.java,v 1.22 2005/06/14 10:11:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Iterator;

import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * Класс используется для сохранения топологической схемы на сервере
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/06/14 10:11:17 $
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
		if(EditorDialog.showEditorDialog(
				LangModelMap.getString("MapViewProperties"), 
				this.mapView, 
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel())) {
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
