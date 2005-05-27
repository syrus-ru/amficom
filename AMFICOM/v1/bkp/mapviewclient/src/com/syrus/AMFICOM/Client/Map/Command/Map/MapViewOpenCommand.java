/**
 * $Id: MapViewOpenCommand.java,v 1.21 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * открыть вид 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class MapViewOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	protected MapView mapView;

	protected boolean canDelete = false;

	public MapViewOpenCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag) {
		this.canDelete = flag;
	}

	public MapView getMapView() {
		return this.mapView;
	}

	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		Collection mapViews;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAPVIEW_ENTITY_CODE);
			mapViews = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
		} catch(CommunicationException e) {
			e.printStackTrace();
			return;
		} catch(DatabaseException e) {
			e.printStackTrace();
			return;
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		MapViewTableController mapViewTableController = 
			MapViewTableController.getInstance();

		WrapperedTableChooserDialog mapViewChooserDialog = new WrapperedTableChooserDialog(
				LangModelMap.getString("MapView"),
				mapViewTableController,
				mapViewTableController.getKeysArray());

		mapViewChooserDialog.setCanDelete(this.canDelete);

		mapViewChooserDialog.setContents(mapViews);

		mapViewChooserDialog.setModal(true);
		mapViewChooserDialog.setVisible(true);

		if(mapViewChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mapViewChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_OK) {
			this.mapView = (MapView )mapViewChooserDialog.getReturnObject();

			setResult(Command.RESULT_OK);

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished")));
		}
	}

}
