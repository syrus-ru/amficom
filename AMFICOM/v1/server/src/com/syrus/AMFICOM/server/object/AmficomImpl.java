/*
 * $Id: AmficomImpl.java,v 1.1.2.5 2004/12/23 12:06:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.Report.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.server.ResourcedbInterface;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.Connection;
import java.util.*;
import javax.sql.DataSource;
import org.omg.CORBA.*;

/**
 * @version $Revision: 1.1.2.5 $, $Date: 2004/12/23 12:06:01 $
 * @author $Author: bass $
 * @module server_v1
 */
public final class AmficomImpl implements AMFICOMOperations {
	static final DataSource DATA_SOURCE = JdbcConnectionManager.getDataSource();

	/**
	 * Открыть новую сессию взаимодействия оператора ИСМ с РИСД и провести
	 * процедуры идентификации и аутентификации пользователя по имени
	 * пользователя и паролю. В соответствии с указанием цели purpose соединения
	 * пользователя с РИСД на взаимодействие накладываются ограничения.
	 * все последующее взаимодействие осуществляется н основе назначаемого
	 * на данном этапе идентификатора новой сессии. Для поддержания сессии
	 * необходимо осуществлять вызов какой-либо функции объекта РИСД не реже,
	 * чем раз в период времени, устанавливаемый администратором, в противном
	 * случае сессия закрывается.
	 *
	 * Ограничения:
	 *	purpose = 0 - соединение для администрирования
	 *	purpose = 1 - соединение для проведения контроля
	 *
	 * Возвращаемое значение - результат работы функции:
	 * 	ERROR_NO_ERROR
	 *	ERROR_NO_CONNECT
	 *	ERROR_WRONG_LOGIN
	 *	ERROR_WRONG_PASSWORD
	 *	ERROR_CANNOT_CREATE_SESSION
	 *	ERROR_RISD_ERROR
	 *
	 *	sessid - иденлификатор сессии для работы пользователя с ИСМ
	 *
	 * Сделать:
	 *	использование purpose,
	 *	шифрование username и password
	 *	счетчик количества сессий одного пользователя
	 *	счетчик общего количества сессий
	 *	проверка времени действия лицензии и демо-версии
	 * 
	 * @param username
	 * @param password
	 * @param ior
	 * @param accessIdentity
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int Logon(String username, byte password[], String ior, AccessIdentity_TransferableHolder accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.login(conn, username, password, ior, accessIdentity);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * Возвращаемое значение - результат работы функции:
	 *	ERROR_INSUFFICIENT_PRIVILEGES
	 *	ERROR_NO_ERROR
	 * 
	 * @param accessIdentity
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int Logoff(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.logout(conn, accessIdentity);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param userIds
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetLoggedUserIds(AccessIdentity_Transferable accessIdentity, WStringSeqHolder userIds) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.getLoggedUserIds(conn, accessIdentity, userIds);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param oldPassword
	 * @param newPassword
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int ChangePassword(AccessIdentity_Transferable accessIdentity, byte oldPassword[], byte newPassword[]) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.changePassword(conn, accessIdentity, oldPassword, newPassword);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param imageResourceSeq unused.
	 * @param domainSeq
	 * @param operatorCategorySeq
	 * @param operatorGroupSeq
	 * @param operatorProfileSeq
	 * @param commandPermissionAttributesSeq
	 * @param userSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetObjects(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageResourceSeq, DomainSeq_TransferableHolder domainSeq, OperatorCategorySeq_TransferableHolder operatorCategorySeq, OperatorGroupSeq_TransferableHolder operatorGroupSeq, OperatorProfileSeq_TransferableHolder operatorProfileSeq, CommandPermissionAttributesSeq_TransferableHolder commandPermissionAttributesSeq, UserSeq_TransferableHolder userSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				imageResourceSeq.value = new ImageResource_Transferable[0];
				ObjectdbInterfaceLoad.loadDomains(conn, domainSeq);
				ObjectdbInterfaceLoad.loadCategories(conn, operatorCategorySeq);
				ObjectdbInterfaceLoad.loadGroups(conn, operatorGroupSeq);
				ObjectdbInterfaceLoad.loadProfiles(conn, operatorProfileSeq);
				ObjectdbInterfaceLoad.loadExecs(conn, commandPermissionAttributesSeq);
				ObjectdbInterfaceLoad.loadUserDescriptors(conn, userSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param operatorCategoryIds
	 * @param operatorGroupIds
	 * @param operatorProfileIds
	 * @param operatorCategorySeq
	 * @param operatorGroupSeq
	 * @param operatorProfileSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetStatedObjects(AccessIdentity_Transferable accessIdentity, String operatorCategoryIds[], String operatorGroupIds[], String operatorProfileIds[], OperatorCategorySeq_TransferableHolder operatorCategorySeq, OperatorGroupSeq_TransferableHolder operatorGroupSeq, OperatorProfileSeq_TransferableHolder operatorProfileSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				ObjectdbInterfaceLoad.loadCategories(conn, operatorCategorySeq, operatorCategoryIds);
				ObjectdbInterfaceLoad.loadGroups(conn, operatorGroupSeq, operatorGroupIds);
				ObjectdbInterfaceLoad.loadProfiles(conn, operatorProfileSeq, operatorProfileIds);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param imageResourceSeq unused.
	 * @param domainSeq
	 * @param userSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetUserDescriptors(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageResourceSeq, DomainSeq_TransferableHolder domainSeq, UserSeq_TransferableHolder userSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				imageResourceSeq.value = new ImageResource_Transferable[0];
				ObjectdbInterfaceLoad.loadDomains(conn, domainSeq);
				ObjectdbInterfaceLoad.loadUserDescriptors(conn, userSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param commandPermissionAttributesSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetExecDescriptors(AccessIdentity_Transferable accessIdentity, CommandPermissionAttributesSeq_TransferableHolder commandPermissionAttributesSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				ObjectdbInterfaceLoad.loadExecs(conn, commandPermissionAttributesSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param imageResourceSeq
	 * @param domainSeq
	 * @param operatorCategorySeq
	 * @param operatorGroupSeq
	 * @param operatorProfileSeq
	 * @param commandPermissionAttributesSeq
	 * @param userSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int SaveObjects(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable imageResourceSeq[], Domain_Transferable domainSeq[], OperatorCategory_Transferable operatorCategorySeq[], OperatorGroup_Transferable operatorGroupSeq[], OperatorProfile_Transferable operatorProfileSeq[], CommandPermissionAttributes_Transferable commandPermissionAttributesSeq[], User_Transferable userSeq[]) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				ResourcedbInterface.setImages(conn, imageResourceSeq);
				ObjectdbInterfaceSave.saveDomains(conn, domainSeq);
				ObjectdbInterfaceSave.saveCategories(conn, operatorCategorySeq);
				ObjectdbInterfaceSave.saveGroups(conn, operatorGroupSeq);
				ObjectdbInterfaceSave.saveProfiles(conn, operatorProfileSeq);
				ObjectdbInterfaceSave.saveExecs(conn, commandPermissionAttributesSeq);
				ObjectdbInterfaceSave.saveUsers(conn, userSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param domainIdSeq
	 * @param operatorCategoryIdSeq
	 * @param operatorGroupIdSeq
	 * @param operatorProfileIdSeq
	 * @param commandPermissionAttributesIdSeq
	 * @param userIdSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int RemoveObjects(AccessIdentity_Transferable accessIdentity, String domainIdSeq[], String operatorCategoryIdSeq[], String operatorGroupIdSeq[], String operatorProfileIdSeq[], String commandPermissionAttributesIdSeq[], String userIdSeq[]) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				ObjectdbInterfaceRemove.removeDomains(conn, domainIdSeq);
				ObjectdbInterfaceRemove.removeCategories(conn, operatorCategoryIdSeq);
				ObjectdbInterfaceRemove.removeGroups(conn, operatorGroupIdSeq);
				ObjectdbInterfaceRemove.removeProfiles(conn, operatorProfileIdSeq);
				ObjectdbInterfaceRemove.removeExecs(conn, commandPermissionAttributesIdSeq);
				ObjectdbInterfaceRemove.removeUsers(conn, userIdSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param serverSeq
	 * @param clientSeq
	 * @param agentSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetAdminObjects(AccessIdentity_Transferable accessIdentity, ServerSeq_TransferableHolder serverSeq, ClientSeq_TransferableHolder clientSeq, AgentSeq_TransferableHolder agentSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdminDbInterfaceLoad.loadServers(conn, serverSeq);
				AdminDbInterfaceLoad.loadClients(conn, clientSeq);
				AdminDbInterfaceLoad.loadAgents(conn, agentSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param serverIdSeq
	 * @param clientIdSeq
	 * @param agentIdSeq
	 * @param serverSeq
	 * @param clientSeq
	 * @param agentSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetStatedAdminObjects(AccessIdentity_Transferable accessIdentity, String serverIdSeq[], String clientIdSeq[], String agentIdSeq[], ServerSeq_TransferableHolder serverSeq, ClientSeq_TransferableHolder clientSeq, AgentSeq_TransferableHolder agentSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdminDbInterfaceLoad.loadServers(conn, serverSeq, serverIdSeq);
				AdminDbInterfaceLoad.loadClients(conn, clientSeq, clientIdSeq);
				AdminDbInterfaceLoad.loadAgents(conn, agentSeq, agentIdSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param serverSeq
	 * @param clientSeq
	 * @param agentSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int SaveAdminObjects(AccessIdentity_Transferable accessIdentity, Server_Transferable serverSeq[], Client_Transferable clientSeq[], Agent_Transferable agentSeq[]) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdmindbInterfaceSave.saveServers(conn, serverSeq);
				AdmindbInterfaceSave.saveClients(conn, clientSeq);
				AdmindbInterfaceSave.saveAgents(conn, agentSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param serverIdSeq
	 * @param clientIdSeq
	 * @param agentIdSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int RemoveAdminObjects(AccessIdentity_Transferable accessIdentity, String serverIdSeq[], String clientIdSeq[], String agentIdSeq[]) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdmindbInterfaceRemove.removeServers(conn, serverIdSeq);
				AdmindbInterfaceRemove.removeClients(conn, clientIdSeq);
				AdmindbInterfaceRemove.removeAgents(conn, agentIdSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveMaps(AccessIdentity_Transferable accessIdentity, String[] mapseq, String[] equipmentseq, String[] kisseq, String[] markseq, String[] nodeseq, String[] nodelinkseq, String[] linkseq, String[] pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeMaps(conn, accessIdentity, mapseq, equipmentseq, kisseq, markseq, nodeseq, nodelinkseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveMapProtoElements(AccessIdentity_Transferable accessIdentity, String[] group_ids, String[] proto_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeMapProtoElements(conn, accessIdentity, group_ids, proto_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveSchemeProtoElements(AccessIdentity_Transferable accessIdentity, String[] element_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeSchemeProtoElements(conn, accessIdentity, element_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveSchemes(AccessIdentity_Transferable accessIdentity, String[] scheme_ids, String[] scheme_path_ids, String[] scheme_link_ids, String[] scheme_element_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeSchemes(conn, accessIdentity, scheme_ids, scheme_path_ids, scheme_link_ids, scheme_element_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetMessages(AccessIdentity_Transferable accessIdentity, MessageSeq_TransferableHolder messageseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getMessages(conn, accessIdentity, messageseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int SetUserAlarm(AccessIdentity_Transferable accessIdentity, String sourceId, String descriptor) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				SurveydbInterfaceSave.setUserAlarm(conn, accessIdentity, sourceId, descriptor);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetAlarmIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder alarmids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getAlarmIdsForMonitoredElement(conn, accessIdentity, me_id, alarmids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int LoadMaintenanceData(AccessIdentity_Transferable accessIdentity, EventSourceTypeSeq_TransferableHolder est, AlertingMessageSeq_TransferableHolder am, AlertingMessageUserSeq_TransferableHolder amu) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.loadMaintenanceData(conn, accessIdentity, est, am, amu);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int SaveMaintenanceData(AccessIdentity_Transferable accessIdentity, AlertingMessage_Transferable []am, AlertingMessageUser_Transferable []amu) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.saveMaintenanceData(conn, accessIdentity, am, amu);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveMaintenanceData(AccessIdentity_Transferable accessIdentity, String amu_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeMaintenanceData(conn, accessIdentity, amu_id);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetUId(AccessIdentity_Transferable accessIdentity, String type, StringHolder id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				id.value = ResourcedbInterface.getUid(conn, type);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UID, t.toString());
		}
	}

	public int GetResourceDescriptors(AccessIdentity_Transferable accessIdentity, String type, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getResourceDescriptors(conn, accessIdentity, type, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetResourceDescriptor(AccessIdentity_Transferable accessIdentity, String type, String id, ResourceDescriptor_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getResourceDescriptor(conn, accessIdentity, type, id, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetResourceDescriptorsByIds(AccessIdentity_Transferable accessIdentity, String type, String[] ids, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getResourceDescriptorsByIds(conn, accessIdentity, type, ids, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetDomainResourceDescriptors(AccessIdentity_Transferable accessIdentity, String type, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getDomainResourceDescriptors(conn, accessIdentity, type, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetImages(AccessIdentity_Transferable accessIdentity, String ids[], ImageResourceSeq_TransferableHolder imageResourceSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				Collection imageResources = ResourcedbInterface.getImages(conn, Arrays.asList(ids));
				imageResourceSeq.value = (ImageResource_Transferable[]) (imageResources.toArray(new ImageResource_Transferable[imageResources.size()]));
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveNetDirectory(AccessIdentity_Transferable accessIdentity, String[] pt_ids, String[] cpt_ids, String[] eqt_ids, String[] lt_ids, String[] clt_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeNetDirectory(conn, accessIdentity, pt_ids, cpt_ids, eqt_ids, lt_ids, clt_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveISMDirectory(AccessIdentity_Transferable accessIdentity, String[] kst_ids, String[] apt_ids, String[] tpt_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeISMDirectory(conn, accessIdentity, kst_ids, apt_ids, tpt_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int RemoveNet(AccessIdentity_Transferable accessIdentity, String[] p_ids, String[] cp_ids, String[] eq_ids, String[] l_ids, String[] cl_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeNet(conn, accessIdentity, p_ids, cp_ids, eq_ids, l_ids, cl_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetTestIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder testids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getTestIdsForMonitoredElement(conn, accessIdentity, me_id, testids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetAnalysisIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder analysisids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getAnalysisIdsForMonitoredElement(conn, accessIdentity, me_id, analysisids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetModelingIdsForSchemePath(AccessIdentity_Transferable accessIdentity, String scheme_path_id, ResourceDescriptorSeq_TransferableHolder modelingids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getModelingIdsForSchemePath(conn, accessIdentity, scheme_path_id, modelingids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetEvaluationIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder evaluationids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getEvaluationIdsForMonitoredElement(conn, accessIdentity, me_id, evaluationids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetAlarmedTests(AccessIdentity_Transferable accessIdentity, ResourceDescriptorSeq_TransferableHolder resourceDescriptorSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				TestDatadbInterfaceLoad.getAlarmedTests(conn, accessIdentity, resourceDescriptorSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int QueryResource(AccessIdentity_Transferable accessIdentity, String parameter_id, String kis_id, String parameter_type_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.queryResource(conn, accessIdentity, parameter_id, kis_id, parameter_type_id);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetResultSetResultIds(AccessIdentity_Transferable accessIdentity, String result_id, ResourceDescriptorSeq_TransferableHolder result_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getResultSetResultIds(conn, accessIdentity, result_id, result_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public int GetResultSetResultMEIds(AccessIdentity_Transferable accessIdentity, String result_id, String me_id, ResourceDescriptorSeq_TransferableHolder result_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getResultSetResultMEIds(conn, accessIdentity, result_id, me_id, result_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public String saveReportTemplates(AccessIdentity_Transferable accessIdentity, ReportTemplate_Transferable[] rts) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.saveReportTemplates(conn, accessIdentity, rts);
				return "";
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public void getStatedReportTemplates(AccessIdentity_Transferable accessIdentity, String[] ids, ReportTemplateSeq_TransferableHolder reportTemplates) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.getStatedReportTemplates(conn, accessIdentity, ids, reportTemplates);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public void removeReportTemplates(AccessIdentity_Transferable accessIdentity, String[] reportTemplate_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeReportTemplates(conn, accessIdentity, reportTemplate_ids);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public void removeSchemeOptimizeInfo(AccessIdentity_Transferable accessIdentity, String[] soi_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				AMFICOMdbInterface.removeSchemeOptimizeInfo(conn, accessIdentity, soi_ids);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, t.toString());
		}
	}

	public void syncPing(LongHolder serverTimeMillis) throws AMFICOMRemoteException {
		serverTimeMillis.value = System.currentTimeMillis();
	}

	public void registerAlarmReceiver(AccessIdentity_Transferable accessIdentity, AMFICOMClient cli) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				OracleAlarmReceiverMap.put(conn, accessIdentity, cli);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, npe.toString());
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UPDATING, t.toString());
		}
	}

	public void unregisterAlarmReceiver(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				OracleAlarmReceiverMap.remove(conn, accessIdentity);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, npe.toString());
		} catch (Throwable t) {
			t.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UPDATING, t.toString());
		}
	}

	public String lookupDomainName(final Identifier_Transferable id) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.lookupDomainName(conn, id);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}

	public String lookupUserLogin(final Identifier_Transferable id) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.lookupUserLogin(conn, id);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}

	public String lookupUserName(final Identifier_Transferable id) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.lookupUserName(conn, id);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}

	public Identifier_Transferable reverseLookupDomainName(final String domainName) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.reverseLookupDomainName(conn, domainName);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}

	public Identifier_Transferable reverseLookupUserLogin(final String userLogin) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.reverseLookupUserLogin(conn, userLogin);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}

	public Identifier_Transferable reverseLookupUserName(final String userName) throws com.syrus.AMFICOM.general.corba.AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				conn.setAutoCommit(false);
				return AdminDbInterfaceLoad.reverseLookupUserName(conn, userName);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new com.syrus.AMFICOM.general.corba.AMFICOMRemoteException(ErrorCode.ERROR_RISD_ERROR, CompletionStatus.COMPLETED_NO, t.toString());
		}
	}
}
