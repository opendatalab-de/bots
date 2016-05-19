package de.grundid.api.telegram.drinker;

import de.grundid.api.utils.db.AbstractDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class DrinkerDatabaseService extends AbstractDatabaseService {

    public DrinkerDatabaseService() {
        super("drinker-database.json");
    }
}
