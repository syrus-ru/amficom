/*-
* $Id: Manager.java,v 1.4 2005/08/17 15:59:40 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.util.Set;

import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.4 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Manager extends AbstractApplication {
	private static final String APPLICATION_NAME = "manager";

	
	public Manager() {
		super(APPLICATION_NAME);
	}
	
	@Override
	protected void init() {
		super.init();
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));				
		super.startMainFrame(new ManagerMainFrame(super.aContext), null);
	}

	public static void main(String[] args) {
		new Manager();
	}
	
	private class ManagerMainFrame extends AbstractMainFrame {
		private JGraphText	text;

		public ManagerMainFrame(final ApplicationContext aContext) {
			super(aContext, "Manager", new AbstractMainMenuBar(aContext.getApplicationModel()) {

				@Override
				protected void addMenuItems() {
					// TODO Auto-generated method stub
					
				}
			}, new AbstractMainToolBar() {});
			
			
			
			try {
				
				
				TypicalCondition tc = new TypicalCondition("sys", OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN);
				Set<StorableObject> storableObjectsByCondition = StorableObjectPool.getStorableObjectsByCondition(tc, true);

				LoginManager.setUserId(storableObjectsByCondition.iterator().next().getId());
				
//				createCharacteristicTypes();
				
				this.text.openFrames(this.desktopPane);

			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		@Override
		protected void initModule() {
			super.initModule();
			
			this.text = new JGraphText(this.aContext);
			
			ApplicationModel applicationModel = this.aContext.getApplicationModel();
			
			applicationModel.setCommand(ManagerModel.DOMAINS_COMMAND, new DomainsPerspective(this.text));
			applicationModel.setCommand(ManagerModel.FLUSH_COMMAND, new FlushCommand());
		}
	}
	
	void createCharacteristicTypes() 
	throws ApplicationException {
		


		
		if (false){
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					LayoutItem.CHARACTERISCTIC_TYPE_X, 
					"x coordinate", 
					"x coordinate", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}

			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					LayoutItem.CHARACTERISCTIC_TYPE_Y, 
					"y coordinate", 
					"y coordinate", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_NATURE, 
					"Тип должностного лица", 
					"Тип", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_FULLNAME, 
					"Полное имя пользотеля", 
					"ФИО", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_POSITION, 
					"Должность", 
					"Должность", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
	
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_DEPARTEMENT, 
					"Подразделение", 
					"Подразделение", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_COMPANY, 
					"Организация", 
					"Организация", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_ROOM_NO, 
					"Номер комнаты", 
					"Номер комнаты", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_CITY, 
					"Город", 
					"Город", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_STREET, 
					"Улица", 
					"Улица", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_BUILDING, 
					"Дом", 
					"Дом", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_EMAIL, 
					"Адрес электронной почты", 
					"Электронная почта", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_PHONE, 
					"Телефон", 
					"Телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_CELLULAR, 
					"Сотовый телефон", 
					"Сотовый телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
		}
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, LoginManager.getUserId(), true);
	}
}
