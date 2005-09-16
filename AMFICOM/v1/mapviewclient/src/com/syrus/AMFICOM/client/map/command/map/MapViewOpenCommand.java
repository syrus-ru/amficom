/**
 * $Id: MapViewOpenCommand.java,v 1.30 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.ui.MapViewTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * открыть вид 
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
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

	@Override
	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening"))); //$NON-NLS-1$

		Collection mapViews;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAPVIEW_CODE);
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

		this.mapView = (MapView )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("MapView"), //$NON-NLS-1$
				mapViews,
				mapViewTableController,
				mapViewTableController.getKeysArray(),
				true);

		if(this.mapView == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		try {
			this.mapView.getMap().open();
			for(Scheme scheme : this.mapView.getSchemes()) {
				MapViewOpenCommand.openScheme(scheme);
			}
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}
		setResult(Command.RESULT_OK);

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished"))); //$NON-NLS-1$
	}

	// TODO think of moving this method to 'Scheme'
	public static void openScheme(Scheme scheme) throws ApplicationException {
		Set<Identifiable> reverseDependencies = scheme.getReverseDependencies();

		Map<Short, Set<Identifier>> objectsToLoad = new HashMap<Short, Set<Identifier>>();

		for(Identifiable identifiable : reverseDependencies) {
			Short major = Short.valueOf(identifiable.getId().getMajor());
			Set<Identifier> identifierSet = objectsToLoad.get(major);
			if(identifierSet == null) {
				identifierSet = new HashSet<Identifier>();
			}
			identifierSet.add(identifiable.getId());
		}

		for(Short major : objectsToLoad.keySet()) {
			Set<Identifier> identifierSet = objectsToLoad.get(major);
			StorableObjectPool.getStorableObjects(identifierSet, true);
		}
	}
}
