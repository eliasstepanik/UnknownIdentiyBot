package de.sailehd;


import de.sailehd.support.Debug;
import de.sailehd.support.EasyBase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;


import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException, LoginException, URISyntaxException {
        EasyBase config = new EasyBase("Config");



        JDA api = JDABuilder.createDefault((String) config.getData("BotToken"))
                .addEventListeners(new Listener((String) config.getData("Prefix"), config))
                .setActivity(Activity.playing("Wartet auf Reaction"))
                .build();

    }

}
