#
# $Id: Makefile,v 1.13 2005/09/21 14:00:58 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := filter

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/util.jar

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/filter/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/logic/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/newFilter/*.java)


all: $(LIBNAME)

$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/newFilter/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/newFilter/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach


#### Install ####
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)

