/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.remoteconfig;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.firebase.internal.NonNull;
import com.google.firebase.remoteconfig.internal.TemplateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a Remote Config template.
 */
public final class Template {

  private String etag;
  private Map<String, Parameter> parameters;
  private List<Condition> conditions;
  private Map<String, ParameterGroup> parameterGroups;
  private Version version;

  /**
   * Creates a new {@link Template}.
   */
  public Template() {
    parameters = new HashMap<>();
    conditions = new ArrayList<>();
    parameterGroups = new HashMap<>();
  }

  Template(@NonNull TemplateResponse templateResponse) {
    checkNotNull(templateResponse);
    this.parameters = new HashMap<>();
    this.conditions = new ArrayList<>();
    this.parameterGroups = new HashMap<>();
    if (templateResponse.getParameters() != null) {
      for (Map.Entry<String, TemplateResponse.ParameterResponse> entry
              : templateResponse.getParameters().entrySet()) {
        this.parameters.put(entry.getKey(), new Parameter(entry.getValue()));
      }
    }
    if (templateResponse.getConditions() != null) {
      for (TemplateResponse.ConditionResponse conditionResponse
              : templateResponse.getConditions()) {
        this.conditions.add(new Condition(conditionResponse));
      }
    }
    if (templateResponse.getParameterGroups() != null) {
      for (Map.Entry<String, TemplateResponse.ParameterGroupResponse> entry
              : templateResponse.getParameterGroups().entrySet()) {
        this.parameterGroups.put(entry.getKey(), new ParameterGroup(entry.getValue()));
      }
    }
    if (templateResponse.getVersion() != null) {
      this.version = new Version(templateResponse.getVersion());
    }
  }

  /**
   * Gets the ETag of the template.
   *
   * @return The ETag of the template.
   */
  public String getETag() {
    return this.etag;
  }

  /**
   * Gets the map of parameters of the template.
   *
   * @return A non-null map of parameter keys to their optional default values and optional
   *     conditional values.
   */
  @NonNull
  public Map<String, Parameter> getParameters() {
    return this.parameters;
  }

  /**
   * Gets the list of conditions of the template.
   *
   * @return A non-null list of conditions.
   */
  @NonNull
  public List<Condition> getConditions() {
    return conditions;
  }

  /**
   * Gets the map of parameter groups of the template.
   *
   * @return A non-null map of parameter group names to their parameter group instances.
   */
  @NonNull
  public Map<String, ParameterGroup> getParameterGroups() {
    return parameterGroups;
  }

  /**
   * Gets the version information of the template.
   *
   * @return The version information of the template.
   */
  public Version getVersion() {
    return version;
  }

  /**
   * Sets the map of parameters of the template.
   *
   * @param parameters A non-null map of parameter keys to their optional default values and
   *                   optional conditional values.
   * @return This {@link Template} instance.
   */
  public Template setParameters(
          @NonNull Map<String, Parameter> parameters) {
    checkNotNull(parameters, "parameters must not be null.");
    this.parameters = parameters;
    return this;
  }

  /**
   * Sets the list of conditions of the template.
   *
   * @param conditions A non-null list of conditions in descending order by priority.
   * @return This {@link Template} instance.
   */
  public Template setConditions(
          @NonNull List<Condition> conditions) {
    checkNotNull(conditions, "conditions must not be null.");
    this.conditions = conditions;
    return this;
  }

  /**
   * Sets the map of parameter groups of the template.
   *
   * @param parameterGroups A non-null map of parameter group names to their
   *                        parameter group instances.
   * @return This {@link Template} instance.
   */
  public Template setParameterGroups(
          @NonNull Map<String, ParameterGroup> parameterGroups) {
    checkNotNull(parameterGroups, "parameter groups must not be null.");
    this.parameterGroups = parameterGroups;
    return this;
  }

  /**
   * Sets the version information of the template.
   * Only the version's description field can be specified here.
   *
   * @param version A {@link Version} instance.
   * @return This {@link Template} instance.
   */
  public Template setVersion(Version version) {
    this.version = version;
    return this;
  }

  Template setETag(String etag) {
    this.etag = etag;
    return this;
  }

  TemplateResponse toTemplateResponse() {
    Map<String, TemplateResponse.ParameterResponse> parameterResponses = new HashMap<>();
    for (Map.Entry<String, Parameter> entry : this.parameters.entrySet()) {
      parameterResponses.put(entry.getKey(), entry.getValue().toParameterResponse());
    }
    List<TemplateResponse.ConditionResponse> conditionResponses = new ArrayList<>();
    for (Condition condition : this.conditions) {
      conditionResponses.add(condition.toConditionResponse());
    }
    Map<String, TemplateResponse.ParameterGroupResponse> parameterGroupResponse = new HashMap<>();
    for (Map.Entry<String, ParameterGroup> entry : this.parameterGroups.entrySet()) {
      parameterGroupResponse.put(entry.getKey(), entry.getValue().toParameterGroupResponse());
    }
    TemplateResponse.VersionResponse versionResponse = (this.version == null) ? null
            : this.version.toVersionResponse();
    return new TemplateResponse()
            .setParameters(parameterResponses)
            .setConditions(conditionResponses)
            .setParameterGroups(parameterGroupResponse)
            .setVersion(versionResponse);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Template template = (Template) o;
    return Objects.equals(etag, template.etag)
            && Objects.equals(parameters, template.parameters)
            && Objects.equals(conditions, template.conditions)
            && Objects.equals(parameterGroups, template.parameterGroups)
            && Objects.equals(version, template.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(etag, parameters, conditions, parameterGroups, version);
  }
}
