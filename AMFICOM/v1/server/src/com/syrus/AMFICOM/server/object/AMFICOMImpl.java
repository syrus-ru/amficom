/*
 * $Id: AMFICOMImpl.java,v 1.1.2.3 2004/08/27 10:52:13 bass Exp $
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
import org.omg.CORBA.*;
import org.omg.CORBA.StringHolder;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2004/08/27 10:52:13 $
 * @author $Author: bass $
 * @module server_v1
 */
public final class AMFICOMImpl extends _AMFICOMImplBase {
	private static final Connection CONN = DefaultContext.getDefaultContext().getConnection();

	static {
		DatabaseConnection.setConnection(CONN);
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
	 */
	public int Logon(String username, byte password[], String ior, AccessIdentity_TransferableHolder accessIdentity) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.login(CONN, username, password, ior, accessIdentity);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
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
	 */
	public int Logoff(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			AMFICOMdbGeneral.logout(CONN, accessIdentity);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetLoggedUserIds(AccessIdentity_Transferable accessIdentity, wstringSeqHolder userids) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			AMFICOMdbGeneral.getLoggedUserIds(CONN, accessIdentity, userids);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int ChangePassword(AccessIdentity_Transferable accessIdentity, byte oldpassword[], byte newpassword[]) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			AMFICOMdbGeneral.changePassword(CONN, accessIdentity, oldpassword, newpassword);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetObjects(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageseq, DomainSeq_TransferableHolder domainseq, OperatorCategorySeq_TransferableHolder categoryseq, OperatorGroupSeq_TransferableHolder groupseq, OperatorProfileSeq_TransferableHolder profileseq, CommandPermissionAttributesSeq_TransferableHolder execseq, UserSeq_TransferableHolder userseq) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
//			Collection images = ResourcedbInterface.getImages(CONN);
//			imageseq.value = (ImageResource_Transferable[]) (images.toArray(new ImageResource_Transferable[images.size()]));
			imageseq.value = new ImageResource_Transferable[0]; 
			ObjectdbInterfaceLoad.loadDomains(CONN, domainseq);
			ObjectdbInterfaceLoad.loadCategories(CONN, categoryseq);
			ObjectdbInterfaceLoad.loadGroups(CONN, groupseq);
			ObjectdbInterfaceLoad.loadProfiles(CONN, profileseq);
			ObjectdbInterfaceLoad.loadExecs(CONN, execseq);
			ObjectdbInterfaceLoad.loadUserDescriptors(CONN, userseq);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetStatedObjects(AccessIdentity_Transferable accessIdentity, String category_ids[], String group_ids[], String profile_ids[], OperatorCategorySeq_TransferableHolder categoryseq, OperatorGroupSeq_TransferableHolder groupseq, OperatorProfileSeq_TransferableHolder profileseq) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			ObjectdbInterfaceLoad.loadCategories(CONN, categoryseq, category_ids);
			ObjectdbInterfaceLoad.loadGroups(CONN, groupseq, group_ids);
			ObjectdbInterfaceLoad.loadProfiles(CONN, profileseq, profile_ids);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetUserDescriptors(AccessIdentity_Transferable accessIdentity, ImageResourceSeq_TransferableHolder imageseq, DomainSeq_TransferableHolder domainseq, UserSeq_TransferableHolder userseq) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			imageseq.value = new ImageResource_Transferable[0];
			ObjectdbInterfaceLoad.loadDomains(CONN, domainseq);
			ObjectdbInterfaceLoad.loadUserDescriptors(CONN, userseq);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int GetExecDescriptors(AccessIdentity_Transferable accessIdentity, CommandPermissionAttributesSeq_TransferableHolder execseq) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			ObjectdbInterfaceLoad.loadExecs(CONN, execseq);
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int SaveObjects(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] imageseq,
			Domain_Transferable[] domainseq,
			OperatorCategory_Transferable[] categoryseq,
			OperatorGroup_Transferable[] groupseq,
			OperatorProfile_Transferable[] profileseq,
			CommandPermissionAttributes_Transferable[] execseq,
			User_Transferable[] userseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveObjects(
				accessIdentity,
				imageseq,
				domainseq,
				categoryseq,
				groupseq,
				profileseq,
				execseq,
				userseq);
	}

	public int RemoveObjects(
			AccessIdentity_Transferable accessIdentity,
			String[] domainseq,
			String[] categoryseq,
			String[] groupseq,
			String[] profileseq,
			String[] execseq,
			String[] userseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveObjects(
				accessIdentity,
				domainseq,
				categoryseq,
				groupseq,
				profileseq,
				execseq,
				userseq);
	}

	public int GetAdminObjects(
			AccessIdentity_Transferable accessIdentity,
			ServerSeq_TransferableHolder serverseq,
			ClientSeq_TransferableHolder clientseq,
			AgentSeq_TransferableHolder agentseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAdminObjects(
				accessIdentity,
				serverseq,
				clientseq,
				agentseq);
	}

	public int GetStatedAdminObjects(
			AccessIdentity_Transferable accessIdentity,
			String []server_ids,
			String []client_ids,
			String []agent_ids,
			ServerSeq_TransferableHolder serverseq,
			ClientSeq_TransferableHolder clientseq,
			AgentSeq_TransferableHolder agentseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedAdminObjects(
				accessIdentity,
				server_ids,
				client_ids,
				agent_ids,
				serverseq,
				clientseq,
				agentseq);
	}

	public int SaveAdminObjects(
			AccessIdentity_Transferable accessIdentity,
			Server_Transferable[] serverseq,
			Client_Transferable[] clientseq,
			Agent_Transferable[] agentseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveAdminObjects(
				accessIdentity,
				serverseq,
				clientseq,
				agentseq);
	}

	public int RemoveAdminObjects(
			AccessIdentity_Transferable accessIdentity,
			String[] serverseq,
			String[] clientseq,
			String[] agentseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveAdminObjects(
				accessIdentity,
				serverseq,
				clientseq,
				agentseq);
	}

	public int GetMaps(
			AccessIdentity_Transferable accessIdentity,
			ImageResourceSeq_TransferableHolder imageseq,
			MapContextSeq_TransferableHolder mapseq,
			MapElementSeq_TransferableHolder equipmentseq,
			MapElementSeq_TransferableHolder kisseq,
			MapMarkElementSeq_TransferableHolder markseq,
			MapPhysicalNodeElementSeq_TransferableHolder nodeseq,
			MapNodeLinkElementSeq_TransferableHolder nodelinkseq,
			MapPhysicalLinkElementSeq_TransferableHolder linkseq,
			MapPathElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		int retcode = AMFICOMdbInterface.GetMaps(
			accessIdentity,
			imageseq,
			mapseq,
			equipmentseq,
			kisseq,
			markseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
		return retcode;
	}

	public int GetJMaps(
			AccessIdentity_Transferable accessIdentity,
			ImageResourceSeq_TransferableHolder imageseq,
			ISMMapContextSeq_TransferableHolder mapseq,
			MapKISElementSeq_TransferableHolder kisseq,
			MapPhysicalNodeElementSeq_TransferableHolder nodeseq,
			MapNodeLinkElementSeq_TransferableHolder nodelinkseq,
			MapPhysicalLinkElementSeq_TransferableHolder linkseq,
			MapPathElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetJMaps(
			accessIdentity,
			imageseq,
			mapseq,
			kisseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int GetMap(
			AccessIdentity_Transferable accessIdentity,
			String map_id,
			ImageResourceSeq_TransferableHolder imageseq,
			MapContextSeq_TransferableHolder mapseq,
			MapElementSeq_TransferableHolder equipmentseq,
			MapElementSeq_TransferableHolder kisseq,
			MapMarkElementSeq_TransferableHolder markseq,
			MapPhysicalNodeElementSeq_TransferableHolder nodeseq,
			MapNodeLinkElementSeq_TransferableHolder nodelinkseq,
			MapPhysicalLinkElementSeq_TransferableHolder linkseq,
			MapPathElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		int retcode = AMFICOMdbInterface.GetMap(
			accessIdentity,
			map_id,
			imageseq,
			mapseq,
			equipmentseq,
			kisseq,
			markseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
		return retcode;
	}

	public int GetJMap(
			AccessIdentity_Transferable accessIdentity,
			String map_id,
			ImageResourceSeq_TransferableHolder imageseq,
			ISMMapContextSeq_TransferableHolder mapseq,
			MapKISElementSeq_TransferableHolder kisseq,
			MapPhysicalNodeElementSeq_TransferableHolder nodeseq,
			MapNodeLinkElementSeq_TransferableHolder nodelinkseq,
			MapPhysicalLinkElementSeq_TransferableHolder linkseq,
			MapPathElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetJMap(
			accessIdentity,
			map_id,
			imageseq,
			mapseq,
			kisseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int SaveMaps(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] imageseq,
			MapContext_Transferable[] mapseq,
			MapElement_Transferable[] equipmentseq,
			MapElement_Transferable[] kisseq,
			MapMarkElement_Transferable[] markseq,
			MapPhysicalNodeElement_Transferable[] nodeseq,
			MapNodeLinkElement_Transferable[] nodelinkseq,
			MapPhysicalLinkElement_Transferable[] linkseq,
			MapPathElement_Transferable[] pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveMaps(
			accessIdentity,
			imageseq,
			mapseq,
			equipmentseq,
			kisseq,
			markseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int RemoveMaps(
			AccessIdentity_Transferable accessIdentity,
			String[] mapseq,
			String[] equipmentseq,
			String[] kisseq,
			String[] markseq,
			String[] nodeseq,
			String[] nodelinkseq,
			String[] linkseq,
			String[] pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveMaps(
			accessIdentity,
			mapseq,
			equipmentseq,
			kisseq,
			markseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int SaveJMaps(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] imageseq,
			ISMMapContext_Transferable[] mapseq,
			MapKISElement_Transferable[] kisseq,
			MapPhysicalNodeElement_Transferable[] nodeseq,
			MapNodeLinkElement_Transferable[] nodelinkseq,
			MapPhysicalLinkElement_Transferable[] linkseq,
			MapPathElement_Transferable[] pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveJMaps(
			accessIdentity,
			imageseq,
			mapseq,
			kisseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int RemoveJMaps(
			AccessIdentity_Transferable accessIdentity,
			String[] mapseq,
			String[] kisseq,
			String[] nodeseq,
			String[] nodelinkseq,
			String[] linkseq,
			String[] pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveJMaps(
			accessIdentity,
			mapseq,
			kisseq,
			nodeseq,
			nodelinkseq,
			linkseq,
			pathseq);
	}

	public int GetMapProtoElements(
			AccessIdentity_Transferable accessIdentity,
			ImageResourceSeq_TransferableHolder imageseq,
			MapProtoGroupSeq_TransferableHolder groupseq,
			MapProtoElementSeq_TransferableHolder protoseq,
			MapLinkProtoElementSeq_TransferableHolder linkseq,
			MapPathProtoElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetMapProtoElements(
			accessIdentity,
			imageseq,
			groupseq,
			protoseq,
			linkseq,
			pathseq);
	}

	public int GetStatedMapProtoElements(
			AccessIdentity_Transferable accessIdentity,
			String[] group_ids,
			String[] element_ids,
			String[] link_ids,
			String[] path_ids,
			ImageResourceSeq_TransferableHolder imageseq,
			MapProtoGroupSeq_TransferableHolder groupseq,
			MapProtoElementSeq_TransferableHolder protoseq,
			MapLinkProtoElementSeq_TransferableHolder linkseq,
			MapPathProtoElementSeq_TransferableHolder pathseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedMapProtoElements(
			accessIdentity,
			group_ids,
			element_ids,
			link_ids,
			path_ids,
			imageseq,
			groupseq,
			protoseq,
			linkseq,
			pathseq);
	}

	public int SaveMapProtoElements(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] images,
			MapProtoGroup_Transferable[] groups,
			MapProtoElement_Transferable[] protos)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveMapProtoElements(
			accessIdentity,
			images,
			groups,
			protos);
	}

	public int RemoveMapProtoElements(
			AccessIdentity_Transferable accessIdentity,
			String[] group_ids,
			String[] proto_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveMapProtoElements(
			accessIdentity,
			group_ids,
			proto_ids);
	}

	public int GetSchemeProtoElements(
			AccessIdentity_Transferable accessIdentity,
			ImageResourceSeq_TransferableHolder imageseq,
			SchemeProtoElementSeq_TransferableHolder elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetSchemeProtoElements(
			accessIdentity,
			imageseq,
			elementseq);
	}

	public int GetStatedSchemeProtoElements(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ImageResourceSeq_TransferableHolder imageseq,
			SchemeProtoElementSeq_TransferableHolder elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedSchemeProtoElements(
			accessIdentity,
			ids,
			imageseq,
			elementseq);
	}

	public int SaveSchemeProtoElements(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] images,
			SchemeProtoElement_Transferable[] elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveSchemeProtoElements(
			accessIdentity,
			images,
			elementseq);
	}

	public int RemoveSchemeProtoElements(
			AccessIdentity_Transferable accessIdentity,
			String[] element_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveSchemeProtoElements(
			accessIdentity,
			element_ids);
	}

	public int GetSchemes(
			AccessIdentity_Transferable accessIdentity,
			ImageResourceSeq_TransferableHolder imageseq,
			SchemeSeq_TransferableHolder elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetSchemes(
			accessIdentity,
			imageseq,
			elementseq);
	}

	public int GetStatedSchemes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ImageResourceSeq_TransferableHolder imageseq,
			SchemeSeq_TransferableHolder elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedSchemes(
			accessIdentity,
			ids,
			imageseq,
			elementseq);
	}

	public int SaveSchemes(
			AccessIdentity_Transferable accessIdentity,
			ImageResource_Transferable[] images,
			Scheme_Transferable[] elementseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveSchemes(
			accessIdentity,
			images,
			elementseq);
	}

	public int RemoveSchemes(
			AccessIdentity_Transferable accessIdentity,
			String[] scheme_ids,
			String[] scheme_path_ids,
			String[] scheme_link_ids,
			String[] scheme_element_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveSchemes(
			accessIdentity,
			scheme_ids,
			scheme_path_ids,
			scheme_link_ids,
			scheme_element_ids);
	}

	public int LoadAttributeTypes(
			AccessIdentity_Transferable accessIdentity,
			ElementAttributeTypeSeq_TransferableHolder attrseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadAttributeTypes(
			accessIdentity,
			attrseq);
	}

	public int LoadStatedAttributeTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ElementAttributeTypeSeq_TransferableHolder attrseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadStatedAttributeTypes(
			accessIdentity,
			ids,
			attrseq);
	}

	public int ReloadAttributes(
			AccessIdentity_Transferable accessIdentity,
			String[] map_ids,
			ElementAttributeSeq_TransferableHolder attrseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.ReloadAttributes(
			accessIdentity,
			map_ids,
			attrseq);
	}

	public int ReloadISMAttributes(
			AccessIdentity_Transferable accessIdentity,
			String[] ism_map_ids,
			ElementAttributeSeq_TransferableHolder attrseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.ReloadISMAttributes(
			accessIdentity,
			ism_map_ids,
			attrseq);
	}

	public int GetAlarmTypes(
			AccessIdentity_Transferable accessIdentity,
			AlarmTypeSeq_TransferableHolder atseq)
		throws AMFICOMRemoteException
	{
		AlarmType_Transferable[] at_t = new AlarmType_Transferable[0];
		AlarmType[] ats;
		try
		{
			ats = AlarmType.retrieveAlarmTypes();
			at_t = new AlarmType_Transferable[ats.length];
			for(int i = 0; i < ats.length; i++)
				at_t[i] = ats[i].getTransferable();
		}
		catch (SQLException e)
		{
			String mesg = "Exception while retrieving alarm types: " + e.getMessage();
			System.out.println(mesg);
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
		}
		atseq.value = at_t;
		return Constants.ERROR_NO_ERROR;
	}

	public int GetAlarms(
			AccessIdentity_Transferable accessIdentity,
			AlarmSeq_TransferableHolder alarmseq,
			EventSourceSeq_TransferableHolder eventsourceseq,
			EventSeq_TransferableHolder eventseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAlarms(
				accessIdentity,
				alarmseq,
				eventsourceseq,
				eventseq);
	}

	public int GetStatedAlarms(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			AlarmSeq_TransferableHolder alarmseq,
			EventSourceSeq_TransferableHolder eventsourceseq,
			EventSeq_TransferableHolder eventseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedAlarms(
				accessIdentity,
				ids,
				alarmseq,
				eventsourceseq,
				eventseq);
	}

	public int GetStatedAlarmsFiltered(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
            Filter_Transferable filter,
			AlarmSeq_TransferableHolder alarmseq,
			EventSourceSeq_TransferableHolder eventsourceseq,
			EventSeq_TransferableHolder eventseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedAlarmsFiltered(
				accessIdentity,
				ids,
                filter,
				alarmseq,
				eventsourceseq,
				eventseq);
	}

	public int GetMessages(
			AccessIdentity_Transferable accessIdentity,
			MessageSeq_TransferableHolder messageseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetMessages(
				accessIdentity,
				messageseq);
	}

	public int SetAlarm(
			AccessIdentity_Transferable accessIdentity,
			Alarm_Transferable alarm)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SetAlarm(
				accessIdentity,
				alarm);
	}

	public int DeleteAlarm(
			AccessIdentity_Transferable accessIdentity,
			String alarm_id)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.DeleteAlarm(
				accessIdentity,
				alarm_id);
	}

	/**
	 * @param accessIdentity
	 * @param sourceId
	 * @param descriptor
	 * @return 
	 * @throws AMFICOMRemoteException
	 */
	public int SetUserAlarm(AccessIdentity_Transferable accessIdentity, String sourceId, String descriptor) throws AMFICOMRemoteException {
		SurveydbInterfaceSave.setUserAlarm(accessIdentity, sourceId, descriptor);
		return Constants.ERROR_NO_ERROR;
	}

	public int GetAlarmIdsForMonitoredElement(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ResourceDescriptorSeq_TransferableHolder alarmids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAlarmIdsForMonitoredElement(
			accessIdentity,
			me_id,
			alarmids);
	}

	public int LoadMaintenanceData(
			AccessIdentity_Transferable accessIdentity,
			EventSourceTypeSeq_TransferableHolder est,
			AlertingMessageSeq_TransferableHolder am,
			AlertingMessageUserSeq_TransferableHolder amu)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadMaintenanceData(accessIdentity, est, am, amu);
	}

	public int SaveMaintenanceData(
			AccessIdentity_Transferable accessIdentity,
			AlertingMessage_Transferable []am,
			AlertingMessageUser_Transferable []amu)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveMaintenanceData(accessIdentity, am, amu);
	}

	public int RemoveMaintenanceData(
			AccessIdentity_Transferable accessIdentity,
			String amu_id)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveMaintenanceData(accessIdentity, amu_id);
	}

	public int GetUId(
			AccessIdentity_Transferable accessIdentity,
			String type,
			StringHolder id)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetUId(accessIdentity, type, id);
	}

	public int GetResourceDescriptors(
			AccessIdentity_Transferable accessIdentity,
			String type,
			ResourceDescriptorSeq_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResourceDescriptors(accessIdentity, type, desc);
	}

	public int GetResourceDescriptor(
			AccessIdentity_Transferable accessIdentity,
			String type,
			String id,
			ResourceDescriptor_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResourceDescriptor(accessIdentity, type, id, desc);
	}

	public int GetResourceDescriptorsByIds(
			AccessIdentity_Transferable accessIdentity,
			String type,
			String[] ids,
			ResourceDescriptorSeq_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResourceDescriptorsByIds(accessIdentity, type, ids, desc);
	}

	public int GetResultDescriptorsByIds(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ResourceDescriptorSeq_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResultDescriptorsByIds(accessIdentity, ids, desc);
	}

	public int GetDomainResourceDescriptors(
			AccessIdentity_Transferable accessIdentity,
			String type,
			ResourceDescriptorSeq_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetDomainResourceDescriptors(accessIdentity, type, desc);
	}

	public int GetImages(AccessIdentity_Transferable accessIdentity, String ids[], ImageResourceSeq_TransferableHolder imgs) throws AMFICOMRemoteException {
		try {
			AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
			Collection images = ResourcedbInterface.getImages(CONN, Arrays.asList(ids));
			imgs.value = (ImageResource_Transferable[]) (images.toArray(new ImageResource_Transferable[images.size()]));
			return Constants.ERROR_NO_ERROR;
		} catch (AMFICOMRemoteException are) {
			are.printStackTrace();
			throw are;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AMFICOMRemoteException(Constants.ERROR_RISD_ERROR, e.toString());
		}
	}

	public int LoadNetDirectory(
			AccessIdentity_Transferable accessIdentity,
			PortTypeSeq_TransferableHolder porttypes,
			EquipmentTypeSeq_TransferableHolder equipmenttypes,
			LinkTypeSeq_TransferableHolder linktypes,
			TestPortTypeSeq_TransferableHolder tporttypes,
			CharacteristicTypeSeq_TransferableHolder characteristictypes,
			CablePortTypeSeq_TransferableHolder cableporttypes,
			CableLinkTypeSeq_TransferableHolder cablelinktypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadNetDirectory(
			accessIdentity,
			porttypes,
			equipmenttypes,
			linktypes,
			tporttypes,
			characteristictypes,
			cableporttypes,
			cablelinktypes);
	}

	public int LoadISMDirectory(
			AccessIdentity_Transferable accessIdentity,
			EquipmentTypeSeq_TransferableHolder kistypes,
			AccessPortTypeSeq_TransferableHolder aporttypes,
			TransmissionPathTypeSeq_TransferableHolder pathtypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadISMDirectory(
			accessIdentity,
			kistypes,
			aporttypes,
			pathtypes);
	}

	public int LoadStatedNetDirectory(
			AccessIdentity_Transferable accessIdentity,
			String[] pt_ids,
			String[] eqt_ids,
			String[] lt_ids,
			String[] cht_ids,
			String[] cpt_ids,
			String[] clt_ids,
			PortTypeSeq_TransferableHolder porttypes,
			EquipmentTypeSeq_TransferableHolder equipmenttypes,
			LinkTypeSeq_TransferableHolder linktypes,
			TestPortTypeSeq_TransferableHolder tporttypes,
			CharacteristicTypeSeq_TransferableHolder characteristictypes,
			CablePortTypeSeq_TransferableHolder cableporttypes,
			CableLinkTypeSeq_TransferableHolder cablelinktypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadStatedNetDirectory(
			accessIdentity,
			pt_ids,
			eqt_ids,
			lt_ids,
			cht_ids,
			cpt_ids,
			clt_ids,
			porttypes,
			equipmenttypes,
			linktypes,
			tporttypes,
			characteristictypes,
			cableporttypes,
			cablelinktypes);
	}

	public int LoadStatedISMDirectory(
			AccessIdentity_Transferable accessIdentity,
			String[] kis_ids,
			String[] aport_ids,
			String[] path_ids,
			EquipmentTypeSeq_TransferableHolder kistypes,
			AccessPortTypeSeq_TransferableHolder aporttypes,
			TransmissionPathTypeSeq_TransferableHolder pathtypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadStatedISMDirectory(
			accessIdentity,
			kis_ids,
			aport_ids,
			path_ids,
			kistypes,
			aporttypes,
			pathtypes);
	}

	public int RemoveNetDirectory(
			AccessIdentity_Transferable accessIdentity,
			String[] pt_ids,
			String[] cpt_ids,
			String[] eqt_ids,
			String[] lt_ids,
			String[] clt_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveNetDirectory(
			accessIdentity,
			pt_ids,
			cpt_ids,
			eqt_ids,
			lt_ids,
			clt_ids);
	}

	public int RemoveISMDirectory(
			AccessIdentity_Transferable accessIdentity,
			String[] kst_ids,
			String[] apt_ids,
			String[] tpt_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveISMDirectory(
			accessIdentity,
			kst_ids,
			apt_ids,
			tpt_ids);
	}

	public int SaveNetDirectory(
			AccessIdentity_Transferable accessIdentity,
			PortType_Transferable[] porttypes,
			EquipmentType_Transferable[] equipmenttypes,
			LinkType_Transferable[] linktypes,
			TestPortType_Transferable[] tporttypes,
			CharacteristicType_Transferable[] characteristictypes,
			CablePortType_Transferable[] cableporttypes,
			CableLinkType_Transferable[] cablelinktypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveNetDirectory(
			accessIdentity,
			porttypes,
			equipmenttypes,
			linktypes,
			tporttypes,
			characteristictypes,
			cableporttypes,
			cablelinktypes);
	}

	public int SaveISMDirectory(
			AccessIdentity_Transferable accessIdentity,
			EquipmentType_Transferable[] kistypes,
			AccessPortType_Transferable[] aporttypes,
			TransmissionPathType_Transferable[] pathtypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveISMDirectory(
			accessIdentity,
			kistypes,
			aporttypes,
			pathtypes);
	}

	public int LoadNet(
			AccessIdentity_Transferable accessIdentity,
			PortSeq_TransferableHolder ports,
			CablePortSeq_TransferableHolder cports,
			EquipmentSeq_TransferableHolder equipments,
			LinkSeq_TransferableHolder links,
			CableLinkSeq_TransferableHolder clinks,
			TestPortSeq_TransferableHolder testports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadNet(
			accessIdentity,
			ports,
			cports,
			equipments,
			links,
			clinks,
			testports);
	}

	public int LoadStatedNet(
			AccessIdentity_Transferable accessIdentity,
			String[] p_ids,
			String[] cp_ids,
			String[] eq_ids,
			String[] l_ids,
			String[] cl_ids,
			PortSeq_TransferableHolder ports,
			CablePortSeq_TransferableHolder cports,
			EquipmentSeq_TransferableHolder equipments,
			LinkSeq_TransferableHolder links,
			CableLinkSeq_TransferableHolder clinks,
			TestPortSeq_TransferableHolder testports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadStatedNet(
			accessIdentity,
			p_ids,
			cp_ids,
			eq_ids,
			l_ids,
			cl_ids,
			ports,
			cports,
			equipments,
			links,
			clinks,
			testports);
	}

	public int LoadISM(
			AccessIdentity_Transferable accessIdentity,
			PortSeq_TransferableHolder ports,
			CablePortSeq_TransferableHolder cports,
			EquipmentSeq_TransferableHolder kiss,
			LinkSeq_TransferableHolder links,
			CableLinkSeq_TransferableHolder clinks,
			MonitoredElementSeq_TransferableHolder mes,
			TransmissionPathSeq_TransferableHolder paths,
			AccessPortSeq_TransferableHolder accessports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadISM(
			accessIdentity,
			ports,
			cports,
			kiss,
			links,
			clinks,
			mes,
			paths,
			accessports);
	}

	public int LoadStatedISM(
			AccessIdentity_Transferable accessIdentity,
			String[] p_ids,
			String[] cp_ids,
			String[] k_ids,
			String[] l_ids,
			String[] cl_ids,
			String[] me_ids,
			String[] t_ids,
			String[] ap_ids,
			PortSeq_TransferableHolder ports,
			CablePortSeq_TransferableHolder cports,
			EquipmentSeq_TransferableHolder kiss,
			LinkSeq_TransferableHolder links,
			CableLinkSeq_TransferableHolder clinks,
			MonitoredElementSeq_TransferableHolder mes,
			TransmissionPathSeq_TransferableHolder paths,
			AccessPortSeq_TransferableHolder accessports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadStatedISM(
			accessIdentity,
			p_ids,
			cp_ids,
			k_ids,
			l_ids,
			cl_ids,
			me_ids,
			t_ids,
			ap_ids,
			ports,
			cports,
			kiss,
			links,
			clinks,
			mes,
			paths,
			accessports);
	}

	public int SaveNet(
			AccessIdentity_Transferable accessIdentity,
			Port_Transferable[] ports,
			CablePort_Transferable[] cports,
			Equipment_Transferable[] equipments,
			Link_Transferable[] links,
			CableLink_Transferable[] clinks,
			TestPort_Transferable[] testports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveNet(
			accessIdentity,
			ports,
			cports,
			equipments,
			links,
			clinks,
			testports);
	}

	public int SaveISM(
			AccessIdentity_Transferable accessIdentity,
			Port_Transferable[] ports,
			CablePort_Transferable[] cports,
			Equipment_Transferable[] kiss,
			Link_Transferable[] links,
			CableLink_Transferable[] clinks,
			MonitoredElement_Transferable[] mes,
			TransmissionPath_Transferable[] paths,
			AccessPort_Transferable[] accessports)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveISM(
			accessIdentity,
			ports,
			cports,
			kiss,
			links,
			clinks,
			mes,
			paths,
			accessports);
	}

	public int RemoveNet(
			AccessIdentity_Transferable accessIdentity,
			String[] p_ids,
			String[] cp_ids,
			String[] eq_ids,
			String[] l_ids,
			String[] cl_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveNet(
			accessIdentity,
			p_ids,
			cp_ids,
			eq_ids,
			l_ids,
			cl_ids);
	}

	public int RemoveISM(
			AccessIdentity_Transferable accessIdentity,
			String[] p_ids,
			String[] cp_ids,
			String[] ks_ids,
			String[] l_ids,
			String[] cl_ids,
			String[] me_ids,
			String[] t_ids,
			String[] ap_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveISM(
			accessIdentity,
			p_ids,
			cp_ids,
			ks_ids,
			l_ids,
			cl_ids,
			me_ids,
			t_ids,
			ap_ids);
	}

	public int GetResults(
			AccessIdentity_Transferable accessIdentity,
			ClientResultSeq_TransferableHolder resultseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResults(
			accessIdentity,
			resultseq);
	}

	public int GetStatedResults(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientResultSeq_TransferableHolder resultseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedResults(
			accessIdentity,
			ids,
			resultseq);
	}

	public int GetRequests(
			AccessIdentity_Transferable accessIdentity,
			ClientTestRequestSeq_TransferableHolder treqseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetRequests(
			accessIdentity,
			treqseq);
	}

	public int GetTests(
			AccessIdentity_Transferable accessIdentity,
			ClientTestSeq_TransferableHolder testseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetTests(
			accessIdentity,
			testseq);
	}

	public int GetStatedTests(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientTestSeq_TransferableHolder testseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedTests(
			accessIdentity,
			ids,
			testseq);
	}

	public int GetTestIdsInDiapazon(
			AccessIdentity_Transferable accessIdentity,
			long start_time,
			long end_time,
			ResourceDescriptorSeq_TransferableHolder desc)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetTestIdsInDiapazon(
			accessIdentity,
			start_time,
			end_time,
			desc);
	}

	public int GetTestIdsForMonitoredElement(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ResourceDescriptorSeq_TransferableHolder testids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetTestIdsForMonitoredElement(
			accessIdentity,
			me_id,
			testids);
	}

	public int GetAnalysis(
			AccessIdentity_Transferable accessIdentity,
			ClientAnalysisSeq_TransferableHolder analysisseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAnalysis(
			accessIdentity,
			analysisseq);
	}

	public int GetAnalysisIdsForMonitoredElement(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ResourceDescriptorSeq_TransferableHolder analysisids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAnalysisIdsForMonitoredElement(
			accessIdentity,
			me_id,
			analysisids);
	}

	public int GetModelings(
			AccessIdentity_Transferable accessIdentity,
			ClientModelingSeq_TransferableHolder modelingseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetModelings(
			accessIdentity,
			modelingseq);
	}

	public int GetModelingIdsForSchemePath(
			AccessIdentity_Transferable accessIdentity,
			String scheme_path_id,
			ResourceDescriptorSeq_TransferableHolder modelingids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetModelingIdsForSchemePath(
			accessIdentity,
			scheme_path_id,
			modelingids);
	}

	public int GetEvaluations(
			AccessIdentity_Transferable accessIdentity,
			ClientEvaluationSeq_TransferableHolder evaluationseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetEvaluations(
			accessIdentity,
			evaluationseq);
	}

	public int GetEvaluationIdsForMonitoredElement(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ResourceDescriptorSeq_TransferableHolder evaluationids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetEvaluationIdsForMonitoredElement(
			accessIdentity,
			me_id,
			evaluationids);
	}

	public int GetRequestTests(
			AccessIdentity_Transferable accessIdentity,
			String request_id,
			ClientTestSeq_TransferableHolder testseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetRequestTests(
			accessIdentity,
			request_id,
			testseq);
	}

	/**
	 * @param accessIdentity
	 * @param resourceDescriptorSeq
	 * @return 
	 * @throws AMFICOMRemoteException
	 */
	public int GetAlarmedTests(AccessIdentity_Transferable accessIdentity, ResourceDescriptorSeq_TransferableHolder resourceDescriptorSeq) throws AMFICOMRemoteException {
		TestDatadbInterfaceLoad.getAlarmedTests(accessIdentity, resourceDescriptorSeq);
		return Constants.ERROR_NO_ERROR;
	}

	/**
	 * @param accessIdentity
	 * @param clientTestRequest
	 * @param clientTestSeq
	 * @return 
	 * @throws AMFICOMRemoteException
	 */
	public int RequestTest(AccessIdentity_Transferable accessIdentity, ClientTestRequest_Transferable clientTestRequest, ClientTest_Transferable[] clientTestSeq) throws AMFICOMRemoteException {
		TestDatadbInterfaceSave.requestTest(accessIdentity, clientTestRequest, clientTestSeq);
		return Constants.ERROR_NO_ERROR;
	}

	public int QueryResource(
			AccessIdentity_Transferable accessIdentity,
			String parameter_id,
			String kis_id,
			String parameter_type_id)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.QueryResource(
			accessIdentity,
			parameter_id,
			kis_id,
			parameter_type_id);
	}

	public int RemoveTests(
			AccessIdentity_Transferable accessIdentity,
			String[] testids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.RemoveTests(
			accessIdentity,
			testids);
	}

	public int UpdateTests(
			AccessIdentity_Transferable accessIdentity,
			ClientTest_Transferable[] testseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.UpdateTests(
			accessIdentity,
			testseq);
	}

	public int GetLastResult(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ClientResult_TransferableHolder result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetLastResult(
			accessIdentity,
			me_id,
			result);
	}

	public int GetTestResults(
			AccessIdentity_Transferable accessIdentity,
			String test_id,
			ClientResultSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetTestResults(
			accessIdentity,
			test_id,
			results);
	}

	public int GetAnalysisResults(
			AccessIdentity_Transferable accessIdentity,
			String analysis_id,
			ClientResultSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAnalysisResults(
			accessIdentity,
			analysis_id,
			results);
	}

	public ClientResult_Transferable[] GetStatisticsAnalysisResults(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			long start_time,
			long end_time)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatisticsAnalysisResults(
			accessIdentity,
			monitored_element_id,
			start_time,
			end_time);
	}

	public ClientResult_Transferable[] GetStatisticsAnalysisResultsByTS(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			long start_time,
			long end_time,
			String test_setup_id)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatisticsAnalysisResultsByTS(
			accessIdentity,
			monitored_element_id,
			start_time,
			end_time,
			test_setup_id);
	}

	public int GetModelingResult(
			AccessIdentity_Transferable accessIdentity,
			String modeling_id,
			ClientResult_TransferableHolder result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetModelingResult(
			accessIdentity,
			modeling_id,
			result);
	}

	public int GetEvaluationResults(
			AccessIdentity_Transferable accessIdentity,
			String evaluation_id,
			ClientResultSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetEvaluationResults(
			accessIdentity,
			evaluation_id,
			results);
	}

	public int GetLastResultId(
			AccessIdentity_Transferable accessIdentity,
			String me_id,
			ResourceDescriptor_TransferableHolder result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetLastResultId(
			accessIdentity,
			me_id,
			result);
	}

	public int GetTestResultIds(
			AccessIdentity_Transferable accessIdentity,
			String test_id,
			ResourceDescriptorSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetTestResultIds(
			accessIdentity,
			test_id,
			results);
	}

	public int GetAnalysisResultIds(
			AccessIdentity_Transferable accessIdentity,
			String analysis_id,
			ResourceDescriptorSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetAnalysisResultIds(
			accessIdentity,
			analysis_id,
			results);
	}

	public int GetModelingResultId(
			AccessIdentity_Transferable accessIdentity,
			String modeling_id,
			ResourceDescriptor_TransferableHolder result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetModelingResultId(
			accessIdentity,
			modeling_id,
			result);
	}

	public int GetEvaluationResultIds(
			AccessIdentity_Transferable accessIdentity,
			String evaluation_id,
			ResourceDescriptorSeq_TransferableHolder results)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetEvaluationResultIds(
			accessIdentity,
			evaluation_id,
			results);
	}

	public int GetResult(
			AccessIdentity_Transferable accessIdentity,
			String result_id,
			ClientResult_TransferableHolder result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResult(
			accessIdentity,
			result_id,
			result);
	}

	public int SaveAnalysis(
			AccessIdentity_Transferable accessIdentity,
			ClientAnalysis_Transferable analysis,
			ClientResult_Transferable result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveAnalysis(
			accessIdentity,
			analysis,
			result);
	}

	public int SaveModeling(
			AccessIdentity_Transferable accessIdentity,
			ClientModeling_Transferable modeling,
			ClientResult_Transferable result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveModeling(
			accessIdentity,
			modeling,
			result);
	}

	public int SaveEvaluation(
			AccessIdentity_Transferable accessIdentity,
			ClientEvaluation_Transferable evaluation,
			ClientResult_Transferable result)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.SaveEvaluation(
			accessIdentity,
			evaluation,
			result);
	}

	public int GetResultSets(
			AccessIdentity_Transferable accessIdentity,
			ResultSetSeq_TransferableHolder resultsets)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResultSets(
			accessIdentity,
			resultsets);
	}

	public int GetStatedResultSets(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ResultSetSeq_TransferableHolder resultsets)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetStatedResultSets(
			accessIdentity,
			ids,
			resultsets);
	}

	public int GetResultSetResultIds(
			AccessIdentity_Transferable accessIdentity,
			String result_id,
			ResourceDescriptorSeq_TransferableHolder result_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResultSetResultIds(
			accessIdentity,
			result_id,
			result_ids);
	}

	public int GetResultSetResultMEIds(
			AccessIdentity_Transferable accessIdentity,
			String result_id,
			String me_id,
			ResourceDescriptorSeq_TransferableHolder result_ids)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.GetResultSetResultMEIds(
			accessIdentity,
			result_id,
			me_id,
			result_ids);
	}

	public int LoadGlobalParameterTypes(
			AccessIdentity_Transferable accessIdentity,
			GlobalParameterTypeSeq_TransferableHolder params)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadGlobalParameterTypes(
			accessIdentity,
			params);
	}

	public int LoadStatedGlobalParameterTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			GlobalParameterTypeSeq_TransferableHolder params)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadGlobalParameterTypes(
			accessIdentity,
			ids,
			params);
	}

	public int LoadTestTypes(
			AccessIdentity_Transferable accessIdentity,
			TestTypeSeq_TransferableHolder ttypeseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadTestTypes(
			accessIdentity,
			ttypeseq);
	}

	public int LoadStatedTestTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			TestTypeSeq_TransferableHolder ttypeseq)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadTestTypes(
			accessIdentity,
			ids,
			ttypeseq);
	}

	public int LoadAnalysisTypes(
			AccessIdentity_Transferable accessIdentity,
			AnalysisTypeSeq_TransferableHolder atypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadAnalysisTypes(
			accessIdentity,
			atypes);
	}

	public int LoadStatedAnalysisTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			AnalysisTypeSeq_TransferableHolder atypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadAnalysisTypes(
			accessIdentity,
			ids,
			atypes);
	}

	public int LoadEvaluationTypes(
			AccessIdentity_Transferable accessIdentity,
			EvaluationTypeSeq_TransferableHolder etypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadEvaluationTypes(
			accessIdentity,
			etypes);
	}

	public int LoadStatedEvaluationTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			EvaluationTypeSeq_TransferableHolder etypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadEvaluationTypes(
			accessIdentity,
			ids,
			etypes);
	}

	public int LoadModelingTypes(
			AccessIdentity_Transferable accessIdentity,
			ModelingTypeSeq_TransferableHolder mtypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadModelingTypes(
			accessIdentity,
			mtypes);
	}

	public int LoadStatedModelingTypes(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ModelingTypeSeq_TransferableHolder mtypes)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.LoadModelingTypes(
			accessIdentity,
			ids,
			mtypes);
	}

	public int LoadCriteriaSets(
			AccessIdentity_Transferable accessIdentity,
			ClientCriteriaSetSeq_TransferableHolder css)
		throws AMFICOMRemoteException
	{
		return Constants.ERROR_NO_ERROR;
	}

	public int LoadStatedCriteriaSets(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientCriteriaSetSeq_TransferableHolder css)
		throws AMFICOMRemoteException
	{
		Vector vec = new Vector();
		for(int i = 0; i < ids.length; i++)
			vec.add(this.getCriteriaSet(accessIdentity, ids[i]));
		css.value = new ClientCriteriaSet_Transferable[vec.size()];
		vec.copyInto(css.value);
		return Constants.ERROR_NO_ERROR;
	}

	public int LoadThresholdSets(
			AccessIdentity_Transferable accessIdentity,
			ClientThresholdSetSeq_TransferableHolder tss)
		throws AMFICOMRemoteException
	{
		return Constants.ERROR_NO_ERROR;
	}

	public int LoadStatedThresholdSets(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientThresholdSetSeq_TransferableHolder tss)
		throws AMFICOMRemoteException
	{
		return Constants.ERROR_NO_ERROR;
	}

	public int LoadEtalons(
			AccessIdentity_Transferable accessIdentity,
			ClientEtalonSeq_TransferableHolder ets)
		throws AMFICOMRemoteException
	{
		return Constants.ERROR_NO_ERROR;
	}

	public AnalysisType_Transferable[] getAnalysisTypes(
			AccessIdentity_Transferable accessIdentity,
			String test_type_id)
		throws AMFICOMRemoteException
	{
	AnalysisType_Transferable[] an_types_t = new AnalysisType_Transferable[0];
	return an_types_t;
  }

  public String createAnalysisType(
			AccessIdentity_Transferable accessIdentity,
			AnalysisType_Transferable analysis_type_t)
		throws AMFICOMRemoteException
  {
  AnalysisType at = null;
	try {
	  at = new AnalysisType(analysis_type_t);
	}
	catch (SQLException e) {
	  String mesg = "Exception while creating analysis type '" + analysis_type_t.name + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return at.getTransferable().id;
  }

  public ClientTest_Transferable getTestByAnalysis(
			AccessIdentity_Transferable accessIdentity,
			String analysis_id)
		throws AMFICOMRemoteException
  {
	ClientTest_Transferable test_t;
	Analysis anal;
	try {
	  anal = new Analysis(analysis_id);
	  test_t = anal.getTest().getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test for analysis id '" + analysis_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_t;
  }

  public ResourceDescriptor_Transferable getTestIdByAnalysis(
			AccessIdentity_Transferable accessIdentity,
			String analysis_id)
		throws AMFICOMRemoteException
  {
	ClientTest_Transferable test_t;
	Analysis anal;
	try {
	  anal = new Analysis(analysis_id);
	  test_t = anal.getTest().getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test for analysis id '" + analysis_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return new ResourceDescriptor_Transferable(test_t.id, test_t.modified);
  }

  public ClientTest_Transferable getTestByEvaluation(
			AccessIdentity_Transferable accessIdentity,
			String evaluation_id)
		throws AMFICOMRemoteException
  {
	ClientTest_Transferable test_t;
	Evaluation eval;
	try {
	  eval = new Evaluation(evaluation_id);
	  test_t = eval.getTest().getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test for evaluation id '" + evaluation_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_t;
  }

  public ResourceDescriptor_Transferable getTestIdByEvaluation(
			AccessIdentity_Transferable accessIdentity,
			String evaluation_id)
		throws AMFICOMRemoteException
  {
	ClientTest_Transferable test_t;
	Evaluation eval;
	try {
	  eval = new Evaluation(evaluation_id);
	  test_t = eval.getTest().getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test for evaluation id '" + evaluation_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return new ResourceDescriptor_Transferable(test_t.id, test_t.modified);
  }

  public String createAnalysis(
			AccessIdentity_Transferable accessIdentity,
			ClientAnalysis_Transferable analysis_t)
		throws AMFICOMRemoteException
  {
	Analysis a = null;
	try {
	  	a = new Analysis(analysis_t);
		return a.getId();
	}
	catch (Exception e) {
	  String mesg = "Exception while creating analysis '" + analysis_t.name + "' of type '" + analysis_t.type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientAnalysis_Transferable getAnalysisById(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientAnalysis_Transferable analysis_t;
	try {
	  analysis_t = (new Analysis(id)).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving analysis '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return analysis_t;
  }

  public int LoadStatedAnalysis(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientAnalysisSeq_TransferableHolder as)
		throws AMFICOMRemoteException
  {
	ClientAnalysis_Transferable analysis_t;
	Vector loaded = new Vector();
	for(int i = 0; i < ids.length; i++)
	{
		try {
		  analysis_t = (new Analysis(ids[i])).getClientTransferable();
		  loaded.add(analysis_t);
		}
		catch (SQLException e)
		{
		  String mesg = "Exception while retrieving analysis '" + ids[i] + "': " + e.getMessage();
		  System.out.println(mesg);
		  e.printStackTrace();
		}
	}
	as.value = new ClientAnalysis_Transferable[loaded.size()];
	loaded.copyInto(as.value);
	return Constants.ERROR_NO_ERROR;
  }

  public ClientAnalysis_Transferable getAnalysisByTest(
			AccessIdentity_Transferable accessIdentity,
			String test_id)
		throws AMFICOMRemoteException
  {
	ClientAnalysis_Transferable analysis_t;
	try {
	  analysis_t = Analysis.retrieveAnalysis(test_id).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving analysis for test id '" + test_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return analysis_t;
  }

  public ResourceDescriptor_Transferable getAnalysisIdByTest(
			AccessIdentity_Transferable accessIdentity,
			String test_id)
		throws AMFICOMRemoteException
  {
	ClientAnalysis_Transferable analysis_t;
	try {
	  analysis_t = Analysis.retrieveAnalysis(test_id).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving analysis for test id '" + test_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return new ResourceDescriptor_Transferable(analysis_t.id, analysis_t.modified);
  }

  public String createEvaluation(
			AccessIdentity_Transferable accessIdentity,
			ClientEvaluation_Transferable evaluation_t)
		throws AMFICOMRemoteException
  {
	Evaluation a = null;
	try {
		a = new Evaluation(evaluation_t);
		return a.getId();
	}
	catch (Exception e) {
	  String mesg = "Exception while creating evaluation '" + evaluation_t.name + "' of type '" + evaluation_t.type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientEvaluation_Transferable getEvaluationById(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientEvaluation_Transferable evaluation_t;
	try {
	  evaluation_t = (new Evaluation(id)).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving evaluation '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return evaluation_t;
  }

  public int LoadStatedEvaluations(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientEvaluationSeq_TransferableHolder es)
		throws AMFICOMRemoteException
  {
	ClientEvaluation_Transferable evaluation_t;
	Vector loaded = new Vector();
	for(int i = 0; i < ids.length; i++)
	{
		try {
		  evaluation_t = (new Evaluation(ids[i])).getClientTransferable();
		  loaded.add(evaluation_t);
		}
		catch (SQLException e)
		{
		  String mesg = "Exception while retrieving evaluation '" + ids[i] + "': " + e.getMessage();
		  System.out.println(mesg);
		  e.printStackTrace();
		}
	}
	es.value = new ClientEvaluation_Transferable[loaded.size()];
	loaded.copyInto(es.value);
	return Constants.ERROR_NO_ERROR;
  }

  public ClientEvaluation_Transferable getEvaluationByTest(
			AccessIdentity_Transferable accessIdentity,
			String test_id)
		throws AMFICOMRemoteException
  {
	ClientEvaluation_Transferable evaluation_t;
	try {
	  evaluation_t = Evaluation.retrieveEvaluation(test_id).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving evaluation for test id '" + test_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return evaluation_t;
  }

  public ResourceDescriptor_Transferable getEvaluationIdByTest(
			AccessIdentity_Transferable accessIdentity,
			String test_id)
		throws AMFICOMRemoteException
  {
	ClientEvaluation_Transferable evaluation_t;
	try {
	  evaluation_t = Evaluation.retrieveEvaluation(test_id).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving evaluation for test id '" + test_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return new ResourceDescriptor_Transferable(evaluation_t.id, evaluation_t.modified);
  }

  public ClientModeling_Transferable getModelingById(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientModeling_Transferable modeling_t;
	try {
	  modeling_t = (new Modeling(id)).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving modeling '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
	}
	return modeling_t;
  }

  public int LoadStatedModelings(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ClientModelingSeq_TransferableHolder ms)
		throws AMFICOMRemoteException
  {
	ClientModeling_Transferable modeling_t;
	Vector loaded = new Vector();
	for(int i = 0; i < ids.length; i++)
	{
		try {
		  modeling_t = (new Modeling(ids[i])).getClientTransferable();
		  loaded.add(modeling_t);
		}
		catch (SQLException e)
		{
		  String mesg = "Exception while retrieving modeling '" + ids[i] + "': " + e.getMessage();
		  System.out.println(mesg);
		  e.printStackTrace();
		}
	}
	ms.value = new ClientModeling_Transferable[loaded.size()];
	loaded.copyInto(ms.value);
	return Constants.ERROR_NO_ERROR;
  }

  public String createTestSetup(
			AccessIdentity_Transferable accessIdentity,
			TestSetup_Transferable test_setup_t)
		throws AMFICOMRemoteException
  {
	return AMFICOMdbInterface.createTestSetup(accessIdentity, test_setup_t);
  }

  public void attachTestSetupToME(
			AccessIdentity_Transferable accessIdentity,
			String test_setup_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		TestSetup testSetup = new TestSetup(test_setup_id);
	  testSetup.attachToMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while attaching TestSetup id '" + test_setup_id + "' tto monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public void detachTestSetupFromME(
			AccessIdentity_Transferable accessIdentity,
			String test_setup_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		TestSetup testSetup = new TestSetup(test_setup_id);
	  testSetup.detachFromMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while detaching TestSetup id '" + test_setup_id + "' from monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public TestSetup_Transferable getTestSetup(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	TestSetup_Transferable test_setup_t;
	try {
	  test_setup_t = (new TestSetup(id)).getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving TestSetup id '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setup_t;
  }

  public TestSetup_Transferable[] getTestSetupsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	TestSetup_Transferable[] test_setups_t = new TestSetup_Transferable[0];
	TestSetup[] ts;
	try {
	  ts = TestSetup.retrieveTestSetups(monitored_element_id);
	  test_setups_t = new TestSetup_Transferable[ts.length];
	  for(int i = 0; i < ts.length; i++)
		test_setups_t[i] = ts[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving TestSetup for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setups_t;
  }

  public TestSetup_Transferable[] getTestSetupsByTestType(
			AccessIdentity_Transferable accessIdentity,
			String test_type_id)
		throws AMFICOMRemoteException
  {
	TestSetup_Transferable[] test_setups_t = new TestSetup_Transferable[0];
	TestSetup[] ts;
	try {
	  ts = TestSetup.retrieveTestSetupsForTestType(test_type_id);
	  test_setups_t = new TestSetup_Transferable[ts.length];
	  for(int i = 0; i < ts.length; i++)
		test_setups_t[i] = ts[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test setups for test_type_id '" + test_type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setups_t;
  }

  public String[] getTestSetupIdsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	String[] test_setup_ids = new String[0];
	TestSetup[] ts;
	try {
	  ts = TestSetup.retrieveTestSetups(monitored_element_id);
	  test_setup_ids = new String[ts.length];
	  for(int i = 0; i < ts.length; i++)
		test_setup_ids[i] = ts[i].getId();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving TestSetupIds for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setup_ids;
  }

  public String[] getTestSetupIdsByTestType(
			AccessIdentity_Transferable accessIdentity,
			String test_type_id)
		throws AMFICOMRemoteException
  {
	String[] test_setup_ids = new String[0];
	TestSetup[] ts;
	try {
	  ts = TestSetup.retrieveTestSetupsForTestType(test_type_id);
	  test_setup_ids = new String[ts.length];
	  for(int i = 0; i < ts.length; i++)
		test_setup_ids[i] = ts[i].getId();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test setup ids for test_type_id '" + test_type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setup_ids;
  }

  public TestSetup_Transferable[] getTestSetupsByTestTypeAndME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			String test_type_id)
		throws AMFICOMRemoteException
  {
	TestSetup_Transferable[] test_setups_t = new TestSetup_Transferable[0];
	TestSetup[] ts;
	try {
	  ts = TestSetup.retrieveTestSetups(monitored_element_id);
	  test_setups_t = new TestSetup_Transferable[ts.length];
	  for(int i = 0; i < ts.length; i++)
		test_setups_t[i] = ts[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving test setups for monitored_element_id '" + monitored_element_id + "', test_type_id '" + test_type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return test_setups_t;
  }

  public String createTestArgumentSet(
			AccessIdentity_Transferable accessIdentity,
			ClientTestArgumentSet_Transferable arg_set_t)
		throws AMFICOMRemoteException
  {
	return AMFICOMdbInterface.createTestArgumentSet(accessIdentity, arg_set_t);
  }

  public void attachTestArgumentSetToME(
			AccessIdentity_Transferable accessIdentity,
			String arg_set_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		TestArgumentSet testArgumentSet = new TestArgumentSet(arg_set_id);
	  testArgumentSet.attachToMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while attaching TestArgument set id '" + arg_set_id + "' tto monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientTestArgumentSet_Transferable getTestArgumentSet(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientTestArgumentSet_Transferable arg_set_t;
	try {
	  arg_set_t = (new TestArgumentSet(id)).getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving TestArgument set id '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return arg_set_t;
  }

  public ClientTestArgumentSet_Transferable[] getTestArgumentSetsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	ClientTestArgumentSet_Transferable[] arg_sets_t = new ClientTestArgumentSet_Transferable[0];
	TestArgumentSet[] css;
	try {
	  css = TestArgumentSet.retrieveTestArgumentSets(monitored_element_id);
	  arg_sets_t = new ClientTestArgumentSet_Transferable[css.length];
	  for(int i = 0; i < css.length; i++)
		arg_sets_t[i] = css[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving TestArgument sets for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return arg_sets_t;
  }

  public String createCriteriaSet(
			AccessIdentity_Transferable accessIdentity,
			ClientCriteriaSet_Transferable criteria_set_t)
		throws AMFICOMRemoteException
  {
	return AMFICOMdbInterface.createCriteriaSet(accessIdentity, criteria_set_t);
  }

  public void attachCriteriaSetToME(
			AccessIdentity_Transferable accessIdentity,
			String criteria_set_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		CriteriaSet criteriaSet = new CriteriaSet(criteria_set_id);
	  criteriaSet.attachToMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while attaching criteria set id '" + criteria_set_id + "' tto monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientCriteriaSet_Transferable getCriteriaSet(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientCriteriaSet_Transferable criteria_set_t;
	try {
	  criteria_set_t = (new CriteriaSet(id)).getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving criteria set id '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return criteria_set_t;
  }

  public ClientCriteriaSet_Transferable[] getCriteriaSetsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	ClientCriteriaSet_Transferable[] criteria_sets_t = new ClientCriteriaSet_Transferable[0];
	CriteriaSet[] css;
	try {
	  css = CriteriaSet.retrieveCriteriaSets(monitored_element_id);
	  criteria_sets_t = new ClientCriteriaSet_Transferable[css.length];
	  for(int i = 0; i < css.length; i++)
		criteria_sets_t[i] = css[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving criteria sets for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return criteria_sets_t;
  }

  public ClientCriteriaSet_Transferable[] getCriteriaSetsByAnalysisType(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			String analysis_type_id)
		throws AMFICOMRemoteException
  {
	ClientCriteriaSet_Transferable[] criteria_sets_t = new ClientCriteriaSet_Transferable[0];
	CriteriaSet[] css;
	try {
	  css = CriteriaSet.retrieveCriteriaSets(monitored_element_id, analysis_type_id);
	  criteria_sets_t = new ClientCriteriaSet_Transferable[css.length];
	  for(int i = 0; i < css.length; i++)
		criteria_sets_t[i] = css[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving criteria sets for monitored_element_id '" + monitored_element_id + "', analysis_type_id '" + analysis_type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return criteria_sets_t;
  }

  public String createThresholdSet(
			AccessIdentity_Transferable accessIdentity,
			ClientThresholdSet_Transferable th_set_t)
		throws AMFICOMRemoteException
  {
	return AMFICOMdbInterface.createThresholdSet(accessIdentity, th_set_t);
  }

  public void attachThresholdSetToME(
			AccessIdentity_Transferable accessIdentity,
			String th_set_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		ThresholdSet thresholdSet = new ThresholdSet(th_set_id);
	  thresholdSet.attachToMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while attaching Threshold set id '" + th_set_id + "' tto monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientThresholdSet_Transferable getThresholdSet(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientThresholdSet_Transferable th_set_t;
	try {
	  th_set_t = (new ThresholdSet(id)).getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Threshold set id '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return th_set_t;
  }

  public ClientThresholdSet_Transferable[] getThresholdSetsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	ClientThresholdSet_Transferable[] th_sets_t = new ClientThresholdSet_Transferable[0];
	ThresholdSet[] css;
	try {
	  css = ThresholdSet.retrieveThresholdSets(monitored_element_id);
	  th_sets_t = new ClientThresholdSet_Transferable[css.length];
	  for(int i = 0; i < css.length; i++)
		th_sets_t[i] = css[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Threshold sets for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return th_sets_t;
  }

  public ClientThresholdSet_Transferable[] getThresholdSetsByAnalysisType(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			String evaluation_type_id)
		throws AMFICOMRemoteException
  {
	ClientThresholdSet_Transferable[] th_sets_t = new ClientThresholdSet_Transferable[0];
	ThresholdSet[] css;
	try {
	  css = ThresholdSet.retrieveThresholdSets(monitored_element_id, evaluation_type_id);
	  th_sets_t = new ClientThresholdSet_Transferable[css.length];
	  for(int i = 0; i < css.length; i++)
		th_sets_t[i] = css[i].getTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Threshold sets for monitored_element_id '" + monitored_element_id + "', analysis_type_id '" + evaluation_type_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return th_sets_t;
  }

  public String createEtalon(
			AccessIdentity_Transferable accessIdentity,
			ClientEtalon_Transferable e_t)
		throws AMFICOMRemoteException
  {
  	return AMFICOMdbInterface.createEtalon(accessIdentity, e_t);
  }

  public void attachEtalonToME(
			AccessIdentity_Transferable accessIdentity,
			String e_id,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	try {
		Etalon etalon = new Etalon(e_id);
	  etalon.attachToMonitoredElement(monitored_element_id);
	}
	catch (SQLException e) {
	  String mesg = "Exception while attaching Etalon id '" + e_id + "' tto monitored element id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

  public ClientEtalon_Transferable getEtalon(
			AccessIdentity_Transferable accessIdentity,
			String id)
		throws AMFICOMRemoteException
  {
	ClientEtalon_Transferable e_t;
	try {
	  e_t = (new Etalon(id)).getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Threshold set id '" + id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return e_t;
  }

  public ClientEtalon_Transferable[] getEtalonsByME(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id)
		throws AMFICOMRemoteException
  {
	ClientEtalon_Transferable[] es_t = new ClientEtalon_Transferable[0];
	Etalon[] es;
	try {
	  es = Etalon.retrieveEtalons(monitored_element_id);
	  es_t = new ClientEtalon_Transferable[es.length];
	  for(int i = 0; i < es.length; i++)
		es_t[i] = es[i].getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Etalon for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
	return es_t;
  }

	public ClientEtalon_Transferable getEtalonByMEandTime(
			AccessIdentity_Transferable accessIdentity,
			String monitored_element_id,
			long time)
		throws AMFICOMRemoteException
  {
	Etalon[] es;
	long temp = 0L;
	int index;
	try {
	  es = Etalon.retrieveEtalons(monitored_element_id);

	  if(es.length == 0)
		return null;
	  index = 0;
	  temp = es[0].getClientTransferable().modified;
	  for(int i = 1; i < es.length; i++)
	  {
		if(Math.abs(temp - time) > Math.abs(es[0].getClientTransferable().modified - time))
		{
			index = i;
			temp = es[i].getClientTransferable().modified;
		}
	  }
		return es[index].getClientTransferable();
	}
	catch (SQLException e) {
	  String mesg = "Exception while retrieving Etalon for monitored_element_id '" + monitored_element_id + "': " + e.getMessage();
	  System.out.println(mesg);
	  e.printStackTrace();
	  throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
	}
  }

	public String saveReportTemplates(
			AccessIdentity_Transferable accessIdentity,
			ReportTemplate_Transferable[] rts)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.saveReportTemplates(
				accessIdentity,
				rts);
	}

	public void getStatedReportTemplates(
			AccessIdentity_Transferable accessIdentity,
			String[] ids,
			ReportTemplateSeq_TransferableHolder reportTemplates)
		throws AMFICOMRemoteException
	{
		AMFICOMdbInterface.getStatedReportTemplates(
				accessIdentity,
				ids,
				reportTemplates);
	}

	public void removeReportTemplates(AccessIdentity_Transferable accessIdentity, String[] reportTemplate_ids) throws AMFICOMRemoteException {
		AMFICOMdbInterface.removeReportTemplates(accessIdentity, reportTemplate_ids);
	}

	public String saveSchemeOptimizeInfo(
			AccessIdentity_Transferable accessIdentity,
			SchemeOptimizeInfo_Transferable soi)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.saveSchemeOptimizeInfo(
				accessIdentity,
				soi);
	}

	public SchemeOptimizeInfo_Transferable[] getSchemeOptimizeInfo(
			AccessIdentity_Transferable accessIdentity)
		throws AMFICOMRemoteException
	{
		return AMFICOMdbInterface.getSchemeOptimizeInfo(accessIdentity);
	}

	public void removeSchemeOptimizeInfo(AccessIdentity_Transferable accessIdentity, String[] soi_ids) throws AMFICOMRemoteException {
		AMFICOMdbInterface.removeSchemeOptimizeInfo(accessIdentity, soi_ids);
	}

	public String saveSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity, SchemeMonitoringSolution_Transferable sol) throws AMFICOMRemoteException {
		return AMFICOMdbInterface.saveSchemeMonitoringSolutions(accessIdentity, sol);
	}

	public SchemeMonitoringSolution_Transferable[] getSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		return AMFICOMdbInterface.getSchemeMonitoringSolutions(accessIdentity);
	}

	public void removeSchemeMonitoringSolutions(AccessIdentity_Transferable accessIdentity, String[] sol_ids) throws AMFICOMRemoteException {
		AMFICOMdbInterface.removeSchemeMonitoringSolutions(accessIdentity, sol_ids);
	}

	public void syncPing(LongHolder serverTimeMillis) throws AMFICOMRemoteException
	{
		serverTimeMillis.value = System.currentTimeMillis();
	}

	public void registerAlarmReceiver(AccessIdentity_Transferable accessIdentity, AMFICOMClient cli) throws AMFICOMRemoteException {
		try
		{
			OracleAlarmReceiverMap.put(accessIdentity, cli);
		}
		catch (AMFICOMRemoteException are) 
		{
			throw are;
		}
		catch (NullPointerException npe)
		{
			AMFICOMRemoteException are = new AMFICOMRemoteException();
			/*
			 * The code needs to be Java 1.3.x-compatible.
			 */
//			are.initCause(npe);
//			are.setStackTrace(npe.getStackTrace());
			throw are;
		}
		catch (Exception e)
		{
			AMFICOMRemoteException are = new AMFICOMRemoteException(Constants.ERROR_UPDATING, e.getLocalizedMessage());
//			are.initCause(e);
//			are.setStackTrace(e.getStackTrace());
			throw are;
		}
	}

	public void unregisterAlarmReceiver(AccessIdentity_Transferable accessIdentity) throws AMFICOMRemoteException {
		try
		{
			OracleAlarmReceiverMap.remove(accessIdentity);
		}
		catch (AMFICOMRemoteException are) 
		{
			throw are;
		}
		catch (NullPointerException npe)
		{
			AMFICOMRemoteException are = new AMFICOMRemoteException();
//			are.initCause(npe);
//			are.setStackTrace(npe.getStackTrace());
			throw are;
		}
		catch (Exception e)
		{
			AMFICOMRemoteException are = new AMFICOMRemoteException(Constants.ERROR_UPDATING, e.getLocalizedMessage());
//			are.initCause(e);
//			are.setStackTrace(e.getStackTrace());
			throw are;
		}
	}
}
