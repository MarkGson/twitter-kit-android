/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.twitter.sdk.android.core.internal.network;

import com.twitter.sdk.android.core.GuestSessionProvider;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;

public class OkHttpClientHelper {
    public static OkHttpClient getOkHttpClient(GuestSessionProvider guestSessionProvider,
            SSLSocketFactory sslSocketFactory) {
        return getOkHttpClientBuilder(guestSessionProvider, sslSocketFactory).build();
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder(
            GuestSessionProvider guestSessionProvider, SSLSocketFactory sslSocketFactory) {
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory)
                .authenticator(new GuestAuthenticator(guestSessionProvider))
                .addInterceptor(new GuestAuthInterceptor(guestSessionProvider))
                .addNetworkInterceptor(new GuestAuthNetworkInterceptor());
    }

    public static OkHttpClient getOkHttpClient(Session<? extends TwitterAuthToken> session,
            TwitterAuthConfig authConfig, SSLSocketFactory sslSocketFactory) {
        return getOkHttpClientBuilder(session, authConfig, sslSocketFactory).build();
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder(
            Session<? extends TwitterAuthToken> session, TwitterAuthConfig authConfig,
            SSLSocketFactory sslSocketFactory) {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null.");
        }

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory)
                .addInterceptor(new OAuth1aInterceptor(session, authConfig));
    }
}
