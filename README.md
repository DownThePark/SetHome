# SetHome

### Introduction
Looking for a simple SetHome plugin? One without permissions, maybe? Well, you've come to the right place. SetHome is a lightweight plugin designed to do only two things.

1) Set a player's home.
2) Allow the player to go to their home.

This plugin saves each player's home into a "Homes.yml" file under the "SetHome" directory in the plugins folder.

This is great for Survival servers that need something simple and lightweight. Server owners and administraters can use this over any other plugin in favor of extra performance.

### Screenshots:
![Screenshot](https://i.imgur.com/GK3eEFD.png)

### Commands:
- /sethome
- /home

### Permissions:
- None required 

### Extra Features:
- Respawn player to their home saved in "Homes.yml" on death (Can be disabled in 'config.yml' file)
- Play an enderman sound when player teleports to their home (Can be disabled in 'config.yml' file)
- Customize messages under the 'config.yml' file
- Switch between a warm up period and a cooldown for the /home command in the 'config.yml' file
- Automatically reload the config while the server is running

### Changelog:  

6.0.0:
- update api version to 16.5
- add config option to switch between the /home command having a cooldown vs. a warm up period.
- /sethome has a optional cooldown, which was removed in a previous version.

6.1.0:
- fix /home warmup queing up multiple teleports when used in quick succession.
- the config option "sethome-time-delay" has been renamed to "sethome-delay-seconds", and "home-time-delay" to "home-delay-seconds".
- "sethome-command-delay" and "home-command-delay" have been removed from the config. Instead of setting those two to true or false, a delay of 0 is now treated as having no delay at all, in the same way a setting of false would have previously.
- old configs will be automatically converted on startup, properly carrying over their settings to the new config version.
- improved command and error messages.

6.1.1:
- command cooldowns no longer rely on task scheduling, and instead simply track the last time a command was used.
- save player homes only every 5 minutes, and only if /sethome was executed during that time, and on server shutdown. Previously this was done every single time the /sethome command was called by anyone on the server.
- while the server is running, automatically reload the config file when it changes using an asynchronous task, and java.nio.file.WatchService.
