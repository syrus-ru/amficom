#!/usr/bin/perl

%map=();
foreach $line(<STDIN>){
if ($line=~m/\s*(\/\/)?.*"(.+)"\s*,\s*"(.+)"/){
	$rem=$1;
	$k=$2;
	$val=$3;
	$k=~s/ /_/g;	
	$key="$rem$k";
	$key=~s/\/\//#/g;
	$map{$key} = $val;
	}
}

foreach $key(sort keys %map){
print "$key = $map{$key}\n";
}
