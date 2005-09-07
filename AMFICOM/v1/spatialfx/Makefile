#
# $Id: Makefile,v 1.1 2005/09/07 15:53:36 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := spatialfx

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/mapclient.jar:$(LIBDIR)/mapviewclient.jar:$(LIBDIR)/mscharserver_interface.jar
EXTCLASSPATH := $(EXTLIBDIR)/ofx.spatialfx.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/map/objectfx/*.java)


$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		$(SOURCES)
	touch $@

.PHONY: clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach


#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^


#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRCDIR)
