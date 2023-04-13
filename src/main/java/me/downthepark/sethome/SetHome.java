package me.downthepark.sethome;

// --- Import Bukkit libraries

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

// --- Import Java libraries

// --- Beginning of SetHome class
public class SetHome extends JavaPlugin { // --- SetHome is a JavaPlugin (Bukkit plugin)

    // --- Create private instance of 'Homes.yml' file
    private File file = new File(getDataFolder(), "Homes.yml");
    // --- Create package-private instance of 'Homes.yml' (configurable type)
    YamlConfiguration homes = YamlConfiguration.loadConfiguration(file);

    // --- Create instance of config from Bukkit's getConfig() method. (kind of useless)
    private FileConfiguration config = getConfig();

    private HashMap<Player, Integer> cooldownTimeHome;
    private HashMap<Player, BukkitRunnable> cooldownTaskHome;

    private HashMap<Player, Integer> cooldownTimeSetHome;
    private HashMap<Player, BukkitRunnable> cooldownTaskSetHome;

    // --- Create variable of String type that simplifies handling the error prefix for messages
    private static final String prefixError = ChatColor.DARK_RED + "[" + ChatColor.RED + "*" + ChatColor.DARK_RED + "] " + ChatColor.GRAY;

    // --- Create instance of SetHomeUtils and pass in 'JavaPlugin' as parameter
    private SetHomeUtils utils = new SetHomeUtils(this);

    // --- Function called onCommand() that is called by the server to get command information
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // --- Check if command equals 'sethome'
        if (command.getName().equals("sethome")) {
            // --- Check if the one sending the command is the Console
            if (sender instanceof ConsoleCommandSender) {
                // --- If the sender if Console, tell them to piss off    <-- broo what?
                getLogger().log(Level.WARNING, "Only players can use this command.");

            } else if (sender instanceof Player) { // --- Check if the sender is a Player
                // --- If the sender is a player, continue below
                Player player = (Player) sender; // --- Create instance of Player and cast it to sender

                if (config.getBoolean("sethome-command-delay")) {
                    int coolDown = config.getInt("sethome-time-delay");
                    if (cooldownTimeSetHome.containsKey(player)) {
                        player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + cooldownTimeSetHome.get(player) + ChatColor.GRAY + " seconds.");
                    } else {
                        setPlayerHome(player);
                        setCoolDownTimeSetHome(player, coolDown);
                    }
                } else {
                    setPlayerHome(player);
                }


            } else { // --- If anything goes wrong, tell the sender there was some sort of error that took place
                sender.sendMessage(prefixError + "There was an error performing this command.");
            }

        } else if (command.getName().equals("home")) { // --- I think I'm gonna take a break with the comments for now.

            if (sender instanceof ConsoleCommandSender) {
                getLogger().log(Level.WARNING, "Only players can use this command.");
            } else if (sender instanceof Player) {

                Player player = (Player) sender;

                if (utils.homeIsNull(player)) {
                    player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "*" + ChatColor.DARK_RED + "] " + ChatColor.GRAY + "You must first use /sethome");
                } else {
                    if (config.getBoolean("home-command-delay")) {
                        int coolDown = config.getInt("home-time-delay");
                        if (cooldownTimeHome.containsKey(player)) {
                            player.sendMessage(prefixError + "You must wait for " + ChatColor.RED + cooldownTimeHome.get(player) + ChatColor.GRAY + " seconds.");
                        } else {
                            sendPlayerHome(player);
                            setCoolDownTimeHome(player, coolDown);
                        }
                    } else {
                        sendPlayerHome(player);
                    }
                }
            }
        } else if (command.getName().equals("deletehome")) {
            // --- Check if the one sending the command is the Console
            if (sender instanceof ConsoleCommandSender) {
                // --- If the sender if Console, tell them to piss off
                getLogger().log(Level.WARNING, "Only players can use this command.");

            } else if (sender instanceof Player) { // --- Check if the sender is a Player
                // --- If the sender is a player, continue below
                Player player = (Player) sender; // --- Create instance of Player and cast it to sender

                if (utils.homeIsNull(player)) {
                    player.sendMessage(prefixError + "No home found! First set your home with /sethome before you can delete it.");
                } else {
                    deletePlayerHome(player);
                }
            } else { // --- If anything goes wrong, tell the sender there was some sort of error that took place
                sender.sendMessage(prefixError + "There was an error performing this command.");
            }

        }

        return false;
    }

    public void onEnable() {

        // --- Make commands work using this 'getCommand()' function
        getCommand("sethome").setExecutor(this);
        getCommand("home").setExecutor(this);
        getCommand("deletehome").setExecutor(this);

        // --- Register plugin events to server
        getServer().getPluginManager().registerEvents(new SetHomeEvents(this), this);

        // --- Load configuration defaults and save file in data folder
        config.options().copyDefaults(true);
        saveDefaultConfig();

        try {
            config.save(getDataFolder() + File.separator + "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- Check if 'Homes.yml' exists - if not, create a new one
        if (!file.exists()) {
            saveHomesFile();
        }
        cooldownTimeHome = new HashMap<>();
        cooldownTaskHome = new HashMap<>();

        cooldownTimeSetHome = new HashMap<>();
        cooldownTaskSetHome = new HashMap<>();
    }

    // --- Method to save 'Homes.yml' file
    public void saveHomesFile() {
        try {
            homes.save(file);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save homes file.\nHere is the stack trace:");
            e.printStackTrace();
        }
    }

    void sendPlayerHome(Player player) {
        utils.sendHome(player);
        if (config.getBoolean("play-warp-sound")) {
            player.playSound(utils.getHomeLocation(player), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }
        String strFormatted = config.getString("teleport-message").replace("%player%", player.getDisplayName());
        if (config.getBoolean("show-teleport-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', strFormatted));
        }
    }

    void setPlayerHome(Player player) {
        // --- Set player's home by saving it to a file (Homes.yml)
        utils.setHome(player);
        // --- If option 'show-sethome-message' is enabled in config, show the player the 'sethome-message' as defined in 'config.yml'
        if (config.getBoolean("show-sethome-message")) {
            // --- Create instance of a String that is formatted from the 'config.yml' file.
            String strFormatted = config.getString("sethome-message").replace("%player%", player.getDisplayName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', strFormatted));
        }
    }

    void deletePlayerHome(Player player) {
        // --- Delete player's home by removing it from the file (Homes.yml)
        utils.deleteHome(player);
        // --- If option 'show-deletehome-message' is enabled in config, show the player the 'deletehome-message' as defined in 'config.yml'
        String strFormatted = config.getString("deletehome-message").replace("%player%", player.getDisplayName());
        if (config.getBoolean("show-deletehome-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', strFormatted));
        }
    }

    void setCoolDownTimeHome(Player player, int coolDown) {
        cooldownTimeHome.put(player, coolDown);
        cooldownTaskHome.put(player, new BukkitRunnable() {
            public void run() {
                cooldownTimeHome.put(player, cooldownTimeHome.get(player) - 1);
                if (cooldownTimeHome.get(player) == 0) {
                    cooldownTimeHome.remove(player);
                    cooldownTaskHome.remove(player);
                    cancel();
                }
            }
        });
        cooldownTaskHome.get(player).runTaskTimer(this, 20, 20);
    }
    void setCoolDownTimeSetHome(Player player, int coolDown) {
        cooldownTimeSetHome.put(player, coolDown);
        cooldownTaskSetHome.put(player, new BukkitRunnable() {
            public void run() {
                cooldownTimeSetHome.put(player, cooldownTimeSetHome.get(player) - 1);
                if (cooldownTimeSetHome.get(player) == 0) {
                    cooldownTimeSetHome.remove(player);
                    cooldownTaskSetHome.remove(player);
                    cancel();
                }
            }
        });
        cooldownTaskSetHome.get(player).runTaskTimer(this, 20, 20);
    }
}