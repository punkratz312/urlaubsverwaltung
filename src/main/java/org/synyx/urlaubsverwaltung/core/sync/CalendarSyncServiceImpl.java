package org.synyx.urlaubsverwaltung.core.sync;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.synyx.urlaubsverwaltung.core.settings.CalendarSettings;
import org.synyx.urlaubsverwaltung.core.settings.SettingsService;
import org.synyx.urlaubsverwaltung.core.sync.absence.Absence;
import org.synyx.urlaubsverwaltung.core.sync.providers.CalendarProvider;

import java.util.Optional;


/**
 * Implementation of {@link CalendarSyncService}.
 *
 * @author Aljona Murygina - murygina@synyx.de
 */
@Service
public class CalendarSyncServiceImpl implements CalendarSyncService {

    private static final Logger LOG = Logger.getLogger(CalendarSyncServiceImpl.class);

    private final CalendarSettings calendarSettings;
    private final CalendarProvider calendarProvider;

    @Autowired
    public CalendarSyncServiceImpl(SettingsService settingsService, CalendarProvider calendarProvider) {

        this.calendarSettings = settingsService.getSettings().getCalendarSettings();
        this.calendarProvider = calendarProvider;

        LOG.info("The following calendar provider is configured: " + calendarProvider.getClass());
    }

    @Override
    public Optional<String> addAbsence(Absence absence) {

        return calendarProvider.add(absence, calendarSettings);
    }


    @Override
    public void update(Absence absence, String eventId) {

        calendarProvider.update(absence, eventId, calendarSettings);
    }


    @Override
    public void deleteAbsence(String eventId) {

        calendarProvider.delete(eventId, calendarSettings);
    }

    @Override
    public void checkCalendarSyncSettings() {
        calendarProvider.checkCalendarSyncSettings(calendarSettings);
    }
}