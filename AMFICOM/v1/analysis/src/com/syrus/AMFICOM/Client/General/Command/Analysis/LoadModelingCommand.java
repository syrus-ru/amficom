/*-
 * $Id: LoadModelingCommand.java,v 1.1 2006/03/22 14:10:44 stas Exp $
 *
 * Copyright ? 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.ModelLoadDialog;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class LoadModelingCommand extends AbstractCommand {
	ApplicationContext aContext;

	public LoadModelingCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		if(ModelLoadDialog.showDialog(AbstractMainFrame.getActiveMainFrame()) != JOptionPane.OK_OPTION) {
			return;
		}

		Set<Result> results = ModelLoadDialog.getResults();

		if(results.isEmpty()) {
			Log.debugMessage("No results found", Level.WARNING);
			return;
		}

		try {
			for (Result res : results) {
				BellcoreStructure bs = AnalysisUtil.getBellcoreStructureFromResult(res);
				
				if (res.getSort().equals(ResultSort.RESULT_SORT_MODELING)) {
					final Action action = res.getAction();
					Modeling m = (Modeling) action;
					bs.title = m.getName();
					
					MonitoredElement me = StorableObjectPool.getStorableObject(m.getMonitoredElementId(), true);
					
					if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
						Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();
						
						LinkedIdsCondition condition = new LinkedIdsCondition(tpathIds, ObjectEntities.SCHEMEPATH_CODE);
						Set<SchemePath> paths = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						
						for (SchemePath path : paths) {
							bs.schemePathId = path.getId().getIdentifierString();
							break;
						}
					}
					
					if (Heap.getPrimaryTrace() == null) { // no primary trace loaded yet - load as primary
						Heap.openPrimaryTraceFromBS(bs, Heap.PRIMARY_TRACE_KEY);
						Heap.makePrimaryAnalysis();
					} else { // load as secondary
						Heap.putSecondaryTraceByKeyFromBS(m.getId().getIdentifierString(), bs);
						Heap.setCurrentTrace(m.getId().getIdentifierString());
					}
					break;
				}
			}
		} catch (SimpleApplicationException e) {
			Log.errorMessage(e);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
