package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.dto.ResourceTypeDTO;
import com.circular.Circular.economy.entity.ResourceType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/resourceTypes")
public class ResourceTypeController {

    @GetMapping
    public ResponseEntity<?> getResourceTypes() {
        List<ResourceTypeDTO> resourceTypeDTOList = Arrays.stream(ResourceType.values())
                .map(ResourceTypeDTO::new)
                .toList();

        return ResponseEntity.ok(Map.of("data", resourceTypeDTOList));
    }

    @GetMapping("/descriptions")
    public ResponseEntity<?> getResourceTypeDescriptions() {
        List<String> resourceTypeDTOList = Arrays.stream(ResourceType.values())
                .map(ResourceType::getDescription)
                .toList();
        return ResponseEntity.ok(Map.of("descriptions", resourceTypeDTOList));
    }

}