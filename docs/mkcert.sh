#!/bin/bash

# Directories
#############

ssldir=/home/webapps/ssl_certs
webroot=/home/webapps/www.vybor1491.cz/ROOT
wellknowndir=".well-known"
acmechallengedir="acme-challenge"

echo "Creating directories..."

if [ ! -d $webroot/$wellknowndir ]; then
	echo "$webroot/$wellknowndir doesnt exist"
	echo "creating..."
	sudo mkdir $webroot/$wellknowndir
	sudo chown berk $webroot/$wellknowndir
        sudo chgrp tomcat7 $webroot/$wellknowndir
        sudo chmod 750 $webroot/$wellknowndir
fi

if [ ! -d $webroot/$wellknowndir/$acmechallengedir ]; then
	echo "$webroot/$wellknowndir/$acmechallengedir doesnt exist"
	echo "creating..."
        sudo mkdir $webroot/$wellknowndir/$acmechallengedir
	sudo chown berk $webroot/$wellknowndir/$acmechallengedir
        sudo chgrp tomcat7 $webroot/$wellknowndir/$acmechallengedir
        sudo chmod 750 $webroot/$wellknowndir/$acmechallengedir
fi

if [ ! -d $ssldir ]; then
	echo "$ssldir doesnt exist"
	echo "creating..."
	sudo mkdir $ssldir
	sudo chown berk $ssldir
	sudo chgrp tomcat7 $ssldir
	chmod 750 $ssldir
fi



# Get Certificate
#################

echo "Getting certificate..."

# https://github.com/srvrco/getssl
getssl www.vybor1491.cz

# To Create Bundle
##################

echo "Creating bundle..."

# https://blogs.oracle.com/blogbypuneeth/steps-to-create-a-jks-keystore-using-key-and-crt-files


cat $ssldir/chain.crt /etc/ssl/certs/ca-certificates.crt > $ssldir/all.crt

openssl pkcs12 -export -chain \
	-CAfile $ssldir/all.crt \
	-in $ssldir/www.vybor1491.cz.crt \
	-inkey $ssldir/www.vybor1491.cz.key \
	-out $ssldir/keystore.p12 \
	-name tomcat \
	-passout pass:***

cd $ssldir

keytool -importkeystore \
	-destkeystore keystore.jks \
	-deststorepass changeit \
	-srckeystore keystore.p12 \
	-srcstoretype pkcs12 \
	-srcstorepass *** \
	-alias tomcat \
	

echo "please run: sudo /etc/init.d/tomcat7 restart"

