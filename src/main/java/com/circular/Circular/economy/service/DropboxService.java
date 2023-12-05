package com.circular.Circular.economy.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.Metadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DropboxService {

    private String ACCESS_TOKEN = "ACCESS_TOKEN";
    private String REFRESH_TOKEN = "REFRESH_TOKEN";
    private String APP_KEY = "APP_KEY";
    private String APP_SECRET = "APP_SECRET";
    private final DbxClientV2 dropboxClient;
    private final DbxRequestConfig config;

    public DropboxService() {
        this.config = DbxRequestConfig.newBuilder("salvage").build();
        this.dropboxClient = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public void refreshAccessToken() {
        OkHttpClient httpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", REFRESH_TOKEN)
                .build();

        Request request = new Request.Builder()
                .url("https://api.dropbox.com/oauth2/token")
                .post(requestBody)
                .addHeader("Authorization", Credentials.basic(APP_KEY, APP_SECRET))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                // Update class variables with the new access token and refresh token
                ACCESS_TOKEN = jsonNode.get("access_token").asText();
                REFRESH_TOKEN = jsonNode.get("refresh_token").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String uploadFile(InputStream fileInputStream, String dropboxFilePath) throws IOException, DbxException {
        try {
            FileMetadata uploadedFileMetadata = dropboxClient.files().uploadBuilder(dropboxFilePath).uploadAndFinish(fileInputStream);
            return uploadedFileMetadata.getPathDisplay();
        } catch (DbxException e) {
            refreshAccessToken();
            FileMetadata uploadedFileMetadata = dropboxClient.files().uploadBuilder(dropboxFilePath).uploadAndFinish(fileInputStream);
            return uploadedFileMetadata.getPathDisplay();
        }
    }

    public String getTemporaryLink(String originalFileName) throws DbxException {
        try {
            GetTemporaryLinkResult temporaryLinkResult = dropboxClient.files().getTemporaryLink(originalFileName);
            return temporaryLinkResult.getLink();
        } catch (DbxException e) {
            refreshAccessToken();
            GetTemporaryLinkResult temporaryLinkResult = dropboxClient.files().getTemporaryLink(originalFileName);
            return temporaryLinkResult.getLink();
        }
    }

    public boolean deleteFile(String path) throws DbxException {
        try {
            Metadata metadata = dropboxClient.files().getMetadata(path);
            if (metadata != null) {
                DeleteResult deleteResult = dropboxClient.files().deleteV2(path);
                return true;
            } else {
                return false;
            }
        } catch (DbxException e) {
            refreshAccessToken();
            Metadata metadata = dropboxClient.files().getMetadata(path);
            if (metadata != null) {
                DeleteResult deleteResult = dropboxClient.files().deleteV2(path);
                return true;
            } else {
                return false;
            }
        }
    }
}