/**
 * $Id: MapAddMapCommand.java,v 1.15 2005/09/16 14:53:33 krupenn Exp $
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
import java.util.Iterator;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.AbstractNodeController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * �������� � ��� ����� �� ������
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
 */
public class MapAddMapCommand extends AbstractCommand
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

	@Override
	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		
		if(mapFrame == null)
			return;

		MapView mapView = mapFrame.getMapView();
	
		if(mapView == null)
			return;

		this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
				this,
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening"))); //$NON-NLS-1$

		MapTableController mapTableController = MapTableController.getInstance();

		Collection availableMaps;
		try
		{
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(domainId, ObjectEntities.MAP_CODE);
			availableMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			availableMaps.remove(mapView.getMap());
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return;
		}

		this.map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("Map"), //$NON-NLS-1$
				availableMaps,
				mapTableController,
				mapTableController.getKeysArray());

		if(this.map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted"))); //$NON-NLS-1$
			return;
		}

		if(!mapView.getMap().getMaps().contains(this.map))
		{
			mapView.getMap().addMap(this.map);

			MapViewController mapViewController = mapFrame.getMapViewer()
				.getLogicalNetLayer().getMapViewController();

			for(Iterator iter = this.map.getNodes().iterator(); iter.hasNext();) {
				AbstractNode node = (AbstractNode )iter.next();
				AbstractNodeController nodeController = (AbstractNodeController )
					mapViewController.getController(node);
				nodeController.updateScaleCoefficient(node);
			}
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(
					this,
					MapEvent.MAP_VIEW_CHANGED, 
					mapView));
		}
		this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
				this,
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelGeneral.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

}
