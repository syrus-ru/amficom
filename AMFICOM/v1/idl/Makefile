#
# $Id: Makefile,v 1.3 2004/06/21 11:58:54 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = idl_v1
MODULE_VERSION = $(shell date +%Y%m%d)

USER = AMFICOM
PASSWORD = amficom
HOST = RESEARCH
SERVICE_NAME = SHAITAN

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
SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class \
	_SJProfile0.class \
	_SJProfileKeys.class

IDLDIR = idl
SRCDIR = src
CLASSDIR = classes
ifeq ($(shell hostname -s),bass)
LIBDIR = lib
else
LIBDIR = ../lib
endif
ENCODING = windows-1251
JAVACFLAGS = -source 1.3 -sourcepath $(SRCDIR) -deprecation -g -O
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = \
	-target 1.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	+E \
	+P \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pno-switchcheck \
	+Pnaming-convention \
	+T=8
IDL2JAVAFLAGS = \
	-C \
	-H \
	-no_bind \
	-no_examples \
	-narrow_compliance \
	-portable \
	-root_dir $(SRCDIR) \
	-strict \
	-no_toString
SQLJFLAGS = \
	-cache=true \
	-checkfilename=true \
	-checksource=true \
	-codegen=iso \
	-compile=true \
	-compiler-encoding-flag=true \
	-explain=true \
	-linemap=true \
	-parse=both \
	-password "$(PASSWORD)" \
	-profile=true \
	-ser2class=true \
	-status=false \
	-url "jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=$(HOST))(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=$(SERVICE_NAME))))" \
	-user "$(USER)" \
	-warn=all

include Makefile.inc
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
		-classpath $(AMFICOMCLASSPATH)$(AURORACLASSPATH):$(VBJCLASSPATH):$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDL2JAVA) \
		$(IDL2JAVAFLAGS) \
		$(IDLDIR)/AMFICOM.idl \
		$(IDLDIR)/admin.idl \
		$(IDLDIR)/alarm.idl \
		$(IDLDIR)/client.idl \
		$(IDLDIR)/constants.idl \
		$(IDLDIR)/general.idl \
		$(IDLDIR)/ISM.idl \
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
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClient.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMClientImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_AMFICOMClient.class

$(LIBNAME_ADMIN): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Admin

$(LIBNAME_ALARM): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Alarm

$(LIBNAME_GENERAL): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/general/corba \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constant \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/General \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Resource \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constants.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_ConstantsImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_Constants.class

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
