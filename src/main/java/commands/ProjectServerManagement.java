package commands;

import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import utils.Bot;
import utils.Colors;
import utils.Utils;

import java.util.List;

/**
 * This class contains some seldom-used methods for composing the info messages, updating the rules, and other
 * single-use moderation tools in the AP Survey Project Discord server.
 */
public class ProjectServerManagement {
    public static void createDevChannels() {
        Category devCategory = Bot.DEVELOPMENT_GUILD.getCategoryById(865703868364619826L);
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
        List<Role> allRoles = Bot.DEVELOPMENT_GUILD.getRoles();

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

    public static void updateRulesEmbeds() {
        TextChannel rulesChannel = Bot.DEVELOPMENT_GUILD.getTextChannelById(865690380199264257L);
        if (rulesChannel == null) {
            Main.LOG.error("Failed to locate #rules in the AP Survey Project server");
            return;
        }

        // First clear out the old messages in the channel
        rulesChannel.purgeMessages(rulesChannel.getHistory().retrievePast(10).complete());

        rulesChannel.sendMessageEmbeds(
                Utils.makeEmbed(
                        "Rules",
                        "`1` Be respectful of others\n" +
                        "`2` No spam, advertising, or NSFW content\n" +
                        "`3` No unsolicited DMs\n" +
                        "`4` No impersonation or triggering usernames\n" +
                        "`5` Abide by Discord's ToS",
                        Colors.YELLOW,
                        "Adapted from the AP Students Discord").build()
        ).queue();
    }

    public static void updateInfoEmbeds() {
        TextChannel infoChannel = Bot.DEVELOPMENT_GUILD.getTextChannelById(865690906929922069L);
        if (infoChannel == null) {
            Main.LOG.error("Failed to locate #rules in the AP Survey Project server");
            return;
        }

        // First clear out the old messages in the channel
        infoChannel.purgeMessages(infoChannel.getHistory().retrievePast(10).complete());

        infoChannel.sendMessageEmbeds(
                Utils.makeEmbed(
                        "About Us",
                        "The AP Survey Project is a group-led effort to produce high quality, informative " +
                        "documents to help AP students.\n\n" +
                        "We use input from seasoned students familiar with each AP course to identify new students' " +
                        "most frequently asked questions. We look for subjective questions, such as \"what are the " +
                        "best resources?\" and \"how hard is this course?\"\n\n" +
                        "Developers then work with us to produce a comprehensive survey for each course, which we " +
                        "use to help answer those questions. We administer the survey to as many past students as " +
                        "possible, and use the data to write high-quality answers in an official FAQ.",
                        Colors.WHITE).build()
        ).queue();

        infoChannel.sendMessage(
                Utils.makeEmbed(
                        "Become a Developer",
                        "Are you an experienced AP student interested in helping us write FAQs for a " +
                        "specific AP course? Fill out " + Utils.link(Bot.FAQ_VOLUNTEER_FORM, "this form") +
                        " today to join the AP Survey Project development team.",
                        Colors.GREEN,
                        Bot.FAQ_VOLUNTEER_FORM,
                        "Become an FAQ Dev")
                        .buildMessage()
        ).queue();

        infoChannel.sendMessageEmbeds(
                Utils.makeEmbed(
                        "Background",
                        "This project was inspired by the AP Statistics Survey and FAQ, a month-long " +
                        "project where we surveyed over 100 AP students. We analyzed the results and " +
                        "compiled this " + Utils.link(Bot.AP_STATS_FAQ, "22 page FAQ") +
                        " currently pinned in " + Utils.mentionChannel(689902917070749787L) + ".",
                        Colors.WHITE).build()
        ).queue();

        infoChannel.sendMessageEmbeds(
                Utils.makeEmbed(
                        "Server Invite Link",
                        Bot.SERVER_INVITE,
                        Colors.WHITE).build()
        ).queue();
    }
}
