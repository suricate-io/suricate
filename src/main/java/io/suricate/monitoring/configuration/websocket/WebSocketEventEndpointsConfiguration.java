/*
 * Copyright 2012-2018 the original author or authors.
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
 */

package io.suricate.monitoring.configuration.websocket;

import io.suricate.monitoring.model.dto.websocket.WebsocketClient;
import io.suricate.monitoring.services.websocket.DashboardWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manage the subscription/unsubscription events of the web sockets
 */
@Configuration
public class WebSocketEventEndpointsConfiguration {

    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventEndpointsConfiguration.class);

    /**
     * Regex group for project token
     */
    private static final int PROJECT_TOKEN_REGEX_GROUP = 1;

    /**
     * Regex group for screen code
     */
    private static final int SCREEN_CODE_REGEX_GROUP = 2;

    /**
     * The dashboard websocket service
     */
    private final DashboardWebSocketService dashboardWebSocketService;

    /**
     * Constructor
     *
     * @param dashboardWebSocketService The dashboard websocket service
     */
    public WebSocketEventEndpointsConfiguration(final DashboardWebSocketService dashboardWebSocketService) {
        this.dashboardWebSocketService = dashboardWebSocketService;
    }

    /**
     * Entry point when a client subscribes to a dashboard.
     * The server opens a unique web socket on /user/dashboard_token/queue/unique.
     * When a client subscribes to a dashboard, then an event is sent to the socket.
     * The client is added to the project through a dynamic map.
     *
     * @param event The subscription event
     */
    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String simpDestination = (String) stompHeaderAccessor.getHeader("simpDestination");

        if (simpDestination != null) {
            Pattern pattern = Pattern.compile("/user/([A-Z0-9]+)-([0-9]+)/queue/unique");
            Matcher matcher = pattern.matcher(simpDestination);

            if (matcher.find()) {
                WebsocketClient websocketClient = new WebsocketClient(
                    matcher.group(PROJECT_TOKEN_REGEX_GROUP),
                    stompHeaderAccessor.getSessionId(),
                    stompHeaderAccessor.getSubscriptionId(),
                    matcher.group(SCREEN_CODE_REGEX_GROUP)
                );

                LOGGER.debug("A new client (web socket session ID: {}, screen code ID: {}) is trying to subscribe to the project {}", websocketClient.getSessionId(), websocketClient.getScreenCode(),
                        websocketClient.getProjectToken());

                dashboardWebSocketService.addClientToProject(websocketClient.getProjectToken(), websocketClient);
                dashboardWebSocketService.addSessionClient(websocketClient.getSessionId(), websocketClient);
            }
        }
    }

    /**
     * Entry point when a client unsubscribe to the web sockets
     * Called when an unsubscription is triggered manually
     *
     * @param event The unsubscribe event
     */
    @EventListener
    public void onSessionUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        WebsocketClient websocketClient = dashboardWebSocketService.removeSessionClientByWebsocketSessionIdAndSubscriptionId(stompHeaderAccessor.getSessionId(), stompHeaderAccessor.getSubscriptionId());

        if (websocketClient != null) {
            LOGGER.debug("Unsubscribe client {} with id {} for project {}", websocketClient.getSessionId(), websocketClient.getScreenCode(), websocketClient.getProjectToken());
            dashboardWebSocketService.removeProjectClient(websocketClient.getProjectToken(), websocketClient);
        }
    }

    /**
     * Entry point when a client disconnect to the web sockets
     * Called when a disconnection is triggered by a page refreshment
     *
     * @param event The disconnect event
     */
    @EventListener
    public void onSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        WebsocketClient websocketClient = dashboardWebSocketService.removeSessionClientByWebsocketSessionId(stompHeaderAccessor.getSessionId());

        if (websocketClient != null) {
            LOGGER.debug("Disconnect client {} with id {} for project {}", websocketClient.getSessionId(), websocketClient.getScreenCode(), websocketClient.getProjectToken());
            dashboardWebSocketService.removeProjectClient(websocketClient.getProjectToken(), websocketClient);
        }
    }
}
