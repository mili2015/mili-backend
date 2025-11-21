package br.com.mili.milibackend.academia.adapter.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
public class AcademiaExternalSaveUserResponse {

    private Integer id;
    private String username;
    private String name;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;
    private String url;
    private String description;
    private String link;
    private String locale;
    private String nickname;
    private String slug;
    private List<String> roles;

    @JsonProperty("registered_date")
    private OffsetDateTime registeredDate;

    private Map<String, Boolean> capabilities;

    @JsonProperty("extra_capabilities")
    private Map<String, Boolean> extraCapabilities;

    @JsonProperty("avatar_urls")
    private AvatarUrls avatarUrls;

    private Meta meta;

    @JsonProperty("elementor_introduction")
    private String elementorIntroduction;

    @JsonProperty("_links")
    private Links links;

    // -------------------------
    // Subclasses (estruturas internas)
    // -------------------------

    @Data
    public static class AvatarUrls {
        @JsonProperty("24")
        private String size24;

        @JsonProperty("48")
        private String size48;

        @JsonProperty("96")
        private String size96;
    }

    @Data
    public static class Meta {
        @JsonProperty("persisted_preferences")
        private List<Object> persistedPreferences;

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

