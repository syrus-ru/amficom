package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import java.awt.event.KeyEvent;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class SchemePanelNoEdition extends SchemePanel
{
	Map animators = new HashMap();

	public SchemePanelNoEdition(ApplicationContext aContext)
	{
		super(aContext);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void init_module()
	{
		super.init_module();
		dispatcher.register(this, MapNavigateEvent.MAP_NAVIGATE);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.CREATE_ALARMED_LINK)
			{
				SchemeElement se = (SchemeElement)see.obj;
				SchemePath path = se.alarmedPath();
				startPathAnimator(path, se.alarmedPathElement());
			}

			if (!see.OPEN_PRIMARY_SCHEME)
				return;
			if (see.OBJECT_TYPE_UPDATE)
				return;

		}
		if (ae.getActionCommand().equals(MapNavigateEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent)ae;
			if (mne.isDataMarkerCreated())
			{
				//				System.out.println("DATA_ALARMMARKER_CREATED: " + mne.linkID);
				try {
					SchemePath path = (SchemePath)SchemeStorableObjectPool.getStorableObject(mne.getSchemePathId(), true);
					startPathAnimator(path, SchemeUtils.getPathElement(path, mne.getSchemePathElementId()));
				}
				catch (ApplicationException ex) {
				}
			}
			if (mne.isDataMarkerDeleted())
			{
//				System.out.println("DATA_ALARMMARKER_DELETED: " + mne.linkID);
				stopPathAnimator(mne.getSchemePathElementId());
			}
		}
		super.operationPerformed(ae);
	}

	public String getReportTitle()
	{
		return LangModelSchematics.getString("scheme");
	}

	public void keyPressed(KeyEvent e)
	{
	}

	private void jbInit() throws Exception
	{
		remove(toolbar);
		graph.setGridVisible(false);
		graph.setGridVisibleAtActualSize(false);
		graph.setBorderVisible(false);
		graph.setPortsVisible(false);
		//graph.setRequestFocusEnabled(true);
		//graph.setEditable(false);
		//graph.getSelectionModel().setChildrenSelectable(false);

		graph.setGraphEditable(false);
	}

	public void startPathAnimator(SchemePath path, PathElement pe)
	{
		if (pe == null)
			return;

		AlarmedPathAnimator ap;
		if (animators.containsKey(pe.id()))
			ap = (AlarmedPathAnimator)animators.get(pe.id());
		else
		{
			ap = new AlarmedPathAnimator(aContext, this, path, pe);
			animators.put(pe.id(), ap);
			ap.mark();
		}
	}

	public void stopPathAnimator(Identifier pe_id)
	{
		if (pe_id != null)
		{
			if (animators.containsKey(pe_id))
			{
				AlarmedPathAnimator ap = (AlarmedPathAnimator)animators.get(pe_id);
				ap.unmark();
				animators.remove(pe_id);
			}
		}
		else
		{
			for (Iterator it = animators.values().iterator(); it.hasNext();)
			{
				AlarmedPathAnimator ap = (AlarmedPathAnimator)it.next();
				ap.unmark();
			}
			animators = new HashMap();
		}
	}
}

