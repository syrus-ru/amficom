package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Set;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class PathElementsFrame extends AnalysisFrame {
	private static final long serialVersionUID = 4137589435411835248L;
	ApplicationContext aContext;

	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher, AnalysisLayeredPanel panel) {
		super(dispatcher, panel);
		this.aContext = aContext;
	}

	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher) {
		this(aContext, dispatcher, new PathElementsLayeredPanel(dispatcher));
	}

	
	@Override
	protected AnalysisPanel createSpecificAnalysisPanel(BellcoreStructure bs) {
		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

		SchemePath path = null;
		try {
			MonitoredElement me = StorableObjectPool.getStorableObject(new Identifier(bs.monitoredElementId), true);

			Set<SchemePath> paths = StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.SCHEMEPATH_CODE), true); 
			if (!paths.isEmpty()) {
				path = paths.iterator().next();
			}
//			if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
//				LinkedIdsCondition condition = new LinkedIdsCondition(me.getMeasurementPortId(), ObjectEntities.PATHELEMENT_CODE);
//				Set<PathElement> pathElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//				
//				if (!pathElements.isEmpty()) {
//					PathElement pe = pathElements.iterator().next();
//					path = pe.getParentPathOwner();
//				}
//			}
			setTitle(me.getName());
		} catch (ApplicationException ex) {
			Log.errorException(ex);
			setTitle(LangModelAnalyse.getString("analysisTitle"));
		} catch (Exception ex) {
			setTitle(LangModelAnalyse.getString("analysisTitle"));
		}

		PathElementsPanel p = new PathElementsPanel((PathElementsLayeredPanel) this.panel, this.dispatcher, y, deltaX);
		if (path != null) {
			p.setPath(path);
		}
		return p;
	}
}
