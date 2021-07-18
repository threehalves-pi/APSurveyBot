package utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is simply an enhanced version of an {@link EmbedBuilder} that allows you to attach links to the end of the
 * message by encasing the {@link MessageEmbed} in an {@link MessageBuilder}.
 * <p>
 * To build a {@link LinkBuilder} with an attached link for sending to Discord, use {@link #buildMessage()}.
 */
public class LinkBuilder extends EmbedBuilder {
    public static final List<Button> buttons = new ArrayList<>();

    public LinkBuilder() {
    }

    public LinkBuilder(@Nullable EmbedBuilder builder) {
        super(builder);
    }

    public LinkBuilder(@Nullable MessageEmbed embed) {
        super(embed);
    }

    /**
     * This creates a {@link LinkBuilder} from an existing {@link EmbedBuilder} and adds a link button.
     *
     * @param embed the existing embed
     * @param url   the destination url
     * @param label the button label
     * @return the new {@link LinkBuilder}
     */
    public static LinkBuilder of(@Nullable EmbedBuilder embed, String url, String label) {
        return new LinkBuilder(embed).addButton(url, label);
    }

    /**
     * Adds a button to the end of the message
     *
     * @param url   the target URL for this button
     * @param label the text to display on the button
     * @return this {@link LinkBuilder} instance for chaining
     */
    public LinkBuilder addButton(String url, String label) {
        buttons.add(Button.link(url, label));
        return this;
    }

    public @Nonnull
    Message buildMessage() {
        return new MessageBuilder().setEmbeds(super.build()).setActionRows(
                ActionRow.of(buttons)
        ).build();
    }

    @NotNull
    public LinkBuilder clear() {
        super.clear();
        return this;
    }

    @NotNull
    public LinkBuilder setTitle(@Nullable String title) {
        super.setTitle(title);
        return this;
    }

    @NotNull
    public LinkBuilder setTitle(@Nullable String title, @Nullable String url) {
        super.setTitle(title, url);
        return this;
    }

    @NotNull
    public LinkBuilder appendDescription(@NotNull CharSequence description) {
        super.appendDescription(description);
        return this;
    }

    @NotNull
    public LinkBuilder setTimestamp(@Nullable TemporalAccessor temporal) {
        super.setTimestamp(temporal);
        return this;
    }

    @NotNull
    public LinkBuilder setColor(@Nullable Color color) {
        super.setColor(color);
        return this;
    }

    @NotNull
    public LinkBuilder setColor(int color) {
        super.setColor(color);
        return this;
    }

    @NotNull
    public LinkBuilder setThumbnail(@Nullable String url) {
        super.setThumbnail(url);
        return this;
    }

    @NotNull
    public LinkBuilder setImage(@Nullable String url) {
        super.setImage(url);
        return this;
    }

    @NotNull
    public LinkBuilder setAuthor(@Nullable String name) {
        super.setAuthor(name);
        return this;
    }

    @NotNull
    public LinkBuilder setAuthor(@Nullable String name, @Nullable String url) {
        super.setAuthor(name, url);
        return this;
    }

    @NotNull
    public LinkBuilder setAuthor(@Nullable String name, @Nullable String url, @Nullable String iconUrl) {
        super.setAuthor(name, url, iconUrl);
        return this;
    }

    @NotNull
    public LinkBuilder setFooter(@Nullable String text) {
        super.setFooter(text);
        return this;
    }

    @NotNull
    public LinkBuilder setFooter(@Nullable String text, @Nullable String iconUrl) {
        super.setFooter(text, iconUrl);
        return this;
    }

    @NotNull
    public LinkBuilder addField(@Nullable MessageEmbed.Field field) {
        super.addField(field);
        return this;
    }

    @NotNull
    public LinkBuilder addField(@Nullable String name, @Nullable String value, boolean inline) {
        super.addField(name, value, inline);
        return this;
    }

    @NotNull
    public LinkBuilder addBlankField(boolean inline) {
        super.addBlankField(inline);
        return this;
    }

    @NotNull
    public LinkBuilder clearFields() {
        super.clearFields();
        return this;
    }
}
