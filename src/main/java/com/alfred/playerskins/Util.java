package com.alfred.playerskins;

import com.alfred.playerskins.config.Config;
import com.alfred.playerskins.events.init.InitListener;
import com.alfred.playerskins.interfaces.AdvancedPlayerModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Util {
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;

    private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public static void loadTextures(PlayerEntity player) {
        try {
            AdvancedPlayerModel adv = (AdvancedPlayerModel)player;
            String uuid = getUuid(player);

            final String result = request(BASE_URL + uuid);

            JsonElement json = JsonParser.parseString(result);
            final String textureData = new String(Base64.getDecoder().decode(json.getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString()), StandardCharsets.UTF_8);

            JsonObject textures = JsonParser.parseString(textureData).getAsJsonObject().get("textures").getAsJsonObject();
            JsonObject skin = textures.get("SKIN").getAsJsonObject();
            adv.setSlim(skin.has("metadata") && "slim".equals(skin.get("metadata").getAsJsonObject().get("model").getAsString()));
            player.skinUrl = skin.get("url").getAsString();

            if (textures.has("CAPE") && Config.allowCapes)
                player.playerCapeUrl = player.capeUrl = textures.get("CAPE").getAsJsonObject().get("url").getAsString();
        } catch (Exception e) {
            InitListener.LOGGER.error("Failed to fetch skin for player {}: {}", player.name, e);
        }
    }

    private static String getUuid(PlayerEntity player) throws IOException {
        return JsonParser.parseString(request(UUID_URL + player.name)).getAsJsonObject().get("id").getAsString();
    }

    private static String request(String url) throws IOException {
        URL url2 = URI.create(url).toURL();
        HttpURLConnection conn = (HttpURLConnection)url2.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setUseCaches(false);

        InputStream stream = conn.getInputStream();
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }
}
