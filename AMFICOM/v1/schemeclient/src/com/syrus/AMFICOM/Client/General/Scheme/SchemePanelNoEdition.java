package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import java.awt.event.KeyEvent;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

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
		dispatcher.register(this, MapNavigateEvent.type);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.CREATE_ALARMED_LINK)
				startPathAnimator((String)see.obj);
			if (!see.OPEN_PRIMARY_SCHEME)
				return;
			if (see.OBJECT_TYPE_UPDATE)
				return;

		}
		if (ae.getActionCommand().equals(MapNavigateEvent.type))
		{
			MapNavigateEvent mne = (MapNavigateEvent)ae;
			if (mne.DATA_ALARMMARKER_CREATED)
			{
				//				System.out.println("DATA_ALARMMARKER_CREATED: " + mne.linkID);
				startPathAnimator(mne.linkID);
			}
			if (mne.DATA_ALARMMARKER_DELETED)
			{
//				System.out.println("DATA_ALARMMARKER_DELETED: " + mne.linkID);
				stopPathAnimator(mne.linkID);
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

	public void startPathAnimator(String link_id)
	{
		if (link_id == null || link_id.equals(""))
			return;

		System.out.println("link_id =  " + link_id);
		AlarmedPathAnimator ap;

		if (animators.containsKey(link_id))
		{
			ap = (AlarmedPathAnimator)animators.get(link_id);
			System.out.println("get ap " + ap.hashCode());
		}
		else
		{
			ap = new AlarmedPathAnimator(aContext, this, link_id);
			animators.put(link_id, ap);
			ap.mark();
			System.out.println("create ap " + ap.hashCode());
		}
		System.out.println("start ap " + ap.hashCode());
	}

	public void stopPathAnimator(String link_id)
	{
		if (link_id != null)
		{
			if (animators.containsKey(link_id))
			{
				AlarmedPathAnimator ap = (AlarmedPathAnimator)animators.get(link_id);
				ap.unmark();
				animators.remove(link_id);
				System.out.println("stop ap " + ap.hashCode());
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

