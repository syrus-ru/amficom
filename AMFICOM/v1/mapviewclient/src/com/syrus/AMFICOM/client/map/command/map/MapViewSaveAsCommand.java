/*
 * $Id: MapViewSaveAsCommand.java,v 1.14 2005/02/25 13:49:16 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Props.MapViewPanel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * ����� ������������ ��� ���������� �������������� ����� � �����
 * ������
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/25 13:49:16 $
 * @module mapviewclient_v1
 */
public class MapViewSaveAsCommand extends VoidCommand
{
	MapView mapView;
	MapView newMapView;
    ApplicationContext aContext;

	public MapViewSaveAsCommand(MapView mapView, ApplicationContext aContext)
	{
		this.mapView = mapView;
		this.aContext = aContext;
	}

	public void execute()
	{
		AccessIdentifier_Transferable ait = 
			this.aContext.getSessionInterface().getAccessIdentifier();
		Identifier creatorId = new Identifier(ait.user_id);
		Identifier domainId = new Identifier(ait.domain_id);

		try
		{
			this.newMapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					creatorId,
					domainId,
					LangModelMap.getString("New"),
					"",
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					this.mapView.getMap());

			MapViewStorableObjectPool.putStorableObject(this.newMapView);

			this.newMapView.setName(this.mapView.getName() + "(Copy)");
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
				this.newMapView,
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
			} catch(ApplicationException e) {
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

	public MapView getMapView()
	{
		return this.mapView;
	}

	public MapView getNewMapView()
	{
		return this.newMapView;
	}

}
