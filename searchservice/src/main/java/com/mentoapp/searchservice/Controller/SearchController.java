package com.mentoapp.searchservice.Controller;

import com.mentoapp.searchservice.Entity.SearchUserObject;
import com.mentoapp.searchservice.Service.SearchService.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/search/text-search")
public class SearchController {
    @Autowired
    private SearchService searchUsers;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<SearchHit<SearchUserObject>> fuzzySearch(@RequestParam(name = "query", required = true) String query) throws IOException {
        return searchUsers.fuzzySearch(query);
    }
}
