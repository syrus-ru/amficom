#
# $Id: Makefile,v 1.1 2004/07/06 07:05:09 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = analysis_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.PHONY: export
export:
$(error Not yet implemented)
