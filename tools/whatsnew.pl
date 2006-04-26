#!/usr/bin/perl
use strict;

# Executes cvs -q -n up, cvs status,cvs log and prints
# authors and cvs log messages that are related to 'U' and 'P' records.
#
# (F) saa, 2005, 2006

my $targetTag = '';
my $interactive = 0;
foreach (@ARGV) {
    if ($_ eq '-i') { $interactive=1; next; }
    if ($_ eq '-A') { $targetTag='HEAD'; next; }
    if ($_ =~ /^\-r(\w+)$/) { $targetTag=$1; next; }
    die "unknown option: '$_'";
}

#print "using CVS options: $cvsOptions\n" if $cvsOptions ne '';

my $fileCount; # counter of modified files
my $revCount;
my %authors;
my %messages;
my @messages;

sub wider($$$)
{
	my $line = $_[0];
	my $len = $_[1] + 0;
	my $at = $_[2] + 0;
	while (length($line) < $len)
	{
		my $at2 = $at;
		if ($at2 == 2) { $at2 = length($line) % 2; }
		if ($at2) { $line = ' ' . $line }
		else { $line = $line . ' ' }
	}
	return $line;
}

my $targetOpt = $targetTag eq '' ? '' : "-r $targetTag";
my @cvsUpOut;
foreach (`cvs -q -n up -d $targetOpt`)
{
	chomp;
	next if m#^[?] #; # such files are likely to be out of interest at all
	print "$_\n";
	next unless m#^[UP] (.+)#; # ignore C, R, M, A records
	push @cvsUpOut,$1;
}

if ($interactive)
{
	print "-----\n";
} else
{
	# show progress bar on console
	if (scalar @cvsUpOut)
	{
	   print ' ', (' ' x @cvsUpOut), "]\r[";
	}
}

foreach (@cvsUpOut)
{
	print '.' unless $interactive;
	$fileCount++;
	my $ffn = $_;
	my $n = $_;
	my $d = '.';
	if ($ffn =~ m#(.+)/(.+)#) {$n = $2; $d = $1;}

	# print "'$d' / '$n'\n";

	my $workingRev = '';
	my $repositRev = '';
	open FTMP, "cvs status $ffn 2>nul |";
	while(<FTMP>)
	{
		$workingRev = $1 if m/^\s*Working revision\:\s*([\d.]+)/;
		$repositRev = $1 if m/^\s*Repository revision\:\s*([\d.]+)/;
	}
	close FTMP;

	$repositRev = $targetTag if $targetTag ne '';

	my $revSpec = $workingRev eq '' ? '' : "-r$workingRev::$repositRev";
	# print "revSpec = $revSpec\n";

	open FCVSLOG, "cvs log -N $revSpec $ffn |";

	while (<FCVSLOG>)
	{
		chomp;
		last if $_ eq 'description:';
	}

	my $revSeparator = '----------------------------';
	my $lastSeparator = '=============================================================================';
	$_ = <FCVSLOG>;
	chomp;
	die "'$_' is ne '$revSeparator'" unless $_ eq $revSeparator or $_ eq $lastSeparator;

	while (<FCVSLOG>)
	{
		chomp;
		next if !m /^revision [\d.]+/;
		$_ = <FCVSLOG>;
		chomp;
		next if !m /author: (\S+);/;
		my $author = $1;
		$authors{$author}++;
		$revCount++;
		while (<FCVSLOG>)
		{
			chomp;
			last if $_ eq $revSeparator;
			last if $_ eq $lastSeparator;
			$_ = wider("$author ", 11, 0) .$_;
			next if $messages{$_}++;
			push @messages, $_;
			print " * $_\n" if $interactive;
		}
	}

	close FCVSLOG;

}
print "\n" if $fileCount;

if ($revCount)
{
#print "-----\n";
print "Updated files: $fileCount\n";
print "New revisions: $revCount\n";
print "Authors: ", join(", ", sort keys %authors), "\n";
print "Messages #/author/text:\n";
print map {wider($messages{$_}, 2, 1) . " $_\n"} @messages;
}
else
{
print "No updates\n";
}
