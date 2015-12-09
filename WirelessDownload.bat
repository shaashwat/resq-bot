@echo off
REM Use this Batch file to wirelessly conenct to to Android Phones through a Router.
REM This assumes that you can connected your phones to the router, but that the
REM IP addresses may change each time they connect (ie: you can't lock them in).  
REM So to allow for this, you need to look at the phones while this scirp is running,
REM and enter each IP address when prompted.
REM For more help, go to www.YouTube.com/user/GEARSinc/playlists
REM
echo --  Starting ADB
adb kill-server
echo --  Make sure the phone is connected to the computer via USB
set /p ok= --  Hit enter when phone is plugged in:
adb usb
Timeout 10
adb tcpip 5555
echo --
echo --  Make sure your phone ALSO is connected to the "Router" network.
echo --  Click the Settings-WiFi link to get the first phone's IP address
set /p phoneip= --  Enter the IP address here:  
adb connect %phoneip%
adb devices
set /p ok= --  Unplug the phone and hit Enter to see the final connection.
adb devices
Timeout 5