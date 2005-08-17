/**
 * $Id: MapAddExternalNodeCommand.java,v 1.13 2005/08/17 14:14:18 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.map.ui.ExternalMapElementChooserDialog;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * �������� � ��� ����� �� ������
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/08/17 14:14:18 $
 * @module mapviewclient
 */
public class MapAddExternalNodeCommand extends AbstractCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected Map map;
	protected SiteNode node;

	public MapAddExternalNodeCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Map getMap() {
		return this.map;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		MapView mapView = mapFrame.getMapView();

		if(mapView == null)
			return;

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		MapTableController mapTableController = MapTableController.getInstance();

		Collection availableMaps;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_CODE);
			availableMaps = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			availableMaps.remove(mapView.getMap());
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		this.map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("Map"),
				availableMaps,
				mapTableController,
				mapTableController.getKeysArray());

		if(this.map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		ExternalMapElementChooserDialog elemengChooserDialog = 
			new ExternalMapElementChooserDialog(
				this.map,
				LangModelMap.getString("SiteNode"));

		elemengChooserDialog.setModal(true);
		elemengChooserDialog.setVisible(true);
		if(elemengChooserDialog.getReturnCode() == ExternalMapElementChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		if(elemengChooserDialog.getReturnCode() != ExternalMapElementChooserDialog.RET_OK)
			return;

		this.node = elemengChooserDialog.getReturnObject();

		SiteNodeController siteNodeController = (SiteNodeController )mapFrame
				.getMapViewer().getLogicalNetLayer().getMapViewController()
				.getController(this.node);

		siteNodeController.updateScaleCoefficient(this.node);
		mapView.getMap().addExternalNode(this.node);

		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(mapView, MapEvent.MAP_CHANGED));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
