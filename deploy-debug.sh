#!/bin/bash

adb shell pm uninstall us.beamto.newplayer
adb -s $1 install bin/BeamTo-debug.apk
adb shell am start -n us.beamto.newplayer/us.beamto.newplayer.ui.activites.NewMediaPlayerActivity
pause