/*-
 * $Id: SchemeSaveAllCommand.java,v 1.3 2006/02/10 12:39:36 stas Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class SchemeSaveAllCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	SchemeTabbedPane schemeTab;
	
	private static short[] entities = new short[] {
			ObjectEntities.SCHEME_CODE,
			ObjectEntities.SCHEMECABLELINK_CODE,
			ObjectEntities.SCHEMECABLEPORT_CODE,
			ObjectEntities.SCHEMECABLETHREAD_CODE,
			ObjectEntities.SCHEMEDEVICE_CODE,
			ObjectEntities.SCHEMEELEMENT_CODE,
			ObjectEntities.SCHEMELINK_CODE,
			ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE,
			ObjectEntities.SCHEMEOPTIMIZEINFO_CODE,
			ObjectEntities.SCHEMEPATH_CODE,
			ObjectEntities.SCHEMEPORT_CODE,
			ObjectEntities.CABLECHANNELINGITEM_CODE,
			ObjectEntities.CABLELINK_CODE,
			ObjectEntities.CABLELINK_TYPE_CODE,
			ObjectEntities.CABLETHREAD_CODE,
			ObjectEntities.CABLETHREAD_TYPE_CODE,
			ObjectEntities.CHARACTERISTIC_CODE,
			ObjectEntities.CHARACTERISTIC_TYPE_CODE,
			ObjectEntities.EQUIPMENT_CODE,
			ObjectEntities.IMAGERESOURCE_CODE,
			ObjectEntities.KIS_CODE,
			ObjectEntities.LINK_CODE,
			ObjectEntities.LINK_TYPE_CODE,
			ObjectEntities.MEASUREMENTPORT_CODE,
			ObjectEntities.MEASUREMENTPORT_TYPE_CODE,
			ObjectEntities.MONITOREDELEMENT_CODE,
			ObjectEntities.PATHELEMENT_CODE,
			ObjectEntities.PORT_CODE,
			ObjectEntities.PORT_TYPE_CODE,
			ObjectEntities.TRANSMISSIONPATH_CODE,
			ObjectEntities.TRANSPATH_TYPE_CODE
	};

	public SchemeSaveAllCommand(SchemeTabbedPane schemeTab) {
		this.schemeTab = schemeTab;
	}

	@Override
	public void execute() {
		
		//check for valid scheme image 
		for (ElementsPanel panel : this.schemeTab.getAllPanels()) {
			SchemeGraph graph = panel.getGraph();
			
			long status = SchemeActions.getGraphState(graph);
			if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.empty_scheme"), //$NON-NLS-1$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			}
			if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.ungrouped_device"), //$NON-NLS-1$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			}
			if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.component_not_found"), //$NON-NLS-1$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			}
		}
		
		//save all graph changes
		for (ElementsPanel panel : this.schemeTab.getAllPanels()) {
			SchemeGraph graph = panel.getGraph();
			if (graph.isGraphChanged()) {
				SchemeResource res = panel.getSchemeResource();
				
				try {
					SchemeImageResource schemeCell = null;
					if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) { // сохраняем компонент
						SchemeElement se = res.getSchemeElement();
						schemeCell = se.getSchemeCell();
						if (schemeCell == null) {
							schemeCell = SchemeObjectsFactory.createSchemeImageResource();
							se.setSchemeCell(schemeCell);
						}
					} else if (res.getCellContainerType() == SchemeResource.SCHEME) { // сохраняем схему
						Scheme scheme = res.getScheme();
						schemeCell = scheme.getSchemeCell();
						if (schemeCell == null) {
							schemeCell = SchemeObjectsFactory.createSchemeImageResource();
							scheme.setSchemeCell(schemeCell);
						}
					} else {
						assert false : "Can't save cellContainer " + res.getCellContainer() + " with type " + res.getCellContainerType();
					}
					schemeCell.setData((List)graph.getArchiveableState());
					this.schemeTab.setGraphChanged(graph, false);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
		saveEntities();
	}
	
	public static void saveEntities() {
		// save all scheme deals entities
		Identifier userId = LoginManager.getUserId();
		
		try {
			long t = System.currentTimeMillis();
			for (short entityCode : entities) {
				saveEntity(userId, entityCode);
			}
			Log.debugMessage("Total save took " + (System.currentTimeMillis() - t) + "ms", Level.FINEST);			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.information.all_schemes_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
					LangModelScheme.getString("Message.information"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.save_scheme") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			Log.errorMessage(e);
		}
	}
	
	private static void saveEntity(Identifier modifierId, short entityCode) throws ApplicationException {
		long t = System.currentTimeMillis();
		StorableObjectPool.flush(entityCode, modifierId, false);
		long t1 = System.currentTimeMillis();
		Log.debugMessage(ObjectEntities.codeToString(entityCode) + " saved for " + (t1 - t) + "ms", Level.FINEST);
	}
}
