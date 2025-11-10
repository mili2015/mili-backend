package br.com.mili.milibackend.academia.adapter.external.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademiaExternalGetCousesByIdResponse {
    private Integer id;
    private LocalDateTime date;
    private LocalDateTime dateGmt;
    private GuidDto guid;
    private LocalDateTime modified;
    private LocalDateTime modifiedGmt;
    private String slug;
    private String status;
    private String type;
    private String link;
    private TitleDto title;
    private ContentDto content;
    private Integer author;
    private Integer featuredMedia;
    private Integer menuOrder;
    private String template;
    private MetaDto meta;
    private List<String> categories;
    private List<String> tags;
    private List<String> ldCourseCategory;
    private List<String> ldCourseTag;
    private List<String> classList;
    private LinksDto _links;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuidDto {
        private String rendered;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleDto {
        private String rendered;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentDto {
        private String rendered;
        private boolean isProtected;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaDto {
        private boolean _kadence_starter_templates_imported_post;
        private String _kad_post_transparent;
        private String _kad_post_title;
        private String _kad_post_layout;
        private String _kad_post_sidebar_id;
        private String _kad_post_content_style;
        private String _kad_post_vertical_padding;
        private String _kad_post_feature;
        private String _kad_post_feature_position;
        private String _kad_post_header;
        private String _kad_post_footer;
        private String footnotes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinksDto {
        private List<LinkItem> self;
        private List<LinkItem> collection;
        private List<LinkItem> about;
        private List<LinkItem> author;
        private List<LinkItem> versionHistory;
        private List<LinkItem> predecessorVersion;
        private List<LinkItem> wpFeaturedmedia;
        private List<LinkItem> wpAttachment;
        private List<TermItem> wpTerm;
        private List<CuriesItem> curies;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkItem {
        private String href;
        private Map<String, Object> targetHints;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermItem {
        private String taxonomy;
        private boolean embeddable;
        private String href;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CuriesItem {
        private String name;
        private String href;
        private boolean templated;
    }
}
