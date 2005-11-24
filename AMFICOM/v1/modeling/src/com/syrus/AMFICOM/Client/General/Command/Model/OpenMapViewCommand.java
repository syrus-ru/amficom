/*-
 * $Id: OpenMapViewCommand.java,v 1.1 2005/11/24 15:46:12 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class OpenMapViewCommand extends MapEditorOpenViewCommand {

	public OpenMapViewCommand(
			JDesktopPane desktop, 
			ApplicationContext aContext, 
			MapApplicationModelFactory factory) {
		super(desktop, aContext, factory);
		setCanDelete(false);
	}

	@Override
	public void execute() {
		try {
			super.execute();
	
			if(super.getResult() == Command.RESULT_OK) {
				MapFrame frame = super.getMapFrame();
				frame.setName(MapFrame.NAME);

				frame.getModel().getCommand(MapApplicationModel.MODE_NODES).execute();
				frame.getModel().getCommand(MapApplicationModel.MODE_PATH).execute();
				
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS).execute();
				aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR).execute();
				aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS).execute();
				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_TRANS_DATA).execute();
				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS).execute();
				
				aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();

				// also open linked scheme
				LinkedIdsCondition condition = new LinkedIdsCondition(
						super.mapView.getMap().getId(), ObjectEntities.SCHEME_CODE);
				try {
					Set<Scheme> schemes = StorableObjectPool.getStorableObjectsByCondition(condition, false);
					if (!schemes.isEmpty()) {
						Scheme scheme = schemes.iterator().next();
						this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));						
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
			}
		} catch(RuntimeException ex) {
			ex.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}
}