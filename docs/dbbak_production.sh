#!/bin/bash

GBAK="gbak"

USER="***"
PASSWORD="***"

DB_NAME=${1}
BACKUP_NAME="/home/DB/backup/${DB_NAME}_backup_`date +%u`.fbk"
LINK_NAME="/home/DB/backup/${DB_NAME}_backup_current.fbk"

echo "STARTING..."
echo $BACKUP_NAME

if [ -h $LINK_NAME ]
then
rm $LINK_NAME
fi

if [ -f $BACKUP_NAME ]
then
rm $BACKUP_NAME
fi

$GBAK -user $USER -password $PASSWORD localhost:$DB_NAME $BACKUP_NAME
ln -s $BACKUP_NAME $LINK_NAME

echo "DONE!"
