package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Map;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeOpenCommand extends VoidCommand
{
	ApplicationContext aContext;

	public SchemeOpenCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SchemeOpenCommand(aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeChooserDialog mcd = new SchemeChooserDialog(aContext.getDataSourceInterface());//mapFrame, "Выберите карту", true);

		Map dataSet = Pool.getMap(Scheme.typ);
		java.util.HashMap h;
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

			if (scheme.schemecell == null || scheme.schemecell.length == 0)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"Ошибка открытия схемы " + scheme.getName(),
						"Ошибка",
						JOptionPane.OK_OPTION);
				return;
			}

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
