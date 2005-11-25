package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Model.ModelLoadDialog;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Measurement;
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

		if(ModelLoadDialog.showDialog(Environment.getActiveWindow()) != JOptionPane.OK_OPTION) {
			return;
		}

		Set<Result> results = ModelLoadDialog.getResults();

		if(results.isEmpty()) {
			return;
		}

		Result res = results.iterator().next();
		BellcoreStructure bs;
		try {
			bs = AnalysisUtil.getBellcoreStructureFromResult(res);
		} catch (SimpleApplicationException e) {
			Log.errorMessage(e);
			return;
		} 
		
		if (res.getSort().equals(ResultSort.RESULT_SORT_MODELING)) {
			final Action action = res.getAction();
			Measurement m = (Measurement) action;
			bs.title = m.getName();

			try {
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
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		else {
			final Action action = res.getAction();
			Measurement m = (Measurement) action;
			bs.title = m.getName();
		}

		Heap.openPrimaryTraceFromBS(bs, Heap.PRIMARY_TRACE_KEY);
		Heap.makePrimaryAnalysis();
	}
}

