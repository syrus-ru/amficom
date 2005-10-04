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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.impexp.ChoosableFileFilter;
import com.syrus.impexp.ImportExportException;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/10/04 17:06:47 $
 * @module mapviewclient_v1
 */
public class UniCableMapDialog extends JFrame 
{
	private static final String ALL_OBJECTS = "все";

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
	private JButton importMapButton = new JButton();
	private JButton importMapLibraryButton = new JButton();
	private JButton importSchemeButton = new JButton();
	JPanel buttonsPanel = new JPanel();

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
	
	private JTextField surveyUnField = new JTextField();
	private JButton surveyUnButton = new JButton();

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
		Properties properties = new Properties();
		properties.load(new FileInputStream("impexp.properties"));
		
		String input = properties.getProperty("base");
		String output = properties.getProperty("output");
		
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
		this.connectButton.setText("Подключение");
		this.connectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					connect();
				}
			});
		this.disconnectButton.setText("Отключение");
		this.disconnectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					disconnect();
				}
			});
		this.databaseLabel.setText("База данных");
		this.databaseField.setText("jTextField1");
		this.browseGDBButton.setText("Выбрать");
		this.browseGDBButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseGDB();
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
					browseESF();
				}
			});
		this.importMapButton.setText("Карта");
		this.importMapButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					importMap();
				}
			});
		this.importMapButton.setPreferredSize(this.browseESFButton.getPreferredSize());

		this.importMapLibraryButton.setText("Типы");
		this.importMapLibraryButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					importMapLibrary();
				}
			});
		this.importMapLibraryButton.setPreferredSize(this.browseESFButton.getPreferredSize());

		this.importSchemeButton.setText("Схема");
		this.importSchemeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					importScheme();
				}
			});
		this.importSchemeButton.setPreferredSize(this.browseESFButton.getPreferredSize());

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
		this.surveyUnField.setText("");

		this.surveyButton.setText("Обзор");
		this.surveyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				survey();
			}
		});

		this.surveyUnButton.setText("Обзор Un");
		this.surveyUnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surveyUn();
			}
		});

		this.mainPanel.setLayout(new GridBagLayout());

		this.connectionButtonsPanel.add(this.connectButton, null);
		this.connectionButtonsPanel.add(this.disconnectButton, null);

		GridBagConstraints constraints = new GridBagConstraints();
		
		this.connectionPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.databaseLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.databaseField, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.browseGDBButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.hostLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.hostField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.usernameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.usernameField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.passwordLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.passwordField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.connectionPanel.add(this.connectionButtonsPanel, constraints);

		this.buttonsPanel.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.3;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.importMapButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.3;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.importMapLibraryButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.3;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.importSchemeButton, constraints);

		this.importPanel.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.importPanel.add(this.exportFileLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.importPanel.add(this.exportFileField, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.importPanel.add(this.browseESFButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.importPanel.add(this.buttonsPanel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.importPanel.add(this.statusLabel, constraints);

		this.surveyPanel.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 4;
		constraints.weightx = 0.7;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.surveyTypesScrollPane, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.radioButtonsPanel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.3;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.surveyFileField, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.surveyButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.3;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.surveyUnField, constraints);

		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.surveyPanel.add(this.surveyUnButton, constraints);
		

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.mainPanel.add(this.connectionPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.mainPanel.add(this.jSeparator1, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.mainPanel.add(this.importPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.mainPanel.add(this.jSeparator2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.7;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.mainPanel.add(this.surveyPanel, constraints);
//		this.mainPanel.add(this.jSeparator3, ReusedGridBagConstraints.get(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
//		this.mainPanel.add(this.logTextArea, ReusedGridBagConstraints.get(0, 6, 1, 1, 1.0, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
		
		this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);

		this.usernameField.setText("sysdba");
		this.passwordField.setText("masterkey");
		this.hostField.setText("localhost");
		this.databaseField.setText(input);
		this.exportFileField.setText(output);

		this.connectButton.setEnabled(true);
		this.importMapButton.setEnabled(false);
		this.importMapLibraryButton.setEnabled(false);
		this.importSchemeButton.setEnabled(false);
		this.surveyButton.setEnabled(false);
		this.surveyUnButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
	}

	void connect()
	{
		try
		{
			this.ucmDatabase = new UniCableMapDatabase(
					this.usernameField.getText(),
					this.passwordField.getText(),
					this.hostField.getText(),
					this.databaseField.getText());
			this.statusLabel.setText("Есть соединение!");
			this.connectButton.setEnabled(false);
			this.importMapButton.setEnabled(true);
			this.importMapLibraryButton.setEnabled(true);
			this.importSchemeButton.setEnabled(true);
			this.surveyButton.setEnabled(true);
			this.surveyUnButton.setEnabled(true);
			this.disconnectButton.setEnabled(true);

			Collection<UniCableMapObject> types = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_TYPE));
			this.surveyTypes.setListData(types.toArray(new UniCableMapObject[] {}));
		}
		catch (ImportExportException ex)
		{
			this.statusLabel.setText(ex.getMessage());
			this.connectButton.setEnabled(true);
			this.importMapButton.setEnabled(false);
			this.importMapLibraryButton.setEnabled(false);
			this.surveyButton.setEnabled(false);
			this.surveyUnButton.setEnabled(false);
			this.disconnectButton.setEnabled(false);
		}
	}

	void disconnect()
	{
		this.surveyTypes.removeAll();
		this.ucmDatabase.close();
		this.statusLabel.setText("Соединение закрыто.");
		this.connectButton.setEnabled(true);
		this.importMapButton.setEnabled(false);
		this.importMapLibraryButton.setEnabled(false);
		this.importSchemeButton.setEnabled(false);
		this.surveyButton.setEnabled(false);
		this.surveyUnButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
	}

	void importMap()
	{
		UniCableMapExportCommand command = new UniCableMapExportCommand(
			this.ucmDatabase, 
			this.exportFileField.getText());
		command.execute();
		this.statusLabel.setText("OK!");
	}

	void importMapLibrary()
	{
		UniCableMapLibraryExportCommand command = new UniCableMapLibraryExportCommand(
			this.ucmDatabase, 
			this.exportFileField.getText());
		command.execute();
		this.statusLabel.setText("OK!");
	}

	void importScheme()
	{
		UCMSchemeExportCommand command = new UCMSchemeExportCommand(this.ucmDatabase, this.exportFileField.getText());
		command.execute();
		this.statusLabel.setText("OK!");
	}

	protected void survey() {
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
		pw.flush();
		pw.close();
		this.statusLabel.setText("OK!");
		try {
			if(fos != null)
				fos.close();
		} catch(IOException e1) {
			e1.printStackTrace();
		}
	}

	protected void surveyUn() {
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
		String unString = this.surveyUnField.getText();
		int un = Integer.parseInt(unString);
		try {
			UCMParser.surveyObject(pw, this.ucmDatabase, this.ucmDatabase.getObject(un));
			pw.flush();
			pw.close();
			this.statusLabel.setText("OK!");
			try {
				if(fos != null)
					fos.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	void browseGDB()
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

	void browseESF()
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