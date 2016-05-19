package de.grundid.api.telegram.weather;

import de.grundid.api.utils.db.AbstractDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class WeatherDatabaseService extends AbstractDatabaseService {

    public WeatherDatabaseService() {
        super("cowohn-weather-database.json");
    }
}
