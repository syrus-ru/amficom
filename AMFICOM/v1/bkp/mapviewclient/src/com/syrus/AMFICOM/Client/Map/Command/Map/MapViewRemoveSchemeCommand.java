/**
 * $Id: MapViewRemoveSchemeCommand.java,v 1.5 2005/03/01 15:38:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.corba.Scheme;

/**
 * ������ �� ���� ��������� ����� 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/03/01 15:38:42 $
 * @module mapviewclient_v1
 */
public class MapViewRemoveSchemeCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	public MapViewRemoveSchemeCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
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

		mcd.setContents(mapView.getSchemes());

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() != ObjectResourceChooserDialog.RET_OK)
		{
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		Scheme scheme = (Scheme )mcd.getReturnObject();

		controller.removeScheme(scheme);
		mapFrame.getContext().getDispatcher().notify(new MapEvent(
				mapFrame.getMapView(),
				MapEvent.MAP_VIEW_CHANGED));

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
