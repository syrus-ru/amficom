/*
 * $Id: MapViewSaveAsCommand.java,v 1.28 2005/09/25 16:08:02 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� ������������ ��� ���������� �������������� ����� � �����
 * ������
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/09/25 16:08:02 $
 * @module mapviewclient
 */
public class MapViewSaveAsCommand extends AbstractCommand {
	MapView mapView;

	MapView newMapView;

	ApplicationContext aContext;

	public MapViewSaveAsCommand(MapView mapView, ApplicationContext aContext) {
		this.mapView = mapView;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		try {
			this.newMapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					userId,
					domainId,
					LangModelMap.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING,
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					this.mapView.getMap());

			StorableObjectPool.putStorableObject(this.newMapView);

			this.newMapView.setName(this.mapView.getName() + LangModelMap.getString(MapEditorResourceKeys.IS_ACOPY_IN_PARENTHESIS));
		} catch(CreateObjectException e) {
			e.printStackTrace();
			return;
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		}

		if(EditorDialog.showEditorDialog(
				LangModelMap.getString(MapEditorResourceKeys.TITLE_MAP_VIEW_PROPERTIES),
				this.newMapView,
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel())) {
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
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
					this,
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString(MapEditorResourceKeys.STATUS_MAP_VIEW_SAVING)));
			try {
				// save mapview
				StorableObjectPool.flush(this.newMapView, userId, true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

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

	public MapView getMapView() {
		return this.mapView;
	}

	public MapView getNewMapView() {
		return this.newMapView;
	}

}
