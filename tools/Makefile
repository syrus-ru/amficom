MODULE_NAME = tools
LIBNAME = $(MODULE_NAME).jar
ARCHFILE = $(MODULE_NAME).tar.gz

LIBDIR=../lib

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,PMD_HOME)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/pmd/rules/*.java) 


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com rulesets

.PHONY: clean
clean: 
	rm -f $(LIBNAME)

.mach: $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(PMD_JARS) \
		$(SOURCES)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile Makefile.java.inc.mk Makefile.cpp.inc.mk \
		common.properties \
		copyjars \
		archlogs \
		oracledb \
		orbd \
		amficom \
		amficomsrv amficomtab \
		amficomsrvenv amficomsrvrc

