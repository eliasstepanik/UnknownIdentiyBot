package de.sailehd;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import de.sailehd.support.Debug;
import de.sailehd.support.EasyBase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.hc.core5.http.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    private static final String clientId = "e7d733680aa84f549bb87a35375dadd5";
    private static final String clientSecret = "eddd5cd9a6e74afca3d3d2ea9c77cd3f";


    public static void main(String[] args) throws IOException, LoginException, ParseException, SpotifyWebApiException, URISyntaxException {
        EasyBase config = new EasyBase("Config");



        JDA api = JDABuilder.createDefault("ODE0NTgxODExNTE4NjM2MDc3.YDf8bg.dXVUDoCosKSqGHkPdgJsK1bZwcA")
                .addEventListeners(new Listener((String) config.getData("Prefix"), config))
                .setActivity(Activity.playing("Wartet auf Reaction"))
                .build();

    }

}
