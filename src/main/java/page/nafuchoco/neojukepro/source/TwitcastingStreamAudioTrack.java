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

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.stream.M3uStreamSegmentUrlProvider;
import com.sedmelluq.discord.lavaplayer.source.stream.MpegTsM3uStreamAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static page.nafuchoco.neojukepro.source.TwitcastingStreamAudioSourceManager.getUserIdentifierFromUrl;

public class TwitcastingStreamAudioTrack extends MpegTsM3uStreamAudioTrack {
    private static final Logger log = LoggerFactory.getLogger(TwitcastingStreamAudioTrack.class);

    private final TwitcastingStreamAudioSourceManager sourceManager;
    private final M3uStreamSegmentUrlProvider segmentUrlProvider;

    public TwitcastingStreamAudioTrack(AudioTrackInfo trackInfo, TwitcastingStreamAudioSourceManager sourceManager) {
        super(trackInfo);
        this.sourceManager = sourceManager;
        this.segmentUrlProvider = new TwitcastingStreamSegmentUrlProvider(getUserName());
    }

    public String getUserName() {
        return getUserIdentifierFromUrl(trackInfo.identifier);
    }

    @Override
    protected M3uStreamSegmentUrlProvider getSegmentUrlProvider() {
        return segmentUrlProvider;
    }

    @Override
    protected HttpInterface getHttpInterface() {
        return sourceManager.getHttpInterface();
    }

    @Override
    public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
        log.debug("Starting to play Twitcasting live {}.", getUserName());

        super.process(localExecutor);
    }

    @Override
    protected AudioTrack makeShallowClone() {
        return super.makeShallowClone();
    }

    @Override
    public AudioSourceManager getSourceManager() {
        return sourceManager;
    }
}
