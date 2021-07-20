package utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Utils {
    /**
     * This is a convenience method for creating {@link MessageEmbed.Field} instances, which are used to create {@link
     * EmbedBuilder} instances.
     *
     * @param title  the field title
     * @param value  the contents of the field
     * @param inline whether the field should display inline
     * @return the newly created {@link MessageEmbed.Field}
     */
    public static MessageEmbed.Field makeEmbedField(String title, String value, boolean inline) {
        return new MessageEmbed.Field(title, value, inline);
    }

    /**
     * This is an overloaded method for {@link #makeEmbedField(String, String, boolean)} that sets inline to false by
     * default.
     *
     * @param title the field title
     * @param value the contents of the field
     * @return the newly created {@link MessageEmbed.Field}
     */
    public static MessageEmbed.Field makeEmbedField(String title, String value) {
        return makeEmbedField(title, value, false);
    }

    /**
     * This is an overloaded method for {@link #makeEmbed(String, String, Color, MessageEmbed.Field...)} that creates an
     * {@link EmbedBuilder} with an additional footer parameter. Additional parameters can be added to the returned
     * embed.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param footer      the footer
     * @param fields      one or more fields (optionally created with {@link #makeEmbedField(String, String, boolean)}
     * @return the {@link EmbedBuilder}
     */
    public static EmbedBuilder makeEmbed(
            String title,
            String description,
            Color color,
            String footer,
            MessageEmbed.Field... fields) {
        return makeEmbed(title, description, color, fields).setFooter(footer);
    }

    /**
     * This is an overloaded method for {@link #makeEmbed(String, String, Color)} that creates an {@link EmbedBuilder}
     * with one or more embed fields. Additional parameters can be set to the returned embed.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param fields      one or more fields (optionally created with {@link #makeEmbedField(String, String, boolean)}
     * @return the {@link EmbedBuilder}
     */
    public static EmbedBuilder makeEmbed(String title, String description, Color color, MessageEmbed.Field... fields) {
        EmbedBuilder e = makeEmbed(title, description, color);
        for (MessageEmbed.Field field : fields)
            e.addField(field);
        return e;
    }

    /**
     * This is an overloaded method for {@link #makeEmbed(String, String, Color)} that creates an {@link EmbedBuilder}
     * with a footer. Additional parameters can be set to the returned embed.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param footer      the footer
     * @return the {@link EmbedBuilder}
     */
    public static EmbedBuilder makeEmbed(String title, String description, Color color, String footer) {
        return makeEmbed(title, description, color).setFooter(footer);
    }

    /**
     * This creates an {@link EmbedBuilder} with the most basic set of parameters: a title, description, and color.
     * Additional parameters can be added to the returned embed.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @return the {@link EmbedBuilder}
     */
    public static EmbedBuilder makeEmbed(String title, String description, Color color) {
        return new EmbedBuilder().setTitle(title).setDescription(description).setColor(color);
    }

    /**
     * This creates a {@link LinkBuilder}, which is simply an {@link EmbedBuilder} with the ability to attach link
     * buttons for convenience.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param url         the destination url
     * @param label       the button label
     * @return the {@link LinkBuilder}
     */
    public static LinkBuilder makeEmbed(String title, String description, Color color, String url, String label) {
        return LinkBuilder.of(makeEmbed(title, description, color), url, label);
    }

    /**
     * This creates a {@link LinkBuilder}, which is simply an {@link EmbedBuilder} with the ability to attach link
     * buttons for convenience.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param footer      the footer
     * @param url         the destination url
     * @param label       the button label
     * @return the {@link LinkBuilder}
     */
    public static LinkBuilder makeEmbed(String title, String description, Color color,
                                        String footer, String url, String label) {
        return makeEmbed(title, description, color, url, label).setFooter(footer);
    }

    /**
     * This creates a {@link LinkBuilder}, which is simply an {@link EmbedBuilder} with the ability to attach link
     * buttons for convenience.
     *
     * @param title       the title
     * @param description the description
     * @param color       the color
     * @param footer      the footer
     * @param url         the destination url
     * @param label       the button label
     * @param fields one or more fields
     * @return the {@link LinkBuilder}
     */
    public static LinkBuilder makeEmbed(String title, String description, Color color,
                              String footer, String url, String label, MessageEmbed.Field... fields) {
        LinkBuilder l = makeEmbed(title, description, color, footer, url, label);
        for (MessageEmbed.Field field : fields)
            l.addField(field);
        return l;
    }

    /**
     * Create a hyperlink for embeds.
     *
     * @param url  the destination url
     * @param text the hyperlink text
     * @return a formatted hyperlink
     */
    public static String link(String url, String text) {
        return String.format("[%s](%s)", text, url);
    }

    /**
     * This returns a formatted string for mentioning a Discord channel from its id.
     * @param id the channel id
     * @return the channel mention
     */
    public static String mentionChannel(long id) {
        return String.format("<#%d>", id);
    }
}
