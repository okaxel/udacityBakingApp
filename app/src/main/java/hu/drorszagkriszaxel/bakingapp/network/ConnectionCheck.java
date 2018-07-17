package hu.drorszagkriszaxel.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * Baking App component.
 *
 * Copyright © 2018 by Axel Ország-Krisz Dr.
 *
 * @author Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * This class checks whether the device is connected or not.
 */
public class ConnectionCheck {

    private Context context;

    /**
     * Simple constructor.
     *
     * @param context Context to work with
     */
    public ConnectionCheck(Context context) {

        this.context = context;

    }

    /**
     * Checks whether device is connected.
     *
     * @return True if connected, false elsewhere
     */
    public boolean isConnected() {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;

        if (manager != null) info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();

    };

}
