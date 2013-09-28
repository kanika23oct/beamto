#!/bin/bash

if [ $# eq 0 ]
  then
   sh build-release.sh
fi

adb shell pm uninstall com.example.newplayer

adb -d install bin/BeamTo-release.apk
adb shell am start -n us.beamto.newplayer/us.beamto.newplayer.ui.activites.NewMediaPlayerActivity
pause