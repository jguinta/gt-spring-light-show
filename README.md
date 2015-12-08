# Music-driven backyard light show

##Required items:
1. Android mobile device.
2. WF 310 DMX Receiver.[You can buy one here] (http://www.ebay.com/itm/WF310-WIFI-DMX-Convertor-Controller-Dimmer-for-High-Power-LED-Light-Lamp-/321741608302?hash=item4ae94b556e:g:UCEAAOSwNSxVQdEa)
3. DMX lights of your choice. (Note: we support regular, strip and moving head DMX lights as of right now)
4. DMX amplifier, to amplify signal from the receiver to the lights.[You can buy one here](http://www.amazon.com/RioRand-DMX512-Amplifier-Separator-Isolated/dp/B00DH5N1X8/ref=sr_1_2?ie=UTF8&qid=1449359103&sr=8-2&keywords=dmx+amplifier)
5. DMX cables to connect the lights with receiver.
6. Our Android app, it's not on google play right now but [here is our apk](https://github.com/jguinta/gt-spring-light-show/blob/master/release.apk?raw=true)

###MBLS App
Our app consists of Local and Spotify player.

######    Local Player:
  The Android application allowed the user to play locally stored music files using a custom-designed media player, based upon Google’s  Ringdroid application, and was capable of playing .mp3 files. Once a song was selected to play, it was converted to raw PCM format. This provided access to the raw audio data for analysis at the cost of a several second loading time. Once loaded, the user could play, pause, fast-forward, and rewind to any point in the song. Furthermore, a waveform visualized the audio for the user on the application itself, in addition to the light fixtures. Due to the preprocessing of the audio to turn it into PCM format, only one song could be viewed at a time. Future work could enable users to browse their library while playing a song, or better structuring the application by provided search and sorting functionality.
  
######    Spotify Player:
  The second method of music input allowed the user to play music from Spotify, an app that allows users to listen to their favorite music by streaming it from a web service. MBLS used  Spotify’s Android SDK API. The ability to stream music from Spotify allowed for dynamic music selection as opposed to being forced to download music prior to use. Spotify support required higher power usage rather than playing local music files due to additional internet usage to stream music. Using Spotify alone was found to consume  on average 1% of power per minute  due to high usage of the transceiver circuit. The Spotify portion of the application enabled users to search for tracks by name or by artist as well as gave access to the user’s saved songs and playlists. The application supported immediately playing or queueing songs, shuffle, repeat, play, pause, and skip functionality. Future efforts could be made to allow users to view details about the currently playing song or to monitor their created playlist.
  
  
Currently we support WiFi and Ad-hoc mode (Note: WF 310 DMX receiver needs to be specifically configured for that.)

#### WiFi mode
  Receiver and mobile device are connected to same wi-fi network. We support both spotify and local player in this mode. 
  
#### Ad-hoc mode(no internet required)
  Mobile device connects directly to the WF 310 receiver. Only local player is supported in this mode, as Spotify requires Wi-Fi conncetion
  
##  Setup WF 310:
1. Install Wi-Fi manager from cd that comes with the recevier.
2. Connect receiver to your PC via USB.
3. Open Wi-Fi manager wait for a few seconds to let your PC recognize the receiver.
4. Select mode you would like receiver to work in: Wi-Fi or Ad-hoc. (If you selected Wi-Fi select desired network you want your receiver to be on.)
5. Save your settings, and wait for a few seconds to let receiver store your settings.
6. Restart the receiver by disconnecting the power source for 5 seconds.
7. Connect your receiver to the amplifier that is connected to the light fixtures.


## Connect to the receiver 
####    Wi-Fi Mode
      1. Connect your mobile device to the same network as the receiver.
      2. Choose Spotify or Local Player.
      3. Play your favorite music and enjoy the light show!
####    Ad-hoc
      1. Connect your mobile device to the WF 310 receiver directly, it should show up as WF310 in your mobile wi-fi manager.
      2. Play your favorite music via Local Player and enjoy the light show!

Videos with setup and demonstration will be here shortly!!!      
  
  

