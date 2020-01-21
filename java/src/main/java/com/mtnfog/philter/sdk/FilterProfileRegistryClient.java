/*******************************************************************************
 * Copyright 2020 Mountain Fog, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.mtnfog.philter.sdk;

import com.mtnfog.philter.sdk.model.Status;
import com.mtnfog.philter.sdk.service.FilterProfileRegistryService;
import com.mtnfog.philter.sdk.util.UnsafeOkHttpClient;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FilterProfileRegistryClient {

	private static final Logger LOGGER = LogManager.getLogger(FilterProfileRegistryClient.class);

	private FilterProfileRegistryService service;

	public FilterProfileRegistryClient(String endpoint, boolean verifySslCertificate) {

		OkHttpClient okHttpClient = new OkHttpClient();

		if(!verifySslCertificate) {

			try {

				LOGGER.warn("Allowing all SSL certificates is not recommended.");
				okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

			} catch (NoSuchAlgorithmException | KeyManagementException ex) {

				LOGGER.error("Cannot create unsafe HTTP client.", ex);
				throw new RuntimeException("Cannot create unsafe HTTP client.", ex);

			}

		}

		final Retrofit.Builder builder = new Retrofit.Builder()
		        .baseUrl(endpoint)
		        .client(okHttpClient)
				.addConverterFactory(ScalarsConverterFactory.create())
		        .addConverterFactory(GsonConverterFactory.create());

		final Retrofit retrofit = builder.build();

		service = retrofit.create(FilterProfileRegistryService.class);

	}

	public Status status() throws IOException {

		return service.status().execute().body();

	}

	public List<String> get() throws IOException {

		return service.get().execute().body();

	}

	public String get(String filterProfileName) throws IOException {

		return service.get(filterProfileName).execute().body();

	}

	public boolean save(String json) throws IOException {

		final Response response = service.save(json).execute();
		return response.isSuccessful();

	}

	public boolean delete(String filterProfileName) throws IOException {

		final Response response = service.delete(filterProfileName).execute();
		return response.isSuccessful();

	}

}