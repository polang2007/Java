package org.example.html2md;

import java.net.URI;
import java.net.URISyntaxException;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static String completeImageUrlSafe(String imageUrl, String currentPageUrl) {
        try {
            if (imageUrl == null || imageUrl.trim().isEmpty()) return "";
            URI baseUri = new URI(currentPageUrl);
            URI resolvedUri = baseUri.resolve(imageUrl);
            return resolvedUri.normalize().toString();
        } catch (URISyntaxException e) {
            return imageUrl;
        }
    }
}

