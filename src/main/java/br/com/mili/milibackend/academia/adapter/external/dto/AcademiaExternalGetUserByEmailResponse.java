package br.com.mili.milibackend.academia.adapter.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AcademiaExternalGetUserByEmailResponse {

    private Integer id;
    private String name;
    private String url;
    private String description;
    private String link;
    private String slug;

    @JsonProperty("avatar_urls")
    private Map<String, String> avatarUrls;

    private Meta meta;

    @JsonProperty("elementor_introduction")
    private JsonNode elementorIntroduction;

    @JsonProperty("_links")
    private Links links;

    @Data
    public static class Meta {
        @JsonProperty("import_plain_pass")
        private String importPlainPass;
    }

    @Data
    public static class Links {
        private List<LinkItem> self;
        private List<LinkItem> collection;
        private List<LinkItem> courses;
        private List<LinkItem> groups;

        @JsonProperty("course-progress")
        private List<LinkItem> courseProgress;

        @JsonProperty("quiz_progress")
        private List<LinkItem> quizProgress;
    }

    @Data
    public static class LinkItem {
        private String href;
        private Boolean embeddable;

        @JsonProperty("targetHints")
        private TargetHints targetHints;
    }

    @Data
    public static class TargetHints {
        private List<String> allow;
    }
}
