if [ -f /etc/bashrc ]
then
	. /etc/bashrc
fi

alias ll='ls -laF'

if [ "${PS1}" ]
then
	PS1="\[\033[1;33;42m\][\u@\h \W]\$\[\033[0m\] "
fi
