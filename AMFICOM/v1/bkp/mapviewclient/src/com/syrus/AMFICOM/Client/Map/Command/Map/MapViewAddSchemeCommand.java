/**
 * $Id: MapViewAddSchemeCommand.java,v 1.2 2005/01/30 15:38:17 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.SchemeController;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;

import java.util.List;

import javax.swing.JDesktopPane;

/**
 * добавить в вид схему из списка
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/30 15:38:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapViewAddSchemeCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected ObjectResource retObj;

	public MapViewAddSchemeCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public ObjectResource getReturnObject()
	{
		return this.retObj;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(desktop);
		
		if(mapFrame == null)
			return;
	
		MapView mapView = mapFrame.getMapView();
	
		if(mapView == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(
				SchemeController.getInstance(), 
				ObjectEntities.SCHEME_ENTITY);

	
		try
		{
			Identifier domainId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().domain_id);
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
					domainId,
					false);
			DomainCondition condition = new DomainCondition(
				domain,
				ObjectEntities.SCHEME_ENTITY_CODE);
			List ss = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);
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
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			Scheme retObj = (Scheme )mcd.getReturnObject();

			if(!mapView.getSchemes().contains(retObj))
			{
				mapView.addScheme((Scheme )retObj);
				aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.MAP_VIEW_CHANGED));
				aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.NEED_REPAINT));
//				mapView.getLogicalNetLayer().repaint(false);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
