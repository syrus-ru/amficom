#
# $Id: Makefile,v 1.13 2004/10/20 08:26:48 bass Exp $
#
# vim:ft=make:
#

MODULE_NAME = idl_v1

LIBNAME = corba.jar
LIBNAME_OBJECT = ServerObjectInterface.jar
LIBNAME_ADMIN = ServerInterface_Admin.jar
LIBNAME_ALARM = ServerInterface_Alarm.jar
LIBNAME_GENERAL = ServerInterface_General.jar
LIBNAME_ISM = ServerInterface_ISM.jar
LIBNAME_MAP = ServerInterface_Map.jar
LIBNAME_NETWORK = ServerInterface_Network.jar
LIBNAME_REPORT = ServerInterface_Report.jar
LIBNAME_SCHEME = ServerInterface_Scheme.jar
LIBNAME_SURVEY = ServerInterface_Survey.jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

.PHONY: export
export: $(LIBNAME) $(LIBNAME_OBJECT) $(LIBNAME_ADMIN) $(LIBNAME_ALARM) $(LIBNAME_GENERAL) $(LIBNAME_ISM) $(LIBNAME_MAP) $(LIBNAME_NETWORK) $(LIBNAME_REPORT) $(LIBNAME_SCHEME) $(LIBNAME_SURVEY)

.PHONY: all
all: .all

.all: .idl
	$(JAVAC) \
		$(JAVACFLAGS) \
		$(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH)$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

# Target created for IDEs compiling everything from scratch not to tie client
# code to (cm)server implementation.
.PHONY: xtra
xtra: .xtra

.xtra: .idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/event.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/measurement.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/cmserver.idl
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/AMFICOM.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/ISM.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/admin.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/constants.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/general.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/map.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/network.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/report.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/resource.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/scheme.idl
	$(IDLJ) $(IDLJFLAGS) -DSERVER $(IDLDIR)/survey.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		$(SRCDIR)/* \
		.idl

.PHONY: xtraclean
xtraclean: idlclean
	$(RM) -r \
		.xtra

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: xtraclean
	$(RM) \
		.all

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(SRCDIR) \
		$(CLASSDIR) \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_OBJECT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClient.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMClientStub.class

$(LIBNAME_ADMIN): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Admin

$(LIBNAME_ALARM): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Alarm

$(LIBNAME_GENERAL): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constant \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/General \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Resource \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constants.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_ConstantsStub.class

$(LIBNAME_ISM): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ISM \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ISMDirectory

$(LIBNAME_MAP): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Map

$(LIBNAME_NETWORK): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Network \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/NetworkDirectory

$(LIBNAME_REPORT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Report

$(LIBNAME_SCHEME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Scheme

$(LIBNAME_SURVEY): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Survey
