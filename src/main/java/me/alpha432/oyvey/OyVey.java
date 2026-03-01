package me.alpha432.oyvey;

import me.alpha432.oyvey.features.gui.HudEditorScreen;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.font.TTFFontRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class OyVey implements ModInitializer, ClientModInitializer {

    public static final String NAME = "Onyx";
    public static final String VERSION = SharedConstants.getGameVersion().name();
    public static float TIMER = 1f;

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    // Managers
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    public static HudEditorScreen hudEditorScreen;

    // Custom Font
    public static TTFFontRenderer fontRenderer;

    // ===============================
    // COMMON INIT
    // ===============================

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing " + NAME + " v" + VERSION);

        // Managers
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();

        TextUtil.init();

        LOGGER.info("Managers initialized.");
    }

    // ===============================
    // CLIENT INIT
    // ===============================

    @Override
    public void onInitializeClient() {

        LOGGER.info("Initializing client...");

        // Init managers that require MC client
        eventManager.init();
        moduleManager.init();

        hudEditorScreen = new HudEditorScreen();

        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    LOGGER.info("Saving config...");
                    configManager.save();
                })
        );

        // ===============================
        // FONT LOADING
        // ===============================

        try (InputStream stream =
                     OyVey.class.getClassLoader()
                             .getResourceAsStream("assets/onyx/font/verdana.ttf")) {

            if (stream == null) {
                LOGGER.error("Font not found: assets/onyx/font/verdana.ttf");
                return;
            }

            fontRenderer = new TTFFontRenderer(stream, 18f);

            LOGGER.info("Custom font loaded successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to load custom font.", e);
        }

        LOGGER.info(NAME + " fully loaded.");
    }
}
