package org.apache.geode.management.internal;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import org.apache.geode.distributed.internal.DistributionConfig;

/**
 * Hosts common utility methods needed by the management package
 * @since Geode 1.0.0.0
 */
public class AgentUtil {


  public static final String ERROR_VARIABLE_NOT_SET =
      "The GEODE_HOME environment variable must be set!";

  private String gemfireVersion = null;

  public AgentUtil(String gemfireVersion) {
    this.gemfireVersion = gemfireVersion;
  }

  /**
   * this method will try to find the named war files in the following order: 1. if GEODE_HOME is
   * defined, it will look under tools/Extensions, tools/Pulse and lib folder (in this order) to
   * find either the name-version.war or the name.war file 2. If GEODE_HOME is not defined, it will
   * try to find either the name-version.war/name.war (in that order) on the classpath
   * @param warFilePrefix : the prefix of the war file, e.g. geode-web, geode-pulse, or
   * geode-web-api
   */
  public String findWarLocation(String warFilePrefix) {
//    List<String> classpathWarLocation = classpathWarLocation(warFilePrefix);
    List<String> classpathWarLocation = Collections.EMPTY_LIST;
    String geodeHome = getGeodeHome();
    if (StringUtils.isNotBlank(geodeHome) || classpathWarLocation.size() > 0) {

      String[] possibleFiles =
          {classpathWarLocation.size() > 0 ? classpathWarLocation.get(0) : "",
              geodeHome + "/tools/Extensions/" + warFilePrefix + "-" + gemfireVersion + ".war",
              geodeHome + "/tools/Pulse/" + warFilePrefix + "-" + gemfireVersion + ".war",
              geodeHome + "/lib/" + warFilePrefix + "-" + gemfireVersion + ".war",
              geodeHome + "/tools/Extensions/" + warFilePrefix + ".war",
              geodeHome + "/tools/Pulse/" + warFilePrefix + ".war",
              geodeHome + "/lib/" + warFilePrefix + ".war"};
      for (String possibleFile : possibleFiles) {
        if (new File(possibleFile).isFile()) {
          return possibleFile;
        }
      }
    }

    // if $GEODE_HOME is not set or we are not able to find it in all the possible locations under
    // $GEODE_HOME, try to
    // find in the classpath
    String[] possibleFiles = {warFilePrefix + "-" + gemfireVersion + ".war",
        "tools/Pulse/" + warFilePrefix + "-" + gemfireVersion + ".war",
        "tools/Extensions/" + warFilePrefix + "-" + gemfireVersion + ".war",
        "lib/" + warFilePrefix + "-" + gemfireVersion + ".war", warFilePrefix + ".war"};
    for (String possibleFile : possibleFiles) {
      URL url = this.getClass().getClassLoader().getResource(possibleFile);
      if (url != null) {
        // found the war file
        return url.getPath();
      }
    }

    // we still couldn't find the war file
    return null;
  }

  private List<String> classpathWarLocation(String warFilePrefix) {
    return Arrays.stream(System.getProperty("java.class.path").split(":"))
        .filter(s -> s.contains(warFilePrefix)).collect(
            Collectors.toList());
  }

  public boolean isWebApplicationAvailable(final String warFileLocation) {
    return StringUtils.isNotBlank(warFileLocation);
  }

  public boolean isWebApplicationAvailable(final String... warFileLocations) {
    for (String warFileLocation : warFileLocations) {
      if (isWebApplicationAvailable(warFileLocation)) {
        return true;
      }
    }

    return false;
  }

  public String getGeodeHome() {

    String geodeHome = System.getenv("GEODE_HOME");

    // Check for empty variable. if empty, then log message and exit HTTP server
    // startup
    if (StringUtils.isBlank(geodeHome)) {
      geodeHome = System.getProperty(DistributionConfig.GEMFIRE_PREFIX + "home");
      if (StringUtils.isBlank(geodeHome)) {
        geodeHome = null;
      }
    }
    return geodeHome;
  }

  public boolean isGemfireHomeDefined() {
    String gemfireHome = getGeodeHome();
    return StringUtils.isNotBlank(gemfireHome);
  }
}
