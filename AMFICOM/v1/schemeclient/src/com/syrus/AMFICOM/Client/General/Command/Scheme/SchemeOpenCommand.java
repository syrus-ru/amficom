package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
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

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(dataSource, Scheme.typ);

		List dataSet = Pool.getList(Scheme.typ);
		ObjectResourceDisplayModel odm = Scheme.getDefaultDisplayModel();
		mcd.setContents(odm, dataSet);

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
