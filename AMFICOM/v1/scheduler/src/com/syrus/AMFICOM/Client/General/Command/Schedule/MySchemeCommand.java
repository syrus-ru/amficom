package com.syrus.AMFICOM.Client.General.Command.Schedule;

import javax.swing.*;
import java.awt.*;

import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class MySchemeCommand extends VoidCommand
{
	ScheduleMDIMain parent;
	ApplicationContext aContext;
	String scheme_id;
	SchemeGraph graph;

	public MySchemeCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.parent = parent;
	}

	public Object clone()
	{
		return new MySchemeCommand(parent, aContext);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			String str = fram[i].getName();
			if (str != null && str.equals("scheme"))
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
			}
		}
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		boolean bool = false;
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			String str = fram[i].getName();
			if (str != null && str.equals("scheme"))
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
				 bool = true;
			}
		}
		if (bool == false)
		{
			JInternalFrame frame = new JInternalFrame();
			frame.setClosable(true);
			frame.setResizable(true);
			frame.setTitle(LangModelScheduleOld.String("MySchemeTitle"));
			frame.setName("scheme");
			frame.setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
			if (dataSource == null)
				return;

			SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
			graph = panel.getGraph();
			frame.getContentPane().add(panel);
			parent.desktopPane.add(frame);
			graph.getSelectionModel().setChildrenSelectable(true);


			SchemeChooserDialog mcd = new SchemeChooserDialog(aContext.getDataSourceInterface());//mapFrame, "Выберите карту", true);

			DataSet dataSet = new DataSet(Pool.getHash(Scheme.typ));
			ObjectResourceDisplayModel odm = new SchemeDisplayModel();
			mcd.setContents(odm, dataSet);

			// отфильтровываем по домену
			ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();
			ortm.setDomainId(aContext.getSessionInterface().getDomainId());
			ortm.restrictToDomain(true);//ф-я фильтрации схем по домену

			mcd.setModal(true);
			mcd.setVisible(true);

			if(mcd.retCode == mcd.RET_CANCEL)
				frame.dispose();

			if(mcd.retCode == mcd.RET_OK)
			{
				frame.show();
				Scheme scheme = (Scheme)mcd.retObject;
				graph.setSelectionCells(new Object[0]);
				Object[] cells = graph.getAll();
				graph.getModel().remove(cells);
				panel.scheme = scheme;

				scheme.unpack();
				graph.setFromArchivedState(scheme.serializable_cell);
			}
		}
	}

	class SchemeChooserDialog extends MapChooserDialog
	{
		public SchemeChooserDialog(DataSourceInterface dsi)
		{
			super(dsi);
			setTitle(LangModelScheduleOld.String("MySchemeTitle"));
		}
	}
}

