//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Диалоговое окно изменения параметров соединения клиентского* //
// *         ПО с сервером (CORBA-объектом) АМФИКОМ - включает в себя     * //
// *         задание IP-адреса сервера, имя серверного объекта, имя       * //
// *         доступа и пароль доступа к объекту                           * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Main\ConnectionDialog.java                             * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.RISDConnectionInfo.*;
import java.awt.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 * @module General
 * 
 * @todo Also show session timeout.
 */
public final class ConnectionDialog extends JDialog {
	private ApplicationContext aContext;

	public int retCode = 0;
	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;
	
	public ConnectionDialog(Frame parent, String title, boolean modal) {
		super(parent, title, modal);
		initComponents();
		
		/*
		 * Some tricky layout code.
		 */
		int h1 = jServerAddressTextField1.getPreferredSize().height;
		int h2 = jResetToDefaultsButton1.getPreferredSize().height;
		Dimension d = new Dimension(230, ((h1 > h2) ? h1 : h2));
//		jServerAddressTextField1.setPreferredSize(d);
//		jServerPortTextField2.setPreferredSize(d);
//		jSIDTextField3.setPreferredSize(d);
//		jCtxTextField4.setPreferredSize(d);
//		jServerObjectTextField5.setPreferredSize(d);
//		jServiceURLTextField6.setPreferredSize(d);
//		jFullURLTextField7.setPreferredSize(d);
//		jUserNameTextField8.setPreferredSize(d);
//		jPasswordTextField9.setPreferredSize(d);
		jResetToDefaultsButton1.setPreferredSize(d);
		pack();
	}

	public ConnectionDialog(ApplicationContext aContext) {
		this(Environment.getActiveWindow(), LangModel.String("ConnectionTitle"), true);
		this.aContext = aContext;
		setFields();
	}
	
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jServerAddressLabel1 = new JLabel();
		jServerAddressTextField1 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldIP = jServerAddressTextField1;
		jServerPortLabel2 = new JLabel();
		jServerPortTextField2 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldTCP = jServerPortTextField2;
		jSIDLabel3 = new JLabel();
		jSIDTextField3 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldSID = jSIDTextField3;
		jCtxLabel4 = new JLabel();
		jCtxTextField4 = new JTextField();
		jServerObjectLabel5 = new JLabel();
		jServerObjectTextField5 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldObject = jServerObjectTextField5;
		jServiceURLLabel6 = new JLabel();
		jServiceURLTextField6 = new JTextField();
		jFullURLLabel7 = new JLabel();
		jFullURLTextField7 = new JTextField();
		jUserNameLabel8 = new JLabel();
		jUserNameTextField8 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldUser = jUserNameTextField8;
		jPasswordLabel9 = new JLabel();
		jPasswordTextField9 = new JTextField();
		/*
		 * For backward compatibility.
		 */
		fieldPassword = jPasswordTextField9;
		jConnectionStatusLabel10 = new JLabel();
		jResetToDefaultsButton1 = new JButton();
		jВсемСепараторамСепараторSeparator1 = new JSeparator();
		jPanel1 = new JPanel();
		jTestConnectionButton2 = new JButton();
		/*
		 * For backward compatibility.
		 */
		buttonCheck = jTestConnectionButton2;
		jOKButton3 = new JButton();
		/*
		 * For backward compatibility.
		 */
		buttonOk = jOKButton3;
		jCancelButton4 = new JButton();
		jHelpButton5 = new JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setModal(true);
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				closeDialog(e);
			}
		});
		
		jServerAddressLabel1.setLabelFor(jServerAddressTextField1);
		jServerAddressLabel1.setText(LangModel.String("labelServerIP"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
		getContentPane().add(jServerAddressLabel1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 10);
		getContentPane().add(jServerAddressTextField1, gridBagConstraints);
		
		jServerPortLabel2.setLabelFor(jServerPortTextField2);
		jServerPortLabel2.setText(LangModel.String("labelServerTCP"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jServerPortLabel2, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jServerPortTextField2, gridBagConstraints);
		
		jSIDLabel3.setLabelFor(jSIDTextField3);
		jSIDLabel3.setText(LangModel.String("labelSID"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jSIDLabel3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jSIDTextField3, gridBagConstraints);
		
		jCtxLabel4.setLabelFor(jCtxTextField4);
		jCtxLabel4.setText(LangModel.String("labelCtx"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jCtxLabel4, gridBagConstraints);
		
		jCtxTextField4.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jCtxTextField4, gridBagConstraints);
		
		jServerObjectLabel5.setLabelFor(jServerObjectTextField5);
		jServerObjectLabel5.setText(LangModel.String("labelServerObject"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jServerObjectLabel5, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jServerObjectTextField5, gridBagConstraints);
		
		jServiceURLLabel6.setLabelFor(jServiceURLTextField6);
		jServiceURLLabel6.setText(LangModel.String("labelServiceURL"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jServiceURLLabel6, gridBagConstraints);
		
		jServiceURLTextField6.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jServiceURLTextField6, gridBagConstraints);
		
		jFullURLLabel7.setLabelFor(jFullURLTextField7);
		jFullURLLabel7.setText(LangModel.String("labelFullURL"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jFullURLLabel7, gridBagConstraints);
		
		jFullURLTextField7.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jFullURLTextField7, gridBagConstraints);
		
		jUserNameLabel8.setLabelFor(jUserNameTextField8);
		jUserNameLabel8.setText(LangModel.String("labelObjectName"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jUserNameLabel8, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jUserNameTextField8, gridBagConstraints);
		
		jPasswordLabel9.setLabelFor(jPasswordTextField9);
		jPasswordLabel9.setText(LangModel.String("labelObjectPassword"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jPasswordLabel9, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jPasswordTextField9, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 5);
		getContentPane().add(jConnectionStatusLabel10, gridBagConstraints);
		
		jResetToDefaultsButton1.setText(LangModel.String("buttonStandard"));
		jResetToDefaultsButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jResetToDefaultsButton1ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		getContentPane().add(jResetToDefaultsButton1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
		getContentPane().add(jВсемСепараторамСепараторSeparator1, gridBagConstraints);
		
		jPanel1.setLayout(new java.awt.GridLayout(1, 4, 5, 0));
		
		jTestConnectionButton2.setText(LangModel.String("buttonCheck"));
		jTestConnectionButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jTestConnectionButton2ActionPerformed(e);
			}
		});
		
		jPanel1.add(jTestConnectionButton2);
		
		jOKButton3.setText(LangModel.String("buttonAccept"));
		jOKButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jOKButton3ActionPerformed(e);
			}
		});
		
		jPanel1.add(jOKButton3);
		
		jCancelButton4.setText(LangModel.String("buttonCancel"));
		jCancelButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jCancelButton4ActionPerformed(e);
			}
		});
		
		jPanel1.add(jCancelButton4);
		
		jHelpButton5.setText(LangModel.String("buttonHelp"));
		jHelpButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jHelpButton5ActionPerformed(e);
			}
		});
		
		jPanel1.add(jHelpButton5);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
		getContentPane().add(jPanel1, gridBagConstraints);
		
		pack();
	}//GEN-END:initComponents

	private void jOKButton3ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jOKButton3ActionPerformed
		retCode = RET_OK;
		closeDialog(null);
	}//GEN-LAST:event_jOKButton3ActionPerformed

	private void jCancelButton4ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jCancelButton4ActionPerformed
		retCode = RET_CANCEL;
		closeDialog(null);
	}//GEN-LAST:event_jCancelButton4ActionPerformed

	private void jHelpButton5ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jHelpButton5ActionPerformed
		JOptionPane.showMessageDialog(this, "No help currently available", "Connection Dialog Help", JOptionPane.INFORMATION_MESSAGE);
	}//GEN-LAST:event_jHelpButton5ActionPerformed

	private void jTestConnectionButton2ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jTestConnectionButton2ActionPerformed
		ConnectionInterface ci = aContext.getConnectionInterface();
		ci.setServerIP(jServerAddressTextField1.getText());
		ci.setTCPport(jServerPortTextField2.getText());
		ci.setSID(jSIDTextField3.getText());
		ci.setObjectName(jServerObjectTextField5.getText());
		ci.setUser(jUserNameTextField8.getText());
		ci.setPassword(jPasswordTextField9.getText());
		
		String serviceURL = ci.getServiceURL();
		String sessionId;
		String objectName = ci.getObjectName();

		new CheckConnectionCommand(aContext.getDispatcher(), aContext).execute();
		if (ci.isConnected()) {
			jConnectionStatusLabel10.setForeground(Color.BLUE);
			jConnectionStatusLabel10.setText(LangModel.String("connectionSuccess"));

			try {
				sessionId = ((RISDConnectionInfo) ci).getSubcontextName();
			} catch (ClassCastException cce) {
				sessionId = RISDConnectionInfo.DEFAULT_SUBCONTEXT_NAME;
			}

			ci.Disconnect();
		} else {
			jConnectionStatusLabel10.setForeground(Color.RED);
			jConnectionStatusLabel10.setText(LangModel.String("connectionFail"));
			
			sessionId = RISDConnectionInfo.DEFAULT_SUBCONTEXT_NAME;
		}

		jCtxTextField4.setText(sessionId);
		jCtxTextField4.setCaretPosition(0);
		jServiceURLTextField6.setText(serviceURL);
		jServiceURLTextField6.setCaretPosition(0);
		jFullURLTextField7.setText(serviceURL + '/' + sessionId + objectName);
		jFullURLTextField7.setCaretPosition(0);
	}//GEN-LAST:event_jTestConnectionButton2ActionPerformed

	private void jResetToDefaultsButton1ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jResetToDefaultsButton1ActionPerformed
		jServerAddressTextField1.setText(ServiceURL.DEFAULT_HOST);
		jServerAddressTextField1.setCaretPosition(0);
		jServerPortTextField2.setText(ServiceURL.DEFAULT_PORT);
		jServerPortTextField2.setCaretPosition(0);
		jSIDTextField3.setText(ServiceURL.DEFAULT_SID);
		jSIDTextField3.setCaretPosition(0);
		jCtxTextField4.setText(RISDConnectionInfo.DEFAULT_SUBCONTEXT_NAME);
		jCtxTextField4.setCaretPosition(0);
		jServerObjectTextField5.setText(RISDConnectionInfo.DEFAULT_objectName);
		jServerObjectTextField5.setCaretPosition(0);
		jServiceURLTextField6.setText(ServiceURL.DEFAULT_SERVICE_URL);
		jServiceURLTextField6.setCaretPosition(0);
		jFullURLTextField7.setText(ServiceURL.DEFAULT_SERVICE_URL + '/' + RISDConnectionInfo.DEFAULT_SUBCONTEXT_NAME + RISDConnectionInfo.DEFAULT_objectName);
		jFullURLTextField7.setCaretPosition(0);
		jUserNameTextField8.setText(RISDConnectionInfo.DEFAULT_user);
		jUserNameTextField8.setCaretPosition(0);
		jPasswordTextField9.setText(RISDConnectionInfo.DEFAULT_password);
		jPasswordTextField9.setCaretPosition(0);
	}//GEN-LAST:event_jResetToDefaultsButton1ActionPerformed

	private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
		setVisible(false);
		dispose();
	}//GEN-LAST:event_closeDialog
	
	public void setFields() {
		ConnectionInterface ci = aContext.getConnectionInterface();

		String serviceURL = ci.getServiceURL();
		String sessionId;
		try {
			sessionId = ((RISDConnectionInfo) ci).getSubcontextName();
		} catch (ClassCastException cce) {
			sessionId = RISDConnectionInfo.DEFAULT_SUBCONTEXT_NAME;
		}
		String objectName = ci.getObjectName();

		jServerAddressTextField1.setText(ci.getServerIP());
		jServerAddressTextField1.setCaretPosition(0);
		jServerPortTextField2.setText(ci.getTCPport());
		jServerPortTextField2.setCaretPosition(0);
		jSIDTextField3.setText(ci.getSID());
		jSIDTextField3.setCaretPosition(0);
		jCtxTextField4.setText(sessionId);
		jCtxTextField4.setCaretPosition(0);
		jServerObjectTextField5.setText(objectName);
		jServerObjectTextField5.setCaretPosition(0);
		jServiceURLTextField6.setText(serviceURL);
		jServiceURLTextField6.setCaretPosition(0);
		jFullURLTextField7.setText(serviceURL + '/' + sessionId + objectName);
		jFullURLTextField7.setCaretPosition(0);
		jUserNameTextField8.setText(ci.getUser());
		jUserNameTextField8.setCaretPosition(0);
		jPasswordTextField9.setText(ci.getPassword());
		jPasswordTextField9.setCaretPosition(0);
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jCancelButton4;
	private javax.swing.JLabel jConnectionStatusLabel10;
	private javax.swing.JLabel jCtxLabel4;
	private javax.swing.JTextField jCtxTextField4;
	private javax.swing.JLabel jFullURLLabel7;
	private javax.swing.JTextField jFullURLTextField7;
	private javax.swing.JButton jHelpButton5;
	private javax.swing.JButton jOKButton3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JLabel jPasswordLabel9;
	private javax.swing.JTextField jPasswordTextField9;
	private javax.swing.JButton jResetToDefaultsButton1;
	private javax.swing.JLabel jSIDLabel3;
	private javax.swing.JTextField jSIDTextField3;
	private javax.swing.JLabel jServerAddressLabel1;
	private javax.swing.JTextField jServerAddressTextField1;
	private javax.swing.JLabel jServerObjectLabel5;
	private javax.swing.JTextField jServerObjectTextField5;
	private javax.swing.JLabel jServerPortLabel2;
	private javax.swing.JTextField jServerPortTextField2;
	private javax.swing.JLabel jServiceURLLabel6;
	private javax.swing.JTextField jServiceURLTextField6;
	private javax.swing.JButton jTestConnectionButton2;
	private javax.swing.JLabel jUserNameLabel8;
	private javax.swing.JTextField jUserNameTextField8;
	private javax.swing.JSeparator jВсемСепараторамСепараторSeparator1;
	// End of variables declaration//GEN-END:variables

	/*
	 * For backward compatibility.
	 */
	public JButton buttonOk;
	public JButton buttonCheck;
	public JTextField fieldIP;
	public JTextField fieldTCP;
	public JTextField fieldSID;
	public JTextField fieldObject;
	public JTextField fieldUser;
	public JTextField fieldPassword;
}
