package com.aquasense.fogapplication.feedguard.om.interfaces.rest;

import com.aquasense.fogapplication.feedguard.data.dto.EdgePondDataDTO;
import com.aquasense.fogapplication.feedguard.om.domain.services.PondRecordCommandService;
import com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources.ReceivePondRecordCommandResource;
import com.aquasense.fogapplication.feedguard.om.interfaces.rest.transform.ReceivePondRecordCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/ponds_record", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pond Record", description = "Available Pond Record Endpoints")
public class PondsRecordController {

    private final PondRecordCommandService pondRecordCommandService;

    public PondsRecordController(PondRecordCommandService pondRecordCommandService) {
        this.pondRecordCommandService = pondRecordCommandService;
    }

    @PostMapping
    @Operation(summary = "Create a new Pond Record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pond Record Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    public ResponseEntity<?> createPondRecord(@RequestBody ReceivePondRecordCommandResource receivePondRecordCommandResource) {
        var receivePondRecordCommand = ReceivePondRecordCommandFromResourceAssembler.toCommandFromResource(receivePondRecordCommandResource);
        pondRecordCommandService.handle(receivePondRecordCommand);
        return ResponseEntity.status(201).build();
    }
}
