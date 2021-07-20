package announcements;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This class represents conditions that a {@link MessageChannel} must meet in order for an {@link Announcement} to be
 * sent there.
 */
public class QueueCondition {
    /**
     * The {@link Condition} required for this {@link QueueCondition} to pass a check.
     */
    private final Condition condition;

    /**
     * Zero or more parameters associated with the {@link #condition}
     */
    private final Object[] parameters;

    /**
     * If {@link #ofCustom(Function)} was called to initialize this {@link QueueCondition}, this stores the function
     * that must be checked on
     */
    private Function<Announcement.AnnouncementTimer, Boolean> function;

    private QueueCondition(Condition condition, Object... parameters) {
        this.condition = condition;
        this.parameters = parameters;
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#INACTIVE}. This requires that the
     * channel receiving the announcement does not contain any messages sent within a certain period of time.
     *
     * @param amount the minimum amount of time that the channel must be inactive for
     * @param unit   the unit of time used to compute <code>amount</code>
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofInactive(long amount, TimeUnit unit) {
        return new QueueCondition(Condition.INACTIVE, Duration.of(amount, unit.toChronoUnit()));
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#ACTIVE}. This requires that the
     * channel receiving the announcement has at least message sent within a certain period of time.
     *
     * @param amount the maximum amount of time that the channel can be inactive for
     * @param unit   the unit of time used to compute <code>amount</code>
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofActive(long amount, TimeUnit unit) {
        return new QueueCondition(Condition.ACTIVE, Duration.of(amount, unit.toChronoUnit()));
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#IMAGE}. This requires that the
     * channel receiving the announcement contains at least one message with an image attachment within the last
     * <count>messageCount</count> messages.
     *
     * @param messageCount the number of recent messages to grab from the channel to check for images
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofImage(int messageCount) {
        return new QueueCondition(Condition.IMAGE, messageCount);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#NO_IMAGE}. This requires that the
     * channel receiving the announcement does not contain any images within the last <count>messageCount</count>
     * messages.
     *
     * @param messageCount the number of recent messages to grab from the channel to check for images
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofNoImage(int messageCount) {
        return new QueueCondition(Condition.NO_IMAGE, messageCount);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#USER_MESSAGE}. This requires that
     * the channel receiving the announcement contains at least one message sent by the given user within the last
     * <count>messageCount</count> messages.
     *
     * @param messageCount the number of recent messages to grab from the channel to check for messages from the user
     * @param userId       the user to look for in the channel's message history
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofUserMessage(int messageCount, long userId) {
        return new QueueCondition(Condition.USER_MESSAGE, messageCount);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#NO_USER_MESSAGE}. This requires that
     * the channel receiving the announcement does not contain any messages sent by the given user within the last
     * <count>messageCount</count> messages.
     *
     * @param messageCount the number of recent messages to grab from the channel to check for messages from the user
     * @param userId       the user to look for in the channel's message history
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofNoUserMessage(int messageCount, long userId) {
        return new QueueCondition(Condition.NO_USER_MESSAGE, messageCount);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#EMPTY}. This requires that the
     * channel receiving the announcement is empty.
     *
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofEmpty() {
        return new QueueCondition(Condition.EMPTY);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#NOT_EMPTY}. This requires that the
     * channel receiving the announcement has at least one message.
     *
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofNotEmpty() {
        return new QueueCondition(Condition.NOT_EMPTY);
    }

    /**
     * Create a new {@link QueueCondition} based on {@link QueueCondition.Condition#CUSTOM}. This allows you to provide
     * your own {@link Function} that is called to {@link #check(Announcement.AnnouncementTimer) check} the {@link
     * QueueCondition} just before the announcement is sent.
     * <p>
     * The function you provide must take a single {@link Announcement.AnnouncementTimer} parameter and return a {@link
     * Boolean}. That boolean should be <code>true</code> if and only if the condition is met, indicating that the
     * {@link Announcement.AnnouncementTimer Timer} should proceed with sending the announcement. If the condition is
     * not met, indicating that the announcement should not be sent, <code>false</code> should be returned.
     * <p>
     * The custom function is called during {@link #check(Announcement.AnnouncementTimer)}.
     *
     * @return the newly created {@link QueueCondition}
     */
    public static QueueCondition ofCustom(@Nonnull Function<Announcement.AnnouncementTimer, Boolean> function) {
        QueueCondition condition = new QueueCondition(Condition.CUSTOM);
        condition.function = function;
        return condition;
    }

    /**
     * Check this {@link QueueCondition}. If it passes, return <code>true</code>. If it fails, return
     * <code>false</code>.
     *
     * @param timer the {@link Announcement.AnnouncementTimer AnnouncementTimer} associated with this {@link
     *              QueueCondition}
     * @return <code>true</code> if the check passes; <code>false</code> otherwise
     */
    public boolean check(@Nonnull Announcement.AnnouncementTimer timer) {
        try {
            switch (condition) {
                case ACTIVE -> {
                    Duration duration = durationSinceLast(timer.getChannel());
                    return duration != null && duration.compareTo(((Duration) parameters[0])) < 0;
                }

                case INACTIVE -> {
                    Duration duration = durationSinceLast(timer.getChannel());
                    return duration != null && duration.compareTo(((Duration) parameters[0])) > 0;
                }

                case IMAGE -> {
                    return hasImage(timer.getChannel(), ((int) parameters[0]));
                }

                case NO_IMAGE -> {
                    return !hasImage(timer.getChannel(), ((int) parameters[0]));
                }

                case USER_MESSAGE -> {
                    return hasUserMessage(timer.getChannel(), ((int) parameters[0]), ((long) parameters[1]));
                }

                case NO_USER_MESSAGE -> {
                    return !hasUserMessage(timer.getChannel(), ((int) parameters[0]), ((long) parameters[1]));
                }

                case EMPTY -> {
                    return getMessages(timer.getChannel(), 1).isEmpty();
                }

                case NOT_EMPTY -> {
                    return !getMessages(timer.getChannel(), 1).isEmpty();
                }

                case CUSTOM -> {
                    return function.apply(timer);
                }

                default -> {
                    // If the check Condition type is unknown, assume that the check failed
                    return false;
                }
            }
        } catch (Exception e) {
            // If an exception is thrown, assume that the check failed
            return false;
        }
    }

    /**
     * This determines whether the given {@link MessageChannel} contains a message with an image attachment in the last
     * <code>count</code> messages.
     *
     * @param channel the channel to check for images
     * @param count   the number of messages to check
     * @return <code>true</code> if there is at least one image; <code>false</code> otherwise
     */
    private boolean hasImage(@Nonnull MessageChannel channel, int count) {
        return getMessages(channel, count)
                .stream()
                .flatMap(message -> message.getAttachments().stream())
                .anyMatch(Message.Attachment::isImage);
    }

    /**
     * This method functions similarly to {@link #hasImage(MessageChannel, int)}, but it checks for messages from a user
     * rather than messages containing an image. It determines whether the given {@link MessageChannel} contains a
     * message sent by the given <code>user</code> in the last <code>count</code> messages
     *
     * @param channel the channel to check for messages from the user
     * @param count   the number of messages to check
     * @param userId  the id of the user to look for
     * @return <code>true</code> if there is at least one message sent by the specified user; <code>false</code>
     * otherwise
     */
    private boolean hasUserMessage(@Nonnull MessageChannel channel, int count, long userId) {
        return getMessages(channel, count)
                .stream()
                .map(Message::getAuthor)
                .map(User::getIdLong)
                .anyMatch(l -> l.equals(userId));
    }

    /**
     * Return a list of the most recent <code>count</code> messages from the given {@link MessageChannel}.
     *
     * @param channel the channel from which to retrieve messages
     * @param count   the number of messages to retrieve
     * @return the list of messages
     */
    @Nonnull
    private List<Message> getMessages(@Nonnull MessageChannel channel, int count) {
        return channel.getHistory().retrievePast(count).complete();
    }

    /**
     * Get the amount of time between right {@link OffsetDateTime#now() now} and the time the most recent message was
     * sent in the provided {@link MessageChannel}.
     * <p>
     * If the channel is empty, <code>null</code> is returned instead.
     *
     * @param channel the channel to check
     * @return the {@link Duration} between the latest message creation time and the current time
     */
    @Nullable
    private Duration durationSinceLast(@Nonnull MessageChannel channel) {
        List<Message> messages = getMessages(channel, 1);
        if (messages.size() == 0)
            return null;
        return Duration.between(OffsetDateTime.now(), messages.get(0).getTimeCreated());
    }

    enum Condition {
        /**
         * Require that a channel contains at least one message sent within the past <code>[time interval]</code>. Note
         * that this is based on the message's original send time, meaning editing the message does not change its
         * timestamp.
         */
        ACTIVE,

        /**
         * Require that a channel does not contain messages sent within the past <code>[time interval]</code>. Note that
         * this is based on the message's original send time, meaning editing the message does not change its
         * timestamp.
         */
        INACTIVE,

        /**
         * Require that a channel contains an image in the most recent number of messages.
         */
        IMAGE,

        /**
         * Require that a channel does not contain an image in the most recent number of messages.
         */
        NO_IMAGE,

        /**
         * Require that a channel contains a message from a given user in the most recent number of messages.
         */
        USER_MESSAGE,

        /**
         * Require that a channel does not contain a message from a given user in the most recent number of messages.
         */
        NO_USER_MESSAGE,

        /**
         * Require that a channel does not contain any messages.
         */
        EMPTY,

        /**
         * Require that a channel has at least one message.
         */
        NOT_EMPTY,

        /**
         * Use your own method to create a custom condition to {@link #check(Announcement.AnnouncementTimer) check}.
         */
        CUSTOM
    }
}
