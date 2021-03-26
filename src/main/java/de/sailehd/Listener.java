package de.sailehd;

import com.google.gson.JsonObject;
import de.sailehd.support.Debug;
import de.sailehd.support.EasyBase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class Listener extends ListenerAdapter {

    String prefix = "";
    EasyBase config;

    static Timer timer;
    static int interval;

    ArrayList<Integer> tempChannelInt = new ArrayList<Integer>();
    ArrayList<String> tempChannelIDs = new ArrayList<String>();


    private Thread loop;
    private int IntervalLoop = 1;

    JDA jda;


    public Listener(String prefix, EasyBase config) throws IOException {
        this.prefix = prefix;
        this.config = config;


        for (int i = 0; i <= 6; i++) {
            tempChannelInt.add(0);
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        MessageChannel messageChannel = event.getChannel();
        String[] command = event.getMessage().getContentRaw().split(" ");
        String primaryCommand = command[0].substring(1);

        String userID = event.getMember().getId();

        //ArrayList<String> admins = (ArrayList<String>) config.getData("Admins");
        jda = event.getJDA();

        ArrayList<String> admins = (ArrayList<String>) config.getData("Admins");


        if(command[0].startsWith(prefix)){
            if(admins.contains(userID)){
                switch (primaryCommand.toLowerCase(Locale.ROOT)){
                    case "exit":
                    case "bye":
                        dcsend(messageChannel, "bye");
                        //loop.stop();
                        event.getJDA().shutdown();
                        System.exit(0);
                        break;

                    case "spam":
                        int amount = Integer.parseInt(command[1]);
                        if(amount == 0 || command[2] == null){
                            dcsend(messageChannel, "Wrong Command");
                            dcsend(messageChannel, "Example: !spam 100 lost");
                        }
                        else{
                            for (int i = 1; i <= amount; i++) {
                                dcsend(messageChannel, command[2]);
                            }
                            return;
                        }
                        break;

                    case "Say":
                        dcsend(messageChannel, command[1]);
                        break;
                    default:
                        dcsend(messageChannel, "Command not found!");
                        break;
                }
            }
        }
    }

    public void dcsend(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
        return;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String user = event.getMember().getAsMention();
        JDA client = event.getJDA();
        Role role = event.getGuild().getRoleById((String) config.getData("DefaultRoleID"));
        event.getGuild().addRoleToMember(event.getMember().getId(), role);

    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        VoiceChannel channel = event.getChannelJoined();
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), event.getMember().getUser().getName() + " joined " + channel.getName());

        Guild guild = event.getGuild();
        JDA jda = event.getJDA();

        Category tempChannelCategory = guild.getCategoryById((String) config.getData("tempChannel"));


        int delay = 1000;
        int period = 1000;

        timer = new Timer();




        //Gaming
        if(channel.getId().equals((String) config.getData("Gaming"))){
            CreateChannel(guild, tempChannelCategory, event, "Gaming", 0);

            /*timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    int tempInterval = setInterval();

                    dcsend(event.getGuild().getTextChannelById("814916409997656124"), String.valueOf(tempInterval));

                    if(tempInterval == 0){
                        dcsend(event.getGuild().getTextChannelById("814916409997656124"), "End");
                    }
                }
            }, delay, period);*/

        }

        //Dead by Daylight
        else if(channel.getId().equals((String) config.getData("DeadByDaylight"))){
            CreateChannel(guild, tempChannelCategory, event, "Dead by Daylight", 4);
        }

        //Phasmophobia
        else if(channel.getId().equals((String) config.getData("Phasmophobia"))){
            CreateChannel(guild, tempChannelCategory, event, "Phasmophobia", 4);
        }

        //PayDay
        else if(channel.getId().equals((String) config.getData("PayDay"))){
            CreateChannel(guild, tempChannelCategory, event, "PayDay", 10);
        }

        //Among Us
        else if(channel.getId().equals((String) config.getData("AmongUs"))){
            CreateChannel(guild, tempChannelCategory, event, "Among Us", 10);
        }

        //GTA
        else if(channel.getId().equals((String) config.getData("GTA"))){
            CreateChannel(guild, tempChannelCategory, event, "GTA", 20);
        }

        //Minecraft
        else if(channel.getId().equals((String) config.getData("Minecraft"))){
            CreateChannel(guild, tempChannelCategory, event, "Minecraft", 20);
        }

        //Gaming
        else if(channel.getId().equals((String) config.getData("Gaming"))){
            CreateChannel(guild, tempChannelCategory, event, "Gaming", 20);
        }
    }


    void CreateChannel(Guild guild, Category tempChannelCategory, GuildVoiceJoinEvent event, String name, Integer userLimit){
        ChannelAction<VoiceChannel> createdChannel = guild.createVoiceChannel(name + " " + UUID.randomUUID().toString());
        createdChannel.setParent(tempChannelCategory);

        ArrayList<Permission> allow = new ArrayList<Permission>();
        allow.add(Permission.VIEW_CHANNEL);
        allow.add(Permission.MANAGE_CHANNEL);
        allow.add(Permission.MANAGE_PERMISSIONS);
        allow.add(Permission.CREATE_INSTANT_INVITE);
        allow.add(Permission.VOICE_CONNECT);
        allow.add(Permission.VOICE_SPEAK);
        allow.add(Permission.VOICE_STREAM);
        allow.add(Permission.VOICE_MUTE_OTHERS);
        allow.add(Permission.VOICE_MOVE_OTHERS);
        allow.add(Permission.VOICE_USE_VAD);
        allow.add(Permission.PRIORITY_SPEAKER);
        allow.add(Permission.VOICE_DEAF_OTHERS);

        ArrayList<Permission> deny = new ArrayList<Permission>();




        createdChannel.addMemberPermissionOverride(Long.parseLong(event.getMember().getId()), allow, deny);

        if(userLimit != 0){
            createdChannel.setUserlimit(userLimit);
        }



        VoiceChannel cChannel = createdChannel.complete();
        tempChannelInt.set(0, tempChannelInt.get(0) + 1);
        guild.moveVoiceMember(event.getMember(), cChannel).queue();
        tempChannelIDs.add(cChannel.getId());

        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), "Created Channel " + cChannel.getName());
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event){
        VoiceChannel channel = event.getChannelLeft();
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), event.getMember().getUser().getName() + " left " + channel.getName());

        Guild guild = event.getGuild();
        JDA jda = event.getJDA();


        if(tempChannelIDs.contains(channel.getId()) && channel.getMembers().size() == 0){

            guild.getVoiceChannelById(channel.getId()).delete().reason("Not Needed").queue();
            dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), "Deleted Channel " + channel.getName());
        }
    }

    private static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }

    public void runLoop(){
        this.loop = new Thread(() -> {
            long time = System.currentTimeMillis();

            while(true){
                if(System.currentTimeMillis() >= time + ((1000 * 60) * IntervalLoop)){
                    time = System.currentTimeMillis();
                    onSecond();
                }
            }


        });
        this.loop.setName("Loop");
        this.loop.start();
    }

    public void onSecond(){
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
