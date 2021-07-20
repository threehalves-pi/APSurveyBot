package announcements;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.internal.entities.DataMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Announcement} is fundamentally a {@link DataMessage} ready for sending to Discord.
 */
public class Announcement {
    /**
     * This the message that is sent when the announcement is triggered.
     */
    private final MessageBuilder message;

    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

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
    public @Nonnull
    Message build() {
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
     * This sends the {@link Announcement} to the given {@link MessageChannel}.
     *
     * @param channel the channel to send the announcement in
     */
    public void send(@Nonnull MessageChannel channel) {
        channel.sendMessage(build()).queue();
    }

    /**
     * This sends the {@link Announcement} to the given {@link MessageChannel} and adds it to the channel pins.
     *
     * @param channel the channel to send the announcement in
     */
    public void sendAndPin(@Nonnull MessageChannel channel) {
        channel.sendMessage(build()).queue(
                m -> m.pin().queue()
        );
    }

    /**
     * This sends the {@link Announcement} in a given {@link MessageChannel} after a fixed amount of time.
     *
     * @param channel the channel to send the announcement in
     * @param delay   the delay after which this computation should be executed, negative to execute immediately
     * @param unit    the {@link TimeUnit} to convert the specified <code>delay</code>
     */
    public void sendDelay(@Nonnull MessageChannel channel, long delay, TimeUnit unit) {
        channel.sendMessage(build()).queueAfter(delay, unit);
    }

    /**
     * Schedule this {@link Announcement} to be sent after a certain period of time. That schedule can be canceled by
     * calling {@link ScheduledFuture#cancel(boolean)} on the {@link ScheduledFuture} returned by this method.
     *
     * @param channel the channel to send the announcement in
     * @param delay   the delay after which the announcement should be sent
     * @param unit    the {@link TimeUnit} to convert the specified <code>delay</code>
     * @return the {@link ScheduledFuture} which can be used to cancel sending the {@link Announcement}
     */
    public ScheduledFuture<?> queue(@Nonnull MessageChannel channel, long delay, TimeUnit unit) {
        return timer.schedule(
                new AnnouncementTimer(this, channel),
                delay,
                unit
        );
    }

    /**
     * Schedule this {@link Announcement} to be sent after a certain period of time. That schedule can be canceled by
     * calling {@link ScheduledFuture#cancel(boolean)} on the {@link ScheduledFuture} returned by this method.
     *
     * @param channel    the channel to send the announcement in
     * @param delay      the delay after which the announcement should be sent
     * @param unit       the {@link TimeUnit} to convert the specified <code>delay</code>
     * @param conditions one or more conditions required for the announcement to be sent
     * @return the {@link ScheduledFuture} which can be used to cancel sending the {@link Announcement}
     */
    public ScheduledFuture<?> queue(@Nonnull MessageChannel channel, long delay, TimeUnit unit,
                                    QueueCondition... conditions) {
        return timer.schedule(
                new AnnouncementTimer(this, channel),
                delay,
                unit
        );
    }

    public static class AnnouncementTimer extends TimerTask {
        /**
         * The {@link Announcement} associated with this {@link AnnouncementTimer Timer}, which is sent when the timer
         * {@link #run() runs}.
         */
        private final Announcement announcement;

        /**
         * The {@link MessageChannel} in Discord that will receive the {@link #announcement} when this timer {@link
         * #run() runs}.
         */
        private final MessageChannel channel;

        /**
         * Zero or more {@link QueueCondition Conditions} that are checked when this timer {@link #run() runs}. If any
         * of the conditions are not met, the {@link #announcement} is not sent.
         */
        private QueueCondition[] conditions;

        /**
         * Create an {@link AnnouncementTimer AnnouncementTimer} based on the {@link Announcement} to send.
         *
         * @param announcement the announcement to send
         * @param channel      where to send the announcement
         */
        private AnnouncementTimer(@Nonnull Announcement announcement, @Nonnull MessageChannel channel) {
            this.announcement = announcement;
            this.channel = channel;
        }

        /**
         * Create an {@link AnnouncementTimer AnnouncementTimer} based on the {@link Announcement} to send. The {@link
         * QueueCondition QueueConditions} set requirements that are checked at run time and must be met for the
         * announcement to actually be sent. If they are not met, the {@link AnnouncementTimer AnnouncementTimer} fails
         * to send and is canceled.
         *
         * @param announcement the announcement to send
         * @param channel      where to send the announcement
         * @param conditions   one or more conditions that must be met for the announcement to be sent
         */
        private AnnouncementTimer(@Nonnull Announcement announcement, @Nonnull MessageChannel channel,
                                  QueueCondition... conditions) {
            this(announcement, channel);
            this.conditions = conditions;
        }

        /**
         * Get the {@link #channel} that this {@link Announcement.AnnouncementTimer Timer} intends to send the {@link
         * Announcement} in.
         *
         * @return the channel
         */
        @Nonnull
        public MessageChannel getChannel() {
            return channel;
        }

        /**
         * Get the {@link #announcement} associated with this {@link AnnouncementTimer Timer}
         *
         * @return the announcement
         */
        @Nonnull
        public Announcement getAnnouncement() {
            return announcement;
        }

        /**
         * Runs this {@link AnnouncementTimer Timer}, sending the {@link #announcement} to the {@link #channel},
         * provided that all the {@link #conditions} are met. If the conditions are not met, nothing happens.
         */
        @Override
        public void run() {
            if (conditions != null)
                // If any conditions fail their check, do not run announcement
                if (Arrays.stream(conditions).anyMatch(c -> !c.check(this))) {
                    System.out.println("Conditions failed for sending announcement");
                    return;
                }
            announcement.send(channel);
        }
    }

}
