package de.grundid.api.telegram.farmbothn;

import de.grundid.api.utils.db.AbstractDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class FarmbotDatabaseService extends AbstractDatabaseService {

    public FarmbotDatabaseService() {
        super("farmbot-database.json");
    }
}
