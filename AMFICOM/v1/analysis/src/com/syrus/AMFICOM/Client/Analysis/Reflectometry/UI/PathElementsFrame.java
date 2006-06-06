package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
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
	protected AnalysisPanel createSpecificAnalysisPanel(PFTrace pf) {
		double deltaX = pf.getResolution();
		double[] y = pf.getFilteredTraceClone();

		SchemePath path = null;
		try {
			Log.debugMessage("Start create PathElements panel", Level.FINEST);
			MonitoredElement me = StorableObjectPool.getStorableObject(Identifier.valueOf(pf.getBS().monitoredElementId), true);
			Log.debugMessage("Found MonitoredElement " + me.getName() + " ("+ me.getId() + ")", Level.FINEST);
			if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
				LinkedIdsCondition condition = new LinkedIdsCondition(me.getMeasurementPortId(), ObjectEntities.PATHELEMENT_CODE);
				Set<PathElement> pathElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				Log.debugMessage("Found " + pathElements.size() + " PathElement(s)", Level.FINEST);	
				if (!pathElements.isEmpty()) {
					PathElement pe = pathElements.iterator().next();
					path = pe.getParentPathOwner();
					Log.debugMessage("Selected Path is " + path.getName() + " (" + path.getId() + ")", Level.FINEST);
				}
			} else {
				Log.debugMessage("ME sort is " + me.getSort() + "; must be " + MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH, Level.FINEST);
			}
			setTitle(me.getName());
		} catch (ApplicationException ex) {
			Log.errorMessage(ex);
			setTitle(I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN));
		} catch (Exception ex) {
			Log.errorMessage("Exception occured while searching path: " + ex.getMessage());
			setTitle(I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN));
		}

		PathElementsPanel p = new PathElementsPanel((PathElementsLayeredPanel) this.panel, this.dispatcher, y, deltaX);
		p.setPath(path);

		return p;
	}
}
