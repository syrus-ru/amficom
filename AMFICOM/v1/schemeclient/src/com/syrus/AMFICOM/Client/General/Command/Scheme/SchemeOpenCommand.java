package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

import java.util.List;

import javax.swing.JOptionPane;

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

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(SchemeController.getInstance(), Scheme.typ);

		List ms = Pool.getList(Scheme.typ);
		mcd.setContents(ms);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true); //ф-я фильтрации схем по домену
		ortm.fireTableDataChanged();

		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.getReturnCode() == mcd.RET_CANCEL)
			return;

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			Scheme scheme = (Scheme)mcd.getReturnObject();

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
}
