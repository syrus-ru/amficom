package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.SchemePropsPanel;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;

public class SchemeSaveAsCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;
	UgoTabbedPane ugoTab;

	public int ret_code = CANCEL;

	public SchemeSaveAsCommand(ApplicationContext aContext, SchemeTabbedPane schemeTab, UgoTabbedPane ugoTab)
	{
		this.aContext = aContext;
		this.schemeTab = schemeTab;
		this.ugoTab = ugoTab;
	}

	public Object clone()
	{
		return new SchemeSaveAsCommand(aContext, schemeTab, ugoTab);
	}

	public void execute()
	{
		SchemeGraph graph = schemeTab.getPanel().getGraph();
		SchemeGraph ugograph = ugoTab.getPanel().getGraph();
		Scheme scheme = graph.getScheme();

//		if (scheme.getId().equals(""))
//		{
//			new SchemeSaveCommand(aContext, schemeTab, ugoTab).execute();
//			return;
//		}

		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
			new PathSaveCommand(aContext, schemeTab).execute();
		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
			return;

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			if (ugograph.getRoots().length == 0)
			{
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?", "Предупреждение", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
		}
		else if (scheme.getUgoCell() == null)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?", "Предупреждение", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}

		SaveDialog sd;
		while (true)
		{
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = sd.init(scheme, scheme.getName()+ " (copy)", false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
//		ComponentSaveCommand.saveTypes(aContext.getDataSourceInterface(), false);
		scheme = (Scheme) scheme.clone();

		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++)
		{
			SchemeElement se = scheme.getSchemeElementsAsArray()[i];
			if (se.getSchemeElementsAsArray().length != 0)
			{
				SchemePanel.copySchemeElementFromArchivedState_virtual(se.getSchemeCell().getData());
			}
		}

			/*********/

		UgoPanel.assignClonedIds(graph.getAll());

		scheme.getSchemeCell().setData((List)graph.getArchiveableState(graph.getRoots()));
		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			UgoPanel.assignClonedIds(ugograph.getAll());
			scheme.getUgoCell().setData((List)ugograph.getArchiveableState(ugograph.getRoots()));
			ugoTab.setGraphChanged(false);
		}
		else
			SchemePanel.copySchemeElementFromArchivedState_virtual(scheme.getSchemeCell().getData());

//		if (!res)
//		{
//			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
//																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
//			return;
//		}

		scheme.setName(sd.name);
		scheme.setDescription(sd.description);
		scheme.setSchemeKind(sd.type);

		final Identifier domainId = new Identifier(
				((RISDSessionInfo) aContext
						.getSessionInterface())
						.getAccessIdentifier().domain_id);
		scheme.setDomainId(domainId);

		try {
			SchemeStorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Схема " + scheme.getName() + " успешно сохранена",
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(new TreeListSelectionEvent("",
					TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

//		scheme.serializable_cell = null;
//		scheme.serializable_ugo = null;
//		scheme.unpack();

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		Pool.removeMap("clonedids");
		ret_code = OK;
	}
}

class SaveDialog extends JDialog
{
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Отмена");

	SchemePropsPanel panel;

	public String name = "";
	public String description = "";
	public SchemeKind type = SchemeKind.NETWORK;

	public int retCode = 0;

	ApplicationContext aContext;
	Dispatcher dispatcher;

	public SaveDialog(ApplicationContext aContext, Dispatcher dispatcher, String title)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		this.dispatcher = dispatcher;

		setTitle(title);
		//setResizable(false);
		setModal(true);

		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				//undo();
			}
		});

		Dimension frameSize = new Dimension (400, 165);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public int init(Scheme scheme, String initialName, boolean show_ugo)
	{
		panel = new SchemePropsPanel(aContext, dispatcher, show_ugo);
		panel.setSchemeName(initialName);
		panel.setSchemeDescription(scheme.getDescription());
		panel.setSchemeType(scheme.getSchemeKind());
	//	panel.init(graph.scheme, aContext.getDataSourceInterface());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		// BUTTONS
		JPanel buttonPanel = new JPanel();

		buttonPanel.setPreferredSize(new Dimension(300, 30));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
		buttonPanel.setLayout(new FlowLayout());
		okButton.setPreferredSize(new Dimension(80, 25));
		cancelButton.setPreferredSize(new Dimension(80, 25));
		buttonPanel.add(okButton, FlowLayout.LEFT);
		buttonPanel.add(cancelButton, FlowLayout.CENTER);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				this_okButtonActionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				//undo();
				dispose();
			}
		});
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setVisible(true);
		return retCode;
	}

	void this_okButtonActionPerformed()
	{
		retCode = 1;
		name = panel.getSchemeName();
		description = panel.getSchemeDescription();
		type = panel.getSchemeType();
		dispose();
	}
}


