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

package page.nafuchoco.neojukepro.twicas4njp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class TwitcastingClient {
    private static final Logger log = LoggerFactory.getLogger(TwitcastingClient.class);

    private TwitcastingClient() {
        throw new UnsupportedOperationException();
    }

    public static TwitcastingStreamInfo getStreamInfo(String url) {
        String title = null;
        String description = null;
        URL thumbnail = null;

        String username = null;
        URL icon = null;
        String userDescription;

        try {
            Document document = Jsoup.connect(url).get();
            Elements metaTags = document.getElementsByTag("meta");

            for (Element metaTag : metaTags) {
                String property = metaTag.attr("property");
                String content = metaTag.attr("content");

                switch (property) {
                    case "og:title":
                        title = content;
                        break;

                    case "og:description":
                        description = content;
                        break;

                    case "og:image":
                        thumbnail = new URL(content);
                        break;

                    default:
                        continue;
                }
            }

            Element mainWrapper = document.getElementById("mainwrapper");

            userDescription = mainWrapper.getElementsByClass("tw-player-page-wrapper").get(0)
                    .getElementsByClass("tw-player-page").get(0)
                    .getElementsByClass("tw-player-page__author").get(0)
                    .getElementsByClass("tw-live-author").get(0)
                    .getElementsByClass("tw-live-author__comment").get(0)
                    .getElementById("authorcomment").text();

            Element userHeader = mainWrapper.getElementsByClass("tw-user-header").get(0);
            Elements userNavInfo =
                    userHeader.getElementsByClass("tw-user-nav").get(0).getElementsByClass("tw-user-nav-info").get(0).children();
            for (Element userInfo : userNavInfo) {
                String className = userInfo.attr("class");
                if (className.equals("tw-user-nav-icon")) {
                    icon = new URL("https://" + userInfo.getElementsByTag("img").attr("src"));
                } else if (className.equals("tw-user-nav-user")) {
                    username = userInfo.getElementsByClass("tw-user-nav-user-inner").get(0)
                            .getElementsByClass("tw-user-nav-profile").get(0)
                            .getElementsByClass("tw-user-nav-name").get(0).text();
                }
            }

            return new TwitcastingStreamInfo(title, description, thumbnail, username, icon, userDescription);
        } catch (IOException e) {
            log.error("Loading Twitcasting live information failed.", e);
        }
        return null;
    }
}
