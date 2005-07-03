#!/usr/bin/perl

use locale;
@mkeys=();

$i=0;
foreach $line(<STDIN>){
if ($line=~m/^\s*(\w+)\s+/){
	$key = $1;
	$keys{$key}=$line;
	$mkeys[$i++]=$key;
	}
}

foreach $key(@mkeys){
#print "$key\n";
$columnName = "COLUMN_".uc($key);
print "\t//$keys{$key}";
print "\tpublic static final String $columnName\t= \"$key\";\n"
}
