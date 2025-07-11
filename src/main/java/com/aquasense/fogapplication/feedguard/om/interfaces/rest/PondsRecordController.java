package com.aquasense.fogapplication.feedguard.om.interfaces.rest;

import com.aquasense.fogapplication.feedguard.data.dto.EdgePondDataDTO;
import com.aquasense.fogapplication.feedguard.data.service.CloudSyncService;
import com.aquasense.fogapplication.feedguard.om.domain.services.PondRecordCommandService;
import com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources.PondRecordDTO;
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

import java.util.Date;

@RestController
@RequestMapping(value = "api/v1/ponds_record", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pond Record", description = "Available Pond Record Endpoints")
public class PondsRecordController {

    private final PondRecordCommandService pondRecordCommandService;
    private final CloudSyncService cloudSyncService;

    public PondsRecordController(PondRecordCommandService pondRecordCommandService, CloudSyncService cloudSyncService) {
        this.pondRecordCommandService = pondRecordCommandService;
        this.cloudSyncService = cloudSyncService;
    }

    @PostMapping
    @Operation(summary = "Create a new Pond Record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pond Record Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    public ResponseEntity<?> createPondRecord(@RequestBody ReceivePondRecordCommandResource receivePondRecordCommandResource) {
        var receivePondRecordCommand = ReceivePondRecordCommandFromResourceAssembler.toCommandFromResource(receivePondRecordCommandResource);
        var pondRecords = pondRecordCommandService.handle(receivePondRecordCommand);

        for (var pondRecord : pondRecords) {
            var pondRecordDTO = new PondRecordDTO(
                    pondRecord.getPondId(),
                    pondRecord.getValue(),
                    pondRecord.getRecordType().toString(),
                    new Date() // hora actual
            );
            cloudSyncService.sendPondRecordToCloud(pondRecordDTO);
        }

        return ResponseEntity.status(201).build();
    }
}
