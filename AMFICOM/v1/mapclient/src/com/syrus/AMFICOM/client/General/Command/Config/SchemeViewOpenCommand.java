package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.General.SchemeGraph;
import com.syrus.AMFICOM.Client.Schematics.General.SchemePanel;
import com.syrus.AMFICOM.Client.Schematics.General.SchemePanelNoEdition;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDisplayModel;
import com.syrus.AMFICOM.Client.Schematics.Elements.ElementsListFrame;
import com.syrus.AMFICOM.Client.Schematics.Elements.PropsFrame;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class SchemeViewOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	public String scheme_id;
	JDesktopPane desktopPane;
	public boolean opened = false;

	public JInternalFrame frame;

	public SchemeViewOpenCommand(JDesktopPane desktopPane, ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.desktopPane = desktopPane;
	}

	public Object clone()
	{
		return new SchemeViewOpenCommand(desktopPane, aContext);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.schemeViewing))
		{
			return;
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		LangModelSchematics.initialize();
		SchemeChooserDialog mcd = new SchemeChooserDialog(dataSource);//mapFrame, "Выберите карту", true);

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
			return;

		if(mcd.retCode == mcd.RET_OK)
		{
			opened = true;
			Scheme scheme = (Scheme)mcd.retObject;
			scheme_id = scheme.getName();//scheme.getId();
//
//
//			Dimension dim = desktopPane.getSize();
//
//			SchemeViewerFrame frame = null;
//			PropsFrame propsFrame = null;
//			ElementsListFrame elementsListFrame = null;
//			for(int i = 0; i < desktopPane.getComponents().length; i++)
//			{
//				Component comp = desktopPane.getComponent(i);
//				if (comp instanceof SchemeViewerFrame)
//					frame = (SchemeViewerFrame)comp;
//				else if (comp instanceof PropsFrame)
//					propsFrame = (PropsFrame)comp;
//				else if (comp instanceof ElementsListFrame)
//					elementsListFrame = (ElementsListFrame)comp;
//			}
//			if (frame == null)
//			{
//				SchemePanel spanel = new SchemePanelNoEdition(aContext);
//				frame = new SchemeViewerFrame(aContext, spanel);
////				frame = new SchemeViewerFrame(aContext);
//				frame.setTitle("Схема сети");
//				frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
//				desktopPane.add(frame);
//			}
//			frame.setLocation(0, 0);
//			frame.setSize(dim.width * 4 / 5, dim.height);
//			frame.show();
//
//			SchemeGraph graph = frame.getGraph();
//
//			graph.setSelectionCells(new Object[0]);
//			Object[] cells = graph.getAll();
//			graph.getModel().remove(cells);
//
//			scheme.unpack();
//			graph.setFromArchivedState(scheme.serializable_cell);
//
//			if (elementsListFrame == null)
//			{
//				elementsListFrame = new ElementsListFrame(aContext, false);
//				desktopPane.add(elementsListFrame);
//				elementsListFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
//			}
//			elementsListFrame.setVisible(true);
//			elementsListFrame.setSize(dim.width / 5, dim.height / 3);
//			elementsListFrame.setLocation(dim.width * 4 / 5, 0);
//			if (propsFrame == null)
//			{
//				propsFrame = new PropsFrame(aContext, false);
//				desktopPane.add(propsFrame);
//				propsFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
//			}
////			propsFrame.can_be_editable = false;
//			propsFrame.setVisible(true);
//			propsFrame.setSize(dim.width / 5, dim.height * 2 / 3);
//			propsFrame.setLocation(dim.width * 4 / 5, dim.height / 3);
//
//			propsFrame.toFront();
//			elementsListFrame.toFront();
//			frame.toFront();
//
//			aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		}
	}

	class SchemeChooserDialog extends MapChooserDialog
	{
		public SchemeChooserDialog(DataSourceInterface dataSource)
		{
			super(dataSource);
		}

		public void jbInit() throws Exception
		{
			super.jbInit();
			this.setTitle("Cхема");
		}
	}
}
