/*
 * Copyright 2020 NAFU_at
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package page.nafuchoco.neojukepro.source;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.ExceptionTools;
import com.sedmelluq.discord.lavaplayer.tools.Units;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import page.nafuchoco.neojukepro.core.MessageManager;
import page.nafuchoco.neojukepro.core.command.MessageUtil;
import page.nafuchoco.neojukepro.core.player.CustomAudioSourceManager;
import page.nafuchoco.neojukepro.core.player.GuildAudioPlayer;
import page.nafuchoco.neojukepro.twicas4njp.TwitcastingClient;
import page.nafuchoco.neojukepro.twicas4njp.TwitcastingStreamInfo;

import java.awt.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitcastingStreamAudioSourceManager implements CustomAudioSourceManager, HttpConfigurable {
    private static final String STREAM_NAME_REGEX = "^https://twitcasting.tv/([^/]+)$";
    private static final Pattern streamNameRegex = Pattern.compile(STREAM_NAME_REGEX);

    private final HttpInterfaceManager httpInterfaceManager;

    public TwitcastingStreamAudioSourceManager() {
        httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();
    }

    @Override
    public String getSourceName() {
        return "Twitcasting";
    }

    @Override
    public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
        String userName = getUserIdentifierFromUrl(reference.identifier);
        if (userName == null)
            return null;

        TwitcastingStreamInfo streamInfo = TwitcastingClient.getStreamInfo(reference.identifier);

        if (streamInfo == null) {
            return AudioReference.NO_TRACK;
        } else {
            String displayName = streamInfo.getUsername();
            String title = streamInfo.getTitle();

            return new TwitcastingStreamAudioTrack(new AudioTrackInfo(
                    title,
                    displayName,
                    Units.DURATION_MS_UNKNOWN,
                    reference.identifier,
                    true,
                    reference.identifier
            ), this);
        }
    }

    @Override
    public boolean isTrackEncodable(AudioTrack track) {
        return true;
    }

    @Override
    public void encodeTrack(AudioTrack track, DataOutput output) throws IOException {
    }

    @Override
    public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) throws IOException {
        return new TwitcastingStreamAudioTrack(trackInfo, this);
    }

    public static String getUserIdentifierFromUrl(String url) {
        Matcher matcher = streamNameRegex.matcher(url);
        if (!matcher.matches())
            return null;
        return matcher.group(1);
    }

    public HttpInterface getHttpInterface() {
        return httpInterfaceManager.getInterface();
    }

    @Override
    public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
        httpInterfaceManager.configureRequests(configurator);
    }

    @Override
    public void configureBuilder(Consumer<HttpClientBuilder> configurator) {
        httpInterfaceManager.configureBuilder(configurator);
    }

    @Override
    public void shutdown() {
        ExceptionTools.closeWithWarnings(httpInterfaceManager);
    }

    @Override
    public Color getSourceColor() {
        return new Color(4, 114, 252);
    }

    @Override
    public MessageEmbed getNowPlayingEmbed(GuildAudioPlayer guildAudioPlayer) {
        EmbedBuilder builder = new EmbedBuilder();
        TwitcastingStreamInfo streamInfo = TwitcastingClient.getStreamInfo(guildAudioPlayer.getNowPlaying().getTrack().getIdentifier());
        builder.setTitle(guildAudioPlayer.getNowPlaying().getTrack().getInfo().title, guildAudioPlayer.getNowPlaying().getTrack().getIdentifier());
        builder.setColor(getSourceColor());
        builder.setAuthor(guildAudioPlayer.getNowPlaying().getTrack().getInfo().author);
        builder.setThumbnail(streamInfo.getThumbnail().toString());
        MessageEmbed.Field time = new MessageEmbed.Field("Time",
                "[" + MessageUtil.formatTime(guildAudioPlayer.getTrackPosition()) + "/" + MessageUtil.formatTime(guildAudioPlayer.getNowPlaying().getTrack().getDuration()) + "]",
                true);
        builder.addField(time);
        MessageEmbed.Field description = new MessageEmbed.Field("Description", streamInfo.getUserDescription(), false);
        builder.addField(description);
        MessageEmbed.Field source = new MessageEmbed.Field("",
                "Loaded from " + guildAudioPlayer.getNowPlaying().getTrack().getSourceManager().getSourceName() + ".", false);
        builder.addField(source);
        builder.setFooter(MessageUtil.format(MessageManager.getMessage("command.nowplay.request"), guildAudioPlayer.getNowPlaying().getInvoker().getEffectiveName()),
                guildAudioPlayer.getNowPlaying().getInvoker().getUser().getAvatarUrl());
        return builder.build();
    }
}
