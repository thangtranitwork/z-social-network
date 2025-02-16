package com.thangtranit.profileservice.repository.client;

import com.thangtranit.profileservice.config.FeignMultipartSupportConfig;
import com.thangtranit.profileservice.config.IdRequestInterceptor;
import com.thangtranit.profileservice.dto.response.ApiResponse;
import com.thangtranit.profileservice.enums.FileType;
import com.thangtranit.profileservice.enums.Privacy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@FeignClient(
        name = "file-client",
        url = "${client.file}",
        configuration = {
                FeignMultipartSupportConfig.class,
                IdRequestInterceptor.class
        })
public interface FileClient {
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<String> uploadFile(@RequestPart("file") MultipartFile file,
                                   @RequestPart("privacy") Privacy privacy,
                                   @RequestPart("ownerId") String ownerId,
                                   @RequestPart("exceptIds") Set<String> exceptIds,
                                   @RequestPart("specificIds") Set<String> specificIds,
                                   @RequestPart("required") FileType fileType
    );
}

