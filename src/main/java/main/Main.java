package main;

import events.OnMessage;
import events.OnSlash;
import events.OnStartup;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static JDA JDA;
    public static final Logger LOG = JDALogger.getLog(Main.class);

    public static void main(String[] args) throws IOException, LoginException {
        String token = new String(
                Objects.requireNonNull(Main.class.getResourceAsStream("/bot.token")).readAllBytes()
        );

        JDA = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new OnMessage())
                .addEventListeners(new OnStartup())
                .addEventListeners(new OnSlash())
                .build();
    }
}
