/*-
* $Id: PermissionAttributes.java,v 1.6 2005/09/09 15:03:06 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributes;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;


/**
 * @version $Revision: 1.6 $, $Date: 2005/09/09 15:03:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module admin
 */
public class PermissionAttributes extends StorableObject {

	private static final long	serialVersionUID	= -7619737537568452039L;

	public static enum PermissionCodenames {
		ADMIN_ENTER,
		
		ADMIN_ADD_DOMAIN ,
		ADMIN_ADD_GROUP ,
		ADMIN_ADD_PROFILE ,
		
		ADMIN_CHANGE_DOMAIN ,
		ADMIN_CHANGE_COMMAND ,
		ADMIN_CHANGE_GROUP ,
		ADMIN_CHANGE_PROFILE ,
		ADMIN_CHANGE_CATEGORY,
		ADMIN_CHANGE_MCM_INFO,
		ADMIN_CHANGE_SERVER_INFO,
		ADMIN_CHANGE_CLIENT_INFO,
		
		ADMIN_READ_DOMAIN ,
		ADMIN_READ_COMMAND ,
		ADMIN_READ_GROUP ,
		ADMIN_READ_PROFILE ,
		ADMIN_READ_CATEGORY,
		ADMIN_READ_USERS_INFO,
		ADMIN_READ_MCM_INFO,
		ADMIN_READ_SERVER_INFO,
		ADMIN_READ_CLIENT_INFO,
		
		ADMIN_REMOVE_GROUP ,
		ADMIN_REMOVE_PROFILE ,
		
		CONFIG_ENTER,
		
		CONFIG_CHANGE_COMPONENTS,
		CONFIG_CHANGE_SCHEMES,
		CONFIG_CHANGE_TOPOLOGY,
		CONFIG_CHANGE_CATALOG_TS,
		CONFIG_CHANGE_CATALOG_SM,
		
		CONFIG_READ_SCHEMES,
		CONFIG_READ_TOPOLOGY,
		CONFIG_READ_CATALOG_TS,		
		CONFIG_READ_CATALOG_SM,
		
		OPTIMIZE_ENTER,
		
		OPTIMIZE_OPEN_MAP,
		OPTIMIZE_OPEN_SCHEME,
		OPTIMIZE_SET_OPTIONS,
		OPTIMIZE_START,
		OPTIMIZE_ABORT,
		OPTIMIZE_SAVE_RESULTS,
		OPTIMIZE_SAVE_OPTIONS,
		
		MODELING_ENTER,
		
		MODELING_OPEN_MODELING_LINE_SCHEME,
		MODELING_OPEN_MODELING_LINE_MAP,
		MODELING_PERFORM,
		MODELING_SET_OPTIONS,
		MODELING_SAVE_REFLECTOGRAM_MODEL,
		
		SCHEDULER_ENTER,
		SCHEDULER_ADD_ONETIME_TEST ,
		SCHEDULER_ADD_PERIODIAL_TEST ,
		SCHEDULER_ADD_GROUP_TEST ,
		SCHEDULER_REFRESH_TESTS ,
		SCHEDULER_SAVE_TESTS,
		
		ANALYSIS_ENTER,
		EXTEND_ANALYSIS_ENTER,
		EVALUATION_ENTER,
		ANALYSIS_OPEN_REFLECTOGRAM_FILE,
		ANALYSIS_OPEN_REFLECTOGRAM_FROM_DB,
		ANALYSIS_PERFORM,
		ANALYSIS_SAVE_REFLECTOGRAM_FILE,
		ANALYSIS_SAVE_REFLECTOGRAM_TO_DB,
		
		
		SURVEY_ENTER,
		SURVEY_VIEW_OPERATIVAL_INFO,
		SURVEY_VIEW_CHANGES_ARCHIVE,
		SURVEY_QUICK_TASK_SETTING,
		SURVEY_VIEW_REFLECTOGRAM,
		SURVEY_VIEW_ALARM_SIGNALS,
		SURVEY_SIGNAL_CONFIRMING,
		SURVEY_DELETE_ALARM_SIGNAL,
		
		PROGNOSTICATION_ENTER,
		PROGNOSTICATION_CALCULATE_REFLECTOGRAM,
		PROGNOSTICATION_ACQUIRE_DATA,
		PROGNOSTICATION_SAVE_REFLECTOGRAM;
		

		private static final String KEY_ROOT = "PermissionAttributes.Description.";
		
		private String codename;
		
		
		private PermissionCodenames() {
			// generate codename as javaNamingStyle from name
			final String name = this.name();
			final StringBuffer buffer = new StringBuffer();
			String[] strings = name.split("_");
			for(int i = 0; i < strings.length; i++) {
				if (i == 0) {
					buffer.append(strings[i].toLowerCase());
				} else {
					buffer.append(strings[i].charAt(0));
					buffer.append(strings[i].substring(1).toLowerCase());
				}
			}
			this.codename = buffer.toString();
		}
		
		public final String getDescription() {
			return LangModelAdministation.getString(KEY_ROOT + this.codename);
		}
		
		public final String getCodename() {
			return this.codename;
		}
	}
	
	private Identifier userId;
	
	private Identifier domainId;
	
	private BigInteger permissionMask;
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public PermissionAttributes(final IdlPermissionAttributes dt) throws CreateObjectException {
		try {
			this.fromTransferable(dt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	PermissionAttributes(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier userId,
			final BigInteger permissionMask) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.domainId = domainId;
		this.userId = userId;
		this.permissionMask = permissionMask;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPermissionAttributes pat = (IdlPermissionAttributes)transferable;
		super.fromTransferable(pat);
		this.domainId = new Identifier(pat.domainId);
		this.userId = new Identifier(pat.userId);
		this.permissionMask = new BigInteger(pat.permissionMask);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlPermissionAttributes getTransferable(final ORB orb) {
		assert this.isValid(): ErrorMessages.OBJECT_STATE_ILLEGAL;
		return IdlPermissionAttributesHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.domainId.getTransferable(),
				this.userId.getTransferable(),
				this.permissionMask.toByteArray());
	}	

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.domainId != null && !this.domainId.isVoid()
				&& this.userId != null && !this.userId.isVoid() 
				&& this.permissionMask != null;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param userId
	 * @param permissionMask
	 * @return new instance for client
	 * @throws CreateObjectException
	 */
	public static PermissionAttributes createInstance(final Identifier creatorId,
	                                                  final Identifier domainId,
	                                                  final Identifier userId,
	                                                  final BigInteger permissionMask) 
	throws CreateObjectException {
		try {
			final PermissionAttributes permissionAttributes = 
				new PermissionAttributes(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERMATTR_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					userId,
					permissionMask);

			assert permissionAttributes.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			permissionAttributes.markAsChanged();

			return permissionAttributes;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier userId,
            final BigInteger permissionMask) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.domainId = domainId;
		this.userId = userId;
		this.permissionMask = permissionMask;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		if (!this.domainId.isVoid()) {
			dependencies.add(this.domainId);
		}
		if (!this.userId.isVoid()) {
			dependencies.add(this.userId);
		}
		return dependencies;
	}
	
	public final void setPermissionEnable(final PermissionCodenames codename,
	                                      final boolean enable) {
		final int bit = codename.ordinal();
		if (enable) {
			this.permissionMask = this.permissionMask.setBit(bit);
		} else {
			this.permissionMask = this.permissionMask.clearBit(bit);
		}
		super.markAsChanged();
	}
	
	public final boolean isPermissionEnable(final PermissionCodenames codename) {
		return this.permissionMask.testBit(codename.ordinal());
	}
	
	public final Identifier getDomainId() {
		return this.domainId;
	}
	
	public final void setDomainId(final Identifier domainId) {
		this.domainId = domainId;
		super.markAsChanged();
	}
	
	public final Identifier getUserId() {
		return this.userId;
	}
	
	public final void setUserId(final Identifier userId) {
		this.userId = userId;
		super.markAsChanged();
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	final BigInteger getPermissionMask() {
		return this.permissionMask;
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	final void setPermissionMask(final BigInteger permissionMask) {
		this.permissionMask = permissionMask;
		super.markAsChanged();
	}
	
}

