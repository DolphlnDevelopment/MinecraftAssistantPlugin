package voicemc.voicemc;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "voicemc-velocity",
        name = "Voicemc",
        version = BuildConstants.VERSION,
        description = "VoiceMC",
        url = "dolphln.com",
        authors = {"Dolphln", "Pol"}
)
public class VoicemcVelocity {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
