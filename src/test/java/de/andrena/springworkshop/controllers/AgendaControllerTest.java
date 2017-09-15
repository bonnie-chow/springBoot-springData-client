package de.andrena.springworkshop.controllers;

import de.andrena.springworkshop.dao.EventDao;
import de.andrena.springworkshop.dto.EventDTO;
import de.andrena.springworkshop.facades.EventFacade;
import de.andrena.springworkshop.facades.EventFacadeImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest()
@RunWith(SpringRunner.class)
public class AgendaControllerTest {

    private static final String SEARCH_TERM = "myTestTitle";

    // was macht das hier? Warum brauchen wir das
    @TestConfiguration
    static class EventFacadeTestConfiguration {

        @Bean
        public EventFacade eventFacade() {
            return new EventFacadeImpl();
        }
    }

    // difference between @Mock, @MockBean ... explain
    @MockBean
    private EventDao eventDao;

    // what is MockMvc - link?
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void agenda_modelContainsEvents() throws Exception {
        List<EventDTO> eventList = Collections.singletonList(new EventDTO());
        when(eventDao.getAllEvents()).thenReturn(eventList);

        mockMvc.perform(get("/agenda"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("events", eventList));
    }

    @Test
    public void search_modelContainsCorrectEvents() throws Exception {
        EventDTO eventWithTitle = new EventDTO();
        eventWithTitle.setTitle(SEARCH_TERM);
        List<EventDTO> searchResult = Collections.singletonList(eventWithTitle);
        when(eventDao.getEventsWithTitleContaining(SEARCH_TERM)).thenReturn(searchResult);

        mockMvc.perform(get("/search").param("title", SEARCH_TERM))
                .andExpect(status().isOk())
                .andExpect(model().attribute("events", hasItem(eventWithTitle)));
    }
}