if [ "${TERM}" = "sun" -o "${TERM}" = "sun-color" ]
then
	# vt100 or dtterm also work
	TERM=ansi
fi

clear
