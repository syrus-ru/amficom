#
# $Id: Makefile.inc,v 1.1 2004/06/07 15:36:09 bass Exp $
#
# vim:set ft=make:
#

exesuffix =

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

sqlj = $(firstword $(wildcard $(addsuffix /bin/sqlj$(exesuffix), $(1))))
SQLJ = $(call sqlj,$(ORACLE_HOME))
ifeq ($(SQLJ),)
SQLJ = $(call sqlj,$(orahome9))
endif
ifeq ($(SQLJ),)
SQLJ = $(call pathsearch,sqlj$(exesuffix))
endif

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
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.jar)
ifeq ($(translator),)
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.zip)
endif
SQLJCLASSPATH = $(runtime12):$(translator)

AMFICOMCLASSPATH ?= $(shell find $(LIBDIR) -name '*\.[jz][ai][pr]' -printf '%p:')

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
