package de.sailehd;

import de.sailehd.support.EasyBase;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {

    EasyBase config;

    public ReactionListener(EasyBase config){
        this.config = config;
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), event.getReactionEmote().getId());
    }


    public void ReactionRoleEvent(){

    }

    public void dcsend(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
        return;
    }
}
