adb shell pm uninstall com.example.newplayer
adb -d install bin\BeamTo-release.apk
adb shell am start -n com.example.newplayer/com.example.beamto.NewMediaPlayer
pause