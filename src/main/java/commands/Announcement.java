package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Announcement} is fundamentally a {@link DataMessage} ready for sending to Discord.
 */
public class Announcement {
    /**
     * This the message that is sent when the announcement is triggered.
     */
    private final MessageBuilder message;

    public Announcement(MessageBuilder message) {
        this.message = message;
    }

    public Announcement(EmbedBuilder message) {
        this.message = new MessageBuilder(message);
    }

    public Announcement(CharSequence message) {
        this.message = new MessageBuilder(message);
    }

    public Announcement(MessageEmbed message) {
        this.message = new MessageBuilder(message);
    }

    public Announcement(Message message) {
        this.message = new MessageBuilder(message);
    }

    /**
     * This sets the text content of the message.
     *
     * @param content The content to use, or {@code null} to reset the content
     * @return this {@link Announcement} instance for chaining
     * @see MessageBuilder#setContent(String)
     */
    public Announcement setContent(@Nullable String content) {
        message.setContent(content);
        return this;
    }

    /**
     * Set the action rows for the message.
     *
     * @param rows the new action rows, or null to reset the components
     * @return this {@link Announcement} instance for chaining
     */
    public Announcement setActionRows(@Nullable ActionRow... rows) {
        message.setActionRows(rows);
        return this;
    }

    /**
     * Set the action rows for the message.
     *
     * @param rows the new action rows, or null to reset the components
     * @return this {@link Announcement} instance for chaining
     */
    public Announcement setActionRows(@Nullable Collection<? extends ActionRow> rows) {
        message.setActionRows(rows);
        return this;
    }

    /**
     * This overwrites the action rows associated with the {@link #message} and replaces them with a single link {@link
     * Button}.
     *
     * @param url   the destination url
     * @param label the button label
     * @return this {@link Announcement} instance for chaining
     */
    public Announcement addLink(String url, String label) {
        setActionRows(ActionRow.of(Button.link(url, label)));
        return this;
    }

    /**
     * Adds up to 10 {@link MessageEmbed MessageEmbeds} to the {@link #message}.
     *
     * @param embeds one or more embeds to add, or an empty array to remove all embeds
     * @return this {@link Announcement} instance for chaining
     */
    @Nonnull
    public Announcement setEmbeds(@Nonnull MessageEmbed... embeds) {
        message.setEmbeds(embeds);
        return this;
    }

    /**
     * Adds up to 10 {@link MessageEmbed MessageEmbeds} to the {@link #message}.
     *
     * @param embeds one or more embeds to add, or an empty array to remove all embeds
     * @return this {@link Announcement} instance for chaining
     */
    @Nonnull
    public Announcement setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
        message.setEmbeds(embeds);
        return this;
    }

    /**
     * Adds up to 10 {@link EmbedBuilder EmbedBuilders} to the {@link #message} by {@link EmbedBuilder#build() building}
     * each of them and calling {@link #setEmbeds(MessageEmbed...)}.
     *
     * @param embeds one or more embeds to add, or an empty array to remove all embeds
     * @return this {@link Announcement} instance for chaining
     */
    public Announcement setEmbeds(@Nonnull EmbedBuilder... embeds) {
        message.setEmbeds(Arrays.stream(embeds).map(EmbedBuilder::build).toList());
        return this;
    }

    /**
     * Build and return the {@link #message} for this {@link Announcement}.
     *
     * @return the built {@link Message}
     */
    public @NotNull Message build() {
        return message.build();
    }

    /**
     * This sends the {@link Announcement} as a reply to a slash command.
     *
     * @param event     the slash command to reply to
     * @param ephemeral whether the reply should be ephemeral (only shown to the user who used the command)
     */
    public void send(@Nonnull SlashCommandEvent event, boolean ephemeral) {
        event.reply(build()).setEphemeral(ephemeral).queue();
    }

    /**
     * This sends the {@link Announcement} in a given {@link MessageChannel}.
     *
     * @param channel the channel to send the announcement in
     */
    public void send(@Nonnull MessageChannel channel) {
        channel.sendMessage(build()).queue();
    }

    public void sendDelay(@Nonnull MessageChannel channel, long delay, TimeUnit timeUnit) {
        RestAction<Message> delay1 = channel.sendMessage(build()).delay(delay, timeUnit);
    }
}
