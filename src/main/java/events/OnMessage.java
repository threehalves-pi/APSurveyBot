package events;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utils.Bot;
import utils.Colors;
import utils.Utils;

import java.util.List;

public class OnMessage extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Ignore messages from the bot
        if (event.getAuthor().getIdLong() == Bot.BOT_ID)
            return;

        Message message = event.getMessage();
        String content = message.getContentDisplay();
    }
}
