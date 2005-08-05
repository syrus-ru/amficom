package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.scheme.Scheme;

public class SchemeSaveAsCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;

	public SchemeSaveAsCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
	}

	public Object clone() {
		return new SchemeSaveAsCommand(this.aContext,this.schemeTab);
	}

	public void execute() {
		SchemeGraph graph = this.schemeTab.getGraph();

//		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
//			new PathSaveCommand(aContext, schemeTab).execute();
//		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
//			return;

		if (graph.getRoots().length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"���������� ��������� ������ �����", "������", JOptionPane.OK_OPTION);
			return;
		}

		SchemeResource res = this.schemeTab.getCurrentPanel().getSchemeResource();
		
		Scheme scheme = res.getScheme();
		
		if (scheme.getUgoCell() == null) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"����� ������ ����� �������� � ������ �����,\n�.�. �� ������� �������� ����������� ����������� �����.\n���������� ����������?",
					"��������������", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}
/*
		SaveDialog sd;
		while (true) {
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "���������� �����");
			int ret = sd.init(scheme, scheme.getName() + " (copy)", false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"������������ �������� �����.", "������", JOptionPane.OK_OPTION);
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

			SchemePanel.copyFromArchivedState_virtual(scheme.getUgoCell().getData());

			scheme.setName(sd.name);
			scheme.setDescription(sd.description);
			scheme.setKind(sd.type);

			final Identifier domainId = new Identifier(((RISDSessionInfo) aContext
					.getSessionInterface()).getAccessIdentifier().domain_id);
			scheme.setDomainId(domainId);

			StorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "����� "
					+ scheme.getName() + " ������� ���������", "���������",
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
	}*/
}
/*
class SaveDialog extends JDialog {
	private JButton okButton = new JButton("OK");

	private JButton cancelButton = new JButton("������");

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
	}*/
}
