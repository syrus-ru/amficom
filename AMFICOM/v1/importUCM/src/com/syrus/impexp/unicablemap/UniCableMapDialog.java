package com.syrus.impexp.unicablemap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.mapinfo.tab.f;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client_.general.ui_.ChoosableFileFilter;
import com.syrus.impexp.ImportExportException;
import com.syrus.util.ApplicationProperties;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/08/30 12:42:37 $
 * @module mapviewclient_v1
 */
public class UniCableMapDialog extends JFrame 
{
	private static final String ALL_OBJECTS = "all";

	private static final String TEN_OBJECTS = "10";

	private static final String ONE_OBJECT = "1";

	UniCableMapDatabase ucmDatabase = null;

	private JPanel connectionButtonsPanel = new JPanel();
	private JButton connectButton = new JButton();
	private JButton disconnectButton = new JButton();
	
	private JPanel mainPanel = new JPanel();

	private JPanel connectionPanel = new JPanel();
	private JLabel databaseLabel = new JLabel();
	private JTextField databaseField = new JTextField();
	private JButton browseGDBButton = new JButton();
	private JLabel usernameLabel = new JLabel();
	private JTextField usernameField = new JTextField();
	private JLabel passwordLabel = new JLabel();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel hostLabel = new JLabel();
	private JTextField hostField = new JTextField();

	private JPanel importPanel = new JPanel();
	private JLabel exportFileLabel = new JLabel();
	private JTextField exportFileField = new JTextField();
	private JButton browseESFButton = new JButton();
	private JLabel statusLabel = new JLabel();
	private JButton importButton = new JButton();

	private JPanel surveyPanel = new JPanel();
	private JList surveyTypes = new JList();
	private JScrollPane surveyTypesScrollPane;
	private JPanel radioButtonsPanel = new JPanel();
	private JRadioButton oneRadioButton = new JRadioButton();
	private JRadioButton tenRadioButton = new JRadioButton();
	private JRadioButton allRadioButton = new JRadioButton();
	private ButtonGroup countButtonGroup = new ButtonGroup();
	private JTextField surveyFileField = new JTextField();
	private JButton surveyButton = new JButton();
	
	private JSeparator jSeparator1 = new JSeparator();
	private JSeparator jSeparator2 = new JSeparator();
	private JSeparator jSeparator3 = new JSeparator();

	private JTextArea logTextArea = new JTextArea();
	
	public UniCableMapDialog()
	{
		this(null, "", false);
	}

	public UniCableMapDialog(Frame parent, String title, boolean modal)
	{
		super(title);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	private void jbInit() throws Exception
	{
		final String	BUNDLE_NAME		= "impexp";
		ApplicationProperties.init(BUNDLE_NAME);
		
		String input = ApplicationProperties.getString("base", "");
		String output = ApplicationProperties.getString("output", "");
		
		Dimension size = new Dimension(600, 530);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(size);
		this.setLocation(
			(screenSize.width - size.width) / 2,
			(screenSize.height - size.height) / 2);

		this.getContentPane().setLayout(new BorderLayout());
		this.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					thisWindowClosing(e);
				}
			});
		this.connectButton.setText("Connect!");
		this.connectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					connectButton_actionPerformed(e);
				}
			});
		this.disconnectButton.setText("Disconnect");
		this.disconnectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					disconnectButton_actionPerformed(e);
				}
			});
		this.databaseLabel.setText("База данных");
		this.databaseField.setText("jTextField1");
		this.browseGDBButton.setText("Выбрать");
		this.browseGDBButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseGDBButton_actionPerformed(e);
				}
			});
		this.usernameLabel.setText("Пользователь");
		this.passwordLabel.setText("Пароль");
		this.usernameField.setText("jTextField2");
		this.passwordField.setText("jPasswordField1");
		this.hostLabel.setText("Хост");
		this.hostField.setText("jTextField3");
		this.jSeparator1.setMinimumSize(new Dimension(1, 1));
		this.jSeparator2.setMinimumSize(new Dimension(1, 1));
		this.jSeparator3.setMinimumSize(new Dimension(1, 1));
		this.exportFileLabel.setText("Выходной файл");
		this.exportFileField.setText("jTextField1");
		this.browseESFButton.setText("Выбрать");
		this.browseESFButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseESFButton_actionPerformed(e);
				}
			});
		this.importButton.setText("Import");
		this.importButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					importButton_actionPerformed(e);
				}
			});
		this.importButton.setPreferredSize(this.browseESFButton.getPreferredSize());
		this.statusLabel.setSize(new Dimension(0, 17));
		this.statusLabel.setPreferredSize(new Dimension(0, 17));
		this.statusLabel.setMinimumSize(new Dimension(0, 17));
		this.statusLabel.setMaximumSize(new Dimension(0, 17));
		this.statusLabel.setForeground(Color.BLUE);
		this.statusLabel.setText("Ok");

		this.oneRadioButton.setText(UniCableMapDialog.ONE_OBJECT);
		this.oneRadioButton.setActionCommand(UniCableMapDialog.ONE_OBJECT);
		this.tenRadioButton.setText(UniCableMapDialog.TEN_OBJECTS);
		this.tenRadioButton.setActionCommand(UniCableMapDialog.TEN_OBJECTS);
		this.allRadioButton.setText(UniCableMapDialog.ALL_OBJECTS);
		this.allRadioButton.setActionCommand(UniCableMapDialog.ALL_OBJECTS);
		this.countButtonGroup.add(this.oneRadioButton);
		this.countButtonGroup.add(this.tenRadioButton);
		this.countButtonGroup.add(this.allRadioButton);
		this.radioButtonsPanel.setLayout(new FlowLayout());
		this.radioButtonsPanel.add(this.oneRadioButton);
		this.radioButtonsPanel.add(this.tenRadioButton);
		this.radioButtonsPanel.add(this.allRadioButton);
		
//		this.surveyTypes.setPreferredSize(new Dimension(100, 100));
		this.surveyTypes.setCellRenderer(new DefaultListCellRenderer() {
		
			public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof UniCableMapObject) {
					UniCableMapObject ucmObject = (UniCableMapObject)value;
					value = ucmObject.text;
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		
		});
		
		this.surveyTypesScrollPane = new JScrollPane(this.surveyTypes);
		this.surveyTypesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.surveyFileField.setText(".\\out.txt");

		this.surveyButton.setText("Survey");
		this.surveyButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					surveyButton_actionPerformed(e);
				}
			});

		this.mainPanel.setLayout(new GridBagLayout());

		this.connectionButtonsPanel.add(this.connectButton, null);
		this.connectionButtonsPanel.add(this.disconnectButton, null);

		this.connectionPanel.setLayout(new GridBagLayout());
		this.connectionPanel.add(this.databaseLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.connectionPanel.add(this.databaseField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.connectionPanel.add(this.browseGDBButton, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.connectionPanel.add(this.hostLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.connectionPanel.add(this.hostField, ReusedGridBagConstraints.get(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.connectionPanel.add(this.usernameLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.connectionPanel.add(this.usernameField, ReusedGridBagConstraints.get(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.connectionPanel.add(this.passwordLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.connectionPanel.add(this.passwordField, ReusedGridBagConstraints.get(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.connectionPanel.add(this.connectionButtonsPanel, ReusedGridBagConstraints.get(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

		this.importPanel.setLayout(new GridBagLayout());
		this.importPanel.add(this.exportFileLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.importPanel.add(this.exportFileField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.importPanel.add(this.browseESFButton, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.importPanel.add(this.statusLabel, ReusedGridBagConstraints.get(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.importPanel.add(this.importButton, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		this.surveyPanel.setLayout(new GridBagLayout());
		this.surveyPanel.add(this.surveyTypesScrollPane, ReusedGridBagConstraints.get(0, 0, 1, 2, 0.7, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
		this.surveyPanel.add(this.radioButtonsPanel, ReusedGridBagConstraints.get(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.surveyPanel.add(this.surveyFileField, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.surveyPanel.add(this.surveyButton, ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		
		this.mainPanel.add(this.connectionPanel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.mainPanel.add(this.jSeparator1, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		this.mainPanel.add(this.importPanel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.mainPanel.add(this.jSeparator2, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		this.mainPanel.add(this.surveyPanel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
//		this.mainPanel.add(this.jSeparator3, ReusedGridBagConstraints.get(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
//		this.mainPanel.add(this.logTextArea, ReusedGridBagConstraints.get(0, 6, 1, 1, 1.0, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
		
		this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);

		this.usernameField.setText("sysdba");
		this.passwordField.setText("masterkey");
		this.hostField.setText("localhost");
		this.databaseField.setText(input);
		this.exportFileField.setText(output);

		this.connectButton.setEnabled(true);
		this.importButton.setEnabled(false);
		this.surveyButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
	}

	void connectButton_actionPerformed(ActionEvent e)
	{
		try
		{
			this.ucmDatabase = new UniCableMapDatabase(
					this.usernameField.getText(),
					this.passwordField.getText(),
					this.hostField.getText(),
					this.databaseField.getText());
			this.statusLabel.setText("Connected!");
			this.connectButton.setEnabled(false);
			this.importButton.setEnabled(true);
			this.surveyButton.setEnabled(true);
			this.disconnectButton.setEnabled(true);

			Collection<UniCableMapObject> types = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_TYPE));
			this.surveyTypes.setListData(types.toArray(new UniCableMapObject[] {}));
		}
		catch (ImportExportException ex)
		{
			this.statusLabel.setText(ex.getMessage());
			this.connectButton.setEnabled(true);
			this.importButton.setEnabled(false);
			this.surveyButton.setEnabled(false);
			this.disconnectButton.setEnabled(false);
		}
	}

	void disconnectButton_actionPerformed(@SuppressWarnings("unused") ActionEvent e)
	{
		this.surveyTypes.removeAll();
		this.ucmDatabase.close();
		this.statusLabel.setText("Disconnected!");
		this.connectButton.setEnabled(true);
		this.importButton.setEnabled(false);
		this.surveyButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
	}

	void importButton_actionPerformed(ActionEvent e)
	{
		UniCableMapExportCommand command = new UniCableMapExportCommand(
			this.ucmDatabase, 
			this.exportFileField.getText());
//		UCMSchemeExportCommand command = new UCMSchemeExportCommand(this.ucmDatabase);
		command.execute();
		this.statusLabel.setText("OK!");
	}

	protected void surveyButton_actionPerformed(ActionEvent e) {
		File f;
		FileOutputStream fos = null;
		PrintWriter pw;
		try {
			f = new File(this.surveyFileField.getText());
			fos = new FileOutputStream(f);
			pw = new PrintWriter(fos);
		}
		catch (FileNotFoundException ex) {
			pw = new PrintWriter(System.out);
		}
		ButtonModel button = this.countButtonGroup.getSelection();
		int count = 1;
		if(button != null) {
			String selection = button.getActionCommand();
			count = selection.equals(ALL_OBJECTS) ? -1 :
				selection.equals(TEN_OBJECTS) ? 10 : 1;
		}
		UniCableMapObject selectedObject = (UniCableMapObject )this.surveyTypes.getSelectedValue();
		UCMParser.surveyObjects(pw, selectedObject.text, this.ucmDatabase, count);
		this.statusLabel.setText("OK!");
		pw.close();
		try {
			if(fos != null)
				fos.close();
		} catch(IOException e1) {
			e1.printStackTrace();
		}
	}

	void browseGDBButton_actionPerformed(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter =
			new ChoosableFileFilter(
			"gdb",
			"gdb file");
		fileChooser.addChoosableFileFilter(esfFilter);

		fileChooser.setDialogTitle("Выберите файл БД UniCableMap");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			String fileName = fileChooser.getSelectedFile().getPath();
			if (!fileName.endsWith(".gdb"))
				fileName = fileName + ".gdb";
			this.databaseField.setText(fileName);
		}
	}

	void browseESFButton_actionPerformed(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter =
			new ChoosableFileFilter(
			"esf",
			"esf Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter =
			new ChoosableFileFilter(
			"xml",
			"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setDialogTitle("Выберите файл для записи");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			String fileName = fileChooser.getSelectedFile().getPath();
			if (!(fileName.endsWith(".esf") || fileName.endsWith(".xml")))
				fileName = fileName + ".xml";
			this.exportFileField.setText(fileName);
		}
	}

	void thisWindowClosing(WindowEvent e)
	{
		System.exit(0);
	}
}