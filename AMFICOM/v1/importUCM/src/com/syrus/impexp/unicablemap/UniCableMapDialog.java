package com.syrus.impexp.unicablemap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client_.general.ui_.ChoosableFileFilter;
import com.syrus.impexp.ImportExportException;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/05/25 16:06:51 $
 * @module mapviewclient_v1
 */
public class UniCableMapDialog extends JDialog 
{
	private JTextArea logTextArea = new JTextArea();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JButton connectButton = new JButton();
	private JButton disconnectButton = new JButton();
	
	UniCableMapDatabase ucmDatabase = null;
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel jLabel1 = new JLabel();
	private JTextField databaseField = new JTextField();
	private JButton browseGDBButton = new JButton();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel jLabel4 = new JLabel();
	private JTextField hostField = new JTextField();
	private JSeparator jSeparator1 = new JSeparator();
	private JLabel jLabel5 = new JLabel();
	private JTextField exportFileField = new JTextField();
	private JButton browseESFButton = new JButton();
	private JButton importButton = new JButton();
	private JLabel statusLabel = new JLabel();

	public UniCableMapDialog()
	{
		this(null, "", false);
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param modal
	 */
	public UniCableMapDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
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
		Dimension size = new Dimension(600, 330);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(size);
		this.setLocation(
			(screenSize.width - size.width) / 2,
			(screenSize.height - size.height) / 2);

		this.getContentPane().setLayout(this.borderLayout1);
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
		this.jPanel2.setLayout(this.gridBagLayout1);
		this.jLabel1.setText("База данных");
		this.databaseField.setText("jTextField1");
		this.browseGDBButton.setText("Выбрать");
		this.browseGDBButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseGDBButton_actionPerformed(e);
				}
			});
		this.jLabel2.setText("Пользователь");
		this.jLabel3.setText("Пароль");
		this.usernameField.setText("jTextField2");
		this.passwordField.setText("jPasswordField1");
		this.jLabel4.setText("Хост");
		this.hostField.setText("jTextField3");
		this.jSeparator1.setMinimumSize(new Dimension(1, 1));
		this.jLabel5.setText("Выходной файл");
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
		this.statusLabel.setSize(new Dimension(0, 17));
		this.statusLabel.setPreferredSize(new Dimension(0, 17));
		this.statusLabel.setMinimumSize(new Dimension(0, 17));
		this.statusLabel.setMaximumSize(new Dimension(0, 17));
		this.statusLabel.setForeground(Color.BLUE);
		this.statusLabel.setText("Ok");
		this.getContentPane().add(this.logTextArea, BorderLayout.CENTER);
		this.jPanel1.add(this.connectButton, null);
		this.jPanel1.add(this.importButton, null);
		this.jPanel1.add(this.disconnectButton, null);
		this.getContentPane().add(this.jPanel1, BorderLayout.SOUTH);
		this.jPanel2.add(this.jLabel1, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel2.add(this.databaseField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.browseGDBButton, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.jLabel2, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel2.add(this.jLabel3, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel2.add(this.usernameField, ReusedGridBagConstraints.get(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.passwordField, ReusedGridBagConstraints.get(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.jLabel4, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel2.add(this.hostField, ReusedGridBagConstraints.get(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.jSeparator1, ReusedGridBagConstraints.get(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		this.jPanel2.add(this.jLabel5, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel2.add(this.exportFileField, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.browseESFButton, ReusedGridBagConstraints.get(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel2.add(this.statusLabel, ReusedGridBagConstraints.get(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.getContentPane().add(this.jPanel2, BorderLayout.NORTH);

		this.usernameField.setText("sysdba");
		this.passwordField.setText("masterkey");
		this.hostField.setText("localhost");
		this.databaseField.setText("d:/My Documents/ISM/doc/Resident/tel.gdb");
		this.exportFileField.setText("d:/My Documents/ISM/prog/java/AMFICOm/run/Data/ucm/testucm.xml");

		this.importButton.setEnabled(false);
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
			this.importButton.setEnabled(true);
			this.disconnectButton.setEnabled(true);
		}
		catch (ImportExportException ex)
		{
			this.statusLabel.setText(ex.getMessage());
			this.importButton.setEnabled(false);
			this.disconnectButton.setEnabled(false);
		}
	}

	void disconnectButton_actionPerformed(ActionEvent e)
	{
		this.ucmDatabase.close();
		this.statusLabel.setText("Disconnected!");
		this.importButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
	}

	void importButton_actionPerformed(ActionEvent e)
	{
		UniCableMapExportCommand command = new UniCableMapExportCommand(
			this.ucmDatabase, 
			this.exportFileField.getText());
		command.execute();
		this.statusLabel.setText("OK!");
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