#
# $Id: Makefile,v 1.9 2006/04/28 10:15:18 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapinfo

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
util \
general \
resource \
map \
csbridge \
commonclient \
mapclient \
mapviewclient \
mscharserver_interface

EXTCLASSPATH := $(EXTLIBDIR)/xalan.jar:$(EXTLIBDIR)/dom4j.jar

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
		$(SOURCES)
