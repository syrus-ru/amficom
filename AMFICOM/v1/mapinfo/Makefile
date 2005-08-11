#
# $Id: Makefile,v 1.2 2005/08/11 18:55:03 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapinfo

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/map.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/mapviewclient.jar:$(LIBDIR)/mscharserver_interface.jar
EXTCLASSPATH := $(EXTLIBDIR)/xalan.jar

MAPINFODIR := $(EXTLIBDIR)/mapinfo
COMMONMAPINFODIR := $(MAPINFODIR)/common
MAPINFOCLASSPATH := $(COMMONMAPINFODIR)/jdom.jar:$(COMMONMAPINFODIR)/commons-logging.jar:$(COMMONMAPINFODIR)/micsys.jar:$(COMMONMAPINFODIR)/miutil.jar:$(COMMONMAPINFODIR)/mxj.jar:$(COMMONMAPINFODIR)/mxjgeom.jar

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/map/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/map/mapinfo/*.java)


$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(MAPINFOCLASSPATH) \
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
