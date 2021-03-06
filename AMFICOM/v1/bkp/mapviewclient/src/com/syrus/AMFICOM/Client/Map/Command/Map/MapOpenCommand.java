/**
 * $Id: MapOpenCommand.java,v 1.21 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ??????? ?????????????????? ???????????????????
 *         ???????????????? ???????? ?????????? ???????????
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
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
import com.syrus.AMFICOM.map.Map;

/**
 * ??????? ?????. ????? ??????????? ? ????? ????
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	protected Map map;

	protected boolean canDelete = false;

	public MapOpenCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag) {
		this.canDelete = flag;
	}

	public Map getMap() {
		return this.map;
	}

	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		Collection maps;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_ENTITY_CODE);
			maps = StorableObjectPool.getStorableObjectsByCondition(
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

		MapTableController mapTableController = MapTableController.getInstance();

		WrapperedTableChooserDialog mapChooserDialog = new WrapperedTableChooserDialog(
				LangModelMap.getString("Map"),
				mapTableController,
				mapTableController.getKeysArray());

		mapChooserDialog.setCanDelete(this.canDelete);

		mapChooserDialog.setContents(maps);

		mapChooserDialog.setModal(true);
		mapChooserDialog.setVisible(true);

		if(mapChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mapChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_OK) {
			this.map = (Map )mapChooserDialog.getReturnObject();

			setResult(Command.RESULT_OK);

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished")));
		}
	}

}
