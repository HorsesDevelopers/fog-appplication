package com.aquasense.fogapplication.feedguard.om.application.internal.commandservices;

import com.aquasense.fogapplication.feedguard.om.domain.model.aggregates.PondRecord;
import com.aquasense.fogapplication.feedguard.om.domain.model.commands.CreatePondRecordCommand;
import com.aquasense.fogapplication.feedguard.om.domain.model.valueobjects.RecordType;
import com.aquasense.fogapplication.feedguard.om.domain.services.PondRecordCommandService;
import com.aquasense.fogapplication.feedguard.om.infrastructure.persistence.jpa.repositories.PondRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class PondRecordCommandServiceImpl implements PondRecordCommandService {

    private final PondRecordRepository pondRecordRepository;

    public PondRecordCommandServiceImpl(PondRecordRepository pondRecordRepository) {
        this.pondRecordRepository = pondRecordRepository;
    }

    @Override
    public void handle(CreatePondRecordCommand command) {
        var tempRecord = new PondRecord(
            command.pondId(),
            command.temp(),
            RecordType.Temp
        );
        var pHRecord = new PondRecord(
            command.pondId(),
            command.pH(),
            RecordType.pH
        );
        var turbidityRecord = new PondRecord(
            command.pondId(),
            command.turbidity(),
            RecordType.Turbidity
        );
        pondRecordRepository.save(tempRecord);
        pondRecordRepository.save(pHRecord);
        pondRecordRepository.save(turbidityRecord);
    }
}
