#
# $Id: Makefile,v 1.7 2005/07/04 11:57:46 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := analysis
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

LIBDIR=../lib
CLASSPATH = $(LIBDIR)/corba.jar;$(LIBDIR)/util.jar;$(LIBDIR)/general.jar;$(LIBDIR)/config.jar;$(LIBDIR)/admin.jar;$(LIBDIR)/corbaportable.jar;$(LIBDIR)/filter.jar;$(LIBDIR)/measurement.jar;$(LIBDIR)/scheme.jar;$(LIBDIR)/generalclient.jar;$(LIBDIR)/schemeclient.jar;$(LIBDIR)/mapclient.jar;../dadara/classes

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

#JAVAC = $(call pathsearch,jikes$(exesuffix))
JIKES ?= jikes
JAVAC = $(JIKES)
include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

JAVACCHECK = $(JIKES)
JAVACFLAGS :=      $(JAVACFLAGS) $(JIKESFLAGS) -classpath $(CLASSPATH) -sourcepath $(SRCDIR)
JAVACCHECKFLAGS := $(JAVACFLAGS) $(JIKESFLAGS) -classpath $(CLASSPATH) -sourcepath $(SRCDIR) +B

SOURCES := $(shell find $(SRCDIR) -name *.java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(CLASSDIR):
	mkdir $@

.PHONY:	clean
clean:
	rm -f $(LIBNAME)
	rm -rf $(CLASSDIR)

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		$(SOURCES)

.PHONY: check
check:
	$(JAVACCHECK) $(JAVACCHECKFLAGS) \
		$(SOURCES)
