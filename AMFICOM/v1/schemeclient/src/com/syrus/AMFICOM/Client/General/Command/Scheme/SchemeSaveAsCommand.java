package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.Schematics.Elements.SchemePropsPanel;

public class SchemeSaveAsCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	ApplicationContext aContext;
	SchemePanel schemePanel;
	UgoPanel ugoPanel;

	public int ret_code = CANCEL;

	public SchemeSaveAsCommand(ApplicationContext aContext, SchemePanel schemePanel, UgoPanel ugoPanel)
	{
		this.aContext = aContext;
		this.schemePanel = schemePanel;
		this.ugoPanel = ugoPanel;
	}

	public Object clone()
	{
		return new SchemeSaveAsCommand(aContext, schemePanel, ugoPanel);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		if (schemePanel.scheme.getId().equals(""))
		{
			new SchemeSaveCommand(aContext, schemePanel, ugoPanel).execute();
			return;
		}

		SchemeGraph graph = schemePanel.getGraph();
		SchemeGraph ugo_graph = ugoPanel.getGraph();

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if (ugo_graph.getRoots().length == 0)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nВы все равно хотите сохранить схему?", "Предупреждение", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}

		SaveDialog sd;
		while (true)
		{
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = //sd.init(schemePanel.scheme.getName(), schemePanel.scheme.description, false);
					sd.init(schemePanel.scheme, schemePanel.scheme.getName()+ " (copy)", false);
			if (ret == 0)
				return;

			if (!MyUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
		ComponentSaveCommand.saveTypes(aContext.getDataSourceInterface(), false);

		Scheme scheme = (Scheme)schemePanel.scheme.clone(aContext.getDataSourceInterface());
		Hashtable ht = Pool.getHash("clonedids");
//		scheme.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
//		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());

//		GraphActions.clearGraph(graph);
//		GraphActions.clearGraph(ugo_graph);

//		Map sclones = graph.copyFromArchivedState(scheme.serializable_cell, new Point(0, 0));
//		Map uclones = ugo_graph.copyFromArchivedState(scheme.serializable_ugo, new Point(0, 0));

		for (Iterator it = scheme.elements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			if (!se.element_ids.isEmpty())
			{
				se.unpack();
				schemePanel.copySchemeElementFromArchivedState_virtual(se);
				se.pack();
			};
//
		}

			/*********/

		schemePanel.assignClonedIds(graph.getAll());
		ugoPanel.assignClonedIds(ugo_graph.getAll());

		scheme.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
		GraphActions.setResizable(ugo_graph, ugo_graph.getAll(), false);
		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());

		boolean res = scheme.pack();
		if (!res)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		scheme.name = sd.name;
		scheme.description = sd.description;
		scheme.created = System.currentTimeMillis();
		scheme.created_by = dataSource.getSession().getUserId();
		scheme.modified_by = dataSource.getSession().getUserId();
		scheme.owner_id = dataSource.getSession().getUserId();
		scheme.domain_id = dataSource.getSession().getDomainId();
		Pool.put(Scheme.typ, scheme.getId(), scheme);

//		schemePanel.scheme = scheme;


//		schemePanel.insertCell(scheme.serializable_cell, true);
//		ugoPanel.insertCell(scheme.serializable_ugo, true);

		Hashtable h = schemePanel.schemes_to_save;
		h.put(scheme.getId(), scheme);

		for (Enumeration e = h.elements(); e.hasMoreElements();)
		{
			Scheme s = (Scheme)e.nextElement();
			dataSource.SaveScheme(s.getId());
		}
		schemePanel.schemes_to_save = new Hashtable();
		graph.setGraphChanged(false);
		ugo_graph.setGraphChanged(false);

		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Схема "+ scheme.getName() + " успешно сохранена",
				"Сообщение",
				JOptionPane.INFORMATION_MESSAGE);

//		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
//				TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));
		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ, TreeListSelectionEvent.REFRESH_EVENT));

		GraphActions.clearGraph(graph);

		scheme.serializable_cell = null;
		scheme.serializable_ugo = null;
		scheme.unpack();

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		Pool.removeHash("clonedids");
		Pool.remove("serialized", "serialized");
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

	public int init(Scheme scheme, String name, boolean show_ugo)
	{
		panel = new SchemePropsPanel(aContext, dispatcher, show_ugo);
		panel.schemeNameTextField.setText(name);
		panel.schemeDescrTextArea.setText(scheme.description);
		panel.schemeTypeComboBox.setSelectedItem(LangModelSchematics.String(scheme.scheme_type));
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
		dispose();
	}
}


