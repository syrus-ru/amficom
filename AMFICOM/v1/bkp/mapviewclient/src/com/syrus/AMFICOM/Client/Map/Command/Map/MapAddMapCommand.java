/**
 * $Id: MapAddMapCommand.java,v 1.2 2005/03/16 13:48:18 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import java.util.Collection;
import javax.swing.JDesktopPane;

/**
 * добавить в вид схему из списка
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/16 13:48:18 $
 * @module mapviewclient_v1
 */
public class MapAddMapCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected Map map;

	public MapAddMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Map getMap()
	{
		return this.map;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		
		if(mapFrame == null)
			return;

		MapView mapView = mapFrame.getMapView();
	
		if(mapView == null)
			return;

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(
				LangModelMap.getString("Map"),
				MapTableController.getInstance());

	
		try
		{
			Identifier domainId = new Identifier(
				this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
//			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
//					domainId,
//					false);
			StorableObjectCondition condition = new LinkedIdsCondition(domainId, ObjectEntities.MAP_ENTITY_CODE);
			Collection ss = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
			ss.remove(mapView.getMap());
			mcd.setContents(ss);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return;
		}

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_CANCEL)
		{
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			this.map = (Map )mcd.getReturnObject();

			if(!mapView.getMap().getMaps().contains(this.map))
			{
				mapView.getMap().addMap(this.map);
				this.aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.MAP_VIEW_CHANGED));
				this.aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.NEED_REPAINT));
			}
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
