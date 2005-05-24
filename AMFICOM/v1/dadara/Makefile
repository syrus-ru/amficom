MODULE_NAME := dadara

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

NATIVE_DIR := ./native/common

include ../tools/Makefile.inc

APPCLASSPATH = $(LIBDIR)/util.jar

#JAVAC=javac
#JAVACFLAGS=-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g

JAVAC=jikes
JAVACFLAGS := -O \
	+E \
	+P \
	+Pno-switchcheck \
	+T=2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(CLASSDIR) \
	-force

$(call checkenve,JAVA_HOME)

.PHONY:	clean cleanmach arch install

PACKAGES = com/syrus/AMFICOM/analysis com/syrus/AMFICOM/analysis/dadara com/syrus/AMFICOM/analysis/dadara/events

$(LIBNAME):	.mach #.native
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH) \
		$(foreach dir,$(PACKAGES),$(wildcard $(SRCDIR)/$(dir)/*.java))

	touch $@

.native:
	$(MAKE) -C native

$(CLASSDIR):
	mkdir $@


clean:	cleanmach
	$(RM) $(LIBNAME)
	$(MAKE) -C native $@

cleanmach:
	$(RM) \
		$(foreach dir,$(PACKAGES),$(CLASSDIR)/$(dir)/*.class)
	$(RM) .mach

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(foreach dir,$(PACKAGES),$(wildcard $(SRCDIR)/$(dir)/*.java))

