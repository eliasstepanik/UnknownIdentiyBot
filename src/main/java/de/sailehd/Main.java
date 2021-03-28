package de.sailehd;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import de.sailehd.support.Debug;
import de.sailehd.support.EasyBase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.LoggerFactory;


import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.FileHandler;

public class Main{


    public static void main(String[] args) throws IOException, LoginException, URISyntaxException {
        EasyBase config = new EasyBase("Config");


        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);


        JDA api = JDABuilder.createDefault((String) config.getData("BotToken"))
                .addEventListeners(new Listener((String) config.getData("Prefix"), config))
                //.addEventListeners(new ReactionListener(config))
                .setActivity(Activity.playing("Wartet auf Reaction")).build();

        Logger logger = lc.getLogger(Main.class);

    }

}
