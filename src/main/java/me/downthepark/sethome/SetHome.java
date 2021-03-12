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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class SetHome extends JavaPlugin
{
    private FileConfiguration config    = getConfig();
    private File              homesFile = new File(getDataFolder(), "Homes.yml");
    private YamlConfiguration homes     = YamlConfiguration.loadConfiguration(homesFile);

    private HashMap<Player, Integer> cooldownTimeHome;
    private HashMap<Player, BukkitRunnable> cooldownTaskHome;
    private HashMap<Player, Integer> cooldownTimeSetHome;
    private HashMap<Player, BukkitRunnable> cooldownTaskSetHome;

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
                        int coolDown = config.getInt("home-time-delay");
                        if ((coolDown > 0) && config.getBoolean("home-command-delay"))
                        {
                            if (config.getBoolean("home-warmup-instead-of-cooldown"))
                            {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&7*&8]&7 Teleporting after " + coolDown + " seconds..."));
                                getServer().getScheduler().scheduleSyncDelayedTask(this, () -> sendPlayerHome(player), 20L * coolDown);
                            }
                            else {
                                if (cooldownTimeHome.containsKey(player))
                                {
                                    player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + cooldownTimeHome.get(player) + ChatColor.GRAY + " seconds.");
                                }
                                else
                                {
                                    sendPlayerHome(player);
                                    setCoolDownTimeHome(player, coolDown);
                                }
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
                    int coolDown = config.getInt("sethome-time-delay");
                    if ((coolDown > 0) && config.getBoolean("sethome-command-delay"))
                    {
                        if (cooldownTimeSetHome.containsKey(player))
                        {
                            player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + cooldownTimeSetHome.get(player) + ChatColor.GRAY + " seconds.");
                        }
                        else
                        {
                            setPlayerHome(player);
                            setCoolDownTimeSetHome(player, coolDown);
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
        // --- Load configuration defaults and save file in data folder
        config.options().copyDefaults(true);
        saveDefaultConfig();
        try
        {
            config.save(getDataFolder() + File.separator + "config.yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (!homesFile.exists())
        {
            saveHomesFile();
        }
        cooldownTimeHome = new HashMap<>();
        cooldownTaskHome = new HashMap<>();
        cooldownTimeSetHome = new HashMap<>();
        cooldownTaskSetHome = new HashMap<>();
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
        // TODO: Is this thread safe?
        try
        {
            homes.save(homesFile);
        }
        catch (IOException e)
        {
            getLogger().log(Level.SEVERE, "Could not save homes file.\nHere is the stack trace:");
            e.printStackTrace();
        }
    }

    private void
    sendPlayerHome(Player player)
    {
        player.teleport(getHomeLocation(player));
        if (config.getBoolean("play-warp-sound"))
        {
            player.playSound(getHomeLocation(player), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
        if (config.getBoolean("show-teleport-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("teleport-message").replace("%player%", player.getDisplayName())));
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
        saveHomesFile(); // Write to disk for persistent homes
        if (config.getBoolean("show-sethome-message"))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("sethome-message").replace("%player%", player.getDisplayName())));
        }
    }

    private void
    setCoolDownTimeHome(Player player, int coolDown)
    {
        cooldownTimeHome.put(player, coolDown);
        cooldownTaskHome.put(player,
                new BukkitRunnable()
                {
                    public void run()
                    {
                        cooldownTimeHome.put(player, cooldownTimeHome.get(player) - 1);
                        if (cooldownTimeHome.get(player) <= 0)
                        {
                            cooldownTimeHome.remove(player);
                            cooldownTaskHome.remove(player);
                            cancel();
                        }
                    }
                }
                );
        cooldownTaskHome.get(player).runTaskTimer(this, 20, 20);
    }

    private void
    setCoolDownTimeSetHome(Player player, int coolDown)
    {
        cooldownTimeSetHome.put(player, coolDown);
        cooldownTaskSetHome.put(player,
                new BukkitRunnable()
                {
                    public void run()
                    {
                        cooldownTimeSetHome.put(player, cooldownTimeSetHome.get(player) - 1);
                        if (cooldownTimeSetHome.get(player) <= 0)
                        {
                            cooldownTimeSetHome.remove(player);
                            cooldownTaskSetHome.remove(player);
                            cancel();
                        }
                    }
                }
                );
        cooldownTaskSetHome.get(player).runTaskTimer(this, 20, 20);
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

    boolean
    homeIsSet(Player player)
    {
        return !(homes.getString("Homes." + player.getUniqueId()) == null);
    }

}