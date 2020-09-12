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

import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser;
import com.sedmelluq.discord.lavaplayer.source.stream.M3uStreamSegmentUrlProvider;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TwitcastingStreamSegmentUrlProvider extends M3uStreamSegmentUrlProvider {
    private static final Logger log = LoggerFactory.getLogger(TwitcastingStreamSegmentUrlProvider.class);
    private final String userName;

    private String streamSegmentPlaylistUrl;

    public TwitcastingStreamSegmentUrlProvider(String userName) {
        this.userName = userName;
    }

    @Override
    protected String getQualityFromM3uDirective(ExtendedM3uParser.Line directiveLine) {
        log.debug("Data: {}\n DirName: {} \n Dir: {}\n Ex: {}", directiveLine.lineData, directiveLine.directiveName, directiveLine.directiveArguments, directiveLine.extraData);
        return "";
    }

    @Override
    protected String fetchSegmentPlaylistUrl(HttpInterface httpInterface) throws IOException {
        HttpUriRequest request = new HttpGet("https://twitcasting.tv/" + userName + "/metastream.m3u8/?video=1");
        String stream = loadStreamsInfo(HttpClientTools.fetchResponseLines(httpInterface, request, "Twitcasting Stream List"));

        if (stream == null)
            throw new IllegalStateException("No streams available.");

        streamSegmentPlaylistUrl = stream;
        return streamSegmentPlaylistUrl;
    }

    @Override
    protected HttpUriRequest createSegmentGetRequest(String url) {
        return new HttpGet(url);
    }

    private String loadStreamsInfo(String[] lines) {
        String infoLine = null;

        for (String lineText : lines) {
            ExtendedM3uParser.Line line = ExtendedM3uParser.parseLine(lineText);

            if (line.lineData != null)
                infoLine = line.lineData;
        }

        return infoLine;
    }
}
