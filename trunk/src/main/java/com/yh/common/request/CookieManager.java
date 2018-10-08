package com.yh.common.request;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import sun.util.logging.PlatformLogger;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class CookieManager extends CookieHandler {

   public CookieManager(CookieStore store) {
      this.store = store;
   }

   public void setStore(CookieStore store) {
      this.store = store;
   }

   @Override
   public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
      // pre-condition check
      if (uri == null || requestHeaders == null) {
         throw new IllegalArgumentException("Argument is null");
      }

      Map<String, List<String>> cookieMap =
         new java.util.HashMap<String, List<String>>();
      List<String> cookieHeader = new ArrayList<String>();

      List<Cookie> cookies = store.getCookies();

      for(Cookie cookie : cookies) {
         cookieHeader.add(new StringBuilder(cookie.getName()).append("=").append(cookie.getValue())
         .append(";path=").append(cookie.getPath()).append(";domain=").append(cookie.getDomain())
         .append(";expires=").append(cookie.getExpiryDate()).toString());
      }

      cookieMap.put("Cookie", cookieHeader);
      return Collections.unmodifiableMap(cookieMap);
   }

   @Override
   public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
      if (uri == null || responseHeaders == null) {
         throw new IllegalArgumentException("Argument is null");
      }

      PlatformLogger logger = PlatformLogger.getLogger("java.net.CookieManager");
      for (String headerKey : responseHeaders.keySet()) {
         // RFC 2965 3.2.2, key must be 'Set-Cookie2'
         // we also accept 'Set-Cookie' here for backward compatibility
         if (headerKey == null
            || !(headerKey.equalsIgnoreCase("Set-Cookie2")
            || headerKey.equalsIgnoreCase("Set-Cookie")
         )
            )
         {
            continue;
         }

         for (String headerValue : responseHeaders.get(headerKey)) {
            try {
               List<HttpCookie> cookies;

               try {
                  cookies = HttpCookie.parse(headerValue);
               } catch (IllegalArgumentException e) {
                  // Bogus header, make an empty list and log the error
                  cookies = java.util.Collections.emptyList();
                  if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                     logger.severe("Invalid cookie for " + uri + ": " + headerValue);
                  }
               }

               for (HttpCookie cookie : cookies) {
                  if (cookie.getPath() == null) {
                     // If no path is specified, then by default
                     // the path is the directory of the page/doc
                     String path = uri.getPath();
                     if (!path.endsWith("/")) {
                        int i = path.lastIndexOf("/");
                        if (i > 0) {
                           path = path.substring(0, i + 1);
                        } else {
                           path = "/";
                        }
                     }
                     cookie.setPath(path);
                  }

                  BasicClientCookie c = new BasicClientCookie(cookie.getName(), cookie.getValue());
                  c.setDomain(cookie.getDomain());
                  c.setComment(cookie.getComment());
                  c.setPath(cookie.getPath());
                  c.setDomain(cookie.getDomain());

                  store.addCookie(c);
               }
            } catch (IllegalArgumentException e) {
               // invalid set-cookie header string
               // no-op
            }
         }
      }
   }

   private CookieStore store;
}
