package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;
import com.syrus.AMFICOM.Client.Schematics.Scheme.*;

public class SurveySchemeOpenCommand extends VoidCommand
{
	ApplicationContext aContext;
	public String scheme_id;
	JDesktopPane desktopPane;
	public boolean opened = false;

	public JInternalFrame frame;

	public SurveySchemeOpenCommand(JDesktopPane desktopPane, ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.desktopPane = desktopPane;
	}

	public Object clone()
	{
		return new SurveySchemeOpenCommand(desktopPane, aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeChooserDialog mcd = new SchemeChooserDialog(dataSource);//mapFrame, "Выберите карту", true);

		List dataSet = Pool.getList(Scheme.typ);
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
			scheme_id = scheme.getId();

			Dimension dim = desktopPane.getSize();

			SchemeViewerFrame frame = null;
			PropsFrame propsFrame = null;
			ElementsListFrame elementsListFrame = null;
			for(int i = 0; i < desktopPane.getComponents().length; i++)
			{
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof SchemeViewerFrame)
					frame = (SchemeViewerFrame)comp;
				else if (comp instanceof PropsFrame)
					propsFrame = (PropsFrame)comp;
				else if (comp instanceof ElementsListFrame)
					elementsListFrame = (ElementsListFrame)comp;
			}
			if (frame == null)
			{
				SchemePanel spanel = new SchemePanelNoEdition(aContext);
				frame = new SchemeViewerFrame(aContext, spanel);
//				frame = new SchemeViewerFrame(aContext);
				frame.setTitle("Схема сети");
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				desktopPane.add(frame);
			}
      
      aContext.getDispatcher().notify (
        new OperationEvent (frame,0,SchemeViewerFrame.schemeFrameDisplayed));
      
      
			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 4 / 5, dim.height * 3 / 4);
			frame.show();

			SchemeGraph graph = frame.getGraph();

			graph.setSelectionCells(new Object[0]);
			Object[] cells = graph.getAll();
			graph.getModel().remove(cells);

			scheme.unpack();
			graph.setFromArchivedState(scheme.serializable_cell);

			if (elementsListFrame == null)
			{
				elementsListFrame = new ElementsListFrame(aContext, false);
				desktopPane.add(elementsListFrame);
				elementsListFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
			elementsListFrame.setVisible(true);
			elementsListFrame.setSize(dim.width / 5, 2 * dim.height / 8);
			elementsListFrame.setLocation(0, dim.height / 4);
			if (propsFrame == null)
			{
				propsFrame = new PropsFrame(aContext, false);
				desktopPane.add(propsFrame);
				propsFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
//			propsFrame.can_be_editable = false;
			propsFrame.setVisible(true);
			propsFrame.setSize(dim.width / 5, 4 * dim.height / 8);
			propsFrame.setLocation(0, dim.height / 2);

			propsFrame.toFront();
			elementsListFrame.toFront();
			frame.toFront();

			aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
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

