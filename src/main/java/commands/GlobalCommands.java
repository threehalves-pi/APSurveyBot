package commands;

import events.OnStartup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import utils.Bot;
import utils.Colors;
import utils.Utils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalCommands {
    public static void registerGlobalSlashCommands(CommandListUpdateAction action) {
        List<CommandData> commands = new ArrayList<>();

        commands.add(new CommandData("source", "See AP Survey Bot on Github"));
        commands.add(new CommandData("help", "Get info about " + Bot.BOT_NAME));

        action.addCommands(commands).queue();
        OnStartup.LOG.info("Registered global slash commands");
    }

    public static void source(@Nonnull SlashCommandEvent event) {
        event.reply(
                Utils.makeEmbed(
                        "Source Code",
                        "I'm open source! You can view my code and even make pull requests on my github, " +
                        "available at: " + Utils.link(Bot.GITHUB, Bot.GITHUB),
                        Colors.WHITE,
                        "",
                        Bot.GITHUB,
                        "Survey Bot on Github").buildMessage()
        ).setEphemeral(true).queue();
    }

    public static void help(@Nonnull SlashCommandEvent event) {
        event.replyEmbeds(Utils.makeEmbed(
                Bot.BOT_NAME + " Info",
                "Hi, I'm " + Bot.BOT_NAME + "! " + Bot.BOT_DESCRIPTION,
                Color.WHITE,
                Utils.makeEmbedField("Version", "I'm currently running `" + Bot.VERSION + "`.", true)
        ).build()).setEphemeral(true).queue();
    }
}
