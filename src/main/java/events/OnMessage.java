package events;

import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utils.Bot;

import java.util.List;
import java.util.Objects;

public class OnMessage extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Ignore messages from the bot
        if (event.getAuthor().getIdLong() == Bot.BOT_ID)
            return;

        Message message = event.getMessage();
        String content = message.getContentDisplay();

        // This command was used for creating the Guild. It is now disabled.
        if (content.equals("!channels")) {
            message.reply("This command has been disabled.").queue();
        }
    }

    public void createDevChannels() {
        Guild guild = Main.JDA.getGuildById(Bot.DEVELOPMENT_SERVER);
        Category devCategory = Objects.requireNonNull(guild).getCategoryById(865703868364619826L);
        assert devCategory != null;

        String[] names = {
                "Art History",
                "Biology",
                "Calculus AB",
                "Calculus BC",
                "Chemistry",
                "Chinese Language and Culture",
                "Comparative Government and Politics",
                "Computer Science A",
                "Computer Science Principles",
                "English Language and Composition",
                "English Literature and Composition",
                "Environmental Science",
                "European History",
                "French Language and Culture",
                "German Language and Culture",
                "Human Geography",
                "Italian Language and Culture",
                "Japanese Language and Culture",
                "Latin",
                "Macroeconomics",
                "Microeconomics",
                "Music Theory",
                "Physics 1: Algebra-Based",
                "Physics 2: Algebra-Based",
                "Physics C: Electricity and Magnetism",
                "Physics C: Mechanics",
                "Psychology",
                "Research",
                "Seminar",
                "Spanish Language and Culture",
                "Spanish Literature and Culture",
                "Statistics",
                "Studio Art: 2-D Design",
                "Studio Art: 3-D Design",
                "Studio Art: Drawing",
                "US Government and Politics",
                "US History",
                "World History: Modern"
        };

        Role[] roles = new Role[names.length];
        List<Role> allRoles = guild.getRoles();

        System.out.println("Loading roles...");
        // Get all the roles
        for (int i = 0; i < names.length; i++) {
            // Find the role with the same name
            for (Role role : allRoles)
                if (role.getName().equalsIgnoreCase(names[i])) {
                    roles[i] = role;
                    break;
                }

            // Make sure a matching role was found
            if (roles[i] == null) {
                new Exception("Failed to find role " + names[i]).printStackTrace();
                return;
            }
        }

        System.out.println("Successfully loaded " + roles.length + " roles.");
        System.out.println("Creating channels...");

        for (int i = 0; i < names.length; i++) {
            TextChannel channel = devCategory.createTextChannel(names[i]).complete();
            channel.putPermissionOverride(roles[i]).setAllow(Permission.VIEW_CHANNEL).complete();
            System.out.println("   Created " + names[i] + " and set permissions.");
        }

        System.out.println("Created channels.");
    }
}
