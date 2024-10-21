package com.ktb.paperplebe.aws.controller;

import com.ktb.paperplebe.aws.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/aws")
@RequiredArgsConstructor
@RestController
public class AwsController {
    private final AwsService awsService;

    @GetMapping("/pre-signed-url/{filename}")
    public ResponseEntity<String> getPreSignedUrl(@PathVariable String filename) {
        return ResponseEntity.status(HttpStatus.OK).body(awsService.getPresignUrl(filename));
    }
}
