package com.example.demoproject.service;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import com.example.demoproject.model.*;
import static org.assertj.core.api.Assertions.*;

class ContextResolverTest {

    private String sampleJson;
    private DocumentContext rootJson;
    private Map<String, Object> contextCache;

    @BeforeEach
    void setUp() {
        sampleJson = """
        {
          "applicants": [
            {
              "type": "PRIMARY",
              "demographics": { "firstName": "John", "lastName": "Doe" },
              "addresses": [
                { "type": "HOME", "line1": "123 Main St", "city": "Springfield" },
                { "type": "MAILING", "line1": "PO Box 123", "city": "Capital" }
              ],
              "currentCoverages": [
                { "isActive": true, "medical": { "planName": "Gold HMO" } }
              ]
            },
            {
              "type": "DEPENDENT",
              "demographics": { "firstName": "Jane" },
              "addresses": [
                { "type": "HOME", "line1": "123 Main St", "city": "Springfield" }
              ]
            }
          ]
        }
        """;
        rootJson = JsonPath.parse(sampleJson);
        contextCache = new HashMap<>();
    }

    @Test
    void shouldResolveRootContext() {
        // Given
        String primaryPath = "$.applicants[?(@.type == 'PRIMARY')][0]";
        Object primary = rootJson.read(primaryPath);
        contextCache.put("primary", primary);

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@primary", rootJson, contextCache);

        // Then
        assertThat(result).isNotNull();
        assertThat(JsonPath.parse(result).read("$.demographics.firstName")).isEqualTo("John");
    }

    @Test
    void shouldResolveNestedContextField() {
        // Given
        String primaryPath = "$.applicants[?(@.type == 'PRIMARY')][0]";
        contextCache.put("primary", rootJson.read(primaryPath));

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@primary.demographics.firstName", rootJson, contextCache);

        // Then
        assertThat(result).isEqualTo("John");
    }

    @Test
    void shouldResolveFilteredAddressFromContext() {
        // Given
        String primaryPath = "$.applicants[?(@.type == 'PRIMARY')][0]";
        contextCache.put("primary", rootJson.read(primaryPath));

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue(
            "@primary.addresses[?(@.type == 'MAILING')].city", 
            rootJson, 
            contextCache
        );

        // Then
        assertThat(result).isEqualTo("Capital");
    }

    @Test
    void shouldReturnNullForMissingContext() {
        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@nonExistent.field", rootJson, contextCache);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullForInvalidSubPath() {
        // Given
        contextCache.put("primary", rootJson.read("$.applicants[0]"));

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@primary.invalid.field", rootJson, contextCache);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldResolveCollectionContext() {
        // Given
        String dependentsPath = "$.applicants[?(@.type == 'DEPENDENT')]";
        contextCache.put("dependents", rootJson.read(dependentsPath));

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@dependents", rootJson, contextCache);

        // Then
        assertThat(result).isInstanceOf(List.class);
        List<?> list = (List<?>) result;
        assertThat(list).hasSize(1);
        assertThat(JsonPath.parse(list.get(0)).read("$.demographics.firstName")).isEqualTo("Jane");
    }

    @Test
    void shouldHandleEmptyContextCache() {
        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@any.field", rootJson, new HashMap<>());

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldSupportDirectJsonPathWithoutContext() {
        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("$.applicants[0].demographics.firstName", rootJson, contextCache);

        // Then
        assertThat(result).isEqualTo("John");
    }

    @Test
    void shouldSupportLegacyDottedPath() {
        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("applicants[0].demographics.firstName", rootJson, contextCache);

        // Then
        assertThat(result).isEqualTo("John");
    }

    @Test
    void shouldHandleContextWithNullValue() {
        // Given
        contextCache.put("empty", null);

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@empty.field", rootJson, contextCache);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldResolveComplexNestedChains() {
        // Given
        contextCache.put("primary", rootJson.read("$.applicants[?(@.type == 'PRIMARY')][0]"));
        contextCache.put("primaryHome", PdfFieldMapperTestHelper.resolveValue(
            "@primary.addresses[?(@.type == 'HOME')][0]", rootJson, contextCache
        ));

        // When
        Object result = PdfFieldMapperTestHelper.resolveValue("@primaryHome.city", rootJson, contextCache);

        // Then
        assertThat(result).isEqualTo("Springfield");
    }
}