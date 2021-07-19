package events;

import commands.GlobalCommands;
import commands.LocalCommands;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnSlash extends ListenerAdapter {
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        switch (event.getName()) {
            // Global commands
            case "help" -> GlobalCommands.help(event);
            case "source" -> GlobalCommands.source(event);

            // Local commands
            case "id" -> LocalCommands.id(event);

            // Local admin commands
            case "update" -> LocalCommands.update(event);
            case "purge" -> LocalCommands.purge(event);

            // Unknown command
            default -> event
                    .reply("Sorry, I don't recognize that command. Please try again later.")
                    .setEphemeral(true)
                    .queue();
        }
    }
}
