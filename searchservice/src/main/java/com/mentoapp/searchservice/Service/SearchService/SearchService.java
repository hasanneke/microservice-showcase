package com.mentoapp.searchservice.Service.SearchService;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mentoapp.searchservice.Domain.UserResponse;
import com.mentoapp.searchservice.Entity.SearchUserObject;
import com.mentoapp.searchservice.Repository.SearchUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class SearchService {
    static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private SearchUserRepository searchUserRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(topics = "mentorcreated", groupId = "searchgroup")
    public void save(String message) throws JsonProcessingException {
        UserResponse userResponse =om.readValue(message, UserResponse.class);
        logger.info(String.format("Mentor Created request received -> %s",userResponse));
        searchUserRepository.save(SearchUserObject
                .builder()
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .roles(userResponse.getRoles())
                .name(userResponse.getName())
                .fullName(userResponse.getFullName())
                .description(userResponse.getDescription())
                .surname(userResponse.getSurname())
                .categories(userResponse.getCategories())
                .urlAvatar(userResponse.getUrlAvatar())
                .build()
        );
    }

    public List<SearchHit<SearchUserObject>> fuzzySearch(String query) {
        NativeQuery buildQuery = new NativeQueryBuilder()
                .withQuery(
                        q -> q.multiMatch(MultiMatchQuery.of(
                                m -> m.query(query)
                                        .fields("name", "categories.name", "description", "fullName")
                                        .operator(Operator.Or)
                                        .fuzziness("2")
                                        .prefixLength(2)

                        ))
                ).withMaxResults(6)
                .build();

        return elasticsearchOperations.search(buildQuery, SearchUserObject.class).getSearchHits();

    }
}