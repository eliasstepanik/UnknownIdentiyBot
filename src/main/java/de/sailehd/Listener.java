package de.sailehd;

import de.sailehd.support.Debug;
import de.sailehd.support.EasyBase;
import de.sailehd.support.TextColor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
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

    ArrayList<String> tempRootChannelIDs = new ArrayList<String>();


    private Thread loop;
    private int IntervalLoop = 1;

    JDA jda;


    public Listener(String prefix, EasyBase config) throws IOException {
        this.prefix = prefix;
        this.config = config;
        this.tempRootChannelIDs = tempRootChannelIDs;


        for (int i = 0; i <= 6; i++) {
            tempChannelInt.add(0);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        tempRootChannelIDs = new ArrayList<String>();
        for (VoiceChannel channel: event.getJDA().getCategoryById((String) config.getData("GamingCat")).getVoiceChannels()) {
            tempRootChannelIDs.add(channel.getId());
        }
    }

    @Override
    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        if(event.getChannel().getParent().getId().equals((String) config.getData("GamingCat"))){
            tempRootChannelIDs = new ArrayList<String>();
            for (VoiceChannel channel: event.getJDA().getCategoryById((String) config.getData("GamingCat")).getVoiceChannels()) {
                tempRootChannelIDs.add(channel.getId());
            }
        }
    }

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        if(event.getChannel().getParent().getId().equals((String) config.getData("GamingCat"))){
            tempRootChannelIDs = new ArrayList<String>();
            for (VoiceChannel channel: event.getJDA().getCategoryById((String) config.getData("GamingCat")).getVoiceChannels()) {
                tempRootChannelIDs.add(channel.getId());
            }
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
                switch (primaryCommand){
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

                    case "say":
                        dcsend(messageChannel, command[1]);
                        break;

                    /*case "update":
                        tempRootChannelIDs = new ArrayList<String>();
                        for (VoiceChannel channel: event.getGuild().getCategoryById((String) config.getData("GamingCat")).getVoiceChannels()) {
                            tempRootChannelIDs.add(channel.getId());
                        }
                        dcsend(messageChannel, "TempChannel Updated");
                        break;
                    case "rRole":
                            switch (command[1]){
                                case "add":
                                    ArrayList<String> tempData = new ArrayList<String>();
                                    tempData.add(command[2]); //Message ID
                                    tempData.add(command[3]); //Reaction
                                    tempData.add(command[4]); //RoleName

                                    config.createData(command[4], tempData);
                                    break;
                                case "remove":
                                    break;
                            }
                        break;
                    */
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
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")),"User " + event.getUser().getName() + " joined and was assigned to the " + role.getName() + "!");
        Debug.log(TextColor.RED + "User " + event.getUser().getName() + " joined and was assigned to the " + role.getName() + "!" + TextColor.RESET);
    }


    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), event.getMember().getUser().getName() + " joined " + event.getChannelJoined().getName());
        Debug.log(TextColor.CYAN_BRIGHT + event.getMember().getUser().getName() + " joined " + event.getChannelJoined().getName() + TextColor.RESET);
        VoiceAction(event);
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event){
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), "Player " + event.getMember().getUser().getName() + " moved to " + event.getChannelJoined().getName());
        Debug.log(TextColor.CYAN + "Player " + event.getMember().getUser().getName() + " moved to " + event.getChannelJoined().getName() + TextColor.RESET);
        VoiceAction(event);
    }


    void VoiceAction(@NotNull Object eventObject){
        if(eventObject instanceof GuildVoiceJoinEvent){
            GuildVoiceJoinEvent event = (GuildVoiceJoinEvent) eventObject;

            VoiceChannel channel = event.getChannelJoined();

            Guild guild = event.getGuild();
            JDA jda = event.getJDA();

            Category tempChannelCategory = guild.getCategoryById((String) config.getData("tempChannel"));


            int delay = 1000;
            int period = 1000;

            timer = new Timer();

            if(tempRootChannelIDs.contains(channel.getId())){
                CreateChannel(guild, tempChannelCategory, event, channel.getName(), 0);
            }



        }
        else if(eventObject instanceof GuildVoiceMoveEvent){
            GuildVoiceMoveEvent event = (GuildVoiceMoveEvent) eventObject;

            VoiceChannel channel = event.getChannelJoined();

            Guild guild = event.getGuild();
            JDA jda = event.getJDA();

            Category tempChannelCategory = guild.getCategoryById((String) config.getData("tempChannel"));


            int delay = 1000;
            int period = 1000;

            timer = new Timer();




            if(tempRootChannelIDs.contains(channel.getId())){
                CreateChannel(guild, tempChannelCategory, event, channel.getName(), 0);
            }

            if(tempChannelIDs.contains(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().size() == 0){
                DeleteChannel(event, event.getChannelLeft());
            }

        }
    }

    void CreateChannel(Guild guild, Category tempChannelCategory, Object eventObject, String name, Integer userLimit){

        if(eventObject instanceof GuildVoiceJoinEvent){
            GuildVoiceJoinEvent event = (GuildVoiceJoinEvent) eventObject;
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
            Debug.log(TextColor.YELLOW + "Created Channel " + cChannel.getName() + TextColor.RESET);
        }
        else if(eventObject instanceof GuildVoiceMoveEvent){
            GuildVoiceMoveEvent event = (GuildVoiceMoveEvent) eventObject;
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
            Debug.log(TextColor.YELLOW + "Created Channel " + cChannel.getName() + TextColor.RESET);
        }

    }

    void DeleteChannel(Object eventObject, VoiceChannel channel){

        if(eventObject instanceof GuildVoiceLeaveEvent){
            GuildVoiceLeaveEvent event = (GuildVoiceLeaveEvent) eventObject;
            //Debug.log("Deleting " + TextColor.RED + event.getChannelLeft().getName());
            event.getGuild().getVoiceChannelById(channel.getId()).delete().reason("Not Needed").queue();
            dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), "Deleted Channel " + channel.getName());
            Debug.log(TextColor.PURPLE + "Deleted Channel " + channel.getName() + TextColor.RESET);
            tempChannelIDs.remove(channel.getId());
        }
        else if(eventObject instanceof GuildVoiceMoveEvent){
            GuildVoiceMoveEvent event = (GuildVoiceMoveEvent) eventObject;
            //Debug.log("Deleting " + TextColor.RED + event.getChannelLeft().getName());
            event.getGuild().getVoiceChannelById(channel.getId()).delete().reason("Not Needed").queue();
            dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), "Deleted Channel " + channel.getName());
            Debug.log(TextColor.PURPLE + "Deleted Channel " + channel.getName() + TextColor.RESET);
            tempChannelIDs.remove(channel.getId());
        }
    }

    /*void CreateChannelMove(Guild guild, Category tempChannelCategory, GuildVoiceMoveEvent event, String name, Integer userLimit){
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
    }*/

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event){
        VoiceChannel channel = event.getChannelLeft();
        dcsend(event.getGuild().getTextChannelById((String) config.getData("EventLog")), event.getMember().getUser().getName() + " left " + channel.getName());
        Debug.log(TextColor.GREEN + event.getMember().getUser().getName() + " left " + channel.getName() + TextColor.RESET);
        Guild guild = event.getGuild();
        JDA jda = event.getJDA();


        if(tempChannelIDs.contains(channel.getId()) && channel.getMembers().size() == 0){
            DeleteChannel(event, channel);
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
