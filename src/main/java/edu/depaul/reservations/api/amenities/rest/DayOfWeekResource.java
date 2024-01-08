package edu.depaul.reservations.api.amenities.rest;

import edu.depaul.reservations.api.amenities.model.DayOfWeek;
import edu.depaul.reservations.api.amenities.model.DayOfWeekItem;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/daysofweek")
public class DayOfWeekResource {

    @GetMapping
    public List<DayOfWeekItem> dayOfWeekItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return Arrays.stream(DayOfWeek.values())
                    .limit(15)
                    .map(this::mapToDayOfWeekItem)
                    .collect(Collectors.toList());
        }

        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> dayOfWeek.getLabel()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .limit(15)
                .map(this::mapToDayOfWeekItem)
                .collect(Collectors.toList());
    }

    private DayOfWeekItem mapToDayOfWeekItem(DayOfWeek dayOfWeek) {
        return DayOfWeekItem.builder()
                .id(dayOfWeek)
                .text(dayOfWeek.getLabel())
                .build();
    }
}
