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

import page.nafuchoco.neojukepro.core.module.NeoModule;
import page.nafuchoco.neojukepro.source.TwitcastingStreamAudioSourceManager;

public class Twicas4NJP extends NeoModule {

    @Override
    public void onLoad() {
        registerAudioSourceManager(new TwitcastingStreamAudioSourceManager());
    }
}
