#
# $Id: Makefile.inc,v 1.3 2004/07/05 08:35:25 bass Exp $
#
# vim:set ft=make:
#

exesuffix =

# User may override MODULE_VERSION
MODULE_VERSION ?= $(shell date +%Y%m%d)

SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class \
	_SJProfile0.class \
	_SJProfileKeys.class

# User may override either of IDLDIR, SRCDIR, CLASSDIR
IDLDIR ?= idl
SRCDIR ?= src
CLASSDIR ?= classes

ifeq ($(shell hostname -s),bass)
LIBDIR = lib
EXTLIBDIR = lib/ext
else
LIBDIR = ../lib
EXTLIBDIR = ../../extlib
endif

checkenve = $(if $($(1)), , $(error $(1) is undefined. Define it prior to issuing `$(MAKE)'))
checkenvw = $(if $($(1)), , $(warning $(1) is undefined. Define it prior to issuing `$(MAKE)'))
pathsearch = $(firstword $(wildcard $(addsuffix /$(1), $(subst :, , $(PATH)))))
orabase = $(dir $(ORACLE_HOME))
orahomes9 = $(wildcard $(orabase)9.*)
orahome9 = $(firstword $(orahomes9))

idl2java = $(firstword $(wildcard $(addsuffix /bin/idl2java$(exesuffix), $(1))))
IDL2JAVA = $(call idl2java,$(ORACLE_HOME))
ifeq ($(IDL2JAVA),)
IDL2JAVA = $(call idl2java,$(orahome9))
endif
ifeq ($(IDL2JAVA),)
IDL2JAVA = $(call pathsearch,idl2java$(exesuffix))
endif
# -portable and -strict are temporarily removed: AMFICOMServer overrides
# _execute() and _methods() from _AMFICOMImplBase.
IDL2JAVAFLAGS = \
	-C \
	-H \
	-I$(IDLDIR) \
	-no_bind \
	-no_examples \
	-narrow_compliance \
	-root_dir $(SRCDIR) \
	-no_tie \
	-no_toString

# User may override database connection settings
USER ?= AMFICOM
PASSWORD ?= amficom
HOST ?= RESEARCH
SERVICE_NAME ?= SHAITAN

sqlj = $(firstword $(wildcard $(addsuffix /bin/sqlj$(exesuffix), $(1))))
SQLJ = $(call sqlj,$(ORACLE_HOME))
ifeq ($(SQLJ),)
SQLJ = $(call sqlj,$(orahome9))
endif
ifeq ($(SQLJ),)
SQLJ = $(call pathsearch,sqlj$(exesuffix))
endif
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
	-warn=all \
	-d $(CLASSDIR) \
	-encoding $(ENCODING)

aurora_client_orbdep = $(wildcard $(ORACLE_HOME)/javavm/lib/aurora_client_orbdep.jar)
ifeq ($(aurora_client_orbdep),)
aurora_client_orbdep = $(wildcard $(ORACLE_HOME)/javavm/lib/aurora_client_orbdep.zip)
endif
ifeq ($(aurora_client_orbdep),)
aurora_client_orbdep = $(wildcard $(orahome9)/javavm/lib/aurora_client_orbdep.jar)
endif
ifeq ($(aurora_client_orbdep),)
aurora_client_orbdep = $(wildcard $(orahome9)/javavm/lib/aurora_client_orbdep.zip)
endif
aurora_client_orbindep = $(wildcard $(ORACLE_HOME)/javavm/lib/aurora_client_orbindep.jar)
ifeq ($(aurora_client_orbindep),)
aurora_client_orbindep = $(wildcard $(ORACLE_HOME)/javavm/lib/aurora_client_orbindep.zip)
endif
ifeq ($(aurora_client_orbindep),)
aurora_client_orbindep = $(wildcard $(orahome9)/javavm/lib/aurora_client_orbindep.jar)
endif
ifeq ($(aurora_client_orbindep),)
aurora_client_orbindep = $(wildcard $(orahome9)/javavm/lib/aurora_client_orbindep.zip)
endif
AURORACLASSPATH = $(aurora_client_orbdep):$(aurora_client_orbindep)

vbjapp = $(wildcard $(ORACLE_HOME)/lib/vbjapp.jar)
ifeq ($(vbjapp),)
vbjapp = $(wildcard $(ORACLE_HOME)/lib/vbjapp.zip)
endif
vbjorb = $(wildcard $(ORACLE_HOME)/lib/vbjorb.jar)
ifeq ($(vbjorb),)
vbjorb = $(wildcard $(ORACLE_HOME)/lib/vbjorb.zip)
endif
VBJCLASSPATH = $(vbjapp):$(vbjorb)

JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/ojdbc14.jar)
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/ojdbc14.zip)
endif
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/classes12.jar)
endif
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/classes12.zip)
endif

runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12.jar)
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12.zip)
endif
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12ee.jar)
endif
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12ee.zip)
endif
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.jar)
ifeq ($(translator),)
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.zip)
endif
SQLJCLASSPATH = $(runtime12):$(translator)

# User may override AMFICOMCLASSPATH and/or EXTCLASSPATH
AMFICOMCLASSPATH ?= $(shell find $(LIBDIR) -name '*\.[jz][ai][pr]' -printf '%p:')
EXTCLASSPATH ?= $(shell find $(EXTLIBDIR) -name '*\.[jz][ai][pr]' -printf '%p:')

jdevrt = $(wildcard $(ORACLE_HOME)/jdev/lib/jdev-rt.jar)
ifeq ($(jdevrt),)
jdevrt = $(wildcard $(orahome9)/jdev/lib/jdev-rt.jar)
endif
DEPRECATEDCLASSPATH = $(jdevrt)

ENCODING = windows-1251
JAVAC_WARNOFF_FLAG = -nowarn
# User may specify either -source 1.3 or -source 1.4 as an initial value
# of JAVACFLAGS
JAVACFLAGS += \
	-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g \
	-sourcepath $(SRCDIR)
# User may specify -target 1.1/1.2/1.3/1.4
SUNJAVACFLAGS +=
# User may specify -target 1.1/1.2/1.3/1.4/1.4.2 
JIKESFLAGS += \
	+E \
	+P \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pno-switchcheck \
	+Pnaming-convention \
	+T=8
JAVAC = $(call pathsearch,jikes$(exesuffix))
ifeq ($(JAVAC),)
JAVAC = $(JAVA_HOME)/bin/javac
JAVACFLAGS := $(JAVACFLAGS) $(SUNJAVACFLAGS)
else
JAVACFLAGS := $(JAVACFLAGS) $(JIKESFLAGS)
endif

JAR = $(call pathsearch,jar-3.3.1$(exesuffix))
ifeq ($(JAR),)
JAR = $(call pathsearch,jar$(exesuffix))
endif
ifeq ($(JAR),)
JAR = $(JAVA_HOME)/bin/jar
endif

JAVA = $(JAVA_HOME)/bin/java

IDLJ = $(JAVA_HOME)/bin/idlj
IDLJFLAGS = \
	-fallTIE \
	-i $(IDLDIR) \
	-keep \
	-td $(SRCDIR) \
	-v

PMD_JAR = $(shell find $(PMD_HOME) -name pmd-*.jar)
PMD_JARS = $(shell find $(PMD_HOME) -name '*\.[jz][ai][pr]' -printf '%p:')

PMD_RULES = rulesets/unusedcode.xml \
	rulesets/imports.xml \
	rulesets/basic.xml \
	rulesets/braces.xml \
	rulesets/clone.xml \
	rulesets/codesize.xml \
	rulesets/controlversial.xml \
	rulesets/coupling.xml \
	rulesets/design.xml \
	rulesets/favorites.xml \
	rulesets/junit.xml \
	rulesets/naming.xml \
	rulesets/newrules.xml \
	rulesets/scratchpad.xml \
	rulesets/strictexception.xml \
	rulesets/strings.xml
