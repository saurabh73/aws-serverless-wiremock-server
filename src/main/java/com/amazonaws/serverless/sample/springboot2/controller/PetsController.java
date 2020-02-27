/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.serverless.sample.springboot2.controller;

import com.amazonaws.serverless.sample.springboot2.model.Pet;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@RestController
@EnableWebMvc
public class PetsController {

    private final WireMockServer mockServer;
    private final RestTemplate restTemplate;
    private static final String WIRE_MOCK_BASE_URL = "http://localhost:9090";

    @Autowired
    public PetsController(WireMockServer mockServer, RestTemplate restTemplate) {
        this.mockServer = mockServer;
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "/mappings")
    public ObjectNode getWireMockMappings() {
        mockServer.start();
        ResponseEntity<ObjectNode> responseEntity =
                restTemplate.getForEntity(WIRE_MOCK_BASE_URL +"/__admin/mappings", ObjectNode.class);
        mockServer.stop();
        return responseEntity.getBody();
    }

    @GetMapping(path = "/pets")
    public Pet[] listPetsWireMock() {
        mockServer.start();
        ResponseEntity<Pet[]> responseEntity =
                restTemplate.getForEntity(WIRE_MOCK_BASE_URL + "/pets", Pet[].class);
        mockServer.stop();
        return responseEntity.getBody();
    }
}
