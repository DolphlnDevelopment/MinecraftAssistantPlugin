# WARNING: DO NOT USE THIS ON PRODUCTION. IT IS UNESTABLE, AND NOT SECURE. THIS IS JUST A PROOF OF CONCEPT

# MinecraftAssistantPlugin

Minecraft, but you have an assistant. It sounds pretty awesome, right? Right now this project is really dirty and unstable, and not thread safe. The main idea comes from Silicon Valley, and it is made for a plugin jam hosted by Stephen.

# How to use

Download and move this plugin to `plugins/` (spigot or paper 1.17+). 
This plugin uses one extra port to host a ServerSocket. If you're using a hosting, you will need to ask/create a new allocation for a new port. Then, you'll need to setup it on the plugin's config.
You will need to download [the client](https://github.com/DolphlnDevelopment/MinecraftAssistantClient) and follow the steps there.

# What doesn't work

- Threads are not being stopped correctly. (The main thread is not blocked in any instant)
- Speech recognition isn't perfect (Maybe using AWS or GCP speech service will work better)
- If the player disconnects, commands can still be ran.
- No comprovation for clients, and linking.
- Probably there are some memory leaks.
