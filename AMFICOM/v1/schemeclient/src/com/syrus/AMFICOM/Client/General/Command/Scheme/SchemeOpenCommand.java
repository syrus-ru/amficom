package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schematics.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;

	public SchemeOpenCommand(ApplicationContext aContext, SchemeGraph graph)
	{
		this.aContext = aContext;
		this.graph = graph;
	}

	public Object clone()
	{
		return new SchemeOpenCommand(aContext, graph);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeChooserDialog mcd = new SchemeChooserDialog(aContext.getDataSourceInterface());//mapFrame, "Выберите карту", true);

		DataSet dataSet = new DataSet(Pool.getHash(Scheme.typ));
		ObjectResourceDisplayModel odm = new SchemeDisplayModel();
		ObjectResourceSorter sorter = Scheme.getDefaultSorter();
		sorter.setDataSet(dataSet);
		mcd.setContents(odm, sorter.default_sort());

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену

		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.retCode == mcd.RET_CANCEL)
			return;

		if(mcd.retCode == mcd.RET_OK)
		{
			Scheme scheme = (Scheme)mcd.retObject;
			GraphActions.clearGraph(graph);

			scheme.serializable_cell = null;
			scheme.serializable_ugo = null;
			scheme.unpack();

			if (scheme.serializable_cell == null)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"Ошибка открытия схемы " + scheme.getName(),
						"Ошибка",
						JOptionPane.OK_OPTION);
			}
			//graph.setSchemeFromArchivedState(scheme.serializable_cell);
		//	graph.copyFromArchivedState(scheme.serializable_cell, new java.awt.Point(0, 0));
//			graph.updatePathsAtScheme(new Vector());

			aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme,
					SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		}
	}

	class SchemeChooserDialog extends MapChooserDialog
	{
		public SchemeChooserDialog(DataSourceInterface dsi)
		{
			super(dsi);
		}

		public void jbInit() throws Exception
		{
			super.jbInit();
			this.setTitle("Cхема");
		}

		public void buttonDelete_actionPerformed(ActionEvent e)
		{
			Scheme scheme = (Scheme)listPane.getSelectedObject();
			aContext.getDataSourceInterface().RemoveScheme(scheme.getId());
		}
	}
}
