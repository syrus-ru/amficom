/**
 * $Id: MapOpenCommand.java,v 1.11 2005/01/20 14:37:52 krupenn Exp $
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
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.Map.UI.MapController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
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
 * открыть карту. карта открывается в новом виде
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2005/01/20 14:37:52 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	protected Map map;

	protected boolean canDelete = false;

	public MapOpenCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag)
	{
		this.canDelete = flag;
	}

	public Map getMap()
	{
		return this.map;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

//		StorableObjectContidion 
		List maps;
		try
		{
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
				new Identifier(
					aContext.getSessionInterface().getAccessIdentifier().domain_id), 
				false);

			DomainCondition condition = new DomainCondition(
					domain,
					ObjectEntities.MAP_ENTITY_CODE);
			maps = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
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

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(MapController.getInstance(), ObjectEntities.MAP_ENTITY);

		mcd.setCanDelete(canDelete);

		mcd.setContents(maps);

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
			map = (Map )mcd.getReturnObject();

			setResult(Command.RESULT_OK);

			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
