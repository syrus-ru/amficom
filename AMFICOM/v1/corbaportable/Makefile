IDLDIR = idl
SRCDIR = src

.PHONY: idl
idl: .idl

.idl:
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/common.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/reflect.idl
	touch $@
