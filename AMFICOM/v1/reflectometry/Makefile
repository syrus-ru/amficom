#
# $Id: Makefile,v 1.9 2006/03/16 12:53:45 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := reflectometry

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/reflectometry.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
util

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/reflectometry/*.java)

$(LIBNAME): .mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/reflectometry/reflectometry*.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/reflectometry
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/reflectometry

.mach: .corba $(CLASSDIR) $(CLASSDIR)/com/syrus/AMFICOM/reflectometry
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/reflectometry/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/reflectometry/corba/*.java
	touch $@

.PHONY:	clean
clean: cleanmach cleancorba javadocsclean
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/reflectometry/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES)

.javadocs: .idl $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-sourcepath $(SRCDIR) \
		-classpath $(APPCLASSPATH) \
		-group "$(MODULE_NAME)" "com.syrus.AMFICOM.reflectometry" \
		-group "$(MODULE_NAME)-IDL" "com.syrus.AMFICOM.reflectometry.corba*" \
		-subpackages "com.syrus.AMFICOM.reflectometry" \
		2>&1 | grep -vE '(XMLInputStream|com\.syrus\.AMFICOM\.[a-z]+\.corba\..*@serial)'
	touch $@
