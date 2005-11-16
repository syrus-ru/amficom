/*
 * $Id: TemplateRendererInnerToolbar.java,v 1.5 2005/11/16 18:49:13 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * <p>Description: Панель инструментов для работы со схемой
 * шаблона и его реализацией</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */
public final class TemplateRendererInnerToolbar extends JToolBar implements PropertyChangeListener{

	protected ReportBuilderApplicationModel	applicationModel;
	protected ApplicationContext aContext;

	protected ActionListener	actionListener;

	protected List				applicationModelListeners;

	protected JToggleButton insertLabelButton = new JToggleButton();
	protected JToggleButton insertImageButton = new JToggleButton();
	protected JButton deleteObjectButton = new JButton();
	protected JButton changeViewButton = new JButton();
	protected JButton saveReportButton = new JButton();
	protected JButton printReportButton = new JButton();
	
	public TemplateRendererInnerToolbar() {		
		this.actionListener = new ActionListener() {

			private boolean	executed	= false;

			public void actionPerformed(ActionEvent e) {
				ApplicationModel model = TemplateRendererInnerToolbar.this.getApplicationModel();
				if (this.executed || model == null)
					return;
				this.executed = true;
				AbstractButton button = (AbstractButton) e.getSource();
				String s = button.getName();
				if (button instanceof JToggleButton)
					s += (((JToggleButton)button).isSelected() ? ReportBuilderApplicationModel.START : ReportBuilderApplicationModel.CANCEL);
				Command command = model.getCommand(s);
				Log.debugMessage("TemplateRendererInnerBar$ActionListener.actionPerformed | command " + (command != null ? '[' + command.getClass().getName() + ']' : "'null'"), Level.FINEST);
				try {
					command.execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					this.executed = false;
				}
			}
		};
		
		this.insertLabelButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_INSERT_LABEL));
		this.insertLabelButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.insertLabel"));
		this.insertLabelButton.setName(ReportBuilderApplicationModel.MENU_INSERT_LABEL);
		this.insertLabelButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		this.insertLabelButton.addActionListener(this.actionListener);
		
		this.insertImageButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_INSERT_IMAGE));
		this.insertImageButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.insertImage"));
		this.insertImageButton.setName(ReportBuilderApplicationModel.MENU_INSERT_IMAGE);
		this.insertImageButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.insertImageButton.addActionListener(this.actionListener);
		
		this.deleteObjectButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_DELETE_OBJECT));
		this.deleteObjectButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.deleteObject"));
		this.deleteObjectButton.setName(ReportBuilderApplicationModel.MENU_DELETE_OBJECT);
		this.deleteObjectButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.deleteObjectButton.addActionListener(this.actionListener);

		this.changeViewButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_VIEW_REPORT));
		this.changeViewButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.changeView"));
		this.changeViewButton.setName(ReportBuilderApplicationModel.MENU_CHANGE_VIEW);
		this.changeViewButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.changeViewButton.addActionListener(this.actionListener);
		
		this.saveReportButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_SAVE_REPORT));
		this.saveReportButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.saveReport"));
		this.saveReportButton.setName(ReportBuilderApplicationModel.MENU_SAVE_REPORT);
		this.saveReportButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.saveReportButton.addActionListener(this.actionListener);

		this.printReportButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_PRINT_REPORT));
		this.printReportButton.setToolTipText(I18N.getString("report.UI.InnerToolbar.printReport"));
		this.printReportButton.setName(ReportBuilderApplicationModel.MENU_PRINT_REPORT);
		this.printReportButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.printReportButton.addActionListener(this.actionListener);
		
		this.add(this.insertLabelButton);
		this.add(this.insertImageButton);
		this.addSeparator();
		this.add(this.deleteObjectButton);
		this.addSeparator();
		this.add(this.changeViewButton);
		this.addSeparator();
		this.add(this.saveReportButton);
		this.add(this.printReportButton);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String e[]) {
				this.modelChanged("");
			}

			public void modelChanged(String elementName) {
				ReportBuilderApplicationModel model = TemplateRendererInnerToolbar.this.getApplicationModel();
				
				TemplateRendererInnerToolbar.this.insertLabelButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_INSERT_LABEL));
				TemplateRendererInnerToolbar.this.insertLabelButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_INSERT_LABEL));
				TemplateRendererInnerToolbar.this.insertImageButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_INSERT_IMAGE));
				TemplateRendererInnerToolbar.this.insertImageButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_INSERT_IMAGE));				
				TemplateRendererInnerToolbar.this.deleteObjectButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_DELETE_OBJECT));
				TemplateRendererInnerToolbar.this.deleteObjectButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT));				
//				TemplateRendererInnerToolbar.this.changeViewButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_CHANGE_VIEW));
//				TemplateRendererInnerToolbar.this.changeViewButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_CHANGE_VIEW));				
				TemplateRendererInnerToolbar.this.saveReportButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_SAVE_REPORT));
				TemplateRendererInnerToolbar.this.saveReportButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_SAVE_REPORT));				
				TemplateRendererInnerToolbar.this.printReportButton.setVisible(model.isVisible(ReportBuilderApplicationModel.MENU_PRINT_REPORT));
				TemplateRendererInnerToolbar.this.printReportButton.setEnabled(model.isEnabled(ReportBuilderApplicationModel.MENU_PRINT_REPORT));				
			}
		});
	}

	public void setApplicationModel(ApplicationModel applicationModel) {
		if (!(applicationModel instanceof ReportBuilderApplicationModel))
			Log.errorMessage("TemplateRendererInnerToolbar.setApplicationModel | wrong model is suuggested!");
		
		this.applicationModel = (ReportBuilderApplicationModel)applicationModel;
		if (	this.applicationModelListeners != null
			&&	!this.applicationModelListeners.isEmpty()) {
			for (Iterator iterator = this.applicationModelListeners.iterator(); 
					iterator.hasNext();) {
				ApplicationModelListener listener = 
					(ApplicationModelListener) iterator.next();
				//Иначе, если выставить одну и ту же модель для тулбара дважды,
				//дважды выставятся и лисенеры. 
				this.applicationModel.removeListener(listener);				
				this.applicationModel.addListener(listener);
			}
		}
		
	}

	public ReportBuilderApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	protected void addApplicationModelListener(ApplicationModelListener listener) {
		if (this.applicationModelListeners == null) {
			this.applicationModelListeners = new LinkedList();
		}
		if (!this.applicationModelListeners.contains(listener)) {
			this.applicationModelListeners.add(listener);
		}
	}

	protected void removeApplicationModelListener(ApplicationModelListener listener) {
		if (this.applicationModelListeners != null) {
			this.applicationModelListeners.remove(listener);
		}
	}

	public List getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ReportEvent.TYPE, this);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(ReportEvent.TYPE, this);
		}
	}
	
	public void propertyChange(PropertyChangeEvent pce) {
		if (!(pce instanceof ReportFlagEvent))
			return;
		
		String eventType = ((ReportFlagEvent)pce).getEventType();
		if (eventType.equals(ReportFlagEvent.SPECIAL_MODE_CANCELED)) {
			this.insertImageButton.setSelected(false);
			this.insertLabelButton.setSelected(false);			
		}
	}
	
	public JButton getChangeViewButton() {
		return this.changeViewButton;
	}
}

//public void changeViewButton_actionPerformed()
//{
//	List objectRenderers = mainWindow.layoutWOCPanel.reportTemplate.
//									 objectRenderers;
//	List labels = mainWindow.layoutWOCPanel.reportTemplate.labels;
//
//	if (mainWindow.isTemplateSchemeMode)
//	{
//		try
//		{
//			if (!mainWindow.layoutWOCPanel.checkTemplatesScheme())
//			{
//				JOptionPane.showMessageDialog(
//					Environment.getActiveWindow(),
//					I18N.getString("label_crossesExist"),
//					I18N.getString("label_error"),
//					JOptionPane.ERROR_MESSAGE);
//
//				return;
//			}
//
//			mainWindow.layoutWCPanel = new ReportTemplateImplementationPanel(
//				mainWindow.aContext,
//				mainWindow.layoutWOCPanel.reportTemplate,
//				false,
//				mainWindow.layoutWOCPanel.imagableRect);
//
//			changeViewButton.setToolTipText(
//				I18N.getString("label_viewTemplatesScheme"));
//			mainWindow.layoutScrollPane.setTitle(I18N.getString(
//				"label_reportForTemplate"));
//			changeViewButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
//				getImage(
//				"images/open_templ_scheme.gif").getScaledInstance(16, 16,
//				Image.SCALE_SMOOTH)));
//
//			mainWindow.layoutScrollPane.getContentPane().removeAll();
//
//			mainWindow.layoutScrollPane.getContentPane().add(mainWindow.
//				innerToolBar,
//				BorderLayout.NORTH);
//			mainWindow.layoutScrollPane.getContentPane().add(new JScrollPane(
//				mainWindow.layoutWCPanel), BorderLayout.CENTER);
//
//			// В конструкторе была инициализирована ширина таблицы чтобы вместить
//			// все столбцы, ширины которых здесь выставляются
//			for (ListIterator lIt = objectRenderers.listIterator(); lIt.hasNext();)
//				((RenderingObject) lIt.next()).setColumnWidths();
//
//			// у надписей меняются owner теперь они на новой панели
//			for (ListIterator lIt = labels.listIterator(); lIt.hasNext();)
//				mainWindow.layoutWOCPanel.remove((FirmedTextPane) lIt.next());
//
//			setEditToolBarState(false);
//			saveReportButton.setEnabled(true);
//			printReportButton.setEnabled(true);
//			changeViewButton.setEnabled(true);
//			mainWindow.selectReportsPanel.setEnabled(false);
//
//			Dispatcher theDisp = mainWindow.aContext.getDispatcher();
//			if (mainWindow.additionalPanel != null)
//			{
//				if (mainWindow.additionalPanel instanceof SetRestrictionsWindow)
//				{
//					SetRestrictionsWindow addPanel =
//						(SetRestrictionsWindow) mainWindow.additionalPanel;
//					theDisp.notify(
//						new OperationEvent(addPanel.orfp.getFilter(), 0,
//												 ObjectResourceFilterPane.
//												 state_filterClosed));
//					addPanel.closeSchemeWindow();
//				}
//				else
//					theDisp.notify(
//						new OperationEvent("", 0,
//												 SelectReportsPanel.ev_closingAdditionalPanel));
//
//				mainWindow.additionalPanel.dispose();
//			}
//
//			mainWindow.isTemplateSchemeMode = false;
//		}
//		catch (CreateReportException cre)
//		{
//			JOptionPane.showMessageDialog(
//				Environment.getActiveWindow(),
//				cre.getMessage(),
//				I18N.getString("label_error"),
//				JOptionPane.ERROR_MESSAGE);
//
//			mainWindow.layoutWOCPanel.repaint();
//			return;
//		}
//	}
//	else
//	{
//		// Сохраняем информацию о колонках таблиц
//  for (ListIterator lIt = objectRenderers.listIterator(); lIt.hasNext();)
//    ((RenderingObject) lIt.next()).saveColumnWidths();
//  
//		changeViewButton.setToolTipText(I18N.getString(
//			"label_viewReport"));
//		changeViewButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
//															getImage(
//			"images/view_report.gif").
//															getScaledInstance(16, 16,
//			Image.SCALE_SMOOTH)));
//
//		mainWindow.layoutScrollPane.setTitle(I18N.getString(
//			"label_templateScheme"));
//		mainWindow.layoutScrollPane.getContentPane().removeAll();
//		mainWindow.layoutScrollPane.getContentPane().add(mainWindow.
//			innerToolBar,
//			BorderLayout.NORTH);
//		mainWindow.layoutScrollPane.getContentPane().add(new JScrollPane(
//			mainWindow.layoutWOCPanel), BorderLayout.CENTER);
//		// Преобразовываем графические отображения объектов к элементам схемы
//		mainWindow.layoutWOCPanel.setSchemeObjectsNewParameters();
//
//		mainWindow.layoutWCPanel = null;
//		// Это требуется в FirmedTextPane для правильного нахождения координат
//		// привязанных надписей
//  
//  for (ListIterator lIt = objectRenderers.listIterator(); lIt.hasNext();)
//    ((RenderingObject) lIt.next()).rendererPanel = null;
//  
//		setEditToolBarState(true);
//		saveReportButton.setEnabled(false);
//		printReportButton.setEnabled(false);
//		mainWindow.selectReportsPanel.setEnabled(true);
//
//		mainWindow.isTemplateSchemeMode = true;
//	}
//}
