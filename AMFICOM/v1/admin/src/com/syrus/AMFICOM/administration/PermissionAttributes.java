/*-
* $Id: PermissionAttributes.java,v 1.9 2005/09/28 09:19:46 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributes;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesHelper;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesPackage.IdlModule;
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
import com.syrus.util.Log;


/**
 * @version $Revision: 1.9 $, $Date: 2005/09/28 09:19:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module administration
 */
public class PermissionAttributes extends StorableObject {

	// TODO generate serialVersionUID when all enum will be made 

	public static enum Module {
		ADMINSTRATION,
		SCHEME;
		
		private static Module[] values = values();
		private static List<Module> valueList = 
			Collections.unmodifiableList(Arrays.asList(values));
		
		public static final Module valueOf(final int ordinal) {			
			return values[ordinal];
		} 
		
		public static final Module valueOf(final Integer ordinal) {
			return valueOf(ordinal.intValue());
		}

		public static Module valueOf(final IdlModule idlModule) {
			return values[idlModule.value()];
		}
		
		public static final List<Module> getValueList() {
			return valueList;
		}		
		
		public final IdlModule getTransferable() {
			return IdlModule.from_int(this.ordinal());
		}
	}
	
	private interface SwitchableGroupNumber {
		Module getModule();
		boolean isEnable();
	}
	
	/**
	 * Administation permission enum
	 * 
	 *  <p><b>WARNING !!! Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Adminstration implements SwitchableGroupNumber {
		ENTER(true),
		CREATE_DOMAIN(true);
		
		private final boolean	enable;

		private Adminstration(final boolean enable) {
			this.enable = enable;			
		}	
		
		public boolean isEnable() {
			return this.enable;
		}
		
		public final Module getModule() {
			return Module.ADMINSTRATION;
		}
	}
	
	/**
	 * Scheme permission enum
	 * 
	 *  <p><b>ОХНУНГ !!! Стасег и иво прадалжатили, Never change the order or delete items, only appending to the end of list permitted</b></p>  
	 */
	private enum Scheme implements SwitchableGroupNumber {
		ENTER(true),
		CREATE_AND_EDIT(true),
		SAVING(true);
		
		private final boolean	enable;
		
		private Scheme(final boolean enable) {
			this.enable = enable;			
		}			
		
		public boolean isEnable() {
			return this.enable;
		}
		
		public final Module getModule() {
			return Module.SCHEME;
		}
	}
	
	public static enum PermissionCodename {
		ADMINSTRATION_ENTER(Adminstration.ENTER),
		ADMINSTRATION_CREATE_DOMAIN(Adminstration.CREATE_DOMAIN),
		
		SCHEME_ENTER(Scheme.ENTER),
		SCHEME_CREATE_AND_EDIT(Scheme.CREATE_AND_EDIT),
		SCHEME_SAVING(Scheme.SAVING);
		
		private final Enum	e;		

		private static final String KEY_ROOT = "PermissionAttributes.Description.";
		
		private String codename;

		private PermissionCodename(final SwitchableGroupNumber e) {
			this.e = (Enum) e;
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
		
		public final int getOrderInGroup() {
			return this.e.ordinal();
		}
		
		public final Module getModule() {
			return ((SwitchableGroupNumber)this.e).getModule();
		}
		
		public boolean isEnable() {
			return ((SwitchableGroupNumber)this.e).isEnable();
		}
	}
	
	private Identifier userId;
	
	private Identifier domainId;
	
	private Module module;
	
	private BitSet permissions;
	
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
			final Module module,
			final BigInteger permissions) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.domainId = domainId;
		this.userId = userId;
		this.module = module;
		this.setPermissions0(permissions);
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
		this.module = Module.valueOf(pat._module);
		this.setPermissionsByteArray0(pat.permissionMask);

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
				this.module.getTransferable(),
				this.getPermissionByteArray0());
	}	

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.domainId != null && !this.domainId.isVoid()
				&& this.userId != null && !this.userId.isVoid() 
				&& this.permissions != null;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param userId
	 * @param permissions
	 * @return new instance for client
	 * @throws CreateObjectException
	 */
	public static PermissionAttributes createInstance(final Identifier creatorId,
	                                                  final Identifier domainId,
	                                                  final Identifier userId,
	                                                  final Module module,
	                                                  final BigInteger permissions) 
	throws CreateObjectException {
		try {
			final PermissionAttributes permissionAttributes = 
				new PermissionAttributes(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERMATTR_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					userId,
					module,
					permissions);

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
			final Module module,
            final BigInteger permissions) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.domainId = domainId;
		this.userId = userId;
		this.module = module;
		this.setPermissions0(permissions);
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
	
	/**
	 * @param permissionCode permission codename
	 * @param enable
	 * 
	 * @throws IllegalArgumentException if permission disabled 
	 * or permission for other module code
	 */
	public final void setPermissionEnable(final PermissionCodename permissionCode,
	                                      final boolean enable) {
		if (!permissionCode.isEnable()) {
			throw new IllegalArgumentException("PermissionCode " + permissionCode.name() + " disabled.");
		}
		
		if (this.module != permissionCode.getModule()) {
			throw new IllegalArgumentException("This Permission doen't support " + permissionCode.name());
		}
		assert Log.debugMessage("Permissions.setPermissionEnable | " 
				+ (enable ? "en" : "dis") + "able " 
				+ permissionCode.getOrderInGroup() 
				+ " bit in "
				+ this.module + " group" ,
			Log.DEBUGLEVEL10);
		this.permissions.set(permissionCode.getOrderInGroup(), enable);
	}
	
	/**
	 * @param permissionCode
	 * @return true if permission is enabled for permissioncode 
	 * 
	 * @throws IllegalArgumentException if permission disabled 
	 * or permission for other module code
	 */
	public final boolean isPermissionEnable(final PermissionCodename permissionCode) {
		if (!permissionCode.isEnable()) {
			throw new IllegalArgumentException("PermissionCode " + permissionCode.name() + " disabled.");
		}
		
		if (this.module != permissionCode.getModule()) {
			throw new IllegalArgumentException("This Permission doen't support " + permissionCode.name());
		}
		
		return this.permissions.get(permissionCode.getOrderInGroup());
	}
	
	
	/**
	 * @return permission digital form 
	 */
	public final BigInteger getPermissions(){
		return new BigInteger(this.getPermissionByteArray0());
	}

	/**
	 * @return permission byte array digital form
	 */
	final byte[] getPermissionByteArray0() {
		final byte[] bytes = new byte[this.permissions.length()/8+1];
		for (int i=0; i<this.permissions.length(); i++) {
		    if (this.permissions.get(i)) {
		        bytes[bytes.length-i/8-1] |= 1<<(i%8);
		    }
		}
		return bytes;
	}
	
	/**
	 * set permission from permission digital form
	 * @param permissions
	 */
	public final void setPermissions(final BigInteger permissions) {
		this.setPermissions0(permissions);
		super.markAsChanged();
	}
	
	final void setPermissions0(final BigInteger permissions) {
		this.setPermissionsByteArray0(permissions.toByteArray());
	}

	/**
	 * set permission from permission byte array digital form
	 * @param bytes permission byte array digital form
	 */
	final void setPermissionsByteArray0(final byte[] bytes) {
		this.permissions.clear();
		for (int i=0; i<bytes.length*8; i++) {
		    if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
		        this.permissions.set(i);
		    }
		}
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
	
	public final Module getModule() {
		return this.module;
	}
	
}

