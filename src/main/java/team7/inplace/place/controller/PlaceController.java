package team7.inplace.place.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.place.domain.Category;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @GetMapping("/categories")
    public ResponseEntity<Map<String, List<String>>> getCategories() {
        List<String> categories = Arrays.stream(Category.values())
            .map(Enum::name)
            .collect(Collectors.toList());

        Map<String, List<String>> response = new HashMap<>();
        response.put("categories", categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
