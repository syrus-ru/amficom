/**
 * $Id: MapViewOpenCommand.java,v 1.9 2004/12/27 16:49:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
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
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewController;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

import com.syrus.AMFICOM.map.MapStorableObjectPool;
import java.util.List;

import javax.swing.JDesktopPane;

/**
 * открыть вид 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/27 16:49:35 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapViewOpenCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	protected Object retObj;

	protected boolean canDelete = false;

	public MapViewOpenCommand()
	{
	}

	public MapViewOpenCommand(JDesktopPane desktop, MapFrame mapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag)
	{
		this.canDelete = flag;
	}
	
	public Object getReturnObject()
	{
		return this.retObj;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		List mvs;
		try
		{
			Domain domain = (Domain )ConfigurationStorableObjectPool.getStorableObject(
				new Identifier(
					aContext.getSessionInterface().getAccessIdentifier().domain_id), 
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

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(MapViewController.getInstance(), ObjectEntities.MAP_ENTITY);//ObjectEntities.MAP_VIEW_ENTITY
		mcd.setCanDelete(canDelete);

		mcd.setContents(mvs);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену
		ortm.fireTableDataChanged();

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == mcd.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			retObj = mcd.getReturnObject();

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				setResult(Command.RESULT_NO);
			}
			else
			{
				MapView mapView = mapFrame.getMapView();
		
				Map map = mapView.getMap();
		
				mapFrame.setMapView((MapView)mcd.getReturnObject());
				setResult(Command.RESULT_OK);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
