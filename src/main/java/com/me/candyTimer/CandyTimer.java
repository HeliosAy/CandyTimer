package com.me.candyTimer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CandyTimer extends JavaPlugin {
    private FileConfiguration timersConfig;
    private Map<String, TimerData> timersCache;
    private Set<String> executedToday;
    private String lastCheckedTime = "";

    @Override
    public void onEnable() {
        createFolderAndConfig();
        loadTimersConfig();
        cacheTimers();
        startTimerChecker();

        // Gece yarısında executed set'ini temizle
        startMidnightReset();

        getLogger().info("CandyTimer plugin aktif!");
    }

    private void createFolderAndConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "timers.yml");
        if (!configFile.exists()) {
            getLogger().info("timers.yml dosyası bulunamadı, oluşturuluyor...");
            saveResource("timers.yml", false);
        }
    }

    private void loadTimersConfig() {
        timersConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "timers.yml"));
    }

    private void cacheTimers() {
        timersCache = new HashMap<>();
        executedToday = new HashSet<>();

        ConfigurationSection timers = timersConfig.getConfigurationSection("timers");
        if (timers == null) {
            getLogger().warning("timers.yml dosyasındaki 'timers' bölümü bulunamadı!");
            return;
        }

        for (String key : timers.getKeys(false)) {
            String time = timers.getString(key + ".time");
            List<String> commands = timers.getStringList(key + ".commands");

            if (time != null && commands != null && !commands.isEmpty()) {
                timersCache.put(key, new TimerData(time, commands));
            }
        }

        getLogger().info(timersCache.size() + " timer yüklendi!");
    }

    private void startTimerChecker() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            // Aynı dakikada tekrar kontrol etmeyi önle
            if (currentTime.equals(lastCheckedTime)) {
                return;
            }
            lastCheckedTime = currentTime;

            // Sadece ilgili timer'ları kontrol et
            for (Map.Entry<String, TimerData> entry : timersCache.entrySet()) {
                String timerName = entry.getKey();
                TimerData timerData = entry.getValue();

                // Bu timer bugün zaten çalıştırıldı mı?
                String dailyKey = timerName + "_" + currentTime;
                if (executedToday.contains(dailyKey)) {
                    continue;
                }

                if (currentTime.equals(timerData.time)) {
                    executeCommands(timerName, timerData.commands);
                    executedToday.add(dailyKey);
                }
            }

        }, 0L, 1200L); // 1 dakika = 1200 tick
    }

    private void executeCommands(String timerName, List<String> commands) {
        for (String command : commands) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } catch (Exception e) {
                getLogger().warning("Komut çalıştırılamadı (" + timerName + "): " + command);
                getLogger().warning("Hata: " + e.getMessage());
            }
        }
        getLogger().info("Timer çalıştırıldı: " + timerName + " (" + commands.size() + " komut)");
    }

    private void startMidnightReset() {
        // Her gece 00:00'da executed set'ini temizle
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            if ("00:00".equals(currentTime)) {
                executedToday.clear();
                getLogger().info("Günlük timer listesi sıfırlandı!");
            }
        }, 0L, 1200L);
    }

    // Config reload komutu için || METHODU EKLEDIM AMA KOMUTU KODLAMAYA ÜŞENDİM :))
    public void reloadTimers() {
        loadTimersConfig();
        cacheTimers();
        getLogger().info("timers.yml yeniden yüklendi!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("CandyTimer plugin kapatıldı!");
    }

    // Timer verilerini tutmak için yardımcı sınıf
    private static class TimerData {
        final String time;
        final List<String> commands;

        TimerData(String time, List<String> commands) {
            this.time = time;
            this.commands = new ArrayList<>(commands);
        }
    }
}