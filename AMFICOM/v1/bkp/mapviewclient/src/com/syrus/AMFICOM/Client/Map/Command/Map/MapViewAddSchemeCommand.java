/**
 * $Id: MapViewAddSchemeCommand.java,v 1.7 2005/03/01 15:38:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.List;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.SchemeController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;

/**
 * �������� � ��� ����� �� ������
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/03/01 15:38:42 $
 * @module mapviewclient_v1
 */
public class MapViewAddSchemeCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected Scheme scheme;

	public MapViewAddSchemeCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Scheme getScheme()
	{
		return this.scheme;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		
		if(mapFrame == null)
			return;

		MapViewController controller = mapFrame.getMapViewer()
			.getLogicalNetLayer().getMapViewController();
	
		MapView mapView = mapFrame.getMapView();
	
		if(mapView == null)
			return;

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(
				LangModelMap.getString("Scheme"),
				SchemeController.getInstance());

	
		try
		{
			Identifier domainId = new Identifier(
				this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
//			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
//					domainId,
//					false);
			StorableObjectCondition condition = new LinkedIdsCondition(domainId, ObjectEntities.SCHEME_ENTITY_CODE);
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
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			this.scheme = (Scheme )mcd.getReturnObject();

			if(!mapView.getSchemes().contains(this.scheme))
			{
				controller.addScheme(this.scheme);
				this.aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.MAP_VIEW_CHANGED));
				this.aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.NEED_REPAINT));
//				mapView.getLogicalNetLayer().repaint(false);
			}
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}

}
