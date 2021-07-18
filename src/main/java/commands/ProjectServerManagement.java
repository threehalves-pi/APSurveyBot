package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import utils.Bot;
import utils.Colors;
import utils.Utils;

import java.util.List;

/**
 * This class contains some seldom-used methods for composing the info messages, updating the rules, and other
 * single-use moderation tools in the AP Survey Project Discord server.
 */
public class ProjectServerManagement {
    /**
     * This sends the embeds in #contributor-info that help new contributors understand what this project is all about,
     * how it will function, and what they can do to help.
     *
     * @param event the slash command that triggered this update
     * @param clearChannel true if the old messages in the channel should be cleared first; false if not
     */
    public static void updateContributorInfo(SlashCommandEvent event, boolean clearChannel) {
        TextChannel contributorInfo = Bot.DEVELOPMENT_GUILD.getTextChannelById(866343297965228083L);
        if (contributorInfo == null) {
            event.reply("Failed to locate #contributor-info in the AP Survey Project server").queue();
            return;
        }

        event.reply("Updating #contributor-info...").queue();

        // First clear out the old messages in the channel
        if (clearChannel)
            contributorInfo.purgeMessages(contributorInfo.getHistory().retrievePast(10).complete());

        contributorInfo.sendMessageEmbeds(
                Utils.makeEmbed(
                        "How does this project work?",
                        "Our goal here is to make FAQ documents for every single AP course and make them " +
                        "available in the AP Students Discord server. That's a big project, so here's the plan:",
                        Colors.WHITE,
                        Utils.makeEmbedField(
                                "1. Recruit contributors",
                                "We can't make all these FAQs on our own, partially because we simply haven't " +
                                "taken every AP course. That's why we need contributors. We need helpers and " +
                                "experienced students from each AP course to make this happen."),
                        Utils.makeEmbedField(
                                "2. What to ask?",
                                "What are the frequently asked questions that new students want to know? " +
                                "We're not sure, so we need to work together to brainstorm this. You can help by " +
                                "filling out the " + Utils.link(Bot.PRELIMINARY_SURVEY, "preliminary survey") +
                                " for each AP you want to help with."),
                        Utils.makeEmbedField(
                                "3. Distribute the Survey",
                                "The next step is data collection. We'll survey as many past AP students as " +
                                "possible in each subject. That data will help us *answer* all the questions we came " +
                                "up with in phase 2."),
                        Utils.makeEmbedField(
                                "4. Data Analysis",
                                "Once the data starts pouring in, we'll need contributors experienced in " +
                                "Google Sheets to help us analyze the data. The goal is to convert it into " +
                                "easy-to-read graphs for the FAQs."),
                        Utils.makeEmbedField(
                                "5. Write the FAQs",
                                "Using the data from the surveys, we'll begin writing the FAQs for each " +
                                "subject. This is when we most need your help. You know your AP courses the best, " +
                                "so we need you to help write high-quality informative answers to each question. " +
                                "Once that's all done, we'll begin distributing the FAQs with AP Survey Bot and " +
                                "pinning them to AP channels.")
                ).build()
        ).queue();

        contributorInfo.sendMessage(
                Utils.makeEmbed("Getting started!",
                        "Check out the contributor channels for each of the APs you signed up for. Help " +
                        "brainstorm questions to ask, and be sure to fill out the " +
                        Utils.link(Bot.PRELIMINARY_SURVEY, "preliminary survey") + " for contributors. " +
                        "Your help is greatly appreciated.",
                        Colors.WHITE,
                        Bot.PRELIMINARY_SURVEY,
                        "Preliminary survey"
                ).buildMessage()
        ).queue();

        event.getHook().sendMessage("Successfully updated embeds.").queue();
    }

    /**
     * This sends the embeds in #rules that list the server rules.
     *
     * @param event the slash command that triggered this update
     * @param clearChannel true if the old messages in the channel should be cleared first; false if not
     */
    public static void updateRulesEmbeds(SlashCommandEvent event, boolean clearChannel) {
        TextChannel rulesChannel = Bot.DEVELOPMENT_GUILD.getTextChannelById(865690380199264257L);
        if (rulesChannel == null) {
            event.reply("Failed to locate #rules in the AP Survey Project server").queue();
            return;
        }

        event.reply("Updating #rules...").queue();

        // First clear out the old messages in the channel
        if (clearChannel)
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

        event.getHook().sendMessage("Successfully updated embeds.").queue();
    }

    /**
     * This sends the embeds in #info that explain what the AP Survey Project is and how people can get involved.
     *
     * @param event the slash command that triggered this update
     * @param clearChannel true if the old messages in the channel should be cleared first; false if not
     */
    public static void updateInfoEmbeds(SlashCommandEvent event, boolean clearChannel) {
        TextChannel infoChannel = Bot.DEVELOPMENT_GUILD.getTextChannelById(865690906929922069L);
        if (infoChannel == null) {
            event.reply("Failed to locate #rules in the AP Survey Project server").queue();
            return;
        }

        event.reply("Updating #info...").queue();

        // First clear out the old messages in the channel
        if (clearChannel)
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

        event.getHook().sendMessage("Successfully updated embeds.").queue();
    }

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
}
