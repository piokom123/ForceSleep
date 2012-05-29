# ForceSleep
## Force users to go to bed from time to time
### Version 1.0

## Features:
 * countdown to next sleep
 * warning messages each x seconds (can be defined in config)
 * causing damage to users that ignored warnings
 * definable hp limit on which plugin will stop causing damage (separately to day and night)
 * translation file (you can easily translate plugin to your language)

## Permissions:
 * forcesleep.immunity: Gives player immunity
 * forcesleep.info: Gives player possibility to use info commands
 
## Commands:
 * /fs info - shows informations about user (first online date, total online time, time from last sleep and time left to next sleep)
 
## Configuration:
  language: EN # plugin language, default is english
  refresh_time: 30 # seconds between users checking (30 is good value)
  sleep_delay: 720 # seconds to next sleep
  warning_delay: 15 # warnings about next 
  minimum_health: # when user have this or lower hp, plugin will stop causing damage
    day: 15 # during the day (user can't go to bed, so it should be higher than night value)
    night: 2 # at night
  
## Translation
You can change plugin language by changing "language" in config file.
Plugin, after restart, will try to download selected language file from the server. If it'll be impossible, plugin will create new empty file where you can put content from default translate_EN.yml and translate it to your language (please contact with me after that, I'll add this file to resources on the server).