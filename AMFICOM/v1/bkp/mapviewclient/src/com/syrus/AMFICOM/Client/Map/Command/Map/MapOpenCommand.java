/**
 * $Id: MapOpenCommand.java,v 1.20 2005/05/18 14:59:46 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * открыть карту. карта открывается в новом виде
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2005/05/18 14:59:46 $
 * @module mapviewclient_v1
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
		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

//		StorableObjectContidion 
		Collection maps;
		try
		{
			Identifier domainId = new Identifier(
					this.aContext.getSessionInterface().getAccessIdentifier().domain_id); 
//			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
//				domainId, 
//				false);

			StorableObjectCondition condition = new LinkedIdsCondition(domainId, ObjectEntities.MAP_ENTITY_CODE);
			maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
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

		ObjectResourceChooserDialog mcd = new MapChooserDialog(
				LangModelMap.getString("Map"),
				MapTableController.getInstance());

		mcd.setCanDelete(this.canDelete);

		mcd.setContents(maps);

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
			this.map = (Map )mcd.getReturnObject();

			setResult(Command.RESULT_OK);

			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
