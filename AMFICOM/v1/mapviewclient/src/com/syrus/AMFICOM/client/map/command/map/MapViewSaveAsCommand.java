/*
 * $Id: MapViewSaveAsCommand.java,v 1.19 2005/06/06 12:57:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс используется для сохранения топологической схемы с новым
 * именем
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/06/06 12:57:02 $
 * @module mapviewclient_v1
 */
public class MapViewSaveAsCommand extends AbstractCommand {
	MapView mapView;

	MapView newMapView;

	ApplicationContext aContext;

	public MapViewSaveAsCommand(MapView mapView, ApplicationContext aContext) {
		this.mapView = mapView;
		this.aContext = aContext;
	}

	public void execute() {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		try {
			this.newMapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					userId,
					domainId,
					LangModelMap.getString("New"),
					"",
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					this.mapView.getMap());

			StorableObjectPool.putStorableObject(this.newMapView);

			this.newMapView.setName(this.mapView.getName() + "(Copy)");
		} catch(CreateObjectException e) {
			e.printStackTrace();
			return;
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		}

		EditorDialog dialog = new EditorDialog(
				LangModelMap.getString("MapViewProperties"),
				true,
				this.newMapView,
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel());

		dialog.setVisible(true);

		if(dialog.ifAccept()) {
// try
// {
// newMapView = (MapView )mapView.clone();
//			}
//			catch(CloneNotSupportedException e)
//			{
//				return;
//			}
/*
			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Pool.removeHash(MapPropertiesManager.MAP_CLONED_IDS);
			}
*/
			try {
				// save mapview
				StorableObjectPool.flush(this.newMapView.getId(), true);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

	public MapView getMapView() {
		return this.mapView;
	}

	public MapView getNewMapView() {
		return this.newMapView;
	}

}
