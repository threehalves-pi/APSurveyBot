package commands;

import events.OnStartup;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import utils.Bot;
import utils.Utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalCommands {
    public static void registerLocalSlashCommands(CommandListUpdateAction action) {
        List<CommandData> commands = new ArrayList<>();

        // Admin/private commands
        commands.add(
                new CommandData("update", "Update official channels")
                        .addOption(OptionType.CHANNEL, "channel", "The channel to update", true)
                        .setDefaultEnabled(false)
        );
        commands.add(
                new CommandData("purge", "Purge channel messages")
                        .addOption(OptionType.INTEGER, "count",
                                "The number of messages to purge", true)
                        .setDefaultEnabled(false)
        );
        commands.add(
                new CommandData("id", "Get a user's Discord ID")
                        .addOption(OptionType.USER, "user", "The user whose ID you want")
        );

        // Send slash commands and update permissions
        action.addCommands(commands).queue(
                c -> setCommandPrivileges(c, Bot.DEVELOPMENT_GUILD)
        );
        OnStartup.LOG.info("Registered local slash commands");

    }

    public static void setCommandPrivileges(List<Command> commands, Guild guild) {
        for (Command command : commands) {
            switch (command.getName()) {
                case "update" -> guild.updateCommandPrivilegesById(
                        command.getId(), CommandPrivilege.enableUser(314889189856378882L)).queue();
                case "purge" -> guild.updateCommandPrivilegesById(
                        command.getIdLong(), CommandPrivilege.enableRole(Bot.ADMIN_ROLE)).queue();
                default -> {
                }
            }
        }
    }

    public static void id(@Nonnull SlashCommandEvent event) {
        OptionMapping user = event.getOption("user");

        if (user == null)
            event.reply("Your Discord id is: `" + event.getUser().getId() + "`").setEphemeral(true).queue();
        else
            event.reply(user.getAsUser().getAsMention() + "'s Discord id is: `" +
                        event.getUser().getId() + "`").setEphemeral(true).queue();
    }

    public static void update(@Nonnull SlashCommandEvent event) {
        MessageChannel channel;

        try {
            channel = Objects.requireNonNull(event.getOption("channel")).getAsMessageChannel();
            assert channel != null;
        } catch (Exception e) {
            event.reply("Error: Failed to retrieve the channel").setEphemeral(true).queue();
            return;
        }

        switch (channel.getId()) {
            case "865690906929922069" -> ProjectServerManagement.updateInfoEmbeds(event, true);
            case "865690380199264257" -> ProjectServerManagement.updateRulesEmbeds(event, true);
            case "866343297965228083" -> ProjectServerManagement.updateContributorInfo(event, true);
            default -> event
                    .reply("There is nothing to update in " + Utils.mentionChannel(channel.getIdLong()) + ".")
                    .queue();
        }
    }

    public static void purge(@Nonnull SlashCommandEvent event) {
        long count = Objects.requireNonNull(event.getOption("count")).getAsLong();
        event.reply("Purging " + count + " messages from " + event.getChannel().getName() + "...")
                .setEphemeral(true).queue();
        event.getChannel().purgeMessages(event.getChannel().getHistory().retrievePast((int) count).complete());
    }
}
