/*
 * $Id: AMFICOMImpl.java,v 1.1.2.4 2004/09/09 11:35:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.AMFICOMClient;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.ISM.*;
import com.syrus.AMFICOM.CORBA.ISMDirectory.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;
import com.syrus.AMFICOM.CORBA.Report.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.server.*;
import com.syrus.AMFICOM.server.event.AlarmType;
import com.syrus.AMFICOM.server.measurement.*;
import com.syrus.util.database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.omg.CORBA.*;
import org.omg.CORBA.StringHolder;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2004/09/09 11:35:20 $
 * @author $Author: bass $
 * @module server_v1
 */
public abstract class AMFICOMImpl extends _AMFICOMImplBase {
	static final DataSource DATA_SOURCE = null; 

	static  {
		try {
			final Connection conn = DATA_SOURCE.getConnection();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						System.err.print("Closing the default connection... ");
						conn.close();
						System.err.println("done.");
					} catch (SQLException sqle) {
						System.err.println("failed.");
						sqle.printStackTrace();
					}
				}
			});
			DefaultContext.setDefaultContext(new DefaultContext(conn));
			DatabaseConnection.setConnection(conn);
		} catch (SQLException sqle) {
			System.err.println("Error occured during server initialization... exiting.");
			sqle.printStackTrace();
			System.exit(-1);
		}
	}

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
				AMFICOMdbGeneral.login(conn, username, password, ior, accessIdentity);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.logout(conn, accessIdentity);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param userIds
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetLoggedUserIds(AccessIdentity_Transferable accessIdentity, wstringSeqHolder userIds) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.getLoggedUserIds(conn, accessIdentity, userIds);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AMFICOMdbGeneral.changePassword(conn, accessIdentity, oldPassword, newPassword);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				ObjectdbInterfaceLoad.loadExecs(conn, commandPermissionAttributesSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdmindbInterfaceLoad.loadServers(conn, serverSeq);
				AdmindbInterfaceLoad.loadClients(conn, clientSeq);
				AdmindbInterfaceLoad.loadAgents(conn, agentSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				AdmindbInterfaceLoad.loadServers(conn, serverSeq, serverIdSeq);
				AdmindbInterfaceLoad.loadClients(conn, clientSeq, clientIdSeq);
				AdmindbInterfaceLoad.loadAgents(conn, agentSeq, agentIdSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param imageResourceSeq unused.
	 * @param mapContextSeq
	 * @param mapElementSeq
	 * @param mapRtuElementSeq
	 * @param mapMarkElementSeq
	 * @param mapPhysicalNodeElementSeq
	 * @param mapNodeLinkElementSeq
	 * @param mapPhysicalLinkElementSeq
	 * @param mapPathElementSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetMaps(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageResourceSeq, MapContextSeq_TransferableHolder mapContextSeq, MapElementSeq_TransferableHolder mapElementSeq, MapElementSeq_TransferableHolder mapRtuElementSeq, MapMarkElementSeq_TransferableHolder mapMarkElementSeq, MapPhysicalNodeElementSeq_TransferableHolder mapPhysicalNodeElementSeq, MapNodeLinkElementSeq_TransferableHolder mapNodeLinkElementSeq, MapPhysicalLinkElementSeq_TransferableHolder mapPhysicalLinkElementSeq, MapPathElementSeq_TransferableHolder mapPathElementSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				Vector mapIdList = new Vector();
				Collection imageResourceIds = new LinkedList();
				imageResourceSeq.value = new ImageResource_Transferable[0];
				MapdbInterfaceLoad.loadMaps(conn, accessIdentity.domain_id, mapIdList, mapContextSeq);
				MapdbInterfaceLoad.loadMapElements(conn, mapIdList, mapElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapKISs(conn, mapIdList, mapRtuElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapMarks(conn, mapIdList, mapMarkElementSeq);
				MapdbInterfaceLoad.loadMapNodes(conn, mapIdList, mapPhysicalNodeElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapNodeLinks(conn, mapIdList, mapNodeLinkElementSeq);
				MapdbInterfaceLoad.loadMapLinks(conn, mapIdList, mapPhysicalLinkElementSeq);
				MapdbInterfaceLoad.loadMapPaths(conn, mapIdList, mapPathElementSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param imageResourceSeq
	 * @param ismMapContextSeq
	 * @param mapRtuElementSeq unused.
	 * @param mapPhysicalNodeElementSeq
	 * @param mapNodeLinkElementSeq
	 * @param mapPhysicalLinkElementSeq
	 * @param mapPathElementSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetJMaps(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageResourceSeq, ISMMapContextSeq_TransferableHolder ismMapContextSeq, MapKISElementSeq_TransferableHolder mapRtuElementSeq, MapPhysicalNodeElementSeq_TransferableHolder mapPhysicalNodeElementSeq, MapNodeLinkElementSeq_TransferableHolder mapNodeLinkElementSeq, MapPhysicalLinkElementSeq_TransferableHolder mapPhysicalLinkElementSeq, MapPathElementSeq_TransferableHolder mapPathElementSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				Vector mapIdList = new Vector();
				Collection imageResourceIds = new LinkedList();
				mapRtuElementSeq.value = new MapKISElement_Transferable[0];
				MapdbInterfaceLoad.loadJMaps(conn, mapIdList, ismMapContextSeq);
				MapdbInterfaceLoad.loadJMapNodes(conn, mapIdList, mapPhysicalNodeElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadJMapNodeLinks(conn, mapIdList, mapNodeLinkElementSeq);
				MapdbInterfaceLoad.loadJMapLinks(conn, mapIdList, mapPhysicalLinkElementSeq);
				MapdbInterfaceLoad.loadJMapPaths(conn, mapIdList, mapPathElementSeq);

				Collection imageResources = ResourcedbInterface.getImages(conn, imageResourceIds);
				imageResourceSeq.value = (ImageResource_Transferable[]) (imageResources.toArray(new ImageResource_Transferable[imageResources.size()]));

				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param mapId
	 * @param imageResourceSeq unused.
	 * @param mapContextSeq
	 * @param mapElementSeq
	 * @param mapRtuElementSeq
	 * @param mapMarkElementSeq
	 * @param mapPhysicalNodeElementSeq
	 * @param mapNodeLinkElementSeq
	 * @param mapPhysicalLinkElementSeq
	 * @param mapPathElementSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetMap(AccessIdentity_Transferable accessIdentity, String mapId, ImageResourceSeq_TransferableHolder imageResourceSeq, MapContextSeq_TransferableHolder mapContextSeq, MapElementSeq_TransferableHolder mapElementSeq, MapElementSeq_TransferableHolder mapRtuElementSeq, MapMarkElementSeq_TransferableHolder mapMarkElementSeq, MapPhysicalNodeElementSeq_TransferableHolder mapPhysicalNodeElementSeq, MapNodeLinkElementSeq_TransferableHolder mapNodeLinkElementSeq, MapPhysicalLinkElementSeq_TransferableHolder mapPhysicalLinkElementSeq, MapPathElementSeq_TransferableHolder mapPathElementSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				Vector mapIds = new Vector();
				Collection imageResourceIds = new LinkedList();
				imageResourceSeq.value = new ImageResource_Transferable[0];

				/**
				 * @todo No separate method to load a single map,
				 *       so ALL maps get loaded and a single one
				 *       is selected.
				 */
				MapdbInterfaceLoad.loadMaps(conn, accessIdentity.domain_id, mapIds, mapContextSeq);
				int i = mapIds.indexOf(mapId);
				mapIds.clear();
				mapIds.add(mapId);
				MapContext_Transferable mapContext = mapContextSeq.value[i];
				mapContextSeq.value = new MapContext_Transferable[1];
				mapContextSeq.value[0] = mapContext;

				MapdbInterfaceLoad.loadMapElements(conn, mapIds, mapElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapKISs(conn, mapIds, mapRtuElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapMarks(conn, mapIds, mapMarkElementSeq);
				MapdbInterfaceLoad.loadMapNodes(conn, mapIds, mapPhysicalNodeElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadMapNodeLinks(conn, mapIds, mapNodeLinkElementSeq);
				MapdbInterfaceLoad.loadMapLinks(conn, mapIds, mapPhysicalLinkElementSeq);
				MapdbInterfaceLoad.loadMapPaths(conn, mapIds, mapPathElementSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity
	 * @param mapId
	 * @param imageResourceSeq
	 * @param imnMapContextSeq
	 * @param mapRtuElementSeq unused.
	 * @param mapPhysicalNodeElementSeq
	 * @param mapNodeLinkElementSeq
	 * @param mapPhysicalLinkElementSeq
	 * @param mapPathElementSeq
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException
	 */
	public int GetJMap(AccessIdentity_Transferable accessIdentity, String mapId, ImageResourceSeq_TransferableHolder imageResourceSeq, ISMMapContextSeq_TransferableHolder imnMapContextSeq, MapKISElementSeq_TransferableHolder mapRtuElementSeq, MapPhysicalNodeElementSeq_TransferableHolder mapPhysicalNodeElementSeq, MapNodeLinkElementSeq_TransferableHolder mapNodeLinkElementSeq, MapPhysicalLinkElementSeq_TransferableHolder mapPhysicalLinkElementSeq, MapPathElementSeq_TransferableHolder mapPathElementSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				Vector mapIds = new Vector();
				Collection imageResourceIds = new LinkedList();
				mapRtuElementSeq.value = new MapKISElement_Transferable[0];
		
				/**
				 * @todo No separate method to load a single map,
				 *       so ALL maps get loaded and a single one
				 *       is selected.
				 */
				MapdbInterfaceLoad.loadJMaps(conn, mapIds, imnMapContextSeq);
				int i = mapIds.indexOf(mapId);
				mapIds.clear();
				mapIds.add(mapId);
				ISMMapContext_Transferable imnMapContext = imnMapContextSeq.value[i];
				imnMapContextSeq.value = new ISMMapContext_Transferable[1];
				imnMapContextSeq.value[0] = imnMapContext;
		
				MapdbInterfaceLoad.loadJMapNodes(conn, mapIds, mapPhysicalNodeElementSeq, imageResourceIds);
				MapdbInterfaceLoad.loadJMapNodeLinks(conn, mapIds, mapNodeLinkElementSeq);
				MapdbInterfaceLoad.loadJMapLinks(conn, mapIds, mapPhysicalLinkElementSeq);
				MapdbInterfaceLoad.loadJMapPaths(conn, mapIds, mapPathElementSeq);

				Collection imageResources = ResourcedbInterface.getImages(conn, imageResourceIds);
				imageResourceSeq.value = (ImageResource_Transferable[]) (imageResources.toArray(new ImageResource_Transferable[imageResources.size()]));

				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveMaps(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable[] imageseq, MapContext_Transferable[] mapseq, MapElement_Transferable[] equipmentseq, MapElement_Transferable[] kisseq, MapMarkElement_Transferable[] markseq, MapPhysicalNodeElement_Transferable[] nodeseq, MapNodeLinkElement_Transferable[] nodelinkseq, MapPhysicalLinkElement_Transferable[] linkseq, MapPathElement_Transferable[] pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveMaps(conn, accessIdentity, imageseq, mapseq, equipmentseq, kisseq, markseq, nodeseq, nodelinkseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveMaps(AccessIdentity_Transferable accessIdentity, String[] mapseq, String[] equipmentseq, String[] kisseq, String[] markseq, String[] nodeseq, String[] nodelinkseq, String[] linkseq, String[] pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeMaps(conn, accessIdentity, mapseq, equipmentseq, kisseq, markseq, nodeseq, nodelinkseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveJMaps(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable[] imageseq, ISMMapContext_Transferable[] mapseq, MapKISElement_Transferable[] kisseq, MapPhysicalNodeElement_Transferable[] nodeseq, MapNodeLinkElement_Transferable[] nodelinkseq, MapPhysicalLinkElement_Transferable[] linkseq, MapPathElement_Transferable[] pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveJMaps(conn, accessIdentity, imageseq, mapseq, kisseq, nodeseq, nodelinkseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveJMaps(AccessIdentity_Transferable accessIdentity, String[] mapseq, String[] kisseq, String[] nodeseq, String[] nodelinkseq, String[] linkseq, String[] pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeJMaps(conn, accessIdentity, mapseq, kisseq, nodeseq, nodelinkseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetMapProtoElements(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageseq, MapProtoGroupSeq_TransferableHolder groupseq, MapProtoElementSeq_TransferableHolder protoseq, MapLinkProtoElementSeq_TransferableHolder linkseq, MapPathProtoElementSeq_TransferableHolder pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getMapProtoElements(conn, accessIdentity, imageseq, groupseq, protoseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedMapProtoElements(AccessIdentity_Transferable accessIdentity, String[] group_ids, String[] element_ids, String[] link_ids, String[] path_ids, ImageResourceSeq_TransferableHolder imageseq, MapProtoGroupSeq_TransferableHolder groupseq, MapProtoElementSeq_TransferableHolder protoseq, MapLinkProtoElementSeq_TransferableHolder linkseq, MapPathProtoElementSeq_TransferableHolder pathseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedMapProtoElements(conn, accessIdentity, group_ids, element_ids, link_ids, path_ids, imageseq, groupseq, protoseq, linkseq, pathseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveMapProtoElements(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable[] images, MapProtoGroup_Transferable[] groups, MapProtoElement_Transferable[] protos) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveMapProtoElements(conn, accessIdentity, images, groups, protos);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveMapProtoElements(AccessIdentity_Transferable accessIdentity, String[] group_ids, String[] proto_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeMapProtoElements(conn, accessIdentity, group_ids, proto_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetSchemeProtoElements(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageseq, SchemeProtoElementSeq_TransferableHolder elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getSchemeProtoElements(conn, accessIdentity, imageseq, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedSchemeProtoElements(AccessIdentity_Transferable accessIdentity, String[] ids, ImageResourceSeq_TransferableHolder imageseq, SchemeProtoElementSeq_TransferableHolder elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedSchemeProtoElements(conn, accessIdentity, ids, imageseq, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveSchemeProtoElements(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable[] images, SchemeProtoElement_Transferable[] elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveSchemeProtoElements(conn, accessIdentity, images, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveSchemeProtoElements(AccessIdentity_Transferable accessIdentity, String[] element_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeSchemeProtoElements(conn, accessIdentity, element_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetSchemes(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageseq, SchemeSeq_TransferableHolder elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getSchemes(conn, accessIdentity, imageseq, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedSchemes(AccessIdentity_Transferable accessIdentity, String[] ids, ImageResourceSeq_TransferableHolder imageseq, SchemeSeq_TransferableHolder elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedSchemes(conn, accessIdentity, ids, imageseq, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveSchemes(AccessIdentity_Transferable accessIdentity, ImageResource_Transferable[] images, Scheme_Transferable[] elementseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveSchemes(conn, accessIdentity, images, elementseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveSchemes(AccessIdentity_Transferable accessIdentity, String[] scheme_ids, String[] scheme_path_ids, String[] scheme_link_ids, String[] scheme_element_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeSchemes(conn, accessIdentity, scheme_ids, scheme_path_ids, scheme_link_ids, scheme_element_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadAttributeTypes(AccessIdentity_Transferable accessIdentity, ElementAttributeTypeSeq_TransferableHolder attrseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadAttributeTypes(conn, accessIdentity, attrseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedAttributeTypes(AccessIdentity_Transferable accessIdentity, String[] ids, ElementAttributeTypeSeq_TransferableHolder attrseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadStatedAttributeTypes(conn, accessIdentity, ids, attrseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int ReloadAttributes(AccessIdentity_Transferable accessIdentity, String[] map_ids, ElementAttributeSeq_TransferableHolder attrseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.reloadAttributes(conn, accessIdentity, map_ids, attrseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int ReloadISMAttributes(AccessIdentity_Transferable accessIdentity, String[] ism_map_ids, ElementAttributeSeq_TransferableHolder attrseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.reloadISMAttributes(conn, accessIdentity, ism_map_ids, attrseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAlarmTypes(AccessIdentity_Transferable accessIdentity, AlarmTypeSeq_TransferableHolder alarmTypeSeq) throws AMFICOMRemoteException {
		try {
			AlarmType ats[] = AlarmType.retrieveAlarmTypes();
			AlarmType_Transferable at_t[] = new AlarmType_Transferable[ats.length];
			for (int i = 0; i < ats.length; i++)
				at_t[i] = ats[i].getTransferable();
			alarmTypeSeq.value = at_t;
			return Constants.ERROR_NO_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAlarms(AccessIdentity_Transferable accessIdentity, AlarmSeq_TransferableHolder alarmseq, EventSourceSeq_TransferableHolder eventsourceseq, EventSeq_TransferableHolder eventseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAlarms(conn, accessIdentity, alarmseq, eventsourceseq, eventseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedAlarms(AccessIdentity_Transferable accessIdentity, String[] ids, AlarmSeq_TransferableHolder alarmseq, EventSourceSeq_TransferableHolder eventsourceseq, EventSeq_TransferableHolder eventseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedAlarms(conn, accessIdentity, ids, alarmseq, eventsourceseq, eventseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedAlarmsFiltered(AccessIdentity_Transferable accessIdentity, String[] ids, Filter_Transferable filter, AlarmSeq_TransferableHolder alarmseq, EventSourceSeq_TransferableHolder eventsourceseq, EventSeq_TransferableHolder eventseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedAlarmsFiltered(conn, accessIdentity, ids, filter, alarmseq, eventsourceseq, eventseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetMessages(AccessIdentity_Transferable accessIdentity, MessageSeq_TransferableHolder messageseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getMessages(conn, accessIdentity, messageseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SetAlarm(AccessIdentity_Transferable accessIdentity, Alarm_Transferable alarm) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.setAlarm(conn, accessIdentity, alarm);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int DeleteAlarm(AccessIdentity_Transferable accessIdentity, String alarm_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.deleteAlarm(conn, accessIdentity, alarm_id);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SetUserAlarm(AccessIdentity_Transferable accessIdentity, String sourceId, String descriptor) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				SurveydbInterfaceSave.setUserAlarm(conn, accessIdentity, sourceId, descriptor);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAlarmIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder alarmids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAlarmIdsForMonitoredElement(conn, accessIdentity, me_id, alarmids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadMaintenanceData(AccessIdentity_Transferable accessIdentity, EventSourceTypeSeq_TransferableHolder est, AlertingMessageSeq_TransferableHolder am, AlertingMessageUserSeq_TransferableHolder amu) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadMaintenanceData(conn, accessIdentity, est, am, amu);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveMaintenanceData(AccessIdentity_Transferable accessIdentity, AlertingMessage_Transferable []am, AlertingMessageUser_Transferable []amu) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveMaintenanceData(conn, accessIdentity, am, amu);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveMaintenanceData(AccessIdentity_Transferable accessIdentity, String amu_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeMaintenanceData(conn, accessIdentity, amu_id);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetUId(AccessIdentity_Transferable accessIdentity, String type, StringHolder id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				id.value = ResourcedbInterface.getUid(conn, type);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UID, e.toString());
		}
	}

	public int GetResourceDescriptors(AccessIdentity_Transferable accessIdentity, String type, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResourceDescriptors(conn, accessIdentity, type, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResourceDescriptor(AccessIdentity_Transferable accessIdentity, String type, String id, ResourceDescriptor_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResourceDescriptor(conn, accessIdentity, type, id, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResourceDescriptorsByIds(AccessIdentity_Transferable accessIdentity, String type, String[] ids, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResourceDescriptorsByIds(conn, accessIdentity, type, ids, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResultDescriptorsByIds(AccessIdentity_Transferable accessIdentity, String[] ids, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResultDescriptorsByIds(conn, accessIdentity, ids, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetDomainResourceDescriptors(AccessIdentity_Transferable accessIdentity, String type, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getDomainResourceDescriptors(conn, accessIdentity, type, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetImages(AccessIdentity_Transferable accessIdentity, String ids[], ImageResourceSeq_TransferableHolder imageResourceSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadNetDirectory(AccessIdentity_Transferable accessIdentity, PortTypeSeq_TransferableHolder porttypes, EquipmentTypeSeq_TransferableHolder equipmenttypes, LinkTypeSeq_TransferableHolder linktypes, TestPortTypeSeq_TransferableHolder tporttypes, CharacteristicTypeSeq_TransferableHolder characteristictypes, CablePortTypeSeq_TransferableHolder cableporttypes, CableLinkTypeSeq_TransferableHolder cablelinktypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadNetDirectory(conn, accessIdentity, porttypes, equipmenttypes, linktypes, tporttypes, characteristictypes, cableporttypes, cablelinktypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadISMDirectory(AccessIdentity_Transferable accessIdentity, EquipmentTypeSeq_TransferableHolder kistypes, AccessPortTypeSeq_TransferableHolder aporttypes, TransmissionPathTypeSeq_TransferableHolder pathtypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadISMDirectory(conn, accessIdentity, kistypes, aporttypes, pathtypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedNetDirectory(AccessIdentity_Transferable accessIdentity, String[] pt_ids, String[] eqt_ids, String[] lt_ids, String[] cht_ids, String[] cpt_ids, String[] clt_ids, PortTypeSeq_TransferableHolder porttypes, EquipmentTypeSeq_TransferableHolder equipmenttypes, LinkTypeSeq_TransferableHolder linktypes, TestPortTypeSeq_TransferableHolder tporttypes, CharacteristicTypeSeq_TransferableHolder characteristictypes, CablePortTypeSeq_TransferableHolder cableporttypes, CableLinkTypeSeq_TransferableHolder cablelinktypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadStatedNetDirectory(conn, accessIdentity, pt_ids, eqt_ids, lt_ids, cht_ids, cpt_ids, clt_ids, porttypes, equipmenttypes, linktypes, tporttypes, characteristictypes, cableporttypes, cablelinktypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedISMDirectory(AccessIdentity_Transferable accessIdentity, String[] kis_ids, String[] aport_ids, String[] path_ids, EquipmentTypeSeq_TransferableHolder kistypes, AccessPortTypeSeq_TransferableHolder aporttypes, TransmissionPathTypeSeq_TransferableHolder pathtypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadStatedISMDirectory(conn, accessIdentity, kis_ids, aport_ids, path_ids, kistypes, aporttypes, pathtypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveNetDirectory(AccessIdentity_Transferable accessIdentity, String[] pt_ids, String[] cpt_ids, String[] eqt_ids, String[] lt_ids, String[] clt_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeNetDirectory(conn, accessIdentity, pt_ids, cpt_ids, eqt_ids, lt_ids, clt_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveISMDirectory(AccessIdentity_Transferable accessIdentity, String[] kst_ids, String[] apt_ids, String[] tpt_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeISMDirectory(conn, accessIdentity, kst_ids, apt_ids, tpt_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveNetDirectory(AccessIdentity_Transferable accessIdentity, PortType_Transferable[] porttypes, EquipmentType_Transferable[] equipmenttypes, LinkType_Transferable[] linktypes, TestPortType_Transferable[] tporttypes, CharacteristicType_Transferable[] characteristictypes, CablePortType_Transferable[] cableporttypes, CableLinkType_Transferable[] cablelinktypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveNetDirectory(conn, accessIdentity, porttypes, equipmenttypes, linktypes, tporttypes, characteristictypes, cableporttypes, cablelinktypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveISMDirectory(AccessIdentity_Transferable accessIdentity, EquipmentType_Transferable[] kistypes, AccessPortType_Transferable[] aporttypes, TransmissionPathType_Transferable[] pathtypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveISMDirectory(conn, accessIdentity, kistypes, aporttypes, pathtypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadNet(AccessIdentity_Transferable accessIdentity, PortSeq_TransferableHolder ports, CablePortSeq_TransferableHolder cports, EquipmentSeq_TransferableHolder equipments, LinkSeq_TransferableHolder links, CableLinkSeq_TransferableHolder clinks, TestPortSeq_TransferableHolder testports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadNet(conn, accessIdentity, ports, cports, equipments, links, clinks, testports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedNet(AccessIdentity_Transferable accessIdentity, String[] p_ids, String[] cp_ids, String[] eq_ids, String[] l_ids, String[] cl_ids, PortSeq_TransferableHolder ports, CablePortSeq_TransferableHolder cports, EquipmentSeq_TransferableHolder equipments, LinkSeq_TransferableHolder links, CableLinkSeq_TransferableHolder clinks, TestPortSeq_TransferableHolder testports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadStatedNet(conn, accessIdentity, p_ids, cp_ids, eq_ids, l_ids, cl_ids, ports, cports, equipments, links, clinks, testports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadISM(AccessIdentity_Transferable accessIdentity, PortSeq_TransferableHolder ports, CablePortSeq_TransferableHolder cports, EquipmentSeq_TransferableHolder kiss, LinkSeq_TransferableHolder links, CableLinkSeq_TransferableHolder clinks, MonitoredElementSeq_TransferableHolder mes, TransmissionPathSeq_TransferableHolder paths, AccessPortSeq_TransferableHolder accessports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadISM(conn, accessIdentity, ports, cports, kiss, links, clinks, mes, paths, accessports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedISM(AccessIdentity_Transferable accessIdentity, String[] p_ids, String[] cp_ids, String[] k_ids, String[] l_ids, String[] cl_ids, String[] me_ids, String[] t_ids, String[] ap_ids, PortSeq_TransferableHolder ports, CablePortSeq_TransferableHolder cports, EquipmentSeq_TransferableHolder kiss, LinkSeq_TransferableHolder links, CableLinkSeq_TransferableHolder clinks, MonitoredElementSeq_TransferableHolder mes, TransmissionPathSeq_TransferableHolder paths, AccessPortSeq_TransferableHolder accessports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadStatedISM(conn, accessIdentity, p_ids, cp_ids, k_ids, l_ids, cl_ids, me_ids, t_ids, ap_ids, ports, cports, kiss, links, clinks, mes, paths, accessports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveNet(AccessIdentity_Transferable accessIdentity, Port_Transferable[] ports, CablePort_Transferable[] cports, Equipment_Transferable[] equipments, Link_Transferable[] links, CableLink_Transferable[] clinks, TestPort_Transferable[] testports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveNet(conn, accessIdentity, ports, cports, equipments, links, clinks, testports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveISM(AccessIdentity_Transferable accessIdentity, Port_Transferable[] ports, CablePort_Transferable[] cports, Equipment_Transferable[] kiss, Link_Transferable[] links, CableLink_Transferable[] clinks, MonitoredElement_Transferable[] mes, TransmissionPath_Transferable[] paths, AccessPort_Transferable[] accessports) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveISM(conn, accessIdentity, ports, cports, kiss, links, clinks, mes, paths, accessports);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveNet(AccessIdentity_Transferable accessIdentity, String[] p_ids, String[] cp_ids, String[] eq_ids, String[] l_ids, String[] cl_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeNet(conn, accessIdentity, p_ids, cp_ids, eq_ids, l_ids, cl_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveISM(AccessIdentity_Transferable accessIdentity, String[] p_ids, String[] cp_ids, String[] ks_ids, String[] l_ids, String[] cl_ids, String[] me_ids, String[] t_ids, String[] ap_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeISM(conn, accessIdentity, p_ids, cp_ids, ks_ids, l_ids, cl_ids, me_ids, t_ids, ap_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResults(AccessIdentity_Transferable accessIdentity, ClientResultSeq_TransferableHolder resultseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResults(conn, accessIdentity, resultseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedResults(AccessIdentity_Transferable accessIdentity, String[] ids, ClientResultSeq_TransferableHolder resultseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedResults(conn, accessIdentity, ids, resultseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetRequests(AccessIdentity_Transferable accessIdentity, ClientTestRequestSeq_TransferableHolder treqseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getRequests(conn, accessIdentity, treqseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetTests(AccessIdentity_Transferable accessIdentity, ClientTestSeq_TransferableHolder testseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getTests(conn, accessIdentity, testseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedTests(AccessIdentity_Transferable accessIdentity, String[] ids, ClientTestSeq_TransferableHolder testseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedTests(conn, accessIdentity, ids, testseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetTestIdsInDiapazon(AccessIdentity_Transferable accessIdentity, long start_time, long end_time, ResourceDescriptorSeq_TransferableHolder desc) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getTestIdsInDiapazon(conn, accessIdentity, start_time, end_time, desc);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetTestIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder testids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getTestIdsForMonitoredElement(conn, accessIdentity, me_id, testids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAnalysis(AccessIdentity_Transferable accessIdentity, ClientAnalysisSeq_TransferableHolder analysisseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAnalysis(conn, accessIdentity, analysisseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAnalysisIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder analysisids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAnalysisIdsForMonitoredElement(conn, accessIdentity, me_id, analysisids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetModelings(AccessIdentity_Transferable accessIdentity, ClientModelingSeq_TransferableHolder modelingseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getModelings(conn, accessIdentity, modelingseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetModelingIdsForSchemePath(AccessIdentity_Transferable accessIdentity, String scheme_path_id, ResourceDescriptorSeq_TransferableHolder modelingids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getModelingIdsForSchemePath(conn, accessIdentity, scheme_path_id, modelingids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetEvaluations(AccessIdentity_Transferable accessIdentity, ClientEvaluationSeq_TransferableHolder evaluationseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getEvaluations(conn, accessIdentity, evaluationseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetEvaluationIdsForMonitoredElement(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptorSeq_TransferableHolder evaluationids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getEvaluationIdsForMonitoredElement(conn, accessIdentity, me_id, evaluationids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetRequestTests(AccessIdentity_Transferable accessIdentity, String request_id, ClientTestSeq_TransferableHolder testseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getRequestTests(conn, accessIdentity, request_id, testseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAlarmedTests(AccessIdentity_Transferable accessIdentity, ResourceDescriptorSeq_TransferableHolder resourceDescriptorSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				TestDatadbInterfaceLoad.getAlarmedTests(conn, accessIdentity, resourceDescriptorSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RequestTest(AccessIdentity_Transferable accessIdentity, ClientTestRequest_Transferable clientTestRequest, ClientTest_Transferable[] clientTestSeq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				TestDatadbInterfaceSave.requestTest(conn, accessIdentity, clientTestRequest, clientTestSeq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int QueryResource(AccessIdentity_Transferable accessIdentity, String parameter_id, String kis_id, String parameter_type_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.queryResource(conn, accessIdentity, parameter_id, kis_id, parameter_type_id);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int RemoveTests(AccessIdentity_Transferable accessIdentity, String[] testids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeTests(conn, accessIdentity, testids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int UpdateTests(AccessIdentity_Transferable accessIdentity, ClientTest_Transferable[] testseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.updateTests(conn, accessIdentity, testseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetLastResult(AccessIdentity_Transferable accessIdentity, String me_id, ClientResult_TransferableHolder result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getLastResult(conn, accessIdentity, me_id, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetTestResults(AccessIdentity_Transferable accessIdentity, String test_id, ClientResultSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getTestResults(conn, accessIdentity, test_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAnalysisResults(AccessIdentity_Transferable accessIdentity, String analysis_id, ClientResultSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAnalysisResults(conn, accessIdentity, analysis_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientResult_Transferable[] GetStatisticsAnalysisResults(AccessIdentity_Transferable accessIdentity, String monitored_element_id, long start_time, long end_time) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.getStatisticsAnalysisResults(conn, accessIdentity, monitored_element_id, start_time, end_time);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientResult_Transferable[] GetStatisticsAnalysisResultsByTS(AccessIdentity_Transferable accessIdentity, String monitored_element_id, long start_time, long end_time, String test_setup_id) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.getStatisticsAnalysisResultsByTS(conn, accessIdentity, monitored_element_id, start_time, end_time, test_setup_id);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetModelingResult(AccessIdentity_Transferable accessIdentity, String modeling_id, ClientResult_TransferableHolder result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getModelingResult(conn, accessIdentity, modeling_id, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetEvaluationResults(AccessIdentity_Transferable accessIdentity, String evaluation_id, ClientResultSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getEvaluationResults(conn, accessIdentity, evaluation_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetLastResultId(AccessIdentity_Transferable accessIdentity, String me_id, ResourceDescriptor_TransferableHolder result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getLastResultId(conn, accessIdentity, me_id, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetTestResultIds(AccessIdentity_Transferable accessIdentity, String test_id, ResourceDescriptorSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getTestResultIds(conn, accessIdentity, test_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetAnalysisResultIds(AccessIdentity_Transferable accessIdentity, String analysis_id, ResourceDescriptorSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getAnalysisResultIds(conn, accessIdentity, analysis_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetModelingResultId(AccessIdentity_Transferable accessIdentity, String modeling_id, ResourceDescriptor_TransferableHolder result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getModelingResultId(conn, accessIdentity, modeling_id, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetEvaluationResultIds(AccessIdentity_Transferable accessIdentity, String evaluation_id, ResourceDescriptorSeq_TransferableHolder results) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getEvaluationResultIds(conn, accessIdentity, evaluation_id, results);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResult(AccessIdentity_Transferable accessIdentity, String result_id, ClientResult_TransferableHolder result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResult(conn, accessIdentity, result_id, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveAnalysis(AccessIdentity_Transferable accessIdentity, ClientAnalysis_Transferable analysis, ClientResult_Transferable result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveAnalysis(conn, accessIdentity, analysis, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveModeling(AccessIdentity_Transferable accessIdentity, ClientModeling_Transferable modeling, ClientResult_Transferable result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveModeling(conn, accessIdentity, modeling, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveEvaluation(AccessIdentity_Transferable accessIdentity, ClientEvaluation_Transferable evaluation, ClientResult_Transferable result) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveEvaluation(conn, accessIdentity, evaluation, result);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResultSets(AccessIdentity_Transferable accessIdentity, ResultSetSeq_TransferableHolder resultsets) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResultSets(conn, accessIdentity, resultsets);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedResultSets(AccessIdentity_Transferable accessIdentity, String[] ids, ResultSetSeq_TransferableHolder resultsets) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedResultSets(conn, accessIdentity, ids, resultsets);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResultSetResultIds(AccessIdentity_Transferable accessIdentity, String result_id, ResourceDescriptorSeq_TransferableHolder result_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResultSetResultIds(conn, accessIdentity, result_id, result_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetResultSetResultMEIds(AccessIdentity_Transferable accessIdentity, String result_id, String me_id, ResourceDescriptorSeq_TransferableHolder result_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getResultSetResultMEIds(conn, accessIdentity, result_id, me_id, result_ids);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadGlobalParameterTypes(AccessIdentity_Transferable accessIdentity, GlobalParameterTypeSeq_TransferableHolder params) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadGlobalParameterTypes(conn, accessIdentity, params);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedGlobalParameterTypes(AccessIdentity_Transferable accessIdentity, String[] ids, GlobalParameterTypeSeq_TransferableHolder params) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadGlobalParameterTypes(conn, accessIdentity, ids, params);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadTestTypes(AccessIdentity_Transferable accessIdentity, TestTypeSeq_TransferableHolder ttypeseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadTestTypes(conn, accessIdentity, ttypeseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedTestTypes(AccessIdentity_Transferable accessIdentity, String[] ids, TestTypeSeq_TransferableHolder ttypeseq) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadTestTypes(conn, accessIdentity, ids, ttypeseq);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadAnalysisTypes(AccessIdentity_Transferable accessIdentity, AnalysisTypeSeq_TransferableHolder atypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadAnalysisTypes(conn, accessIdentity, atypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedAnalysisTypes(AccessIdentity_Transferable accessIdentity, String[] ids, AnalysisTypeSeq_TransferableHolder atypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadAnalysisTypes(conn, accessIdentity, ids, atypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadEvaluationTypes(AccessIdentity_Transferable accessIdentity, EvaluationTypeSeq_TransferableHolder etypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadEvaluationTypes(conn, accessIdentity, etypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedEvaluationTypes(AccessIdentity_Transferable accessIdentity, String[] ids, EvaluationTypeSeq_TransferableHolder etypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadEvaluationTypes(conn, accessIdentity, ids, etypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadModelingTypes(AccessIdentity_Transferable accessIdentity, ModelingTypeSeq_TransferableHolder mtypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadModelingTypes(conn, accessIdentity, mtypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedModelingTypes(AccessIdentity_Transferable accessIdentity, String[] ids, ModelingTypeSeq_TransferableHolder mtypes) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.loadModelingTypes(conn, accessIdentity, ids, mtypes);
				return Constants.ERROR_NO_ERROR;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity unused.
	 * @param clientCriteriaSetSeq unused.
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException never.
	 * @deprecated This method does nothing.
	 */
	public int LoadCriteriaSets(AccessIdentity_Transferable accessIdentity, ClientCriteriaSetSeq_TransferableHolder clientCriteriaSetSeq) throws AMFICOMRemoteException {
		return Constants.ERROR_NO_ERROR;
	}

	public int LoadStatedCriteriaSets(AccessIdentity_Transferable accessIdentity, String[] ids, ClientCriteriaSetSeq_TransferableHolder clientCriteriaSetSeq) throws AMFICOMRemoteException {
		try {
			Collection clientCriteriaSets = new LinkedList();
			for (int i = 0; i < ids.length; i++)
				clientCriteriaSets.add(this.getCriteriaSet(accessIdentity, ids[i]));
			clientCriteriaSetSeq.value = (ClientCriteriaSet_Transferable[]) (clientCriteriaSets.toArray(new ClientCriteriaSet_Transferable[clientCriteriaSets.size()]));
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	/**
	 * @param accessIdentity unused.
	 * @param clientThresholdSetSeq unused.
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException never.
	 * @deprecated This method does nothing.
	 */
	public int LoadThresholdSets(AccessIdentity_Transferable accessIdentity, ClientThresholdSetSeq_TransferableHolder clientThresholdSetSeq) throws AMFICOMRemoteException {
		return Constants.ERROR_NO_ERROR;
	}

	/**
	 * @param accessIdentity unused.
	 * @param ids unused.
	 * @param clientThresholdSetSeq unused.
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException never.
	 * @deprecated This method does nothing.
	 */
	public int LoadStatedThresholdSets(AccessIdentity_Transferable accessIdentity, String ids[], ClientThresholdSetSeq_TransferableHolder clientThresholdSetSeq) throws AMFICOMRemoteException {
		return Constants.ERROR_NO_ERROR;
	}

	/**
	 * @param accessIdentity unused.
	 * @param clientEtalonSeq unused.
	 * @return {@link Constants#ERROR_NO_ERROR}
	 * @throws AMFICOMRemoteException never.
	 * @deprecated This method does nothing.
	 */
	public int LoadEtalons(AccessIdentity_Transferable accessIdentity, ClientEtalonSeq_TransferableHolder clientEtalonSeq) throws AMFICOMRemoteException {
		return Constants.ERROR_NO_ERROR;
	}

	/**
	 * @param accessIdentity unused.
	 * @param testTypeId unused.
	 * @return a zero-length array. 
	 * @throws AMFICOMRemoteException never.
	 * @deprecated This method does nothing.
	 */
	public AnalysisType_Transferable[] getAnalysisTypes(AccessIdentity_Transferable accessIdentity, String testTypeId) throws AMFICOMRemoteException {
		return new AnalysisType_Transferable[0];
	}

	public String createAnalysisType(AccessIdentity_Transferable accessIdentity, AnalysisType_Transferable analysis_type_t) throws AMFICOMRemoteException {
		try {
			return (new AnalysisType(analysis_type_t)).getTransferable().id;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientTest_Transferable getTestByAnalysis(AccessIdentity_Transferable accessIdentity, String analysis_id) throws AMFICOMRemoteException {
		try {
			return (new Analysis(analysis_id)).getTest().getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ResourceDescriptor_Transferable getTestIdByAnalysis(AccessIdentity_Transferable accessIdentity, String analysis_id) throws AMFICOMRemoteException {
		try {
			ClientTest_Transferable test_t = (new Analysis(analysis_id)).getTest().getClientTransferable();
			return new ResourceDescriptor_Transferable(test_t.id, test_t.modified);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientTest_Transferable getTestByEvaluation(AccessIdentity_Transferable accessIdentity, String evaluation_id) throws AMFICOMRemoteException {
		try {
			return (new Evaluation(evaluation_id)).getTest().getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ResourceDescriptor_Transferable getTestIdByEvaluation(AccessIdentity_Transferable accessIdentity, String evaluation_id) throws AMFICOMRemoteException {
		try {
			ClientTest_Transferable test_t = getTestByEvaluation(accessIdentity, evaluation_id);
			return new ResourceDescriptor_Transferable(test_t.id, test_t.modified);
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createAnalysis(AccessIdentity_Transferable accessIdentity, ClientAnalysis_Transferable analysis_t) throws AMFICOMRemoteException {
		try {
			return (new Analysis(analysis_t)).getId();
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientAnalysis_Transferable getAnalysisById(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new Analysis(id)).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedAnalysis(AccessIdentity_Transferable accessIdentity, String[] ids, ClientAnalysisSeq_TransferableHolder clientAnalysisSeq) throws AMFICOMRemoteException {
		try {
			Collection clientAnalyses = new LinkedList();
			for (int i = 0; i < ids.length; i++)
				  clientAnalyses.add((new Analysis(ids[i])).getClientTransferable());
			clientAnalysisSeq.value = (ClientAnalysis_Transferable[]) (clientAnalyses.toArray(new ClientAnalysis_Transferable[clientAnalyses.size()]));
			return Constants.ERROR_NO_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientAnalysis_Transferable getAnalysisByTest(AccessIdentity_Transferable accessIdentity, String test_id) throws AMFICOMRemoteException {
		try {
			return Analysis.retrieveAnalysis(test_id).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ResourceDescriptor_Transferable getAnalysisIdByTest(AccessIdentity_Transferable accessIdentity, String test_id) throws AMFICOMRemoteException {
		try {
			ClientAnalysis_Transferable analysis_t = Analysis.retrieveAnalysis(test_id).getClientTransferable();
			return new ResourceDescriptor_Transferable(analysis_t.id, analysis_t.modified);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createEvaluation(AccessIdentity_Transferable accessIdentity, ClientEvaluation_Transferable evaluation_t) throws AMFICOMRemoteException {
		try {
			return (new Evaluation(evaluation_t)).getId();
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientEvaluation_Transferable getEvaluationById(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new Evaluation(id)).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedEvaluations(AccessIdentity_Transferable accessIdentity, String[] ids, ClientEvaluationSeq_TransferableHolder clientEvaluationSeq) throws AMFICOMRemoteException {
		try {
			Collection clientEvaluations = new LinkedList();
			for (int i = 0; i < ids.length; i++)
				clientEvaluations.add((new Evaluation(ids[i])).getClientTransferable());
			clientEvaluationSeq.value = (ClientEvaluation_Transferable[]) (clientEvaluations.toArray(new ClientEvaluation_Transferable[clientEvaluations.size()]));
			return Constants.ERROR_NO_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientEvaluation_Transferable getEvaluationByTest(AccessIdentity_Transferable accessIdentity, String test_id) throws AMFICOMRemoteException {
		try {
			return Evaluation.retrieveEvaluation(test_id).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ResourceDescriptor_Transferable getEvaluationIdByTest(AccessIdentity_Transferable accessIdentity, String test_id) throws AMFICOMRemoteException {
		try {
			ClientEvaluation_Transferable evaluation_t = Evaluation.retrieveEvaluation(test_id).getClientTransferable();
			return new ResourceDescriptor_Transferable(evaluation_t.id, evaluation_t.modified);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientModeling_Transferable getModelingById(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new Modeling(id)).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadStatedModelings(AccessIdentity_Transferable accessIdentity, String[] ids, ClientModelingSeq_TransferableHolder clientModelingSeq) throws AMFICOMRemoteException {
		try {
			Collection clientModelings = new LinkedList();
			for (int i = 0; i < ids.length; i++)
				clientModelings.add((new Modeling(ids[i])).getClientTransferable());
			clientModelingSeq.value = (ClientModeling_Transferable[]) (clientModelings.toArray(new ClientModeling_Transferable[clientModelings.size()]));
			return Constants.ERROR_NO_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createTestSetup(AccessIdentity_Transferable accessIdentity, TestSetup_Transferable test_setup_t) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.createTestSetup(conn, accessIdentity, test_setup_t);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void attachTestSetupToME(AccessIdentity_Transferable accessIdentity, String test_setup_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			(new TestSetup(test_setup_id)).attachToMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void detachTestSetupFromME(AccessIdentity_Transferable accessIdentity, String test_setup_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			(new TestSetup(test_setup_id)).detachFromMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public TestSetup_Transferable getTestSetup(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new TestSetup(id)).getTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public TestSetup_Transferable[] getTestSetupsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			TestSetup ts[] = TestSetup.retrieveTestSetups(monitored_element_id);
			TestSetup_Transferable test_setups_t[] = new TestSetup_Transferable[ts.length];
			for (int i = 0; i < ts.length; i++)
				test_setups_t[i] = ts[i].getTransferable();
			return test_setups_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public TestSetup_Transferable[] getTestSetupsByTestType(AccessIdentity_Transferable accessIdentity, String test_type_id) throws AMFICOMRemoteException {
		try {
			TestSetup ts[] = TestSetup.retrieveTestSetupsForTestType(test_type_id);
			TestSetup_Transferable test_setups_t[] = new TestSetup_Transferable[ts.length];
			for (int i = 0; i < ts.length; i++)
				test_setups_t[i] = ts[i].getTransferable();
			return test_setups_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String[] getTestSetupIdsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			TestSetup ts[] = TestSetup.retrieveTestSetups(monitored_element_id);
			String test_setup_ids[] = new String[ts.length];
			for (int i = 0; i < ts.length; i++)
				test_setup_ids[i] = ts[i].getId();
			return test_setup_ids;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String[] getTestSetupIdsByTestType(AccessIdentity_Transferable accessIdentity, String test_type_id) throws AMFICOMRemoteException {
		try {
			TestSetup ts[] = TestSetup.retrieveTestSetupsForTestType(test_type_id);
			String test_setup_ids[] = new String[ts.length];
			for (int i = 0; i < ts.length; i++)
				test_setup_ids[i] = ts[i].getId();
			return test_setup_ids;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public TestSetup_Transferable[] getTestSetupsByTestTypeAndME(AccessIdentity_Transferable accessIdentity, String monitored_element_id, String test_type_id) throws AMFICOMRemoteException {
		try {
			TestSetup ts[] = TestSetup.retrieveTestSetups(monitored_element_id);
			TestSetup_Transferable test_setups_t[] = new TestSetup_Transferable[ts.length];
			for (int i = 0; i < ts.length; i++)
				test_setups_t[i] = ts[i].getTransferable();
			return test_setups_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createTestArgumentSet(AccessIdentity_Transferable accessIdentity, ClientTestArgumentSet_Transferable arg_set_t) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.createTestArgumentSet(conn, accessIdentity, arg_set_t);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void attachTestArgumentSetToME(AccessIdentity_Transferable accessIdentity, String arg_set_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			(new TestArgumentSet(arg_set_id)).attachToMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientTestArgumentSet_Transferable getTestArgumentSet(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new TestArgumentSet(id)).getTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientTestArgumentSet_Transferable[] getTestArgumentSetsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			TestArgumentSet css[] = TestArgumentSet.retrieveTestArgumentSets(monitored_element_id);
			ClientTestArgumentSet_Transferable arg_sets_t[] = new ClientTestArgumentSet_Transferable[css.length];
			for (int i = 0; i < css.length; i++)
				arg_sets_t[i] = css[i].getTransferable();
			return arg_sets_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createCriteriaSet(AccessIdentity_Transferable accessIdentity, ClientCriteriaSet_Transferable criteria_set_t) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.createCriteriaSet(conn, accessIdentity, criteria_set_t);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void attachCriteriaSetToME(AccessIdentity_Transferable accessIdentity, String criteria_set_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			(new CriteriaSet(criteria_set_id)).attachToMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientCriteriaSet_Transferable getCriteriaSet(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new CriteriaSet(id)).getTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientCriteriaSet_Transferable[] getCriteriaSetsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			CriteriaSet css[] = CriteriaSet.retrieveCriteriaSets(monitored_element_id);
			ClientCriteriaSet_Transferable criteria_sets_t[] = new ClientCriteriaSet_Transferable[css.length];
			for (int i = 0; i < css.length; i++)
				criteria_sets_t[i] = css[i].getTransferable();
			return criteria_sets_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientCriteriaSet_Transferable[] getCriteriaSetsByAnalysisType(AccessIdentity_Transferable accessIdentity, String monitored_element_id, String analysis_type_id) throws AMFICOMRemoteException {
		try {
			CriteriaSet css[] = CriteriaSet.retrieveCriteriaSets(monitored_element_id, analysis_type_id);
			ClientCriteriaSet_Transferable criteria_sets_t[] = new ClientCriteriaSet_Transferable[css.length];
			for (int i = 0; i < css.length; i++)
				criteria_sets_t[i] = css[i].getTransferable();
			return criteria_sets_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createThresholdSet(AccessIdentity_Transferable accessIdentity, ClientThresholdSet_Transferable th_set_t) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.createThresholdSet(conn, accessIdentity, th_set_t);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void attachThresholdSetToME(AccessIdentity_Transferable accessIdentity, String th_set_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			  (new ThresholdSet(th_set_id)).attachToMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}
	
	public ClientThresholdSet_Transferable getThresholdSet(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new ThresholdSet(id)).getTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientThresholdSet_Transferable[] getThresholdSetsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			ThresholdSet css[] = ThresholdSet.retrieveThresholdSets(monitored_element_id);
			ClientThresholdSet_Transferable th_sets_t[] = new ClientThresholdSet_Transferable[css.length];
			for (int i = 0; i < css.length; i++)
				th_sets_t[i] = css[i].getTransferable();
			return th_sets_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientThresholdSet_Transferable[] getThresholdSetsByAnalysisType(AccessIdentity_Transferable accessIdentity, String monitored_element_id, String evaluation_type_id) throws AMFICOMRemoteException {
		try {
			ThresholdSet css[] = ThresholdSet.retrieveThresholdSets(monitored_element_id, evaluation_type_id);
			ClientThresholdSet_Transferable th_sets_t[] = new ClientThresholdSet_Transferable[css.length];
			for (int i = 0; i < css.length; i++)
				th_sets_t[i] = css[i].getTransferable();
			return th_sets_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String createEtalon(AccessIdentity_Transferable accessIdentity, ClientEtalon_Transferable e_t) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
			  	return AMFICOMdbInterface.createEtalon(conn, accessIdentity, e_t);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void attachEtalonToME(AccessIdentity_Transferable accessIdentity, String e_id, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			(new Etalon(e_id)).attachToMonitoredElement(monitored_element_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientEtalon_Transferable getEtalon(AccessIdentity_Transferable accessIdentity, String id) throws AMFICOMRemoteException {
		try {
			return (new Etalon(id)).getClientTransferable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientEtalon_Transferable[] getEtalonsByME(AccessIdentity_Transferable accessIdentity, String monitored_element_id) throws AMFICOMRemoteException {
		try {
			Etalon es[] = Etalon.retrieveEtalons(monitored_element_id);
			ClientEtalon_Transferable es_t[] = new ClientEtalon_Transferable[es.length];
			for (int i = 0; i < es.length; i++)
				es_t[i] = es[i].getClientTransferable();
			return es_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public ClientEtalon_Transferable getEtalonByMEandTime(AccessIdentity_Transferable accessIdentity, String monitored_element_id, long time) throws AMFICOMRemoteException {
		try {
			Etalon es[] = Etalon.retrieveEtalons(monitored_element_id);
			if (es.length == 0)
				throw new AMFICOMRemoteException(Constants.ERROR_EMPTY, "No etalon could be found for monitoredElementId specified.");
			int index = 0;
			long temp = es[0].getClientTransferable().modified;
			for (int i = 1; i < es.length; i++)
				if (Math.abs(temp - time) > Math.abs(es[0].getClientTransferable().modified - time)) {
					index = i;
					temp = es[i].getClientTransferable().modified;
				}
			return es[index].getClientTransferable();
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String saveReportTemplates(AccessIdentity_Transferable accessIdentity, ReportTemplate_Transferable[] rts) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveReportTemplates(conn, accessIdentity, rts);
				return "";
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void getStatedReportTemplates(AccessIdentity_Transferable accessIdentity, String[] ids, ReportTemplateSeq_TransferableHolder reportTemplates) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.getStatedReportTemplates(conn, accessIdentity, ids, reportTemplates);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void removeReportTemplates(AccessIdentity_Transferable accessIdentity, String[] reportTemplate_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeReportTemplates(conn, accessIdentity, reportTemplate_ids);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String saveSchemeOptimizeInfo(AccessIdentity_Transferable accessIdentity, SchemeOptimizeInfo_Transferable soi) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveSchemeOptimizeInfo(conn, accessIdentity, soi);
				return "";
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public SchemeOptimizeInfo_Transferable[] getSchemeOptimizeInfo(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.getSchemeOptimizeInfo(conn, accessIdentity);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void removeSchemeOptimizeInfo(AccessIdentity_Transferable accessIdentity, String[] soi_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.removeSchemeOptimizeInfo(conn, accessIdentity, soi_ids);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public String saveSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity, SchemeMonitoringSolution_Transferable sol) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbInterface.saveSchemeMonitoringSolutions(conn, accessIdentity, sol);
				return "";
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public SchemeMonitoringSolution_Transferable[] getSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				return AMFICOMdbInterface.getSchemeMonitoringSolutions(conn, accessIdentity);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public void removeSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity, String[] sol_ids) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
				AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
				for (int i = 0; i < sol_ids.length; i++)
					(new SchemeMonitoringSolution(conn, sol_ids[i])).delete(conn);
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UPDATING, e.toString());
		}
	}

	public void unregisterAlarmReceiver(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			Connection conn = null;
			try {
				conn = DATA_SOURCE.getConnection();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_UPDATING, e.toString());
		}
	}
}
