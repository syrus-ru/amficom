#
# $Id: Makefile,v 1.9 2004/07/06 12:54:22 bass Exp $
#
# vim:set ft=make:
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

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.PHONY: export
export: $(LIBNAME) $(LIBNAME_OBJECT) $(LIBNAME_ADMIN) $(LIBNAME_ALARM) $(LIBNAME_GENERAL) $(LIBNAME_ISM) $(LIBNAME_MAP) $(LIBNAME_NETWORK) $(LIBNAME_REPORT) $(LIBNAME_SCHEME) $(LIBNAME_SURVEY)

.PHONY: all
all: .all

.all: .idl .sqlj
	$(JAVAC) \
		$(JAVACFLAGS) \
		$(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH)$(AURORACLASSPATH):$(VBJCLASSPATH):$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDL2JAVA) \
		$(IDL2JAVAFLAGS) \
		$(IDLDIR)/AMFICOM.idl \
		$(IDLDIR)/ISM.idl \
		$(IDLDIR)/admin.idl \
		$(IDLDIR)/alarm.idl \
		$(IDLDIR)/client.idl \
		$(IDLDIR)/constants.idl \
		$(IDLDIR)/general.idl \
		$(IDLDIR)/map.idl \
		$(IDLDIR)/network.idl \
		$(IDLDIR)/report.idl \
		$(IDLDIR)/resource.idl \
		$(IDLDIR)/scheme.idl \
		$(IDLDIR)/survey.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		$(SRCDIR)/* \
		.idl

.PHONY: sqlj
sqlj: .sqlj

.sqlj: $(CLASSDIR)
	echo "sqlj.compiler-executable=$(JAVAC) $(JAVACFLAGS)" > sqlj.properties
	$(RM) \
		$(filter $(foreach source, $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj')), $(foreach src_suffix, $(SRC_SUFFIXES), $(source)$(src_suffix))), $(shell find $(SRCDIR) -name '*\.java')) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)')
	touch $@

.PHONY: sqljclean
sqljclean:
	$(RM) \
		$(foreach file, $(filter $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.sqlj

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: sqljclean idlclean
	$(RM) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
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
		SQLChecker.cache \
		sqlj.properties \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_OBJECT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_st_AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClient.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMClientImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_st_AMFICOMClient.class

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
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_ConstantsImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_st_Constants.class

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
