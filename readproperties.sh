#/bin/sh

file="./application.properties"

pause(){
  read -p "$*"
}

if [ -f "$file" ]
then
#    echo "$file found."

name=`sed '/^\#/d' $file | grep 'app.name'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
version=`sed '/^\#/d' $file | grep 'app.version'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`

echo $name-$version.war

else
   echo "$file not found."
fi