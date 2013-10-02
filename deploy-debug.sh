#!/bin/bash -e
set -e
if [ $# -eq 0 ] 
 then
  sh build-debug.sh
fi  

output=`adb shell pm uninstall us.beamto.newplayer`
echo $output
if [[ $output == *"Failure"* ]]; then
   echo "Exiting the deploy script"
   exit 1
fi
adb -d install bin/BeamTo-debug.apk
adb shell am start -n us.beamto.newplayer/us.beamto.newplayer.ui.activites.NewMediaPlayerActivity
