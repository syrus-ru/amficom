/**
 * $Id: MapViewOpenCommand.java,v 1.15 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.List;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTableController;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * открыть вид 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/08 15:11:10 $
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

		List mvs;
		try
		{
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
				new Identifier(
					this.aContext.getSessionInterface().getAccessIdentifier().domain_id), 
				false);

			DomainCondition condition = new DomainCondition(
					domain,
					ObjectEntities.MAP_ENTITY_CODE);// ObjectEntities.MAP_VIEW_ENTITY_CODE
			mvs = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
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

		mcd.setContents(mvs);

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
