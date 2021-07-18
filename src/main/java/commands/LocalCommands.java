package commands;

import events.OnStartup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalCommands {
    public static void registerLocalSlashCommands(CommandListUpdateAction action) {
        List<CommandData> commands = new ArrayList<>();


        action.addCommands(commands).queue();
        OnStartup.LOG.info("Registered local slash commands");
    }
}
