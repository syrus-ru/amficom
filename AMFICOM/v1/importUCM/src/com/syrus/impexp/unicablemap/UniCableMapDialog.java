package com.syrus.impexp.unicablemap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client_.general.ui_.ChoosableFileFilter;
import com.syrus.impexp.ImportExportException;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Color;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/03/30 07:51:48 $
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

		this.getContentPane().setLayout(borderLayout1);
		this.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					thisWindowClosing(e);
				}
			});
		connectButton.setText("Connect!");
		connectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					connectButton_actionPerformed(e);
				}
			});
		disconnectButton.setText("Disconnect");
		disconnectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					disconnectButton_actionPerformed(e);
				}
			});
		jPanel2.setLayout(gridBagLayout1);
		jLabel1.setText("База данных");
		databaseField.setText("jTextField1");
		browseGDBButton.setText("Выбрать");
		browseGDBButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseGDBButton_actionPerformed(e);
				}
			});
		jLabel2.setText("Пользователь");
		jLabel3.setText("Пароль");
		usernameField.setText("jTextField2");
		passwordField.setText("jPasswordField1");
		jLabel4.setText("Хост");
		hostField.setText("jTextField3");
		jSeparator1.setMinimumSize(new Dimension(1, 1));
		jLabel5.setText("Выходной файл");
		exportFileField.setText("jTextField1");
		browseESFButton.setText("Выбрать");
		browseESFButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					browseESFButton_actionPerformed(e);
				}
			});
		importButton.setText("Import");
		importButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					importButton_actionPerformed(e);
				}
			});
		statusLabel.setSize(new Dimension(0, 17));
		statusLabel.setPreferredSize(new Dimension(0, 17));
		statusLabel.setMinimumSize(new Dimension(0, 17));
		statusLabel.setMaximumSize(new Dimension(0, 17));
		statusLabel.setForeground(Color.BLUE);
		statusLabel.setText("Ok");
		this.getContentPane().add(logTextArea, BorderLayout.CENTER);
		jPanel1.add(connectButton, null);
		jPanel1.add(importButton, null);
		jPanel1.add(disconnectButton, null);
		this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
		jPanel2.add(jLabel1, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		jPanel2.add(databaseField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(browseGDBButton, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(jLabel2, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		jPanel2.add(jLabel3, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		jPanel2.add(usernameField, ReusedGridBagConstraints.get(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(passwordField, ReusedGridBagConstraints.get(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(jLabel4, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		jPanel2.add(hostField, ReusedGridBagConstraints.get(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(jSeparator1, ReusedGridBagConstraints.get(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		jPanel2.add(jLabel5, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		jPanel2.add(exportFileField, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(browseESFButton, ReusedGridBagConstraints.get(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(statusLabel, ReusedGridBagConstraints.get(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		this.getContentPane().add(jPanel2, BorderLayout.NORTH);

		usernameField.setText("sysdba");
		passwordField.setText("masterkey");
		hostField.setText("localhost");
		databaseField.setText("d:/My Documents/ISM/doc/Resident/tel.gdb");
		exportFileField.setText("d:/My Documents/ISM/prog/java/AMFICOm/run/Data/ucm/testucm.esf");
	}

	private void connectButton_actionPerformed(ActionEvent e)
	{
		try
		{
			this.ucmDatabase = new UniCableMapDatabase(
				usernameField.getText(),
				passwordField.getText(),
				hostField.getText(),
				databaseField.getText());
		}
		catch (ImportExportException ex)
		{
			statusLabel.setText(ex.getMessage());
		}
		statusLabel.setText("Connected!");
	}

	private void disconnectButton_actionPerformed(ActionEvent e)
	{
		this.ucmDatabase.close();
		statusLabel.setText("Disconnected!");
	}

	private void importButton_actionPerformed(ActionEvent e)
	{
		UniCableMapExportCommand command = new UniCableMapExportCommand(
			ucmDatabase, 
			exportFileField.getText());
		command.execute();
		statusLabel.setText("OK!");
	}

	private void browseGDBButton_actionPerformed(ActionEvent e)
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
			databaseField.setText(fileName);
		}
	}

	private void browseESFButton_actionPerformed(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter =
			new ChoosableFileFilter(
			"esf",
			"esf Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

		fileChooser.setDialogTitle("Выберите файл для записи");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			String fileName = fileChooser.getSelectedFile().getPath();
			if (!fileName.endsWith(".gdb"))
				fileName = fileName + ".gdb";
			databaseField.setText(fileName);
		}
	}

	private void thisWindowClosing(WindowEvent e)
	{
		System.exit(0);
	}
}