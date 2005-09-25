/*
 * $Id: MapSaveCommand.java,v 1.23 2005/09/25 16:08:02 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * ����� ������������ ��� ���������� �������������� ����� �� �������
 * @author $Author: krupenn $
 * @version $Revision: 1.23 $, $Date: 2005/09/25 16:08:02 $
 * @module mapviewclient
 */
public class MapSaveCommand extends AbstractCommand {
	Map map;

	ApplicationContext aContext;

	public MapSaveCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString(MapEditorResourceKeys.STATUS_MAP_SAVING)));

		if(EditorDialog.showEditorDialog(
				LangModelMap.getString(MapEditorResourceKeys.TITLE_MAP_PROPERTIES),
				this.map, 
				MapVisualManager.getInstance().getGeneralPropertiesPanel())) {
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					true));
			try {
				StorableObjectPool.putStorableObject(this.map);
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
			}
			try {
				// save map
				StorableObjectPool.flush(this.map, LoginManager.getUserId(), true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					false));
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished"))); //$NON-NLS-1$
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
		}
	}

}
