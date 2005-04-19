package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.SchemePropsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;
import com.syrus.util.Log;

public class SchemeSaveAsCommand extends VoidCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;
	UgoTabbedPane ugoTab;

	public SchemeSaveAsCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab, UgoTabbedPane ugoTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
		this.ugoTab = ugoTab;
	}

	public Object clone() {
		return new SchemeSaveAsCommand(aContext, schemeTab, ugoTab);
	}

	public void execute() {
		SchemeGraph graph = schemeTab.getGraph();
		SchemeGraph ugograph = ugoTab.getGraph();

//		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
//			new PathSaveCommand(aContext, schemeTab).execute();
//		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
//			return;

		if (graph.getRoots().length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		SchemeResource res = schemeTab.getCurrentPanel().getSchemeResource();
		SchemeResource ugores = ugoTab.getCurrentPanel().getSchemeResource();
		
		Scheme scheme = res.getScheme();
		
		if (scheme.equals(ugores.getScheme())) {
			if (ugograph.getRoots().length == 0) {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?",
						"Предупреждение", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
		} 
		else if (scheme.getUgoCell() == null) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?",
					"Предупреждение", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}

		SaveDialog sd;
		while (true) {
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = sd.init(scheme, scheme.getName() + " (copy)", false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
		scheme = (Scheme) scheme.clone();

		for (Iterator it = scheme.getSchemeElements().iterator(); it.hasNext();) {
			SchemeElement se = (SchemeElement)it.next();
			if (!se.getSchemeElements().isEmpty()) {
				SchemePanel.copyFromArchivedState_virtual(se.getSchemeCell().getData());
			}
		}
		UgoPanel.assignClonedIds(graph.getAll());

		try {		
			if (scheme.getSchemeCell() == null) {
				scheme.setSchemeCell(SchemeObjectsFactory.createImageResource());
			}
			scheme.getSchemeCell().setData((List) graph.getArchiveableState());

			if (scheme.equals(ugores.getScheme())) {
				UgoPanel.assignClonedIds(ugograph.getAll());
				if (scheme.getUgoCell() == null) {
					scheme.setUgoCell(SchemeObjectsFactory.createImageResource());
				}
				scheme.getUgoCell().setData((List) ugograph.getArchiveableState());
				ugoTab.setGraphChanged(false);
			} else
				SchemePanel.copyFromArchivedState_virtual(scheme.getSchemeCell()
						.getData());

			scheme.setName(sd.name);
			scheme.setDescription(sd.description);
			scheme.setKind(sd.type);

			final Identifier domainId = new Identifier(((RISDSessionInfo) aContext
					.getSessionInterface()).getAccessIdentifier().domain_id);
			scheme.setDomainId(domainId);

			SchemeStorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Схема "
					+ scheme.getName() + " успешно сохранена", "Сообщение",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(
					new TreeListSelectionEvent("", TreeListSelectionEvent.SELECT_EVENT
							+ TreeListSelectionEvent.REFRESH_EVENT));
			aContext.getDispatcher().notify(
				new SchemeEvent(this, scheme, SchemeEvent.OPEN_SCHEME));
			Pool.removeMap("clonedids");
			ret_code = OK;
		} 
		catch (ApplicationException ex) {
			Log.errorException(ex);
		}
	}
}

class SaveDialog extends JDialog {
	private JButton okButton = new JButton("OK");

	private JButton cancelButton = new JButton("Отмена");

	SchemePropsPanel panel;

	public String name = "";

	public String description = "";

	public Kind type = Kind.NETWORK;

	public int retCode = 0;

	ApplicationContext aContext;

	Dispatcher dispatcher;

	public SaveDialog(ApplicationContext aContext, Dispatcher dispatcher,
			String title) {
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		this.dispatcher = dispatcher;

		setTitle(title);
		// setResizable(false);
		setModal(true);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// undo();
			}
		});

		Dimension frameSize = new Dimension(400, 165);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

	public int init(Scheme scheme, String initialName, boolean show_ugo) {
		panel = new SchemePropsPanel(aContext, dispatcher, show_ugo);
		panel.setSchemeName(initialName);
		panel.setSchemeDescription(scheme.getDescription());
		panel.setSchemeType(scheme.getKind());
		// panel.init(graph.scheme, aContext.getDataSourceInterface());
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
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				this_okButtonActionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				// undo();
				dispose();
			}
		});
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setVisible(true);
		return retCode;
	}

	void this_okButtonActionPerformed() {
		retCode = 1;
		name = panel.getSchemeName();
		description = panel.getSchemeDescription();
		type = panel.getSchemeType();
		dispose();
	}
}
