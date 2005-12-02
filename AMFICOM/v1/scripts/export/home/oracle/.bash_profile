if [ -f ~/.bashrc ]
then
	. ~/.bashrc
fi

export TEMP="/tmp"
export TMPDIR="${TEMP}"

export NLS_LANG="RUSSIAN_RUSSIA.CL8KOI8R"

export ORACLE_OWNER="${LOGNAME}"
export ORACLE_TERM="xterm"
export ORACLE_SID="vltava"

ORACLE_VERSION="10.1.0.3.0_db"

export ORACLE_BASE="/orabase"
export ORACLE_HOME="/opt/oracle/product/${ORACLE_VERSION}"
export ORACLE_DOC="/opt/oracle/doc/${ORACLE_VERSION}"
export ORA_NLS33="${ORACLE_HOME}/ocommon/nls/admin/data"
export TNS_ADMIN="/opt/oracle/config/${ORACLE_VERSION}"
export PATH="${ORACLE_HOME}/bin:${PATH}"
if [ "${LD_LIBRARY_PATH_64}" ]
then
	export LD_LIBRARY_PATH="${ORACLE_HOME}/lib32:${LD_LIBRARY_PATH}"
	export LD_LIBRARY_PATH_64="${ORACLE_HOME}/lib:${LD_LIBRARY_PATH_64}"
else
	export LD_LIBRARY_PATH="${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}"
fi

unset ORACLE_VERSION

umask 022
