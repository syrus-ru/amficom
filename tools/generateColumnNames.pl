#!/usr/bin/perl

use locale;

foreach $line(<STDIN>){
if ($line=~m/^\s*(\w+)\s+/){
	$key = $1;
	$keys{$key}=$line;
	}
}

foreach $key(sort keys %keys){
#print "$key\n";
$columnName = "COLUMN_".uc($key);
print "\t//$keys{$key}";
print "\tpublic static final String $columnName\t= \"$key\";\n"
}
