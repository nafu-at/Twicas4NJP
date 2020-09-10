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

package page.nafuchoco.neojukepro.twicas4mjp;

import java.net.URL;

public class TwitcastingStreamInfo {
    private final String title;
    private final String description;
    private final URL thumbnail;

    private final String username;
    private final URL icon;
    private final String userDescription;

    public TwitcastingStreamInfo(String title, String description, URL thumbnail, String username, URL icon, String userDescription) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.username = username;
        this.icon = icon;
        this.userDescription = userDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public URL getThumbnail() {
        return thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public URL getIcon() {
        return icon;
    }

    public String getUserDescription() {
        return userDescription;
    }

    @Override
    public String toString() {
        return "TwitcastingStreamInfo{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail=" + thumbnail +
                ", username='" + username + '\'' +
                ", icon=" + icon +
                ", userDescription='" + userDescription + '\'' +
                '}';
    }
}
