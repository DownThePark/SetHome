package me.downthepark.sethome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.logging.Level;

public class SetHome extends JavaPlugin
{
    private FileConfiguration config    = getConfig();
    private File              homesFile = new File(getDataFolder(), "Homes.yml");
    private YamlConfiguration homes     = YamlConfiguration.loadConfiguration(homesFile);
    private boolean homesHasChanged     = false;
    private Plugin thisPlugin           = this;

    private HashMap<Player, Long> lastUsedHome;
    private HashMap<Player, Long> lastUsedSetHome;

    private static final String prefixError = ChatColor.DARK_RED + "[" + ChatColor.RED + "*" + ChatColor.DARK_RED + "] " + ChatColor.GRAY;

    @Override // --- This is called by the server to get command information.
    public boolean
    onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean success = true;
        switch (command.getName())
        {
            case "home":
            {
                if (sender instanceof Player)
                {
                    Player player = (Player) sender;
                    if (homeIsSet(player))
                    {
                        int coolDownTime = config.getInt("home-delay-seconds");
                        if (coolDownTime > 0)
                        {
                            double SecondsSinceLastUse = ((System.currentTimeMillis() - lastUsedHome.getOrDefault(player, 0L)) * 0.001);
                            if (SecondsSinceLastUse < coolDownTime) // Command is ON cooldown
                            {
                                int SecondsLeft = (int) (coolDownTime - SecondsSinceLastUse);
                                if (config.getBoolean("home-warmup-instead-of-cooldown"))
                                {
                                    player.sendMessage(prefixError + "Teleporting after " + ChatColor.RED + SecondsLeft + ChatColor.GRAY + " seconds...");
                                }
                                else
                                {
                                    player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + SecondsLeft + ChatColor.GRAY + " seconds.");
                                }
                            }
                            else // Command is OFF cooldown
                            {
                                if (config.getBoolean("home-warmup-instead-of-cooldown"))
                                {
                                    getServer().getScheduler().scheduleSyncDelayedTask(this, () -> sendPlayerHome(player), 20L * coolDownTime);
                                    player.sendMessage(prefixError + "Teleporting after " + ChatColor.RED + coolDownTime + ChatColor.GRAY + " seconds...");
                                }
                                else
                                {
                                    sendPlayerHome(player);
                                }
                                lastUsedHome.put(player, System.currentTimeMillis());
                            }
                        }
                        else
                        {
                            sendPlayerHome(player);
                        }
                    }
                    else
                    {
                        player.sendMessage(prefixError + "You must first use /sethome");
                        success = false;
                    }
                }
                else // Command is unavailable from the console
                {
                    getLogger().log(Level.WARNING, "Only players can use this command.");
                    success = false;
                }
                break;
            }
            case "sethome":
            {
                if (sender instanceof Player)
                {
                    Player player = (Player) sender;
                    int coolDownTime = config.getInt("sethome-delay-seconds");
                    if (coolDownTime > 0)
                    {
                        double SecondsSinceLastUse = ((System.currentTimeMillis() - lastUsedSetHome.getOrDefault(player, 0L)) * 0.001);
                        if (SecondsSinceLastUse < coolDownTime) // Command is ON cooldown
                        {
                            int SecondsLeft = (int) (coolDownTime - SecondsSinceLastUse);
                            player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + SecondsLeft + ChatColor.GRAY + " seconds.");
                        }
                        else // Command is OFF cooldown
                        {
                            setPlayerHome(player);
                            lastUsedSetHome.put(player, System.currentTimeMillis());
                        }
                    }
                    else
                    {
                        setPlayerHome(player);
                    }
                }
                else // Command is unavailable from the console
                {
                    getLogger().log(Level.WARNING, "Only players can use this command.");
                    success = false;
                }
                break;
            }
            default:
            {
                getLogger().log(Level.WARNING, "Unrecognized Command received.");
                success = false;
            }
        }
        return success;
    }

    @Override // Called when this plugin is enabled
    public void
    onEnable()
    {
        getCommand("sethome").setExecutor((CommandExecutor)this);
        getCommand("home"   ).setExecutor((CommandExecutor)this);
        getServer().getPluginManager().registerEvents(new SetHomeEvents(this), this);
        // Load configuration and save file in data folder, and start config auto reloading task
        saveDefaultConfig();
        reloadConfig2();
        boolean foundOldConfig = convertFromOldConfig();
        if (foundOldConfig)
        {
            saveConfig();
            reloadConfig2();
        }
        startAutoReloadConfigTask();
        // Save "homes" to disk, and set up a synchronous task to save every 5 minutes
        if (!homesFile.exists())
        {
            saveHomesFile();
        }
        new BukkitRunnable()
        {
            public void run()
            {
                if (homesHasChanged)
                {
                    saveHomesFile();
                    homesHasChanged = false;
                }
            }
        }.runTaskTimer(this, 6000, 6000); // 6000 ~= 5 minutes between each check (20 ticks/second * 300 seconds)
        // Initialize command cooldown HashMaps
        lastUsedHome    = new HashMap<>();
        lastUsedSetHome = new HashMap<>();
    }

    @Override // Called when this plugin is disabled (e.g. at server shutdown)
    public void
    onDisable()
    {
        saveHomesFile();
    }

    private void
    saveHomesFile()
    {
        try
        {
            homes.save(homesFile);
            getLogger().log(Level.INFO, "Homes have been saved to disk."); // TODO: Disable this message?
        }
        catch (IOException e)
        {
            getLogger().log(Level.SEVERE, "Could not save homes file.\nHere is the stack trace:");
            e.printStackTrace();
        }
    }

    private void
    setPlayerHome(Player player)
    {
        Location location    = player.getLocation();
        String playerPrefix  = "Homes." + player.getUniqueId().toString();
        homes.set(playerPrefix + ".X"    , location.getX());
        homes.set(playerPrefix + ".Y"    , location.getY());
        homes.set(playerPrefix + ".Z"    , location.getZ());
        homes.set(playerPrefix + ".Yaw"  , location.getYaw());
        homes.set(playerPrefix + ".Pitch", location.getPitch());
        homes.set(playerPrefix + ".World", location.getWorld().getName());
        homesHasChanged = true;
        if (config.getBoolean("show-sethome-message"))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("sethome-message").replace("%player%", player.getDisplayName())));
        }
    }

    private void
    sendPlayerHome(Player player)
    {
        Location location = getHomeLocation(player);
        player.teleport(location);
        if (config.getBoolean("play-warp-sound"))
        {
            player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
        if (config.getBoolean("show-teleport-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("teleport-message").replace("%player%", player.getDisplayName())));
        }
    }

    boolean
    homeIsSet(Player player)
    {
        return !(homes.getString("Homes." + player.getUniqueId()) == null);
    }

    Location
    getHomeLocation(Player player)
    {
        String playerPrefix = "Homes." + player.getUniqueId().toString();
        return new Location(
                Bukkit.getWorld(homes.getString(playerPrefix  + ".World")),
                homes.getDouble(playerPrefix + ".X"),
                homes.getDouble(playerPrefix + ".Y"),
                homes.getDouble(playerPrefix + ".Z"),
                (float) homes.getLong(playerPrefix + ".Yaw"),
                (float) homes.getLong(playerPrefix + ".Pitch")
        );
    }

    private void
    reloadConfig2()
    {
        reloadConfig();
        config = getConfig();
        config.options().copyDefaults(true);
    }

    private boolean
    convertFromOldConfig()
    {
        // assume that if this old setting exists, then this is an old config which needs to be converted.
        // if it doesn't exists, don't bother checking the rest, as it could not be a valid config anyway.
        // some extra care is taken during conversion to make sure we produce valid entries to our new config.
        boolean foundOldConfig = config.isSet("home-time-delay");
        if (foundOldConfig)
        {
            boolean success = true;
            // Convert /home command
            if (config.isSet("home-command-delay"))
            {
                if (config.isSet("home-time-delay"))
                {
                    if (config.getBoolean("home-command-delay"))
                    {
                        config.set("home-delay-seconds", Math.max(0, config.getInt("home-time-delay")));
                    }
                    else
                    {
                        config.set("home-delay-seconds", 0);
                    }
                }
                else
                {
                    getLogger().log(Level.WARNING, "Corrupted old config file detected! "
                            + "Config contains \"home-command-delay\", but is missing \"home-time-delay\".");
                    success = false;
                }
            }
            else
            {
                getLogger().log(Level.WARNING, "Corrupted old config file detected! "
                        + "Config contains \"home-time-delay\", but is missing \"home-command-delay\".");
                success = false;
            }

            // Convert /sethome command
            if (config.isSet("sethome-command-delay"))
            {
                if (config.isSet("sethome-time-delay"))
                {
                    if (config.getBoolean("sethome-command-delay"))
                    {
                        config.set("sethome-delay-seconds", Math.max(0, config.getInt("sethome-time-delay")));
                    }
                    else
                    {
                        config.set("sethome-delay-seconds", 0);
                    }
                }
                else
                {
                    getLogger().log(Level.WARNING, "Corrupted old config file detected! "
                            + "Config contains \"sethome-command-delay\", but is missing \"sethome-time-delay\".");
                    success = false;
                }
            }
            else
            {
                getLogger().log(Level.WARNING, "Corrupted old config file detected! "
                        + "Config contains \"home-time-delay\", but is missing \"sethome-command-delay\".");
                success = false;
            }
            config.set("sethome-command-delay", null);
            config.set("sethome-time-delay"   , null);
            config.set("home-command-delay"   , null);
            config.set("home-time-delay"      , null);
            if (!success)
            {
                // conversion failed, issue warning.
                getLogger().log(Level.WARNING, "Found a old config file, but it was missing keys unexpectedly. "
                        + "Please check the config file to make sure everything is OK.");
            }
        }
        return foundOldConfig;
    }

    /**
     *  Uses a java WatchService to asynchronously wait for changes to the dataFolder directory.
     *  If a config.yml file is created or modified, a synchronous task is started, which reloads the config file.
     */
    private void
    startAutoReloadConfigTask()
    {
        new BukkitRunnable()
        {
            public void run()
            {
                boolean configHasChanged = false;
                long timeOfLastReload    = System.currentTimeMillis();
                long msSinceLastReload;
                try
                {
                    WatchService watchService   = FileSystems.getDefault().newWatchService();
                    Path         dataFolderPath = getDataFolder().toPath();
                    dataFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
                    WatchKey key;
                    //noinspection InfiniteLoopStatement
                    while (true)
                    {
                        key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents())
                        {
                            // getLogger().log(Level.INFO, "Auto Reload event: " + event.context().toString() + " " + event.kind().toString() + " " + event.count());
                            if(event.context().toString().endsWith("config.yml"))
                            {
                                configHasChanged = true;
                            }
                        }
                        if (configHasChanged)
                        {
                            msSinceLastReload = System.currentTimeMillis() - timeOfLastReload;
                            if (msSinceLastReload > 8000) // Wait at least 8 seconds before reloading again.
                            {
                                new BukkitRunnable()
                                {
                                    public void run()
                                    {
                                        reloadConfig2();
                                        boolean foundOldConfig = convertFromOldConfig();
                                        if (foundOldConfig)
                                        {
                                            saveConfig();
                                            reloadConfig2();
                                        }
                                        getLogger().log(Level.INFO, "Config has been automatically reloaded.");
                                    }
                                }.runTaskLater(thisPlugin, 40); // Do the actual reloading in a synchronous task ~2 seconds later.
                                timeOfLastReload = System.currentTimeMillis() + 2000;
                            }
                            configHasChanged = false;
                            // Thread.sleep(4000);
                        }
                        key.reset();
                    }
                }
                catch (IOException e)
                {
                    getLogger().log(Level.WARNING, "Auto Config reloading has crashed due to an IOException (it won't restart):");
                    e.printStackTrace();
                    cancel();
                }
                catch (InterruptedException e)
                {
                    getLogger().log(Level.WARNING, "Auto Config reloading has stopped due to having been Interrupted (it won't restart):");
                    e.printStackTrace();
                    cancel();
                }
                catch (Exception e)
                {
                    getLogger().log(Level.WARNING, "Auto Config reloading has crashed unexpectedly (it won't restart):");
                    e.printStackTrace();
                    cancel();
                }

            }
        }.runTaskLaterAsynchronously(this, 100);
    }

}