/**
 * $Id: MapViewOpenCommand.java,v 1.17 2005/02/22 11:00:14 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTableController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * ������� ��� 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/02/22 11:00:14 $
 * @module mapviewclient_v1
 */
public class MapViewOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	protected MapView mapView;

	protected boolean canDelete = false;

	public MapViewOpenCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag)
	{
		this.canDelete = flag;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	public void execute()
	{
		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		Collection mapViews;
		try
		{
			Identifier domainId = new Identifier(
					this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
//				Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
//						domainId,
//						false);

			StorableObjectCondition condition = new LinkedIdsCondition(domainId, ObjectEntities.MAPVIEW_ENTITY_CODE);
			mapViews = MapViewStorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
			return;
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
			return;
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return;
		}

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(MapViewTableController.getInstance(), ObjectEntities.MAP_ENTITY);//ObjectEntities.MAP_VIEW_ENTITY

		mcd.setCanDelete(this.canDelete);

		mcd.setContents(mapViews);

		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_CANCEL)
		{
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			this.mapView = (MapView )mcd.getReturnObject();

			setResult(Command.RESULT_OK);

			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
