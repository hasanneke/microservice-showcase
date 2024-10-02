package com.mentoapp.mentoapp.Controller.MentorSearchController;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.SearchService.SearchService;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Utility.CustomMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v2/search")
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorSearchController {
    @Autowired
    private SearchService searchUsers;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<PagedModel<UserResponse>> searchMentor(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "categories", required = false, defaultValue = "") Set<Long> categories) throws NoSuchMethodException {
        Page<User> userPage = searchUsers.searchUsers(categories, PageRequest.of(page, size));
        List<UserResponse> userResponses = userPage.getContent().stream().map(CustomMapper::userToUserResponse).toList();

        PagedModel<UserResponse> pagedModel = PagedModel.of(userResponses,
                new PagedModel.PageMetadata(userPage.getSize(), userPage.getNumber(), userPage.getTotalElements(), userPage.getTotalPages()));

        Method method = this.getClass().getMethod("searchMentor", Integer.class, Integer.class, Set.class);

        if (page < userPage.getTotalPages()) {
            Link link = linkTo(method, page + 1, size, categories).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if (page > 0) {
            Link link = linkTo(method, page - 1, size, categories).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }

        return new HttpEntity<>(pagedModel);
    }
}
